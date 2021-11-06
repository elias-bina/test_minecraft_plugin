package org.ensimag.myplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.ensimag.myplugin.blockreplacing.*;
import org.ensimag.myplugin.chestsorting.*;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Hop hop hop le launch du serv");

        this.getCommand("uwu").setExecutor(new CommandBasic());
        this.getCommand("sortchest").setExecutor(SortingCommand.getInstance());
        this.getCommand("ninjamode").setExecutor(NinjaModeCommand.getInstance());
        getServer().getPluginManager().registerEvents(ListenerBasic.getInstance(), this);
        getServer().getPluginManager().registerEvents(ChestSortingListener.getInstance(), this);
        getServer().getPluginManager().registerEvents(ItemReplacingListener.getInstance(), this);
        
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}