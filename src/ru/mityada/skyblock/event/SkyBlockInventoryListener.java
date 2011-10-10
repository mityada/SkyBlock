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

import java.io.File;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.Configuration;

import org.getspout.spoutapi.event.inventory.InventoryCraftEvent;
import org.getspout.spoutapi.event.inventory.InventoryListener;

public class SkyBlockInventoryListener extends InventoryListener {
    
    private static SkyBlock plugin;
    
    public SkyBlockInventoryListener(SkyBlock instance) {
        plugin = instance;
    }
    
    @Override
    public void onInventoryCraft(InventoryCraftEvent event) {
        Player player = event.getPlayer();
        World skyBlockWorld = plugin.getServer().getWorld(plugin.WORLD_PREFIX + player.getName());
        
        if (player.getWorld() != skyBlockWorld) {
            return;
        }
        
        ItemStack result = event.getResult();
        
        switch (result.getType()) {
            case SMOOTH_BRICK:
                processCraftAchievement(event, "brick", 40, "stone bricks");
                break;
            case TORCH:
                processCraftAchievement(event, "torch", 20, "torches");
                break;
            case MUSHROOM_SOUP:
                processCraftAchievement(event, "stew", 10, "mushroom stew");
                break;
            case JACK_O_LANTERN:
                processCraftAchievement(event, "lantern", 10, "Jack 'o' lanterns");
                break;
            case BOOKSHELF:
                processCraftAchievement(event, "bookshelf", 10, "bookshelves");
                break;
            case BREAD:
                processCraftAchievement(event, "bread", 10, "bread");
                break;
        }
    }
    
    private void processCraftAchievement(InventoryCraftEvent event, String achievement, int total, String item) {
        Player player = event.getPlayer();
        Configuration conf = new Configuration(new File(plugin.getDataFolder(), "players/" + player.getName() + ".yml"));
        conf.load();
        if (!conf.getBoolean("achievement." + achievement + ".done", false)) {
            int progress = conf.getInt("achievement." + achievement + ".progress", 0);
            progress += event.getResult().getAmount() * getCraftsCount(event);
            conf.setProperty("achievement." + achievement + ".progress", progress);
            if (progress >= total) {
                conf.setProperty("achievement." + achievement + ".done", true);
                plugin.getServer().broadcastMessage("[SkyBlock] " + player.getDisplayName() + " has crafted " + total + " " + item + "!");
            } else {
                player.sendMessage("[SkyBlock] You have crafted " + progress + "/" + total + " " + item);
            }
            conf.save();
        }
    }
    
    private int getCraftsCount(InventoryCraftEvent event) {
        ItemStack[][] receipe = event.getRecipe();
        int count;
        if (event.isShiftClick()) {
            count = 64;
            for (int i = 0; i < receipe.length; i++) {
                for (int j = 0; j < receipe.length; j++) {
                    if (receipe[i][j] != null) {
                        count = Math.min(count, receipe[i][j].getAmount());
                    }
                }
            }
        } else {
            count = 1;
        }
        return count;
    }
    
}
