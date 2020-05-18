package me.moshe.blockshuffle;

import me.moshe.blockshuffle.commands.BlockShuffleCommand;
import me.moshe.blockshuffle.listeners.BlockShuffleEvents;
import me.moshe.blockshuffle.util.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class BlockShuffle extends JavaPlugin {

    public Map<Player, Material> map = new HashMap<>();

    @Override
    public void onEnable(){
        new Utils(this);
        new BlockShuffleCommand(this);
        getServer().getPluginManager().registerEvents(new BlockShuffleEvents(this), this);
    }


}
