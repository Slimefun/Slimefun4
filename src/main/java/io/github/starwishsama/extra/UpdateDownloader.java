package io.github.starwishsama.extra;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class UpdateDownloader {
    private static final String downloadDir = SlimefunPlugin.instance.getServer().getUpdateFolder();
    private static final String browserUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36";

    public static void downloadUpdate(String address, String fileName) {
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5_000);
            conn.setReadTimeout(5_000);
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", browserUA);

            InputStream in = conn.getInputStream();
            byte[] data = readInputStream(conn.getInputStream());
            File saveDir = new File(downloadDir.replace("update", "plugins"));
            File file = new File(saveDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);

            fos.close();
            in.close();

            SlimefunPlugin.instance.getFile().deleteOnExit();

            Slimefun.getLogger().info(ChatColors.color("&a自动更新已完成, 重启服务端后即可更新到最新版本"));
        } catch (Exception e) {
            Slimefun.getLogger().log(Level.SEVERE, e, () -> "在下载时发生了错误");
        }
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int leng;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        while ((leng = inputStream.read(buffer)) != -1) {
            b.write(buffer, 0, leng);
        }
        b.close();

        return b.toByteArray();
    }
}
