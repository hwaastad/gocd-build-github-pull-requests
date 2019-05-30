package in.ashwanthkumar.gocd.github.provider.gitea;

import com.tw.go.plugin.model.GitConfig;

public class GiteaGitConfig extends GitConfig {
   private String api;

   public GiteaGitConfig(String url, String username, String password, String branch, String api) {
      super(url, username, password, branch);
      this.api = api;
   }

   public GiteaGitConfig(String url) {
      super(url);
   }


   public String getApi() {
      return api;
   }

   public void setApi(String api) {
      this.api = api;
   }
}
