package org.ensimag.myplugin.chestsorting;

import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

class SortByName implements Comparator<ItemStack>
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
