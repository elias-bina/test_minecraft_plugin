package org.ensimag.myplugin.chestSorting;

import org.bukkit.event.Listener;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ChestSortingListener implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryOpenEvent event){
        Inventory inv = event.getInventory();
        if(inv.getHolder() instanceof BlockInventoryHolder || inv.getHolder() instanceof DoubleChest){
            Bukkit.getLogger().info("Chest Opened");
            sortInventory(inv);
        }
    }

    void sortInventory(Inventory inv){

        List<ItemStack> items = Arrays.asList(inv.getContents());
        items.sort(new SortbyName());
        inv.clear();
        inv.setContents((ItemStack[])items.toArray());
    }

}



class SortbyName implements Comparator<ItemStack>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(ItemStack a, ItemStack b)
    {
        return a.getType().compareTo(b.getType()) ;
    }
}
