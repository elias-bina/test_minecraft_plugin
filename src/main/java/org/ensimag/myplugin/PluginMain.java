package org.ensimag.myplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.ensimag.myplugin.blockreplacing.*;
import org.ensimag.myplugin.chestsorting.*;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Hop hop hop le launch du serv");

        this.getCommand("uwu").setExecutor(new CommandBasic());
        this.getCommand("chestsort").setExecutor(SortingCommand.getInstance());
        getServer().getPluginManager().registerEvents(new ListenerBasic(), this);
        getServer().getPluginManager().registerEvents(new ChestSortingListener(), this);
        getServer().getPluginManager().registerEvents(new ItemReplacingListener(), this);
        
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}