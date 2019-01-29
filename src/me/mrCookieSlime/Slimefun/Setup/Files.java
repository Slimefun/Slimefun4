package me.mrCookieSlime.Slimefun.Setup;

import java.io.File;

public class Files {
	
	public static File RESEARCHES = new File("plugins/Slimefun/Researches.yml");
	public static File CONFIG = new File("plugins/Slimefun/config.yml");
	public static File ITEMS = new File("plugins/Slimefun/Items.yml");
	public static File DATABASE = new File("data-storage/Slimefun/Players");
	public static File WHITELIST = new File("plugins/Slimefun/whitelist.yml");
	public static File MESSAGES = new File("plugins/Slimefun/messages.yml");
	
	public static void cleanup() {
		if (!RESEARCHES.exists()) {
			System.err.println("###############################################");
			System.err.println("############## = -  信息  - = #################");
			System.err.println("###############################################");
			System.err.println("      ");
			System.err.println("Slimefun 警告:");
			System.err.println("         ");
			System.err.println("Slimefun 检测到文件已过时或不存在,");
			System.err.println("我们已自动生成了新文件以保证正常工作.");
			System.err.println("如果你此先曾经使用过 Slimefun,");
			System.err.println("你的配置文件已被删除, 请重新配置!");
			delete(new File("plugins/Slimefun"));
			delete(new File("data-storage/Slimefun"));
		}
		
		if (!DATABASE.exists()) {
			DATABASE.mkdirs();
		}
	}
	
	public static void delete(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File current: files) {
				if (current.isDirectory()) {
					delete(current);
				}
				else {
					current.delete();
				}
			}
		}
		folder.delete();
	}

}
