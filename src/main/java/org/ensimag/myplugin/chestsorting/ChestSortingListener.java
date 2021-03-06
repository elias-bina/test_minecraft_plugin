package org.ensimag.myplugin.chestsorting;

import org.bukkit.event.Listener;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ChestSortingListener implements Listener{

    private static ChestSortingListener instance;

    private ChestSortingListener(){

    }

    public static ChestSortingListener getInstance(){
        if (instance == null) {
            instance = new ChestSortingListener();
        }
        return instance;
    }


    @EventHandler
    public void onInventoryClick(InventoryOpenEvent event){
        Inventory inv = event.getInventory();
        Boolean isOn = SortingCommand.getInstance().getChestSortingPerPlayer().get(event.getPlayer().getName());
        if (isOn != null && isOn) {
            if (inv.getHolder() instanceof BlockInventoryHolder || inv.getHolder() instanceof DoubleChest) {
                Bukkit.getLogger().info("Chest sorted");
                sortInventory(inv);
                compressInventory(inv);
            }
        }
    }

    void sortInventory(Inventory inv){

        List<ItemStack> items = Arrays.asList(inv.getContents());


        // Bukkit.getLogger().info(items.toString());

        // TODO: More inteligent sorting
        items.sort(new SortbyName());
        inv.clear();


        // Bukkit.getLogger().info(items.toString());
        ItemStack[] i = items.toArray(ItemStack[]::new);

        inv.setContents(i);
    }

    void compressInventory(Inventory inv){

        int len = inv.getSize();
        
        List<ItemStack> items = Arrays.asList(inv.getContents());
        List<ItemStack> res = new ArrayList<ItemStack>();

        Material holdType = null;
        int maxNb = 0;
        int actualNb = 0;
        
        for (ItemStack stack : items) {

            if(stack == null){
                res.add(new ItemStack(holdType,actualNb));
                break;
            } else {

                if(holdType != stack.getType()){
                    if(holdType != null && actualNb != 0){
                        res.add(new ItemStack(holdType,actualNb));
                    }
                    holdType = stack.getType();
                    maxNb = stack.getMaxStackSize();
                    actualNb = 0;
                }

                // Bukkit.getLogger().info(stack.toString() + " : " + stack.hasItemMeta() + "\n");
                
                if(stack.hasItemMeta()){
                    res.add(stack);
                }else {
                    actualNb = actualNb + stack.getAmount();
                    if(actualNb >= maxNb){
                        res.add(new ItemStack(holdType,maxNb));
                        actualNb -= maxNb;
                    }
                }
            }

        }

        while(res.size() < len){
            res.add(null);
        }


        ItemStack[] i = res.toArray(ItemStack[]::new);
        inv.setContents(i);
    }

}



class SortbyName implements Comparator<ItemStack>
{
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
