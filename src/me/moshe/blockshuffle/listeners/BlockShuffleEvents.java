package me.moshe.blockshuffle.listeners;

import me.moshe.blockshuffle.BlockShuffle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockShuffleEvents implements Listener {
    private BlockShuffle plugin;

    public BlockShuffleEvents(BlockShuffle plugin) {
        this.plugin = plugin;
    }

    public static boolean active = false;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (plugin.map.containsKey(p)) {
            Material block = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
            if (block == Material.AIR) return;
            if (block == plugin.map.get(p)) {
                active = true;
                Bukkit.getScheduler().runTaskLater(plugin, () -> active = false, 20);
                return;
            }
        }
    }
}
