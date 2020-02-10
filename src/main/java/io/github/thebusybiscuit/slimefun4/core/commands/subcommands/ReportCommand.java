package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.slimefun4.api.ErrorReport;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ReportCommand extends SubCommand {

    private final JsonParser parser = new JsonParser();

    private final String issueFormat = "## Description (Required)\n" +
            "<!-- A clear and detailed description of what exactly the Issue consists of. -->\n" +
            "\n" +
            "## Steps to reproduce the Issue (Required)\n" +
            "<!-- Youtube Videos and Screenshots are recommended! -->\n" +
            "\n" +
            "## Expected behavior (Required)\n" +
            "<!-- What did you expect to happen? -->\n" +
            "\n" +
            "## Server Log / Error Report\n" +
            "<!-- This has been automatically generated for you -->\n" +
            "<details>\n<summary>Generated error/warning log</summary>\n%s\n</details>\n\n" + // First insertion by us
            "## Environment (Required)\n" +
            "<!-- This has been automatically generated for you -->\n" +
            "%s"; // Second insertion by us

    private final String githubUrl = "https://github.com/TheBusyBiscuit/Slimefun4/issues/new?labels=Bug%20Report&body=";

    public ReportCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "report";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("slimefun.report")) {
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
            return;
        }

        String str;
        try {
            str = githubUrl + URLEncoder.encode(
                    String.format(issueFormat,
                            ErrorReport.sumErrors(),
                            createEnv()
                    ),
                    StandardCharsets.UTF_8.name()
            );
        } catch (UnsupportedEncodingException e) {
            ErrorReport.error("Failed to encode URL", e);
            return;
        }

        String url = getURL(str);
        if (url != null)
            sender.sendMessage(ChatColor.GRAY + "You can open up a new Slimefun issue here: " + url);
        else
            sender.sendMessage(ChatColor.RED + "Failed to create SF issue link! Please check console!");
    }

    private String getURL(String ghUrl) {
        if (ghUrl == null) return null;

        int responseCode = -1;
        JsonObject obj = null;
        IOException ex = null;
        try {
            HttpsURLConnection httpClient = (HttpsURLConnection) new URL("https://slimefun.dev/new").openConnection();

            httpClient.setRequestMethod("POST");
            httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpClient.setRequestProperty("Content-Type", "application/json");

            JsonObject data = new JsonObject();
            data.addProperty("url", ghUrl);

            httpClient.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
                wr.writeBytes(data.toString());
                wr.flush();
            }

            responseCode = httpClient.getResponseCode();
            InputStream is = responseCode != 200 ? httpClient.getErrorStream() : httpClient.getInputStream();
            obj = parser.parse(new InputStreamReader(is)).getAsJsonObject();

            if (responseCode == 200)
                return obj.get("url").getAsString();
        } catch (IOException e) {
            ex = e;
        }

        ErrorReport.error("Failed to post report (" + responseCode + ") - "
                + (obj != null ? obj.get("error").getAsString() : ex.getMessage()),
                ex
        );
        return null;
    }

    private String createEnv() {
        String versions = " - Minecraft Version: " + Bukkit.getName() + ' ' + ReflectionUtils.getVersion() + "\n" +
                " - Slimefun Version: " + plugin.getDescription().getVersion() + "\n" +
                " - CS-CoreLib Version: " + CSCoreLib.getLib().getDescription().getVersion() + "\n\n";

        StringBuilder addons = new StringBuilder("Addons: \n");
        StringBuilder plugins = new StringBuilder();

        int i = 0;
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            i++;
            boolean enabled = Bukkit.getPluginManager().isPluginEnabled(plugin);
            if (plugin.getDescription().getDepend().contains("Slimefun") || plugin.getDescription().getSoftDepend().contains("Slimefun"))
                addons.append("* ").append(!enabled ? "~~" : "").append(plugin.getName()).append(" - ")
                        .append(plugin.getDescription().getVersion()).append(!enabled ? "~~" : "").append("\n");
            else
                plugins.append("* ").append(!enabled ? "~~" : "").append(plugin.getName()).append(" - ")
                        .append(plugin.getDescription().getVersion()).append(!enabled ? "~~" : "").append("\n");
        }

        return versions + addons
                + "\n<details>\n<summary>Plugins (" + i + ")</summary>\n" + plugins + "\n</details>";
    }
}
