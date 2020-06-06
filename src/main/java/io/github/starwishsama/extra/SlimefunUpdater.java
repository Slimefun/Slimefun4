package io.github.starwishsama.extra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author StarWishsama
 */
public class SlimefunUpdater {

    private GithubBean updateInfoCache;
    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private List<GithubBean> getReleaseBean() {
        try {
            URL url = new URL("https://api.github.com/repos/StarWishsama/Slimefun4/releases");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(8_000);
            conn.addRequestProperty("Accept-Charset", "UTF-8");
            conn.addRequestProperty("User-Agent", "Slimefun 4 Update Checker by StarWishsama");
            conn.setDoOutput(true);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String cache;
            StringBuilder result = new StringBuilder();
            while ((cache = br.readLine()) != null) {
                result.append(cache);
            }

            conn.disconnect();

            return gson.fromJson(result.toString().trim(), new TypeToken<List<GithubBean>>() {
            }.getType());
        } catch (IOException e) {
            if (e instanceof SocketException) {
                Slimefun.getLogger().log(Level.WARNING, "连接至 Github 服务器出错");
            } else {
                Slimefun.getLogger().log(Level.WARNING, "在获取更新时发生了异常");
            }
        } catch (JsonSyntaxException e) {
            Slimefun.getLogger().log(Level.WARNING, "从服务器获取到的 json 文本不正确");
        }

        return new ArrayList<>();
    }

    private GithubBean getGithubBean() {
        List<GithubBean> beans = getReleaseBean();

        if (!beans.isEmpty()) {
            updateInfoCache = beans.get(0);
            return beans.get(0);
        }

        return null;
    }

    public void checkUpdate() {
        GithubBean bean = getCache();

        if (bean != null) {
            String[] splitVersion = SlimefunPlugin.getVersion().split("-");
            String version = splitVersion.length >= 3 ? splitVersion[2] : "";

            int latest = Integer.parseInt(bean.getTagName().split("-")[1]);
            if (!version.isEmpty() && StringUtils.isNumeric(version)) {
                int current = Integer.parseInt(version);
                if (current >= latest && splitVersion.length == 3) {
                    Slimefun.getLogger().info(ChatColors.color("&a你正在使用最新版本 " + SlimefunPlugin.getVersion()));
                } else {
                    String updateInfo = "&e有更新了 &7| &b" + bean.getTagName() + " 现已发布\n&a正在自动下载更新中, 下载完成后重启服务器生效";
                    Slimefun.getLogger().info(ChatColors.color(updateInfo));
                    UpdateDownloader.downloadUpdate(getCache().getAssets().get(0).getDownloadUrl(), getCache().getAssets().get(0).getName());
                }
            }
        }

        Slimefun.getLogger().info("无法获取到更新信息");
    }

    private GithubBean getCache() {
        return updateInfoCache == null ? getGithubBean() : updateInfoCache;
    }
}
