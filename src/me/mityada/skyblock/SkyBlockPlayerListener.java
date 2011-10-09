package me.mityada.skyblock;

import org.bukkit.World;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SkyBlockPlayerListener extends PlayerListener {
    
    private static SkyBlock plugin;
    
    public SkyBlockPlayerListener(SkyBlock instance) {
        plugin = instance;
    }
    
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        World skyBlockWorld = plugin.getServer().getWorld(plugin.WORLD_PREFIX + event.getPlayer().getName());
        World playerWorld = event.getPlayer().getLocation().getWorld();
        
        if (playerWorld == skyBlockWorld && event.getRespawnLocation().getWorld() != skyBlockWorld) {
            event.setRespawnLocation(skyBlockWorld.getSpawnLocation());
        }
    }
    
}
