package io.github.starwishsama.extra;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubBean {
    private String html_url;
    private int id;
    private String tag_name;
    private String name;
    private boolean prerelease;
    @SerializedName("create_at")
    private String createTime;
    @SerializedName("published_at")
    private String publishTime;
    @SerializedName("body")
    private String changeLog;
    private List<AssetsBean> assets;

    public String getTag_name() {
        return tag_name;
    }

    public List<AssetsBean> getAssets() {
        return assets;
    }

    public static class AssetsBean {
        private String url;
        private String name;
        private Object label;
        private String content_type;
        private String state;
        private int size;
        private int download_count;
        private String created_at;
        private String updated_at;
        private String browser_download_url;

        public String getBrowser_download_url() {
            return browser_download_url;
        }
    }
}
