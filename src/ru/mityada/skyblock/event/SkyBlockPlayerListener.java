/*
 * Bukkit server plugin based on the Noobcrew's SkyBlock map
 * Copyright (C) 2011 mityada
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.mityada.skyblock.event;

import ru.mityada.skyblock.SkyBlock;

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
