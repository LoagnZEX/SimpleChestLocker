package com.logan.simplechestlock.simplechestlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class Main extends JavaPlugin {

    private static List<Map<?, ?>> LocationChestLinkPlayerUUID;
    private static HashMap<Location, String> HASH;
    private static Main main;

    private static File dataFile;
    private static YamlConfiguration yaml;

    private String dataFileName = "SimpleChestLockerData.yml";
    final static String sectionName = "LOCATIONLINKPLAYER";

    public static Main getInstanceOfMain() {
        return main;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new onClickChest(), this);
        main = this;

        try {
            initiateFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateFiles() throws Exception {
        //data folder
        if (!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //datafile
        dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("SimpleChestLocker").getDataFolder(), dataFileName);
        if (!dataFile.exists()) {
            dataFile.createNewFile();
        }
        yaml = YamlConfiguration.loadConfiguration(dataFile);


        if (!yaml.contains(sectionName)) {
            yaml.createSection(sectionName);
        }

        if (!yaml.isList(sectionName)) {
            LocationChestLinkPlayerUUID = new ArrayList<>();
            LocationChestLinkPlayerUUID.add(new HashMap<Location, String>());

            yaml.set(sectionName, LocationChestLinkPlayerUUID);
        }

        LocationChestLinkPlayerUUID = yaml.getMapList(sectionName);
        HASH = (HashMap<Location, String>) LocationChestLinkPlayerUUID.get(0);
        saveReload();
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    public static HashMap<Location, String> getHASH() {
        return HASH;
    }

    public static void saveReload() throws IOException {
        save();
        reload();
    }

    private static void save() throws IOException {
        LocationChestLinkPlayerUUID.set(0,HASH);
        yaml.set(sectionName, LocationChestLinkPlayerUUID);
        yaml.save(dataFile);
    }

    public static void reload() {
        yaml = YamlConfiguration.loadConfiguration(dataFile);
        LocationChestLinkPlayerUUID = yaml.getMapList(sectionName);
    }

    @Override
    public void onDisable() {

        // Plugin shutdown logic
    }
}
