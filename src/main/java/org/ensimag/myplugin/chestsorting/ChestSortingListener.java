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
                Bukkit.getLogger().info("Chest sorted");
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
            items.sort(new SortbyName());
        }

        inv.clear();

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
                if(holdType != null) res.add(new ItemStack(holdType,actualNb));
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

    List<List<ItemStack>> groupItems(List<ItemStack> items){
        // Group items of same type in lists
        Map<Material, List<ItemStack>> itemMap = new HashMap<Material, List<ItemStack>>();
        for (ItemStack itemStack : items) {
            if(itemStack != null){
                List<ItemStack> stackList = itemMap.get(itemStack.getType());
                if(stackList == null){
                    stackList = new LinkedList<ItemStack>();
                    itemMap.put(itemStack.getType(), stackList);
                }
                stackList.add(itemStack);
            }
        }
        // Convert Map to List
        List<List<ItemStack>> res = new LinkedList<List<ItemStack>>();
        itemMap.forEach((k, v) -> res.add(v)); 

        // Sort by list size
        res.sort(new SortbyLen());

        return res;
    }

    List<ItemStack> smartSort(List<List<ItemStack>> itemLists, int inventorySize){
        final int maxLine = inventorySize/INVENTORY_LINE_SIZE;

        List<ItemStack> items = new LinkedList<>();
        int lineCompletion = 0;
        int invLine = 0;

        // Premier tour de l'inv où on essaie de mettre bien

        while(invLine < maxLine){
            lineCompletion = 0;
            for(List<ItemStack> itemList : itemLists){
                // On ne met des grosses listes d'items  qu'en début de ligne
                while(itemList.size() > INVENTORY_LINE_SIZE && lineCompletion == 0 && invLine < maxLine){
                    for(int i = 0; i<INVENTORY_LINE_SIZE; i++){
                        moveFirstItemStack(itemList, items);
                        lineCompletion++;
                    }
                    lineCompletion = 0;
                    invLine++;
                }

                int size = itemList.size();
                // Complète la ligne avec une liste d'objets pas trop grande
                if(size + lineCompletion <= INVENTORY_LINE_SIZE && size > 0 && invLine < maxLine){
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

        // Deuxième tour de l'inv où on met les invendus
        int actualSlot = 0;
        while(actualSlot < inventorySize){
            for(List<ItemStack> itemList : itemLists){
                int size = itemList.size();
                if(size > 0 && actualSlot < inventorySize){
                    for(int i = 0; i < size; i++){
                        while(items.get(actualSlot) != null) actualSlot++;
                        moveFirstItemStack(itemList, items);
                        actualSlot++;
                    }
                }
            }
            actualSlot++;
        }

        return items;
    }

    void moveFirstItemStack(List<ItemStack> src, List<ItemStack> dest){
        dest.add(src.get(0));
        src.remove(0);
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

class SortbyLen implements Comparator<List<ItemStack>>
{
    public int compare(List<ItemStack> a, List<ItemStack> b)
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
        return b.size() - a.size();
    }
}
