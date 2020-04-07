package io.github.starwishsama.extra;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class GithubBean {

    /**
     * url : https://api.github.com/repos/StarWishsama/Slimefun4/releases/25209964
     * assets_url : https://api.github.com/repos/StarWishsama/Slimefun4/releases/25209964/assets
     * upload_url : https://uploads.github.com/repos/StarWishsama/Slimefun4/releases/25209964/assets{?name,label}
     * html_url : https://github.com/StarWishsama/Slimefun4/releases/tag/v4.3-200405
     * id : 25209964
     * node_id : MDc6UmVsZWFzZTI1MjA5OTY0
     * tag_name : v4.3-200405
     * target_commitish : master
     * name : 粘液科技汉化版 v2.0.5 (1.13-1.15)
     * draft : false
     * author : {"login":"StarWishsama","id":25561848,"node_id":"MDQ6VXNlcjI1NTYxODQ4","avatar_url":"https://avatars1.githubusercontent.com/u/25561848?v=4","gravatar_id":"","url":"https://api.github.com/users/StarWishsama","html_url":"https://github.com/StarWishsama","followers_url":"https://api.github.com/users/StarWishsama/followers","following_url":"https://api.github.com/users/StarWishsama/following{/other_user}","gists_url":"https://api.github.com/users/StarWishsama/gists{/gist_id}","starred_url":"https://api.github.com/users/StarWishsama/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/StarWishsama/subscriptions","organizations_url":"https://api.github.com/users/StarWishsama/orgs","repos_url":"https://api.github.com/users/StarWishsama/repos","events_url":"https://api.github.com/users/StarWishsama/events{/privacy}","received_events_url":"https://api.github.com/users/StarWishsama/received_events","type":"User","site_admin":false}
     * prerelease : false
     * created_at : 2020-04-05T11:23:02Z
     * published_at : 2020-04-05T11:27:02Z
     * assets : [{"url":"https://api.github.com/repos/StarWishsama/Slimefun4/releases/assets/19399045","id":19399045,"node_id":"MDEyOlJlbGVhc2VBc3NldDE5Mzk5MDQ1","name":"Slimefun.v4.3-NIGHTLY.jar","label":null,"uploader":{"login":"StarWishsama","id":25561848,"node_id":"MDQ6VXNlcjI1NTYxODQ4","avatar_url":"https://avatars1.githubusercontent.com/u/25561848?v=4","gravatar_id":"","url":"https://api.github.com/users/StarWishsama","html_url":"https://github.com/StarWishsama","followers_url":"https://api.github.com/users/StarWishsama/followers","following_url":"https://api.github.com/users/StarWishsama/following{/other_user}","gists_url":"https://api.github.com/users/StarWishsama/gists{/gist_id}","starred_url":"https://api.github.com/users/StarWishsama/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/StarWishsama/subscriptions","organizations_url":"https://api.github.com/users/StarWishsama/orgs","repos_url":"https://api.github.com/users/StarWishsama/repos","events_url":"https://api.github.com/users/StarWishsama/events{/privacy}","received_events_url":"https://api.github.com/users/StarWishsama/received_events","type":"User","site_admin":false},"content_type":"application/java-archive","state":"uploaded","size":1565100,"download_count":76,"created_at":"2020-04-05T11:26:55Z","updated_at":"2020-04-05T11:26:59Z","browser_download_url":"https://github.com/StarWishsama/Slimefun4/releases/download/v4.3-200405/Slimefun.v4.3-NIGHTLY.jar"}]
     * tarball_url : https://api.github.com/repos/StarWishsama/Slimefun4/tarball/v4.3-200405
     * zipball_url : https://api.github.com/repos/StarWishsama/Slimefun4/zipball/v4.3-200405
     * body : * 修复 #78 (@ClayCoffee)
     * 合并上游代码
     */

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

    @Data
    public static class AssetsBean {
        /**
         * url : https://api.github.com/repos/StarWishsama/Slimefun4/releases/assets/19399045
         * id : 19399045
         * node_id : MDEyOlJlbGVhc2VBc3NldDE5Mzk5MDQ1
         * name : Slimefun.v4.3-NIGHTLY.jar
         * label : null
         * uploader : {"login":"StarWishsama","id":25561848,"node_id":"MDQ6VXNlcjI1NTYxODQ4","avatar_url":"https://avatars1.githubusercontent.com/u/25561848?v=4","gravatar_id":"","url":"https://api.github.com/users/StarWishsama","html_url":"https://github.com/StarWishsama","followers_url":"https://api.github.com/users/StarWishsama/followers","following_url":"https://api.github.com/users/StarWishsama/following{/other_user}","gists_url":"https://api.github.com/users/StarWishsama/gists{/gist_id}","starred_url":"https://api.github.com/users/StarWishsama/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/StarWishsama/subscriptions","organizations_url":"https://api.github.com/users/StarWishsama/orgs","repos_url":"https://api.github.com/users/StarWishsama/repos","events_url":"https://api.github.com/users/StarWishsama/events{/privacy}","received_events_url":"https://api.github.com/users/StarWishsama/received_events","type":"User","site_admin":false}
         * content_type : application/java-archive
         * state : uploaded
         * size : 1565100
         * download_count : 76
         * created_at : 2020-04-05T11:26:55Z
         * updated_at : 2020-04-05T11:26:59Z
         * browser_download_url : https://github.com/StarWishsama/Slimefun4/releases/download/v4.3-200405/Slimefun.v4.3-NIGHTLY.jar
         */

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
    }
}
