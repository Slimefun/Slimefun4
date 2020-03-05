package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This Service creates a Backup of your Slimefun world data on every server shutdown.
 * 
 * @author TheBusyBiscuit
 *
 */
public class BackupService implements Runnable {

    @Override
    public void run() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

        File folder = new File("data-storage/Slimefun/block-backups");
        List<File> backups = Arrays.asList(folder.listFiles());

        if (backups.size() > 20) {
            deleteOldBackups(format, backups);
        }

        File file = new File("data-storage/Slimefun/block-backups/" + format.format(new Date()) + ".zip");

        if (!file.exists() || file.delete()) {
            try {
                if (file.createNewFile()) {
                    try (ZipOutputStream output = new ZipOutputStream(new FileOutputStream(file))) {
                        createBackup(output);
                    }

                    Slimefun.getLogger().log(Level.INFO, "Backed up Data to: " + file.getName());
                }
                else {
                    Slimefun.getLogger().log(Level.WARNING, "Could not create backup-file: " + file.getName());
                }
            }
            catch (IOException x) {
                Slimefun.getLogger().log(Level.SEVERE, "An Error occured while creating a World-Backup for Slimefun " + SlimefunPlugin.getVersion(), x);
            }
        }
    }

    private void createBackup(ZipOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];

        for (File folder : new File("data-storage/Slimefun/stored-blocks/").listFiles()) {
            for (File file : folder.listFiles()) {
                ZipEntry entry = new ZipEntry("stored-blocks/" + folder.getName() + '/' + file.getName());
                output.putNextEntry(entry);

                try (FileInputStream input = new FileInputStream(file)) {
                    int length;

                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                }

                output.closeEntry();
            }
        }

        for (File file : new File("data-storage/Slimefun/universal-inventories/").listFiles()) {
            ZipEntry entry = new ZipEntry("universal-inventories/" + file.getName());
            output.putNextEntry(entry);

            try (FileInputStream input = new FileInputStream(file)) {
                int length;

                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }

            output.closeEntry();
        }

        for (File file : new File("data-storage/Slimefun/stored-inventories/").listFiles()) {
            ZipEntry entry = new ZipEntry("stored-inventories/" + file.getName());
            output.putNextEntry(entry);

            try (FileInputStream input = new FileInputStream(file)) {
                int length;

                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }

            output.closeEntry();
        }

        File chunks = new File("data-storage/Slimefun/stored-chunks/chunks.sfc");

        if (chunks.exists()) {
            ZipEntry entry = new ZipEntry("stored-chunks/chunks.sfc");
            output.putNextEntry(entry);

            try (FileInputStream input = new FileInputStream(chunks)) {
                int length;

                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }

            output.closeEntry();
        }
    }

    private void deleteOldBackups(DateFormat format, List<File> backups) {
        Collections.sort(backups, (a, b) -> {
            try {
                return (int) (format.parse(a.getName().replace(".zip", "")).getTime() - format.parse(b.getName().replace(".zip", "")).getTime());
            }
            catch (ParseException e) {
                return 0;
            }
        });

        for (int i = backups.size() - 20; i > 0; i--) {
            if (!backups.get(i).delete()) {
                Slimefun.getLogger().log(Level.WARNING, "Could not delete Backup: " + backups.get(i).getName());
            }
        }
    }

}
