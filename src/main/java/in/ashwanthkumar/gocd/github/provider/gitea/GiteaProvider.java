package in.ashwanthkumar.gocd.github.provider.gitea;

import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.tw.go.plugin.model.GitConfig;
import in.ashwanthkumar.gocd.github.provider.github.GHUtils;
import in.ashwanthkumar.gocd.github.provider.github.GitHubProvider;
import in.ashwanthkumar.gocd.github.provider.github.model.PullRequestStatus;
import in.ashwanthkumar.gocd.github.settings.scm.DefaultScmPluginConfigurationView;
import in.ashwanthkumar.gocd.github.settings.scm.ScmPluginConfigurationView;
import in.ashwanthkumar.utils.func.Function;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GiteaProvider extends GitHubProvider {
   private static final Logger LOG = Logger.getLoggerFor(GiteaProvider.class);

   @Override
   public GoPluginIdentifier getPluginId() {
      return new GoPluginIdentifier("github.pr", Arrays.asList("1.0"));
   }

   @Override
   public String getName() {
      return "Gitea";
   }

   @Override
   public ScmPluginConfigurationView getScmConfigurationView() {
      return new DefaultScmPluginConfigurationView();
   }

   @Override
   public void checkConnection(GitConfig gitConfig) {
      try {
         loginWith(gitConfig).getRepository(GHUtils.parseGithubUrl(gitConfig.getEffectiveUrl()));
      } catch (Exception e) {
         throw new RuntimeException(String.format("check connection failed. %s", e.getMessage()), e);
      }
   }


   @Override
   public void populateRevisionData(GitConfig gitConfig, String prId, String prSHA, Map<String, String> data) {
      LOG.info("effective-url: {}, prId: {} prSHA: {} data: {}", gitConfig.getEffectiveUrl(), prId, prSHA, data);
      data.put("PR_ID", prId);

     /*
      PullRequestStatus prStatus = null;
      boolean isDisabled = System.getProperty("go.plugin.github.pr.populate-details", "Y").equals("N");
      LOG.info("Populating PR details is {}", isDisabled);
      if (!isDisabled) {
         prStatus = getPullRequestStatus(gitConfig, prId, prSHA);
      }

      if (prStatus != null) {
         data.put("PR_BRANCH", String.valueOf(prStatus.getPrBranch()));
         data.put("TARGET_BRANCH", String.valueOf(prStatus.getToBranch()));
         data.put("PR_URL", String.valueOf(prStatus.getUrl()));
         data.put("PR_AUTHOR", prStatus.getAuthor());
         data.put("PR_AUTHOR_EMAIL", prStatus.getAuthorEmail());
         data.put("PR_DESCRIPTION", prStatus.getDescription());
         data.put("PR_TITLE", prStatus.getTitle());
      }
      */
   }

   private GitHub loginWith(GitConfig gitConfig) throws IOException {
      if (hasCredentials(gitConfig) && isGithub(gitConfig.getUrl()))
         return GitHub.connectUsingPassword(gitConfig.getUsername(), gitConfig.getPassword());
         //  return GitHub.connectToEnterprise("http://gitea.gitea.10.210.212.105.xip.io/api/v1/", gitConfig.getUsername(), gitConfig.getPassword());
      else if (hasCredentials(gitConfig) && !isGithub(gitConfig.getUrl())) {
         LOG.info("Connecting to: " + filterApiUrl(gitConfig.getUrl()));
         return GitHub.connectToEnterprise(filterApiUrl(gitConfig.getUrl()), gitConfig.getUsername(), gitConfig.getPassword());
      } else return GitHub.connect();
   }

   private boolean hasCredentials(GitConfig gitConfig) {
      return StringUtils.isNotEmpty(gitConfig.getUsername()) && StringUtils.isNotEmpty(gitConfig.getPassword());
   }

   private boolean isGithub(String url) {
      return StringUtils.contains(url, "github");
   }

   private String filterApiUrl(String url) {
      Pattern pattern = Pattern.compile("(https?://[^:^/]*)(:\\d*)?(.*)?");
      Matcher matcher = pattern.matcher(url);
      matcher.find();
      if (null != matcher.group(2)) {
         return String.format("%s%s/api/v1", matcher.group(1), matcher.group(2));
      }
      return String.format("%s/api/v1", matcher.group(1));
   }

   private PullRequestStatus getPullRequestStatus(GitConfig gitConfig, String prId, String prSHA) {
      try {
         return transformGHPullRequestToPullRequestStatus(prSHA).apply(pullRequestFrom(gitConfig, Integer.parseInt(prId)));
      } catch (Exception e) {
         LOG.warn(e.getMessage(), e);
      }
      return null;
   }

   private Function<GHPullRequest, PullRequestStatus> transformGHPullRequestToPullRequestStatus(final String mergedSHA) {
      return input -> {
         int prID = GiteaGHUtils.prIdFrom(input.getDiffUrl().toString());
         try {
            GHUser user = input.getUser();
            //      input.getUser().getEmail();
            return new PullRequestStatus(prID, input.getHead().getSha(), mergedSHA, input.getHead().getLabel(), input.getBase().getLabel(), input.getHtmlUrl().toString(), user.getName(), user.getEmail(), input.getBody(), input.getTitle());
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      };
   }

   private GHPullRequest pullRequestFrom(GitConfig gitConfig, int currentPullRequestID) throws IOException {
      return loginWith(gitConfig)
              .getRepository(GHUtils.parseGithubUrl(gitConfig.getEffectiveUrl()))
              .getPullRequest(currentPullRequestID);
   }
}
