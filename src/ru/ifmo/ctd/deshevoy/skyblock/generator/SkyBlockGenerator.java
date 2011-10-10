/*
 * Bukkit server plugin based on the Noobcrew's SkyBlock map
 * Copyright (C) 2011 Dmitry Deshevoy
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

package ru.ifmo.ctd.deshevoy.skyblock.generator;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class SkyBlockGenerator extends ChunkGenerator {
    
    static byte GRASS = 0x02;
    static byte DIRT = 0x03;
    static byte BEDROCK = 0x07;
    static byte SAND = 0x0c;
    static byte WOOD = 0x11;
    static byte LEAVES = 0x12;
    static byte CHEST = 0x36;
    
    @Override
    public byte[] generate(World world, Random random, int x, int y) {
        byte[] result = new byte[32768];
        
        if (x == 0 && y == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 3; k++) {
                        result[getIndex(i, j + 64, k + 1)] = DIRT;
                    }
                }
            }
            
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 3; k++) {
                    result[getIndex(i, 66, k + 1)] = GRASS;
                }
            }
            
            result[getIndex(1, 64, 2)] = BEDROCK;
            result[getIndex(1, 65, 2)] = SAND;
            
            for (int i = 0; i < 5; i++) {
                for (int j = 6; j < 8; j++) {
                    for (int k = 0; k < 5; k++) {
                        if ((i != 0 || (k != 0 && k != 4)) && (i != 4 || (k != 0 && k != 4))) {
                            result[getIndex(i, j + 64, k)] = LEAVES;
                        }
                    }
                }
            }
            for (int i = 1; i < 4; i++) {
                for (int k = 1; k < 4; k++) {
                    result[getIndex(i, 72, k)] = LEAVES;
                }
            }
            result[getIndex(2, 73, 2)] = LEAVES;
            result[getIndex(1, 73, 2)] = LEAVES;
            result[getIndex(3, 73, 2)] = LEAVES;
            result[getIndex(2, 73, 1)] = LEAVES;
            result[getIndex(2, 73, 3)] = LEAVES;
            
            for (int j = 3; j < 9; j++) {
                result[getIndex(2, j + 64, 2)] = WOOD;
            }
        }
        
        return result;
    }
    
    private int getIndex(int x, int y, int z) {
        return (x * 16 + z) * 128 + y;
    }
    
    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 1, 67, 2);
    }
    
}
