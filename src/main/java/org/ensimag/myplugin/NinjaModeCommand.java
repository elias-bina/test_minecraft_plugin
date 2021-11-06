package org.ensimag.myplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class NinjaModeCommand implements CommandExecutor {

    private static NinjaModeCommand instance;

    private final Map<String, Boolean> ninjaModePerPlayer;

    private NinjaModeCommand() {
        this.ninjaModePerPlayer = new HashMap<>();
    }

    public static NinjaModeCommand getInstance() {
        if (instance == null) {
            instance = new NinjaModeCommand();
        }
        return instance;
    }

    public Map<String, Boolean> getNinjaModePerPlayer() {
        return ninjaModePerPlayer;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 1){
            return false;
        }
        if (sender instanceof Player player) {
            String isOn = args[0];
            if (isOn.equals("enable") || isOn.equals("disable")){
                boolean on = isOn.equals("enable") ? true:false;
                ninjaModePerPlayer.put(player.getName(), on);
                player.sendMessage("Mode ninja "+ (on ? "":"des") +"activé"); 
            }
            else
                return false;
        }
        return true;
    }
}