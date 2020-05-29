package me.mazeevent.main;

import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mazeevent.main.Actionbar;

public class Listeners implements Listener {
   private Main plugin;

   public Listeners(Main main) {
      this.plugin = main;
   }
  
   @EventHandler
   public boolean walkEvent(PlayerMoveEvent e) {
      Location detectPlayer = this.plugin.winner;
      int r = this.plugin.getConfig().getInt("GetWinner.Radius");
      Iterator var4 = this.plugin.participantes.iterator();

      while(true) {
         String n;
         do {
            if (!var4.hasNext()) {
               return true;
            }

            n = (String)var4.next();
         } while(this.plugin.getServer().getPlayer(n) != e.getPlayer());

         for(int x = r; x >= -r; --x) {
            for(int y = r; y >= -r; --y) {
               for(int z = r; z >= -r; --z) {
                  if (e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).equals(detectPlayer.getWorld().getBlockAt(new Location(detectPlayer.getWorld(), detectPlayer.getX() + (double)x, detectPlayer.getY() + (double)y, detectPlayer.getZ() + (double)z)))) {
                     this.plugin.getConfig().set("Winner", String.valueOf(e.getPlayer().getName()));
                     this.plugin.eSetWin = true;
                     this.plugin.playerWinner(e.getPlayer());
                     break;
                  }
               }
            }
         }
      }
   }

   @EventHandler
   private void onQuit(PlayerQuitEvent e) {
      this.plugin.removePlayer(e.getPlayer());
   }

   @EventHandler
   private void onKick(PlayerKickEvent e) {
      this.plugin.removePlayer(e.getPlayer());
   }

   @EventHandler
   private void onDeath(PlayerDeathEvent e) {
      Player killer = e.getEntity().getPlayer();
      this.plugin.removePlayer(killer);
   }
}