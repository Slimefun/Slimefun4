package io.github.starwishsama.extra;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;

public class UpdateChecker {
    private static List<GithubBean> getReleaseBean() {
        try {
            URL url = new URL("https://api.github.com/repos/StarWishsama/Slimefun4/releases");
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            String read = new String(readFully(bis), StandardCharsets.UTF_8);
            return new GsonBuilder().serializeNulls().create().fromJson(read, new TypeToken<List<GithubBean>>() {
            }.getType());
        } catch (Exception e) {
            if (e instanceof SocketException) {
                Slimefun.getLogger().log(Level.WARNING, "连接至 Github 服务器出错");
            } else {
                Slimefun.getLogger().log(Level.WARNING, "在获取更新时发生了异常", e);
            }
        }
        return null;
    }

    public static String getUpdateInfo(boolean isConsole) {
        List<GithubBean> bean = getReleaseBean();
        if (bean != null) {
            String[] splitVersion = SlimefunPlugin.getVersion().split("-");
            String version = splitVersion.length == 3 ? splitVersion[2] : "未知";

            int latest = Integer.parseInt(bean.get(0).getTag_name().split("-")[1]);
            if (!version.equals("未知") && StringUtils.isNumeric(version)) {
                int current = Integer.parseInt(version);
                if (current >= latest) {
                    return ChatColors.color("&a你正在使用最新版本 " + SlimefunPlugin.getVersion());
                } else {
                    String updateInfo = "&e有更新了 &7| &b" + bean.get(0).getTag_name() + " 现已发布";
                    if (!isConsole) {
                        updateInfo = updateInfo + "\n&r下载地址 > &7" + bean.get(0).getAssets().get(0).getBrowser_download_url();
                    }
                    return ChatColors.color(updateInfo);
                }
            }
        }
        return "无法获取到更新信息";
    }

    private static byte[] readFully(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            bos.write(buf, 0, len);
        }
        return bos.toByteArray();
    }
}
