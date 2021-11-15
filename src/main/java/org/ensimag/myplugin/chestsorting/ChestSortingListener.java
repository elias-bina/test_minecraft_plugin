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

    public static int INVENTORY_LINE_SIZE = 9;

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
        if (isOn == null || isOn) {
            if ((inv.getHolder() instanceof BlockInventoryHolder && inv.getSize() > 5) || inv.getHolder() instanceof DoubleChest) {
                sortInventory(inv, false); // Sorts for compressing
                compressInventory(inv);
                sortInventory(inv, true); // Reorganizes after compression
            }
        }
    }

    void sortInventory(Inventory inv, boolean reorganize){

        List<ItemStack> items = Arrays.asList(inv.getContents());

        if(reorganize){
            List<List<ItemStack>> itemLists = groupItems(items);
            items = smartSort(itemLists, inv.getSize());
        } else {
            //Bukkit.getLogger().info(items.toString());
            items.sort(new SortByName());
            //Bukkit.getLogger().info(items.toString());
        }

        //inv.clear();
        ItemStack[] i = items.toArray(ItemStack[]::new);
    
        inv.setContents(i);
    }

    void compressInventory(Inventory inv){

        int len = inv.getSize();
        
        ItemStack[] items = inv.getContents();
        List<ItemStack> res = new ArrayList<>();

        Material holdType = null;
        int maxNb = 0;
        int actualNb = 0;
        
        for (ItemStack stack : items) {

            if(stack == null){
                if(holdType != null && actualNb != 0) res.add(new ItemStack(holdType,actualNb));
                actualNb = 0;
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
        if(actualNb > 0){
            res.add(new ItemStack(holdType,actualNb));
        }

        while(res.size() < len){
            res.add(null);
        }


        ItemStack[] i = res.toArray(ItemStack[]::new);
        inv.setContents(i);
    }

    List<List<ItemStack>> groupItems(List<ItemStack> items){
        // Group items of same type in lists
        Map<Material, List<ItemStack>> itemMap = new HashMap<>();
        for (ItemStack itemStack : items) {
            if(itemStack != null){
                List<ItemStack> stackList = itemMap.computeIfAbsent(itemStack.getType(), k -> new LinkedList<>());
                stackList.add(itemStack);
            }
        }
        // Convert Map to List
        List<List<ItemStack>> res = new LinkedList<>();
        itemMap.forEach((k, v) -> res.add(v)); 

        // Sort by list size
        res.sort(new SortByLen());

        return res;
    }

    List<ItemStack> smartSort(List<List<ItemStack>> itemLists, int inventorySize){
        final int maxLine = inventorySize/INVENTORY_LINE_SIZE;

        List<ItemStack> items = new LinkedList<>();
        int lineCompletion = 0;
        int invLine = 0;

        // Premier tour de l'inv où on essaie de mettre bien

        while(invLine < maxLine){
            for(List<ItemStack> itemList : itemLists){
                // On ne met des grosses listes d'items  qu'en début de ligne
                while(itemList.size() > INVENTORY_LINE_SIZE && lineCompletion == 0 && invLine < maxLine){
                    for(int i = 0; i<INVENTORY_LINE_SIZE; i++){
                        moveFirstItemStack(itemList, items);
                    }
                    invLine++;
                }

                int size = itemList.size();
                // Complète la ligne avec une liste d'objets pas trop grande
                if(size + lineCompletion <= INVENTORY_LINE_SIZE && invLine < maxLine){
                    for(int i = 0; i<size; i++){
                        moveFirstItemStack(itemList, items);
                        lineCompletion++;
                    }
                }
            }
            // On complète les lignes 
            while(lineCompletion < INVENTORY_LINE_SIZE){
                items.add(null);
                lineCompletion++;
            }
            lineCompletion = 0;
            invLine++;
        }

        //Bukkit.getLogger().info("\nItems restants après le premier tour : " + itemLists + " Taille : " + items.size());

        // Deuxième tour de l'inv où on met les invendus
        int actualSlot = 0;
        for(List<ItemStack> itemList : itemLists){
            int size = itemList.size();
            if(actualSlot < inventorySize){
                for(int i = 0; i < size; i++){
                    while(items.get(actualSlot) != null) actualSlot++;
                    moveFirstItemStack(itemList, items, actualSlot);
                }
            }
        }

        return items;
    }

    void moveFirstItemStack(List<ItemStack> src, List<ItemStack> dest){
        dest.add(src.get(0));
        src.remove(0);
    }

    void moveFirstItemStack(List<ItemStack> src, List<ItemStack> dest, int slot){
        dest.set(slot, src.get(0));
        src.remove(0);
    }


}