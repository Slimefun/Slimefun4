package me.mrCookieSlime.Slimefun.Setup;

import me.mrCookieSlime.Slimefun.api.Slimefun;

import java.io.File;
import java.util.logging.Level;

public final class Files {
	public final File researches = new File("plugins/Slimefun/Researches.yml");
	public final File config = new File("plugins/Slimefun/config.yml");
	public final File items = new File("plugins/Slimefun/Items.yml");
	public final File database = new File("data-storage/Slimefun/Players");
	public final File whitelist = new File("plugins/Slimefun/whitelist.yml");
	
	public void cleanup() {
		if (!researches.exists()) {
			Slimefun.getLogger().log(Level.WARNING, "###############################################");
			Slimefun.getLogger().log(Level.WARNING, "############## = -  信息  - = #################");
			Slimefun.getLogger().log(Level.WARNING, "###############################################");
			Slimefun.getLogger().log(Level.WARNING, "      ");
			Slimefun.getLogger().log(Level.WARNING, "Slimefun 警告:");
			Slimefun.getLogger().log(Level.WARNING, "         ");
			Slimefun.getLogger().log(Level.WARNING, "Slimefun 检测到文件已过时或不存在,");
			Slimefun.getLogger().log(Level.WARNING, "我们已自动生成了新文件以保证正常工作.");
			Slimefun.getLogger().log(Level.WARNING, "如果你此先曾经使用过 Slimefun,");
			Slimefun.getLogger().log(Level.WARNING, "你的配置文件已被删除, 请重新配置!");
			delete(new File("plugins/Slimefun"));
			delete(new File("data-storage/Slimefun"));
		}
		
		if (!database.exists()) {
			database.mkdirs();
		}
	}

    public boolean delete(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File current: files) {
                if (current.isDirectory()) {
                    if (!delete(current)) return false;
                }
                else {
                    if (!current.delete()) return false;
                }
            }
        }

        return folder.delete();
    }

}
