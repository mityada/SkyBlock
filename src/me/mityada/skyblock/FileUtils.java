package me.mityada.skyblock;

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