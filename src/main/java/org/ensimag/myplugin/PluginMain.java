package org.ensimag.myplugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.ensimag.myplugin.blockreplacing.*;
import org.ensimag.myplugin.chestsorting.*;

import java.io.File;

public class PluginMain extends JavaPlugin {
    public PluginMain() {
        super();
    }

    protected PluginMain(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        getLogger().info("Hop hop hop le launch du serv");

        this.getCommand("uwu").setExecutor(new CommandBasic());
        this.getCommand("chestsort").setExecutor(SortingCommand.getInstance());
        getServer().getPluginManager().registerEvents(ListenerBasic.getInstance(), this);
        getServer().getPluginManager().registerEvents(ChestSortingListener.getInstance(), this);
        getServer().getPluginManager().registerEvents(ItemReplacingListener.getInstance(), this);
        
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}