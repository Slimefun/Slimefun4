package io.github.starwishsama.extra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Nameless
 */
public class SlimefunUpdater {

    private GithubBean updateInfoCache;
    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private static final String downloadDir = SlimefunPlugin.instance.getServer().getUpdateFolder();
    private static final String browserUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36";
    private static SlimefunBranch branch;

    /**
     * 下载文件
     *
     * @param address  下载地址
     * @param fileName 下载文件的名称
     */
    public static void downloadUpdate(String address, String fileName) {
        File file = new File(downloadDir.replace("update", "plugins"), fileName);

        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) (url.openConnection());
            conn.setConnectTimeout(5_000);
            conn.setReadTimeout(5_000);
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", browserUA);

            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                bos.write(data, 0, x);
            }

            bos.close();
            in.close();
            fos.close();

            SlimefunPlugin.instance.getFile().deleteOnExit();
            Slimefun.getLogger().info(ChatColors.color("&a自动更新已完成, 重启服务端后即可更新到最新版本"));
        } catch (Exception e) {
            file.delete();

            if (e.getCause() instanceof SSLException) {
                Slimefun.getLogger().log(Level.SEVERE, e, () -> "在下载时发生了错误: 连接超时");
                return;
            }

            Slimefun.getLogger().log(Level.SEVERE, e, () -> "在下载时发生了错误");
        }
    }

    /**
     * 使用 Github API 获取 Releases 信息
     *
     * @return Github Beans
     */
    private List<GithubBean> getReleaseBean() {
        try {
            URL url = new URL("https://api.github.com/repos/StarWishsama/Slimefun4/releases");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5_000);
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

    /**
     * 获取最新版本的信息
     *
     * @return 最新的 Bean
     */
    private GithubBean getGithubBean() {
        List<GithubBean> beans = getReleaseBean();

        if (!beans.isEmpty()) {
            updateInfoCache = beans.get(0);
            return beans.get(0);
        }

        return null;
    }

    /**
     * 检查更新
     */
    public void checkUpdate() {
        GithubBean bean = getCache();

        if (bean != null) {
            if (isOldVersion(SlimefunPlugin.getVersion(), bean.getTagName())) {
                String updateInfo = "有更新了 | " + bean.getTagName() + " 现已发布\n正在自动下载更新中, 下载完成后重启服务器生效";
                Slimefun.getLogger().info(updateInfo);
                downloadUpdate(getCache().getAssets().get(0).getDownloadUrl(), getCache().getAssets().get(0).getName());
            } else {
                Slimefun.getLogger().info(ChatColors.color("&a你正在使用最新版本 " + SlimefunPlugin.getVersion()));
            }
        } else {
            Slimefun.getLogger().info("无法获取到更新信息");
        }
    }

    private boolean isOldVersion(String current, String versionToCompare) {
        Validate.notEmpty(current, "Current version code can't be null!");
        Validate.notEmpty(versionToCompare, "Compare version code can't be empty!");
        int currentVersion;
        int comparedVersion;
        int splitLocation = 1;

        if (branch == SlimefunBranch.STABLE) {
            splitLocation = 2;
        }

        try {
            currentVersion = Integer.parseInt(current.split(" ")[splitLocation]);
            comparedVersion = Integer.parseInt(versionToCompare.split(" ")[splitLocation]);
            return currentVersion >= comparedVersion;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private GithubBean getCache() {
        return updateInfoCache == null ? getGithubBean() : updateInfoCache;
    }

    public static void autoSelectBranch(JavaPlugin plugin) {
        String version = plugin.getDescription().getVersion();

        if (version.contains("RC")) {
            branch = SlimefunBranch.STABLE;
            return;
        }

        branch = SlimefunBranch.DEVELOPMENT;
    }
}
