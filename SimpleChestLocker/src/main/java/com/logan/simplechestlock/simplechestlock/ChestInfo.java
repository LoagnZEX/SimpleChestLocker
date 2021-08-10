package com.logan.simplechestlock.simplechestlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestInfo {

    private boolean locked;
    private String owner;
    private final Location location;
    private int task = 0;

    public ChestInfo(String PlayerOwner , Location location, int taskIDrunnnable){
        this.owner = PlayerOwner;
        this.locked = true;
        this.location = location;
        this.task = taskIDrunnnable;
    }

    public void UnLock() {
        this.locked = false;
        this.owner = "null";
    }

    public boolean isLocked() {
        return locked;
    }

    public String getOwner(){
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public void cancelRunnable(){
        Bukkit.getServer().getScheduler().cancelTask(task);
    }
}
