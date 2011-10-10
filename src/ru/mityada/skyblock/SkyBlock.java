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

package ru.mityada.skyblock;

import ru.mityada.skyblock.generator.SkyBlockGenerator;
import ru.mityada.skyblock.event.SkyBlockInventoryListener;
import ru.mityada.skyblock.event.SkyBlockPlayerListener;
import ru.mityada.skyblock.util.FileUtils;

import java.io.File;
import java.util.logging.Logger;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.config.Configuration;

import com.onarandombox.MultiverseCore.MultiverseCore;

public class SkyBlock extends JavaPlugin {
    
    private static final Logger log = Logger.getLogger("Minecraft");
    private final SkyBlockPlayerListener playerListener = new SkyBlockPlayerListener(this);
    
    public String WORLD_PREFIX = "skyblock_";
    private Server server;
    private PluginManager pluginManager;
    private MultiverseCore mv;
    
    @Override
    public void onLoad() {
        log.info("[SkyBlock] SkyBlock loaded.");
        server = getServer();
        pluginManager = server.getPluginManager();
        
        Plugin plugin = pluginManager.getPlugin("Multiverse-Core");
        if (plugin == null) {
            log.warning("[SkyBlock] Multiverse-Core not found. Disabling...");
            server.getPluginManager().disablePlugin(this);
        } else {
            mv = (MultiverseCore)plugin;
        }
    }
    
    @Override
    public void onEnable() {   
        pluginManager.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Priority.High, this);
        
