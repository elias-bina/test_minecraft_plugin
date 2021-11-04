package org.ensimag.myplugin;

import org.bukkit.event.Listener;

public class BasicListener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MyListener(), this);
    }
}
