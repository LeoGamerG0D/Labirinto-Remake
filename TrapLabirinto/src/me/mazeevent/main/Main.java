package me.mazeevent.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.mazeevent.main.Actionbar;

public class Main extends JavaPlugin {
   public int dayOfAutoStart;
   public int hourOfAutoStart;
   public int minOfAutoStart;
   public int prepareMessage;
   public int globalKey = 0;
   public int timeKey = 0;
   public int point = 0;
   protected boolean eSetStart = true;
   protected boolean eSetWin = true;
   protected Location spawn;
   protected Location exit;
   protected Location spect;
   protected Location begin;
   protected Location winner;
   protected String step = "Desabilitado.";
   protected HashMap<String, Integer> totalParticipantes = new HashMap();
   protected List<String> participantes = new ArrayList();
   final String prefix = "§a[Labirinto] ";

   public void onEnable() {
      Bukkit.getConsoleSender().sendMessage("§e[Labirinto] §2Ativado com sucesso. :)");
      this.getServer().getPluginCommand("labirinto").setExecutor(new Commands(this));
      this.getServer().getPluginManager().registerEvents(new Listeners(this), this);
      this.loadConfig();
      this.dayOfAutoStart = Utils.strToCalendar(this.getConfig().getString("AutoStart.Day"));
      this.hourOfAutoStart = Integer.parseInt(this.getConfig().getString("AutoStart.Hour").substring(0, 2));
      this.minOfAutoStart = Integer.parseInt(this.getConfig().getString("AutoStart.Hour").substring(2, 4));
      this.prepareMessage = Integer.parseInt(this.getConfig().getString("AutoStart.PrepareMessage").substring(0, 2));
      Bukkit.getConsoleSender().sendMessage("§e§lLABIRINTO > §aData setada devido as configuracoes do plugin");
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fDia configurado em §6" + this.dayOfAutoStart + ".");
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fHora configurada em §6" + (this.hourOfAutoStart < 10 ? "0" + this.hourOfAutoStart : this.hourOfAutoStart) + ":" + (this.minOfAutoStart < 10 ? "0" + this.minOfAutoStart : this.minOfAutoStart) + ".");
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fMensagem de Preparo configurada em §6" + (this.hourOfAutoStart < 10 ? "0" + this.hourOfAutoStart : this.hourOfAutoStart) + ":" + (this.prepareMessage < 10 ? "0" + this.prepareMessage : this.prepareMessage) + ".");
      String[] spw = this.getConfig().getString("GetSpawn.Spawn").split(";");
      String[] ext = this.getConfig().getString("GetSpawn.Exit").split(";");
      String[] spc = this.getConfig().getString("GetSpawn.Spect").split(";");
      String[] bgn = this.getConfig().getString("GetSpawn.Begin").split(";");
      String[] wnn = this.getConfig().getString("GetWinner.Winner").split(";");
      this.spawn = new Location(this.getServer().getWorld(spw[0]), Double.parseDouble(spw[1]), Double.parseDouble(spw[2]), Double.parseDouble(spw[3]), Float.parseFloat(spw[4]), Float.parseFloat(spw[5]));
      this.exit = new Location(this.getServer().getWorld(ext[0]), Double.parseDouble(ext[1]), Double.parseDouble(ext[2]), Double.parseDouble(ext[3]), Float.parseFloat(ext[4]), Float.parseFloat(ext[5]));
      this.spect = new Location(this.getServer().getWorld(spc[0]), Double.parseDouble(spc[1]), Double.parseDouble(spc[2]), Double.parseDouble(spc[3]), Float.parseFloat(spc[4]), Float.parseFloat(spc[5]));
      this.begin = new Location(this.getServer().getWorld(bgn[0]), Double.parseDouble(bgn[1]), Double.parseDouble(bgn[2]), Double.parseDouble(bgn[3]), Float.parseFloat(bgn[4]), Float.parseFloat(bgn[5]));
      this.winner = new Location(this.getServer().getWorld(wnn[0]), Double.parseDouble(wnn[1]), Double.parseDouble(wnn[2]), Double.parseDouble(wnn[3]), Float.parseFloat(wnn[4]), Float.parseFloat(wnn[5]));
      this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
         public void run() {
            Calendar cd = Calendar.getInstance();
            if (cd.get(11) - 1 == Main.this.hourOfAutoStart - 1 && cd.get(12) == Main.this.prepareMessage && Main.this.globalKey == 0) {
               Bukkit.broadcastMessage(" ");
               Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("AutoPrepareEvent")));
               Bukkit.broadcastMessage(" ");
            }

         }
      }, 0L, 1000L);
      this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
         public void run() {
            Calendar cd = Calendar.getInstance();
            if (cd.get(7) == Main.this.dayOfAutoStart && cd.get(11) == Main.this.hourOfAutoStart && cd.get(12) == Main.this.minOfAutoStart && Main.this.globalKey == 0) {
               Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fGatilho de tempo acionado.");
               Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fIniciando modulos de ativacao do evento.");
               Main.this.globalKey = 1;
               Main.this.timeKey = 1;
               Main.this.autoEventMessage();
            }

         }
      }, 0L, 1000L);
   }

   public void onDisable() {
      Bukkit.getConsoleSender().sendMessage("§e[Labirinto] §cfoi desativado");
   }

   protected void addPlayer(Player player) {
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fAdicionando " + player.getName() + " no evento.");
      this.totalParticipantes.put(player.getName(), 0);
      this.participantes.add(player.getName());
      player.teleport(this.spawn);
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("WelcomeMessage")));
   }

   protected boolean autoEventMessage() {
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fIniciando parte 1 do evento.");
      if (this.globalKey != 1 && !this.eSetStart) {
         return true;
      } else {
         this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
               if (Main.this.timeKey == 1) {
                  if (Main.this.point == 1) {
                     Main.this.step = ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("SbStep1").replace("/.../", "."));
                     return;
                  }

                  if (Main.this.point == 2) {
                     Main.this.step = ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("SbStep1").replace("/.../", ".."));
                     return;
                  }

                  if (Main.this.point == 3) {
                     Main.this.step = ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("SbStep1").replace("/.../", "..."));
                  }
               }

            }
         }, 0L, 1L);
         this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            private int vtl = Main.this.getConfig().getInt("Timers.Prepare.CounterMessages");

            public void run() {
               if (Main.this.eSetStart && Main.this.timeKey == 1) {
                  if (this.vtl > 0 && this.vtl != 0 && this.vtl <= Main.this.getConfig().getInt("Timers.Prepare.CounterMessages")) {
                     Bukkit.broadcastMessage(" ");
                     Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("AutoBeginEvent")));
                     Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("AutoBeginEvent1")));
                     Bukkit.broadcastMessage(" ");
                     --this.vtl;
                  } else if (this.vtl <= 0) {
                     Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fParte 1 completa! Esperando a parte 2.");
                     Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("AutoBeginEvent2")));
                     Main.this.eSetStart = false;
                     Main.this.globalKey = 2;
                     Main.this.timeKey = 2;
                     Main.this.step = "...";
                     this.vtl = Main.this.getConfig().getInt("Timers.Prepare.CounterMessages") + 1;
                     Main.this.point = 4;
                     Main.this.setPrepare();
                  }
               }

            }
         }, 0L, (long)(20 * this.getConfig().getInt("Timers.Prepare.Warnings")));
         return true;
      }
   }

   private boolean setPrepare() {
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fIniciando parte 2 do evento.");
      if (this.globalKey != 2) {
         return true;
      } else {
         Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fReproduzindo mensagens de preparo.");
         this.step = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("SbStep2"));
         Iterator var1 = this.participantes.iterator();

         while(var1.hasNext()) {
            final String n = (String)var1.next();
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
               public void run() {
                  if (Main.this.timeKey == 2) {
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("PpMessage1")));
                  }

               }
            }, (long)(2 * this.getConfig().getInt("Timers.Starting.Warnings")));
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
               public void run() {
                  if (Main.this.timeKey == 2) {
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("PpMessage2")));
                  }

               }
            }, (long)(4 * this.getConfig().getInt("Timers.Starting.Warnings")));
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
               public void run() {
                  if (Main.this.timeKey == 2) {
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("PpMessage3")));
                  }

               }
            }, (long)(6 * this.getConfig().getInt("Timers.Starting.Warnings")));
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
               public void run() {
                  if (Main.this.timeKey == 2) {
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("PpMessage4")));
                  }

               }
            }, (long)(8 * this.getConfig().getInt("Timers.Starting.Warnings")));
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
               public void run() {
                  if (Main.this.timeKey == 2) {
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("PpMessage5")));
                  }

               }
            }, (long)(10 * this.getConfig().getInt("Timers.Starting.Warnings")));
         }

         this.getServer().getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
               if (Main.this.timeKey == 2) {
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fParte 2 completa! Esperando o contador.");
                  Main.this.globalKey = 3;
                  Main.this.timeKey = 3;
                  Main.this.step = "...";
                  Main.this.counter();
               }

            }
         }, (long)(40 * this.getConfig().getInt("Timers.Prepare.Interval")));
         if (this.totalParticipantes.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("§a[Labirinto] O evento sera resetado por falta de players.");
            Bukkit.getConsoleSender().sendMessage("§a[Labirinto] Em breve o evento sera resetado!");
            this.finalizeMaze();
            return true;
         } else {
            return true;
         }
      }
   }

   private boolean counter() {
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fIniciando o contador do evento.");
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §f5s configurado.");
      if (this.globalKey != 3) {
         return true;
      } else {
         this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            private int v = Main.this.getConfig().getInt("Timers.Starting.CounterTimer");

            public void run() {
               Iterator var1;
               String n;
               if (this.v > 0 && this.v != 0 && this.v <= Main.this.getConfig().getInt("Timers.Starting.CounterTimer")) {
                  var1 = Main.this.participantes.iterator();

                  while(var1.hasNext()) {
                     n = (String)var1.next();
                     Player p = Bukkit.getPlayer(n);
                     Actionbar.EnviarActionbar(p, Main.this.getConfig().getString("BgMessage3").replaceAll("/counter/", Integer.toString(this.v) + "."));
                  }

                  Main.this.step = ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("SbStep3").replace("/timmer/", "" + ChatColor.RED + this.v + "."));
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] " + this.v);
                  --this.v;
               } else if (this.v <= 0 && Main.this.timeKey == 3) {
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fEvento iniciado!");
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fParte 2 completa! Iniciando parte 3.");
                  var1 = Main.this.participantes.iterator();

                  while(var1.hasNext()) {
                     n = (String)var1.next();
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("BgMessage1")));
                     Main.this.getServer().getPlayer(n).teleport(Main.this.begin);
                  }

                  this.v = Main.this.getConfig().getInt("Timers.Starting.CounterTimer") + 1;
                  Main.this.setBegin();
               }

            }
         }, 0L, 20L);
         if (this.totalParticipantes.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("§a[Labirinto] O evento sera resetado por falta de players.");
            Bukkit.getConsoleSender().sendMessage("§a[Labirinto] Em breve o evento sera resetado!");
            this.finalizeMaze();
            return true;
         } else {
            return true;
         }
      }
   }

   private boolean setBegin() {
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fIniciando parte 3 do evento.");
      if (this.globalKey != 3) {
         return true;
      } else {
         this.step = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("SbStep4"));
         this.getServer().getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
               if (Main.this.timeKey == 3) {
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fParte 3 completa! Iniciando parte 4.");
                  Main.this.globalKey = 4;
                  Main.this.timeKey = 4;
                  Main.this.setFinish();
               }

            }
         }, (long)(40 * this.getConfig().getInt("Timers.Begin.Interval")));
         if (this.totalParticipantes.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("§a[Labirinto] O evento sera resetado por falta de players.");
            Bukkit.getConsoleSender().sendMessage("§a[Labirinto] Em breve o evento sera resetado!");
            this.finalizeMaze();
            return true;
         } else {
            return true;
         }
      }
   }

   private boolean setFinish() {
      Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fIniciando parte 4 do evento.");
      if (this.globalKey != 4) {
         return true;
      } else {
         this.step = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("SbStep5"));
         Iterator var1 = this.participantes.iterator();

         while(var1.hasNext()) {
            final String n = (String)var1.next();
            this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("FnMessage1")));
            this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("FnMessage2")));
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
               public void run() {
                  if (Main.this.timeKey == 4) {
                     Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fParte 4 completa! Finalizando o evento.");
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("FnMessage3")));
                     Main.this.finalizeMaze();
                  }

               }
            }, (long)(40 * this.getConfig().getInt("Timers.Begin.Finishing")));
         }

         if (this.totalParticipantes.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("§a[Labirinto] O evento sera resetado por falta de players.");
            Bukkit.getConsoleSender().sendMessage("§a[Labirinto] Em breve o evento sera resetado!");
            this.finalizeMaze();
            return true;
         } else {
            return true;
         }
      }
   }

   protected boolean playerWinner(final Player playerWinner) {
      if (this.globalKey <= 2 && !this.eSetWin) {
         return true;
      } else {
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.getConfig().getString("GetWinner.Premium").replaceAll("{player}", this.getConfig().getString("Winner")));
         playerWinner.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("WnSuccessPremium")));
         Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("WnMessage1").replaceAll("{player}", this.getConfig().getString("Winner"))));
         Iterator var2 = this.participantes.iterator();

         while(var2.hasNext()) {
            String n = (String)var2.next();
            this.getServer().getPlayer(n).teleport(this.spawn);
         }

         this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            private int v = Main.this.getConfig().getInt("CounterWinner");

            public void run() {
               Iterator var1;
               String n;
               if (this.v > 0 && this.v != 0 && this.v <= Main.this.getConfig().getInt("CounterWinner")) {
                  for(var1 = Main.this.participantes.iterator(); var1.hasNext(); Main.this.step = Main.this.getConfig().getString("Winner") + ChatColor.AQUA + " Venceu!") {
                     n = (String)var1.next();
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("WnMessage1").replaceAll("{player}", Main.this.getConfig().getString("Winner"))));
                  }

                  Firework firework = (Firework)playerWinner.getWorld().spawn(playerWinner.getLocation(), Firework.class);
                  FireworkMeta frMeta = firework.getFireworkMeta();
                  frMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(Type.BALL_LARGE).withColor(Color.RED).withFade(Color.PURPLE).build());
                  frMeta.setPower(0);
                  firework.setFireworkMeta(frMeta);
                  Firework firework1 = (Firework)playerWinner.getWorld().spawn(playerWinner.getLocation(), Firework.class);
                  FireworkMeta frMeta1 = firework1.getFireworkMeta();
                  frMeta1.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(Type.STAR).withColor(Color.BLUE).withFade(Color.RED).build());
                  frMeta1.setPower(0);
                  firework1.setFireworkMeta(frMeta1);
                  --this.v;
               } else if (this.v <= 0) {
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fEvento sera finalizado, pois " + Main.this.getConfig().getString("Winner") + "§f venceu.");
                  Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fChave win setada para false.");
                  var1 = Main.this.participantes.iterator();

                  while(var1.hasNext()) {
                     n = (String)var1.next();
                     Main.this.getServer().getPlayer(n).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.this.getConfig().getString("FnMessage3")));
                  }

                  Main.this.eSetWin = false;
                  this.v = Main.this.getConfig().getInt("CounterWinner") + 1;
                  Main.this.finalizeMaze();
               }

            }
         }, 0L, 40L);
         return false;
      }
   }

   protected void removePlayer(Player player) {
      if (this.participantes.contains(player.getName())) {
         this.participantes.remove(player.getName());
         this.totalParticipantes.remove(player.getName());
         Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fPlayer " + player.getName() + " retirado com sucesso!");
         player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("ExitMessage")));
         player.teleport(this.exit);
      }

   }

   protected boolean finalizeMaze() {
      if (this.globalKey == 0) {
         return true;
      } else {
         Iterator var1 = this.participantes.iterator();

         while(var1.hasNext()) {
            String n = (String)var1.next();
            this.getServer().getPlayer(n).teleport(this.exit);
         }

         this.getConfig().set("Winner", "");
         this.participantes.clear();
         this.totalParticipantes.clear();
         this.globalKey = 0;
         this.timeKey = 0;
         this.point = 3;
         this.eSetWin = false;
         Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fPlayers removidos do hashmap!");
         Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fTabela retirada com sucesso!");
         Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fString Winner resetada na config!");
         Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fChave de etapas de evento retornadas para 0");
         Bukkit.getConsoleSender().sendMessage("§a[Labirinto] §fEvento finalizado com sucesso!");
         return true;
      }
   }

   private void loadConfig() {
      this.getConfig().options().copyDefaults(true);
      this.getConfig().options().copyHeader(true);
      this.saveConfig();
   }
}