        if (pluginManager.getPlugin("Spout") == null) {
            log.warning("[SkyBlock] Spout not found.");
        } else {
            pluginManager.registerEvent(Event.Type.CUSTOM_EVENT, new SkyBlockInventoryListener(this), Priority.High, this);
        }
    }
    
    @Override
    public void onDisable() {
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = null;
        String worldName = "";
        
        if (sender instanceof Player) {
            player = (Player) sender;
            worldName = WORLD_PREFIX + player.getName();
        }
        
        if (cmd.getName().equalsIgnoreCase("skyblock")) {
            if (args.length == 0) {
                return false;
            }
            
            if (args[0].equalsIgnoreCase("enter")) {
                if (player == null) {
                    sender.sendMessage("This command can only be run by a player.");
                } else {
                    if (!player.hasPermission("skyblock.enter")) {
                        player.sendMessage("You don't have permission do to that.");
                        return true;
                    }
                    
                    World playerWorld = server.getWorld(worldName);
                    if (playerWorld == null) {
                        playerWorld = createWorld(worldName);
                    }
                    
                    player.teleport(playerWorld.getSpawnLocation());
                }
                
                return true;
            }
            
            if (args[0].equalsIgnoreCase("exit")) {
                if (player == null) {
                    sender.sendMessage("This command can only be run by a player");
                } else {
                    if (!player.hasPermission("skyblock.exit")) {
                        player.sendMessage("You don't have permission do to that.");
                        return true;
                    }
                    
                    World safe = server.getWorld("world");
                    player.teleport(safe.getSpawnLocation());
                }
                
                return true;
            }
            
            if (args[0].equalsIgnoreCase("reset")) {
                if (player == null) {
                    sender.sendMessage("This command can only be run by a player");
                } else {
                    if (!player.hasPermission("skyblock.reset")) {
                        player.sendMessage("You don't have permission do to that.");
                        return true;
                    }
                    
                    World playerWorld = server.getWorld(worldName);
                    boolean tp = false;
                    if (playerWorld != null) {
                        if (player.getLocation().getWorld() == playerWorld) {
                            player.teleport(playerWorld.getSpawnLocation());
                        }
                        reset(worldName, player);
                    } else {
                        player.sendMessage("Nothing do reset. Type '/skyblock start' first.");
                    }

                    player.sendMessage("Reset complete.");
                }
                
                return true;
            }
            
            if (args[0].equalsIgnoreCase("achievements")) {
                String playerName;
                if (args.length < 2) {
                    playerName = player.getName();
                } else {
                    if (server.getWorld(WORLD_PREFIX + args[1]) != null) {
                        playerName = args[1];
                    } else {
                        player.sendMessage("[SkyBlock] Player " + args[1] + " does not exists or does not have a SkyBlock world");
                        return true;
                    }
                }
                player.sendMessage("[SkyBlock] " + playerName + "'s achievements:");
                Configuration conf = new Configuration(new File(getDataFolder(), "players/" + playerName + ".yml"));
                conf.load();
                
                if (conf.getBoolean("achievement.brick.done", false)) {
                    player.sendMessage("Stone bricks: 40/40");
                } else {
                    player.sendMessage("Stone bricks: " + conf.getInt("achievement.brick.progress", 0) + "/40");
                }
                
                if (conf.getBoolean("achievement.torch.done", false)) {
                    player.sendMessage("Torches: 20/20");
                } else {
                    player.sendMessage("Torches: " + conf.getInt("achievement.torch.progress", 0) + "/20");
                }
                
                if (conf.getBoolean("achievement.stew.done", false)) {
                    player.sendMessage("Mushroom stew: 10/10");
                } else {
                    player.sendMessage("Mushroom stew: " + conf.getInt("achievement.stew.progress", 0) + "/10");
                }
                
                if (conf.getBoolean("achievement.lantern.done", false)) {
                    player.sendMessage("Jack 'o' lanterns: 10/10");
                } else {
                    player.sendMessage("Jack 'o' lanterns: " + conf.getInt("achievement.lantern.progress", 0) + "/10");
                }
                
                if (conf.getBoolean("achievement.bookshelf.done", false)) {
                    player.sendMessage("Bookshelves: 10/10");
                } else {
                    player.sendMessage("Bookshelves: " + conf.getInt("achievement.bookshelf.progress", 0) + "/10");
                }
                
                if (conf.getBoolean("achievement.bread.done", false)) {
                    player.sendMessage("Bread: 10/10");
                } else {
                    player.sendMessage("Bread: " + conf.getInt("achievement.bread.progress", 0) + "/10");
                }
                
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new SkyBlockGenerator();
    }
    
    private World createWorld(String name) {
        mv.getWorldManager().addWorld(name, World.Environment.NORMAL, null, "SkyBlock");
        World world = server.getWorld(name);
        mv.getWorldManager().getMVWorld(name).setSpawn(world.getSpawnLocation());
        fillChest(world);
        return server.getWorld(name);
    }
    
    private void reset(String name, Player player) {
        World world = server.getWorld(name);
        List<Entity> entities = world.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            if (!(entities.get(i) instanceof Player)) {
                entities.get(i).remove();
            }
        }
        clearInventory(world, player);
        Chunk[] chunks = world.getLoadedChunks();
        for (int i = 0; i < chunks.length; i++) {
            world.regenerateChunk(chunks[i].getX(), chunks[i].getZ());
        }
        while (world.getBlockAt(2, 67, 3).getType() != Material.CHEST) {
            fillChest(world);
        }
        
        Configuration conf = new Configuration(new File(getDataFolder(), "players/" + player.getName() + ".yml"));
        conf.load();
        conf.removeProperty("achievement");
        conf.save();
    }
    
    private void fillChest(World world) {
        Block block = world.getBlockAt(2, 67, 3);
        if (block.getType() == Material.CHEST) {
            Chest chest = (Chest)block.getState();
            Inventory inv = chest.getInventory();
            inv.clear();
        } else {
            block.setType(Material.CHEST);
        }
        Chest chest = (Chest)block.getState();
        Inventory inv = chest.getInventory();
        inv.addItem(new ItemStack(Material.LAVA_BUCKET, 1));
        inv.addItem(new ItemStack(Material.ICE, 2));
        inv.addItem(new ItemStack(Material.MELON, 1));
    }
    
    private void clearInventory(World world, Player player) {
        File pluginsFolder = new File(getDataFolder().getAbsolutePath()).getParentFile();
        File worldInv = new File(pluginsFolder.getAbsolutePath() + File.separator + "MultiInv" + File.separator + "Worlds" + File.separator + world.getName());
        FileUtils.deleteFolder(worldInv);
        if (player.getLocation().getWorld() == world) {
            player.getInventory().clear();
        }
    }
}
