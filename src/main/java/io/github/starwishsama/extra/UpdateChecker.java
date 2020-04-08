package io.github.starwishsama.extra;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

public class UpdateChecker {
    private static List<GithubBean> getReleaseBean() {
        try {
            URL url = new URL("https://api.github.com/repos/StarWishsama/Slimefun4/releases");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String str;
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }

                return new GsonBuilder().serializeNulls().create().fromJson(sb.toString().trim(), new TypeToken<List<GithubBean>>() {
                }.getType());
            }

            conn.disconnect();
        } catch (Exception e) {
            if (e instanceof SocketException) {
                Slimefun.getLogger().log(Level.WARNING, "连接至 Github 更新服务器超时");
            } else {
                Slimefun.getLogger().log(Level.WARNING, "在获取更新时发生了异常", e);
            }
        }
        return null;
    }

    public static String getUpdateInfo() {
        List<GithubBean> bean = getReleaseBean();
        if (bean != null) {
            String[] splitVersion = SlimefunPlugin.getVersion().split("-");
            String version = splitVersion.length == 3 ? splitVersion[2] : "未知";

            int latest = Integer.parseInt(bean.get(0).getTag_name().split("-")[1]);
            if (!version.equals("未知") && StringUtils.isNumeric(version)) {
                int current = Integer.parseInt(version);
                if (current > latest) {
                    return "你正在使用最新版本";
                } else {
                    return "有更新了 | " + bean.get(0).getTag_name() + " 现已发布\n下载地址 > " + bean.get(0).getAssets().get(0).getBrowser_download_url();
                }
            }
        }
        return "无法获取到更新信息";
    }
}
