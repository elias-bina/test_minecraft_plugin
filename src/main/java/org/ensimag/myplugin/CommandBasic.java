package org.ensimag.myplugin;


import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import org.bukkit.Material;

public class CommandBasic implements CommandExecutor{
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Here we need to give items to our player

            // Create a new ItemStack (type: diamond)
            ItemStack diamond = new ItemStack(Material.DIAMOND);

            // Create a new ItemStack (type: brick)
            ItemStack bricks = new ItemStack(Material.BRICK);

            // Set the amount of the ItemStack
            bricks.setAmount(20);

            // Give the player our items (comma-seperated list of all ItemStack)
            player.getInventory().addItem(bricks, diamond);

            Bukkit.getLogger().info("Miam");
            Bukkit.broadcastMessage("Regardez, il triche");

        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
