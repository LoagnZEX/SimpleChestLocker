package com.logan.simplechestlock.simplechestlock;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    public static HashMap<Location, ChestInfo> LocationLinkChest;
    private static Main main;

    public static Main getInstanceOfMain(){
        return main;
    }

    @Override
    public void onEnable() {

        main = this;
        LocationLinkChest = new HashMap<Location, ChestInfo>();

        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new onLeftClickChest(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
