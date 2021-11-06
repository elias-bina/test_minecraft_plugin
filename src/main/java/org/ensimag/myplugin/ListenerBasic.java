package org.ensimag.myplugin;


import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;


public class ListenerBasic implements Listener{

    private static ListenerBasic instance;

    private final Map<String, BossBar> playersBossBar;

    private ListenerBasic() {
        this.playersBossBar = new HashMap<>();
    }

    public static ListenerBasic getInstance() {
        if (instance == null) {
            instance = new ListenerBasic();
        }
        return instance;
    }

    @EventHandler
     public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.text("Bah alors " + event.getPlayer().getName() + ", on a pas la ref ?"));
    }

        

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        event.quitMessage(Component.text("La ref était trop technique pour " + event.getPlayer().getName() + " :("));
    }


    //  // Executes before the second method because it has a much lower priority.
    //  @EventHandler (priority = EventPriority.LOWEST)
    //  public void onPlayerChat1(AsyncPlayerChatEvent event) {
    //      event.setCancelled(true);
    //  }

    //  // Will not execute unless another listener with a  lower priority has uncancelled the event.
    //  @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    //  public void onPlayerChat2(AsyncPlayerChatEvent event) {
    //      System.out.println("This shouldn't be executing.");
    //  }
}
