package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import me.mrCookieSlime.CSCoreLibPlugin.general.Clock;

public class SlimefunBackup {
	
	public static void start() {
		File folder = new File("data-storage/Slimefun/block-backups");
		List<File> backups = Arrays.asList(folder.listFiles());
		if (backups.size() > 20) {
			Collections.sort(backups, new Comparator<File>() {

				@Override
				public int compare(File f1, File f2) {
					try {
						return (int) (new SimpleDateFormat("yyyy-MM-dd-HH-mm").parse(f1.getName().replace(".zip", "")).getTime() - new SimpleDateFormat("yyyy-MM-dd-HH-mm").parse(f2.getName().replace(".zip", "")).getTime());
					} catch (ParseException e) {
						return 0;
					}
				}
			});

			for (int i = backups.size() - 20; i > 0; i--) {
				backups.get(i).delete();
			}
		}

		File file = new File("data-storage/Slimefun/block-backups/" + Clock.format(new Date()) + ".zip");
		byte[] buffer = new byte[1024];

		if (file.exists()) {
			file.delete();
		}

		try {
			file.createNewFile();

			ZipOutputStream output = new ZipOutputStream(new FileOutputStream(file));

			for (File f1: new File("data-storage/Slimefun/stored-blocks/").listFiles()) {
				for (File f: f1.listFiles()) {
					ZipEntry entry = new ZipEntry("stored-blocks/" + f1.getName() + "/" + f.getName());
					output.putNextEntry(entry);
					FileInputStream input = new FileInputStream(f);

					int length;
					while ((length = input.read(buffer)) > 0) {
						output.write(buffer, 0, length);
					}

					input.close();
					output.closeEntry();
				}
			}

			for (File f: new File("data-storage/Slimefun/universal-inventories/").listFiles()) {
				ZipEntry entry = new ZipEntry("universal-inventories/" + f.getName());
				output.putNextEntry(entry);
				FileInputStream input = new FileInputStream(f);

				int length;
				while ((length = input.read(buffer)) > 0) {
					output.write(buffer, 0, length);
				}

				input.close();
				output.closeEntry();
			}

			for (File f: new File("data-storage/Slimefun/stored-inventories/").listFiles()) {
				ZipEntry entry = new ZipEntry("stored-inventories/" + f.getName());
				output.putNextEntry(entry);
				FileInputStream input = new FileInputStream(f);

				int length;
				while ((length = input.read(buffer)) > 0) {
					output.write(buffer, 0, length);
				}

				input.close();
				output.closeEntry();
			}

			if (new File("data-storage/Slimefun/stored-chunks/chunks.sfc").exists()) {
				ZipEntry entry = new ZipEntry("stored-chunks/chunks.sfc");
				output.putNextEntry(entry);
				FileInputStream input = new FileInputStream(new File("data-storage/Slimefun/stored-chunks/chunks.sfc"));

				int length;
				while ((length = input.read(buffer)) > 0) {
					output.write(buffer, 0, length);
				}

				input.close();
				output.closeEntry();
			}

			output.close();
			System.out.println("[Slimfun] Backed up Blocks to " + file.getName());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
