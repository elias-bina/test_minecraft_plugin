package org.ensimag.myplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.ensimag.myplugin.chestsorting.ChestSortingListener;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!");

        this.getCommand("uwu").setExecutor(new CommandBasic());
        getServer().getPluginManager().registerEvents(new ListenerBasic(), this);
        getServer().getPluginManager().registerEvents(new ChestSortingListener(), this);
        
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}