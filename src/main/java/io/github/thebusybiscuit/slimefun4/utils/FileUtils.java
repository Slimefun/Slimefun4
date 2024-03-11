package io.github.thebusybiscuit.slimefun4.utils;

import java.io.File;

public class FileUtils {

    public static boolean deleteDirectory(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Recursive call to delete files and subfolders
                    if (!deleteDirectory(file)) {
                        return false;
                    }
                }
            }
        }

        // Delete the folder itself
        return folder.delete();
    }
}
