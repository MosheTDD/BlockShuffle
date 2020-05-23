package me.moshe.blockshuffle.commands;

import me.moshe.blockshuffle.BlockShuffle;
import me.moshe.blockshuffle.listeners.BlockShuffleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static me.moshe.blockshuffle.util.Utils.*;
public class BlockShuffleCommand implements CommandExecutor {

    private BlockShuffle plugin;

    public BlockShuffleCommand(BlockShuffle plugin) {
        this.plugin = plugin;
        plugin.getCommand("bs").setExecutor(this);
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!permCheck(sender, "blockshuffle.admin")) return true;
        if (!argsCheck(sender, 1, args)) return true;
        if (args[0].equalsIgnoreCase("all")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if(plugin.map.containsKey(all)){
                    plugin.map.remove(all);
                    all.sendMessage(colorize("&6You have &c&ldisabled &6Block Shuffle Mode."));
                    return true;
                }
                blockShuffleFunc(all);
            }
            return true;
        }
        Player t = Bukkit.getPlayerExact(args[0]);
        if (!offlineCheck(t, sender, args[0])) return true;
        if(plugin.map.containsKey(t)){
            plugin.map.remove(t);
            sendRawMsg(t,"&6You have &c&ldisabled &6Block Shuffle Mode.");
            return true;
        }
        blockShuffleFunc(t);
        return false;
    }

    public void blockShuffleFunc(Player p) {
        ArrayList<Material> blocks = new ArrayList<>();
        for (Material block : Material.values()) {
            if (block.isBlock()) {
                if (!block.isEdible() && block.isSolid() && !block.equals(Material.BARRIER)) {
                    blocks.add(block);
                }
            }
        }
        Random rnd = new Random();
        Material randomB = blocks.get((Math.abs(rnd.nextInt(blocks.size()))));
        System.out.println(randomB);
        plugin.map.put(p, randomB);
        String blockName = randomB.name().toLowerCase().replace("_", " ");
        sendRawMsg(p, "&6You must find and stand on a &c" + blockName + " &6within &b&l" + ci("timer") + " &6seconds!");
        new BukkitRunnable(){
            int countDown = ci("timer");
            public void run(){
                if(!plugin.map.containsKey(p)){
                    cancel();
                }
                if(countDown == 0){
                    p.setHealth(0);
                    plugin.map.remove(p);
                    BlockShuffleEvents.active = false;
                    cancel();
                }
                if(BlockShuffleEvents.active){
                    sendRawMsg(p,"&aGood job! You found the block!");
                    plugin.map.remove(p);
                    cancel();
                }
                if(countDown <= 10){
                    sendRawMsg(p, "&6You have &b&l" + countDown + " &6seconds left to find " + blockName);
                }
                countDown--;
            }
        }.runTaskTimer(plugin, 0,20L);
    }
}
