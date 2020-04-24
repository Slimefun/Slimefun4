package io.github.starwishsama.extra;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author StarWishsama
 */
public class UpdateChecker {
    private static List<GithubBean> getReleaseBean(){
        try {
            URL url = new URL("https://api.github.com/repos/StarWishsama/Slimefun4/releases");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(150_000);
            conn.setReadTimeout(150_000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
            conn.setRequestProperty("content-type", "application/json; charset=utf-8");
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String cache;
            StringBuilder result = new StringBuilder();
            for (cache = br.readLine(); cache != null; cache = br.readLine()) {
                result.append(cache);
            }

            conn.disconnect();
            return new GsonBuilder().serializeNulls().create().fromJson(result.toString().trim(), new TypeToken<List<GithubBean>>() {
            }.getType());
        } catch (Exception e){
            e.printStackTrace();
            /**if (e instanceof SocketException) {
             Slimefun.getLogger().log(Level.WARNING, "连接至 Github 服务器出错");
             } else {
             Slimefun.getLogger().log(Level.WARNING, "在获取更新时发生了异常", e);
             }*/
        }
        return new ArrayList<>();
    }

    public static String getUpdateInfo() {
        List<GithubBean> bean = getReleaseBean();
        if (!bean.isEmpty()) {
            String[] splitVersion = SlimefunPlugin.getVersion().split("-");
            String version = splitVersion.length >= 3 ? splitVersion[2] : "未知";

            int latest = Integer.parseInt(bean.get(0).getTag_name().split("-")[1]);
            if (!version.equals("未知") && StringUtils.isNumeric(version)) {
                int current = Integer.parseInt(version);
                if (current >= latest && splitVersion.length == 3) {
                    return ChatColors.color("&a你正在使用最新版本 " + SlimefunPlugin.getVersion());
                } else {
                    String updateInfo = "&e有更新了 &7| &b" + bean.get(0).getTag_name() + " 现已发布\n&r下载地址 > &7" + bean.get(0).getAssets().get(0).getBrowser_download_url();
                    return ChatColors.color(updateInfo);
                }
            }
        }
        return "无法获取到更新信息";
    }
}
