package me.mazeevent.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mazeevent.main.Actionbar;

import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {
   private Main plugin;

   public Commands(Main main) {
      this.plugin = main;
   }

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      Player p2 = (Player)sender;
      if (cmd.getLabel().equalsIgnoreCase("labirinto") && sender instanceof Player) {
         if (args.length == 0) {
             if (!invVazio(p2)) {
                 Actionbar.EnviarActionbar(p2, "§cOpss.. você precisa esvaziar o inventário para participar.");
                 return true;
              }
            if (!(sender instanceof Player)) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError5")));
               return true;
            } else if (this.plugin.globalKey == 0) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError1")));
               return true;
            } else if (this.plugin.globalKey >= 2 && !this.plugin.participantes.contains(sender.getName())) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError2")));
               return true;
            } else if (this.plugin.participantes.contains(sender.getName())) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError3")));
               return true;
            } else if (((Player)sender).isInsideVehicle()) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError4")));
               return true;
            } else {
               this.plugin.addPlayer((Player)sender);
               return true;
            }
         } else if (!args[0].equalsIgnoreCase("sair") && !args[0].equalsIgnoreCase("s")) {
            if (args[0].equalsIgnoreCase("spect")) {
               if (!p2.hasPermission("spect.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else if (this.plugin.globalKey == 0) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError6")));
                  return true;
               } else if (this.plugin.participantes.contains(sender.getName())) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError3")));
                  return true;
               } else {
                  ((Player)sender).teleport(this.plugin.spect);
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("WelcomeSpect")));
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("help")) {
               if (!p2.hasPermission("help.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else {
                  this.sendHelp((Player)sender);
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("setspawn")) {
               if (!p2.hasPermission("setspawn.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else {
                  this.plugin.spawn = p2.getLocation();
                  this.plugin.getConfig().set("GetSpawn.Spawn", this.plugin.spawn.getWorld().getName() + ";" + this.plugin.spawn.getX() + ";" + this.plugin.spawn.getY() + ";" + this.plugin.spawn.getZ() + ";" + this.plugin.spawn.getYaw() + ";" + this.plugin.spawn.getPitch());
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fCordenada do spawn setada! ( " + this.plugin.getConfig().getString("GetSpawn.Spawn") + " );");
                  this.plugin.saveConfig();
                  Actionbar.EnviarActionbar(p2, ChatColor.GREEN + "Local (Spawn) marcado com sucesso!");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("setexit")) {
               if (!p2.hasPermission("setexit.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else {
                  this.plugin.exit = p2.getLocation();
                  this.plugin.getConfig().set("GetSpawn.Exit", this.plugin.exit.getWorld().getName() + ";" + this.plugin.exit.getX() + ";" + this.plugin.exit.getY() + ";" + this.plugin.exit.getZ() + ";" + this.plugin.exit.getYaw() + ";" + this.plugin.exit.getPitch());
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fCordenada da saida setada! ( " + this.plugin.getConfig().getString("GetSpawn.Exit") + " );");
                  this.plugin.saveConfig();
                  sender.sendMessage("§a[Labirinto] " + ChatColor.GREEN + "Local (Saida) marcada com sucesso!");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("setspect")) {
               if (!p2.hasPermission("setspect.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else {
                  this.plugin.spect = p2.getLocation();
                  this.plugin.getConfig().set("GetSpawn.Spect", this.plugin.spect.getWorld().getName() + ";" + this.plugin.spect.getX() + ";" + this.plugin.spect.getY() + ";" + this.plugin.spect.getZ() + ";" + this.plugin.spect.getYaw() + ";" + this.plugin.spect.getPitch());
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fCordenada do spectador setada! ( " + this.plugin.getConfig().getString("GetSpawn.Spect") + " );");
                  this.plugin.saveConfig();
                  sender.sendMessage("§a[Labirinto] " + ChatColor.GREEN + "Local (Spectador) marcado com sucesso!");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("setbegin")) {
               if (!p2.hasPermission("setbegin.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else {
                  this.plugin.begin = p2.getLocation();
                  this.plugin.getConfig().set("GetSpawn.Begin", this.plugin.begin.getWorld().getName() + ";" + this.plugin.begin.getX() + ";" + this.plugin.begin.getY() + ";" + this.plugin.begin.getZ() + ";" + this.plugin.begin.getYaw() + ";" + this.plugin.begin.getPitch());
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fCordenada inicial setada! ( " + this.plugin.getConfig().getString("GetSpawn.Begin") + " );");
                  this.plugin.saveConfig();
                  sender.sendMessage("§a[Labirinto] " + ChatColor.GREEN + "Local (Inicial) marcado com sucesso!");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("setwinner")) {
               if (!p2.hasPermission("setwinner.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else {
                  this.plugin.winner = p2.getLocation();
                  this.plugin.getConfig().set("GetWinner.Winner", this.plugin.winner.getWorld().getName() + ";" + this.plugin.winner.getX() + ";" + this.plugin.winner.getY() + ";" + this.plugin.winner.getZ() + ";" + this.plugin.winner.getYaw() + ";" + this.plugin.winner.getPitch());
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fCordenada winner setada! ( " + this.plugin.getConfig().getString("GetWinner.Winner") + " );");
                  this.plugin.saveConfig();
                  sender.sendMessage("§a[Labirinto] " + ChatColor.GREEN + "Local (Spawn) marcado com sucesso!");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("forcestart")) {
               if (!p2.hasPermission("forcestart.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else if (this.plugin.globalKey != 0) {
            	   Actionbar.EnviarActionbar(p2, "§a[Labirinto] " + ChatColor.RED + "Opss.. Já existe um evento sendo executado.");
                  return true;
               } else {
                  sender.sendMessage("§a[Labirinto] " + ChatColor.GREEN + "O Evento sera iniciado!");
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fGatilho de tempo acionado.");
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fIniciando modulos de ativacao do evento.");
                  this.plugin.globalKey = 1;
                  this.plugin.timeKey = 1;
                  this.plugin.eSetStart = true;
                  this.plugin.autoEventMessage();
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("forcestop")) {
               if (!p2.hasPermission("forcestop.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else if (this.plugin.globalKey == 0) {
                  sender.sendMessage("§a[Labirinto] " + ChatColor.RED + "Shii.. O evento ainda não está disponível.");
                  return true;
               } else {
                  this.plugin.finalizeMaze();
                  sender.sendMessage("§a[Labirinto] " + ChatColor.GOLD + "O evento sera parado!");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("prepare")) {
               if (!p2.hasPermission("prepare.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else if (this.plugin.globalKey != 0) {
                  sender.sendMessage("§a[Labirinto] " + ChatColor.RED + "Opss... Já há um evento ocorrendo.");
                  return true;
               } else {
                  Bukkit.broadcastMessage(" ");
                  Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("AutoPrepareEvent")));
                  Bukkit.broadcastMessage(" ");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("info")) {
               if (this.plugin.globalKey == 0) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError1")));
                  return true;
               } else {
            	   Actionbar.EnviarActionbar(p2, "§a[Labirinto] " + ChatColor.WHITE + "Restam " + ChatColor.GOLD + this.plugin.participantes.size() + ChatColor.WHITE + " jogadores dentro do Labirinto!");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("son")) {
               if (!p2.hasPermission("son.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else if (this.plugin.globalKey == 0) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError1")));
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("soff")) {
               if (!p2.hasPermission("soff.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else if (this.plugin.globalKey != 0) {
                  sender.sendMessage(ChatColor.RED + "Ha um evento acontecendo!");
                  return true;
               }
            } else if (args[0].equalsIgnoreCase("reload")) {
               if (!p2.hasPermission("reload.use")) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§a[Labirinto] " + this.plugin.getConfig().getString("PermissionError")));
                  return true;
               } else if (this.plugin.globalKey != 0) {
                  sender.sendMessage("§a[Labirinto] " + ChatColor.RED + "Um evento ja esta acontecendo!");
                  return true;
               } else {
                  sender.sendMessage("§a[Labirinto] " + ChatColor.GREEN + "Plugin resetado!");
                  return true;
               }
            } else {
               return true;
            }
         } else if (this.plugin.globalKey == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("EventError1")));
            return true;
         } else {
            this.plugin.removePlayer((Player)sender);
            return true;
         }
      } else {
         return true;
      }
	return false;
   }
   
   public static boolean invVazio(Player p) {
	      boolean isEmpty = true;
	      ItemStack[] var5;
	      int var4 = (var5 = p.getInventory().getContents()).length;

	      for(int var3 = 0; var3 < var4; ++var3) {
	         ItemStack item = var5[var3];
	         if (item != null) {
	            isEmpty = false;
	            break;
	         }
	      }

	      if (isEmpty) {
	         return p.getInventory().getHelmet() == null && p.getInventory().getChestplate() == null && p.getInventory().getLeggings() == null && p.getInventory().getBoots() == null;
	      } else {
	         return false;
	      }
	   }
   
   private void sendHelp(Player player) {
      player.sendMessage(ChatColor.YELLOW + "§e§lLABIRINTO - AJUDA");
      player.sendMessage(ChatColor.GREEN + "/labirinto forcestart " + ChatColor.GRAY + "- Inicia o evento instantaneamente.");
      player.sendMessage(ChatColor.GREEN + "/labirinto forcestop " + ChatColor.GRAY + "- Para o evento instantaneamente.");
      player.sendMessage(ChatColor.GREEN + "/labirinto setspawn " + ChatColor.GRAY + "- Marca local de spawn do evento.");
      player.sendMessage(ChatColor.GREEN + "/labirinto setwinner " + ChatColor.GRAY + "- Marca local do vencedor do evento.");
      player.sendMessage(ChatColor.GREEN + "/labirinto setexit " + ChatColor.GRAY + "- Marca local de saida do evento.");
      player.sendMessage(ChatColor.GREEN + "/labirinto setspect " + ChatColor.GRAY + "- Marca local do spectador do evento.");
      player.sendMessage(ChatColor.GREEN + "/labirinto son " + ChatColor.GRAY + "- Habilita a tabela.");
      player.sendMessage(ChatColor.GREEN + "/labirinto soff " + ChatColor.GRAY + "- Desabilita a tabela caso ocorra bugs.");
      player.sendMessage(ChatColor.GREEN + "/labirinto info " + ChatColor.GRAY + "- Mostra quantos jogadores esta dentro do evento.");
      player.sendMessage(ChatColor.GREEN + "/labirinto reload " + ChatColor.GRAY + "- Recarrega a configuracao.");
   }
}