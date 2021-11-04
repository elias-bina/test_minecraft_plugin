package org.ensimag.myplugin;

import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

public class ChestSortingListener implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory inv = event.getClickedInventory();
        if(inv.getHolder() instanceof BlockInventoryHolder){
            Bukkit.getLogger().info("Chest Opened");
            return;
        }
        Bukkit.getLogger().info("Not a chest");
        return;
    }


}
