package com.logan.simplechestlock.simplechestlock;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class onLeftClickChest implements Listener {

    @EventHandler
    public void onClickChest(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String pn = p.getName();
        Block b = e.getClickedBlock();
        Location bl = b.getLocation();
        Location blPlus = bl.add(0.5,1.2,0.5);
        Action a = e.getAction();


        try {
            if (b.getType().equals(Material.CHEST)) {
                if (a.equals(Action.LEFT_CLICK_BLOCK)) {
                    if (!(hash().containsKey(bl))) {
                        //if it is an unlocked chest
                        hash().put(bl, new ChestInfo(pn, bl, runTask(b, blPlus)));

                        p.sendMessage(ChatColor.GREEN + "chest is LOCKED");

                        System.out.println(pn = "has LOCKED a chest on location: " + bl.toVector().toString());

                    } else {
                        //if it is a locked chest
                        if (hash().get(bl).getOwner().equals(pn)) {
                            hash().get(bl).cancelRunnable();
                            hash().remove(bl);

                            p.sendMessage(ChatColor.GREEN + "chest is UNLOCKED");
                            System.out.println(pn = "has UNLOCKED a chest on location: " + bl.toVector().toString());
                        } else {
                            p.sendMessage(ChatColor.RED + "chest is locked by "
                                    + ChatColor.GOLD + hash().get(bl).getOwner()
                                    + ChatColor.RED + ", only owner of this chest can unlock this");
                        }
                    }
                } else if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (hash().get(bl).isLocked() && !hash().get(bl).getOwner().equals(pn)) {
                        //if it is a locked chest
                        e.setCancelled(true);
                        p.sendMessage("this chest is locked by "
                                + ChatColor.GOLD + hash().get(bl).getOwner());
                    }
                }
            } else {
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    private int runTask(Block b, Location blPlus) {
        return Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstanceOfMain(), new Runnable() {
            @Override
            public void run() {
                b.getWorld().spawnParticle(Particle.HEART, blPlus, 1);
            }
        }, 20, 100);
    }

    private HashMap<Location, ChestInfo> hash() {
        return Main.LocationLinkChest;
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        String pn = p.getName();
        Block b = e.getBlock();
        Location bl = b.getLocation();


        if (b.getType().equals(Material.CHEST)
                && hash().containsKey(bl)
                && !pn.equals(hash().get(bl).getOwner())) {
            e.setCancelled(true);
            p.sendMessage("this chest is locked by "
                    + ChatColor.GOLD + hash().get(bl).getOwner());
        }

    }
}


