package io.github.starwishsama.extra;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author StarWishsama
 */
public class UpdateChecker {
    private static List<GithubBean> getReleaseBean(){
        try {
            URL url = new URL("https://api.github.com/repos/StarWishsama/Slimefun4/releases");
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(8000);
            conn.addRequestProperty("Accept-Charset", "UTF-8");
            conn.addRequestProperty("User-Agent", "Slimefun 4 Update Checker by StarWishsama");
            conn.setDoOutput(true);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String cache;
            StringBuilder result = new StringBuilder();
            while ((cache = br.readLine()) != null) {
                result.append(cache);
            }

            return new GsonBuilder().serializeNulls().create().fromJson(result.toString().trim(), new TypeToken<List<GithubBean>>() {
            }.getType());
        } catch (Exception e) {
            if (e instanceof SocketException) {
                Slimefun.getLogger().log(Level.WARNING, "连接至 Github 服务器出错");
            } else {
                Slimefun.getLogger().log(Level.WARNING, "在获取更新时发生了异常");
            }
        }
        return new ArrayList<>();
    }

    public static String getUpdateInfo() {
        List<GithubBean> bean = getReleaseBean();

        if (!bean.isEmpty()) {
            String[] splitVersion = SlimefunPlugin.getVersion().split("-");
            String version = splitVersion.length >= 3 ? splitVersion[2] : "";

            int latest = Integer.parseInt(bean.get(0).getTagName().split("-")[1]);
            if (!version.isEmpty() && StringUtils.isNumeric(version)) {
                int current = Integer.parseInt(version);
                if (current >= latest && splitVersion.length == 3) {
                    return ChatColors.color("&a你正在使用最新版本 " + SlimefunPlugin.getVersion());
                } else {
                    String updateInfo = "&e有更新了 &7| &b" + bean.get(0).getTagName() + " 现已发布\n&r下载地址 > &7" + bean.get(0).getAssets().get(0).getBrowser_download_url();
                    return ChatColors.color(updateInfo);
                }
            }
        }

        return "无法获取到更新信息";
    }
}
