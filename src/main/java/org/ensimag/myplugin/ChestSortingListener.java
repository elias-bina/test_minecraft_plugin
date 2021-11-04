package org.ensimag.myplugin;

import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

public class ChestSortingListener implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryOpenEvent event){
        Inventory inv = event.getInventory();
        if(inv.getHolder() instanceof BlockInventoryHolder || inv.getHolder() instanceof DoubleChest){
            Bukkit.getLogger().info("Chest Opened");
        }else{
            Bukkit.getLogger().info("Not a chest");
        }
    }


}
