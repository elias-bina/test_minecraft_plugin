package org.ensimag.myplugin.blockreplacing;

import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class ItemReplacingListener implements Listener{

    private static ItemReplacingListener instance;

    private ItemReplacingListener(){

    }

    public static ItemReplacingListener getInstance(){
        if (instance == null) {
            instance = new ItemReplacingListener();
        }
        return instance;
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        // Looks if the hand is empty
        if(event.getItemInHand().getAmount() == 1 && event.getBlockPlaced().getType().equals(event.getItemInHand().getType())){
            
            
            replaceItem(event.getItemInHand(), event.getHand(), event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event){
        //TODO: Fix shields (wtf ?)
        if(event.getBrokenItem().getType() != Material.SHIELD){
            replaceItem(event.getBrokenItem(), event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        Bukkit.getLogger().info("Consume");
        if(event.getItem().getAmount() == 1){
            Bukkit.getLogger().info("Avaled " + event.getItem().toString());
            replaceItem(event.getItem(), event.getPlayer());
        }
    }

    //TODO: Add thrown objects

    void replaceItem(ItemStack stack, Player player){
        PlayerInventory inv = player.getInventory();
        if(inv.getItem(inv.getHeldItemSlot()) != null && inv.getItem(inv.getHeldItemSlot()).equals(stack)){
            replaceItem(stack, EquipmentSlot.HAND, player);
        }else{
            replaceItem(stack, EquipmentSlot.OFF_HAND, player);
        }
    }

    void replaceItem(ItemStack stack, EquipmentSlot hand, Player player){

        Bukkit.getLogger().info(hand.toString() + " slot empty of " + stack.getType());
        
        Material itemType = stack.getType();
        PlayerInventory inv = player.getInventory();

        if(inv.contains(itemType)){
            int slot = inv.first(itemType);
            ItemStack item = inv.getItem(slot);
            
            if(hand == EquipmentSlot.HAND){
                if(slot == inv.getHeldItemSlot()){
                    inv.clear(slot);
                    if(!inv.contains(itemType)) {
                        Bukkit.getLogger().info("OOPSIE");
                        return;
                    }
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


