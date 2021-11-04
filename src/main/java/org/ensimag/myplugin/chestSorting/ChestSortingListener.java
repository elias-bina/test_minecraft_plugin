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
            compressInventory(inv);
        }
    }

    void sortInventory(Inventory inv){

        List<ItemStack> items = Arrays.asList(inv.getContents());


        // Bukkit.getLogger().info(items.toString());
        items.sort(new SortbyName());
        inv.clear();


        // Bukkit.getLogger().info(items.toString());
        ItemStack[] i = items.toArray(ItemStack[]::new);

        inv.setContents(i);
    }

    void compressInventory(Inventory inv){
        
    }

}



class SortbyName implements Comparator<ItemStack>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(ItemStack a, ItemStack b)
    {
        if(a == null){
            if(b == null){
                return 0;
            }
            return 1;
        }
        if(b == null){
            return -1;
        }
        return a.getType().compareTo(b.getType()) ;
    }
}
