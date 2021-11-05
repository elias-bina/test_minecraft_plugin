package org.ensimag.myplugin.blockreplacing;

import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class BlockReplacingListener implements Listener{

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        // Looks if the hand is empty
        if(event.getItemInHand().getAmount() == 1){
            replaceBlock(event.getItemInHand(), event.getHand(), event.getPlayer());
        }
    }

    void replaceBlock(ItemStack stack, EquipmentSlot hand, Player player){

        Bukkit.getLogger().info(hand.toString() + " slot empty of " + stack.getType());
        
        Material itemType = stack.getType();
        PlayerInventory inv = player.getInventory();

        if(inv.contains(itemType)){
            int slot = inv.first(itemType);
            ItemStack item = inv.getItem(slot);
            
            if(hand == EquipmentSlot.HAND){
                if(slot == inv.getHeldItemSlot()){
                    inv.clear(slot);
                    if(!inv.contains(itemType)) return;
                    slot = inv.first(itemType);
                    item = inv.getItem(slot);
                }
                player.getEquipment().setItemInMainHand(item);
            } else {
                player.getEquipment().setItemInOffHand(item);
            }
            inv.clear(slot);

            Bukkit.getLogger().info("Item in " + hand.toString() + " replaced");
        }

    }

}


