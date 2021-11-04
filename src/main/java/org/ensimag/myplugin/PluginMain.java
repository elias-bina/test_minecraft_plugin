package org.ensimag.myplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!");

        // Register our command "kit" (set an instance of your command class as executor)
        this.getCommand("uwu").setExecutor(new CommandBasic());
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}