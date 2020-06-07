package io.github.starwishsama.extra;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class UpdateDownloader {
    private static final String downloadDir = SlimefunPlugin.instance.getServer().getUpdateFolder();
    private static final String browserUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36";

    public static void downloadUpdate(String address, String fileName) {
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) (url.openConnection());
            conn.setConnectTimeout(5_000);
            conn.setReadTimeout(5_000);
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", browserUA);

            long completeFileSize = conn.getContentLength();

            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            File saveDir = new File(downloadDir.replace("update", "plugins"));
            File file = new File(saveDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);
                Slimefun.getLogger().info("下载中, 进度 " + currentProgress + "%");
                bos.write(data, 0, x);
            }
            bos.close();
            in.close();
            fos.close();

            SlimefunPlugin.instance.getFile().deleteOnExit();
            Slimefun.getLogger().info(ChatColors.color("&a自动更新已完成, 重启服务端后即可更新到最新版本"));
        } catch (Exception e) {
            Slimefun.getLogger().log(Level.SEVERE, e, () -> "在下载时发生了错误");
        }
    }
}
