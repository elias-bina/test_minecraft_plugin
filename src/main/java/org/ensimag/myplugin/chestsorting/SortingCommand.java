package org.ensimag.myplugin.chestsorting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SortingCommand implements CommandExecutor {

    private static SortingCommand instance;

    private final Map<String, Boolean> chestSortingPerPlayer;

    private SortingCommand() {
        this.chestSortingPerPlayer = new HashMap<>();
    }

    public static SortingCommand getInstance() {
        if (instance == null) {
            instance = new SortingCommand();
        }
        return instance;
    }

    public Map<String, Boolean> getChestSortingPerPlayer() {
        return chestSortingPerPlayer;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            String isOn = args[0];
            if (isOn.equals("true") || isOn.equals("false")){
                boolean on = Boolean.parseBoolean(args[0]);
                chestSortingPerPlayer.put(player.getName(), on);
                player.sendMessage("Your chests will "+ (on ? "":"not ") +"be sorted."); 
            }
            else
                return false;
        }
        return true;
    }
}
