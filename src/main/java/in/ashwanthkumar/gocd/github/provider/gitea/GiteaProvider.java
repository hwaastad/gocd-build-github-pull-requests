package in.ashwanthkumar.gocd.github.provider.gitea;

import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.tw.go.plugin.model.GitConfig;
import in.ashwanthkumar.gocd.github.provider.github.GHUtils;
import in.ashwanthkumar.gocd.github.provider.github.GitHubProvider;
import in.ashwanthkumar.gocd.github.settings.scm.ScmPluginConfigurationView;
import in.ashwanthkumar.utils.lang.StringUtils;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.Arrays;

public class GiteaProvider extends GitHubProvider {

   @Override
   public GoPluginIdentifier getPluginId() {
      return new GoPluginIdentifier("gitea.pr", Arrays.asList("1.0"));
   }

   @Override
   public String getName() {
      return "Gitea";
   }

   @Override
   public ScmPluginConfigurationView getScmConfigurationView() {
      return new GiteaScmPluginConfigurationView();
   }

   @Override
   public void checkConnection(GitConfig gitConfig) {
      try {
         loginWith(gitConfig).getRepository(GHUtils.parseGithubUrl(gitConfig.getEffectiveUrl()));
      } catch (Exception e) {
         throw new RuntimeException(String.format("check connection failed. %s", e.getMessage()), e);
      }
   }

   private GitHub loginWith(GitConfig gitConfig) throws IOException {
      if (hasCredentials(gitConfig))
         //  return GitHub.connectToEnterprise(gitConfig.getApi(),gitConfig.getUsername(), gitConfig.getPassword());

         return GitHub.connectToEnterprise("http://gitea.gitea.10.210.212.105.xip.io/api/v1/", gitConfig.getUsername(), gitConfig.getPassword());
      else return GitHub.connect();
   }

   private boolean hasCredentials(GitConfig gitConfig) {
      return StringUtils.isNotEmpty(gitConfig.getUsername()) && StringUtils.isNotEmpty(gitConfig.getPassword());
   }
}
