package org.ensimag.myplugin.chestsorting;

import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;

class SortByLen implements Comparator<List<ItemStack>>
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
