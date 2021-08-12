package com.logan.simplechestlock.simplechestlock;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ClickChest implements Listener {

    @EventHandler
    public void onClickChest(PlayerInteractEvent e) throws IOException, NullPointerException {

        if (e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.CHEST)) {
            //initialize paras*************************************
            Player p = e.getPlayer();
            String pUUID = p.getUniqueId().toString();
            Block b = e.getClickedBlock();
            Location bl = b.getLocation();

            HashMap<Location, String> h = Main.getHASH();

            Action a = e.getAction();
            //initialize paras*************************************

            if (a.equals(Action.LEFT_CLICK_BLOCK) && p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {

                if (!h.containsKey(bl)) {
                    //if it is an unlocked chest
                    p.sendMessage(ChatColor.GREEN + "chest is LOCKED");
                    Bukkit.getLogger().info(ChatColor.GREEN + p.getName() + " has LOCKED a chest on location: " + bl.toVector().toString());

                    h.put(bl, pUUID);

                } else if (h.containsKey(bl)) {
                    //if it is a locked chest
                    if (h.get(bl).equals(pUUID)) {
                        p.sendMessage(ChatColor.GREEN + "the chest is UNLOCKED");
                        Bukkit.getLogger().info(ChatColor.GREEN + p.getName() + " has UNLOCKED a chest on location: " + bl.toVector().toString());

                        h.remove(bl);
                    } else {
                        p.sendMessage("only "
                                + Bukkit.getOfflinePlayer(UUID.fromString(h.get(bl))).getName()
                                + " can unlock this chest");
                    }

                } else {
                    System.out.println("something is wrong!");
                }
            } else if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
                if (h.containsKey(bl)) {
                    //when chest has owner
                    if (!h.get(bl).equals(pUUID)) {
                        p.sendMessage("only "
                                + Bukkit.getOfflinePlayer(UUID.fromString(h.get(bl))).getName()
                                + " can open this chest");
                        e.setCancelled(true);
                    }
                }
            }
        }

        Main.saveReload();
    }

//    private int runTask(Block b, Location blPlus) {
//        return Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstanceOfMain(), new Runnable() {
//            @Override
//            public void run() {
//                b.getWorld().spawnParticle(Particle.HEART, blPlus, 1);
//            }
//        }, 20, 100);
//    }


    @EventHandler
    public void onChestBreak(BlockBreakEvent e) {

        if (e.getBlock().getType().equals(Material.CHEST)) {
            Player p = e.getPlayer();
            Block b = e.getBlock();
            Location bl = b.getLocation();
            HashMap<Location, String> h = Main.getHASH();

            if (h.containsKey(bl)) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "this chest has a lock, can not be destroyed, it belongs to " + Bukkit.getOfflinePlayer(UUID.fromString(h.get(bl))).getName());
            }
        }
    }
}


