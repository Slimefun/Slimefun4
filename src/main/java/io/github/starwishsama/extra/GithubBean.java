package io.github.starwishsama.extra;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Nameless
 */
public class GithubBean {
    @SerializedName("html_url")
    private String htmlUrl;
    private int id;
    @SerializedName("tag_name")
    private String tagName;
    private String name;
    @SerializedName("prerelease")
    private boolean isPreRelease;
    @SerializedName("create_at")
    private String createTime;
    @SerializedName("published_at")
    private String publishTime;
    @SerializedName("body")
    private String changeLog;
    private List<AssetsBean> assets;

    public boolean isPreRelease() {
        return isPreRelease;
    }

    public String getTagName() {
        return tagName;
    }

    public List<AssetsBean> getAssets() {
        return assets;
    }

    public static class AssetsBean {
        private String url;
        private String name;
        @SerializedName("content_type")
        private String contentType;
        private String state;
        private int size;
        @SerializedName("download_count")
        private int downloadCount;
        @SerializedName("created_at")
        private String createTime;
        @SerializedName("updated_at")
        private String updateTime;
        @SerializedName("browser_download_url")
        private String downloadUrl;

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public int getSize() {
            return size;
        }

        public String getName() {
            return name;
        }
    }
}
