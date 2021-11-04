package org.ensimag.myplugin;


import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class ListenerBasic implements Listener{
    @EventHandler
     public void onPlayerJoin(PlayerJoinEvent event) {
        
        event.setJoinMessage("Bah alors " + event.getPlayer().getName() + ", on a pas la ref ?");
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
