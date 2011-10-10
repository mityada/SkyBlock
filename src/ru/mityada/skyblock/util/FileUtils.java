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

package ru.mityada.skyblock.util;

import java.io.File;

public class FileUtils {

    public static boolean deleteFolder(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    if (!FileUtils.deleteFolder(f)) {
                        return false;
                    }
                }
            }
            file.delete();
            return !file.exists();
        } else {
            return false;
        }
    }

}