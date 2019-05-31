package in.ashwanthkumar.gocd.github.provider.gitea;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.tw.go.plugin.model.GitConfig;
import in.ashwanthkumar.gocd.github.provider.AbstractProviderTest;
import in.ashwanthkumar.gocd.github.provider.Provider;
import in.ashwanthkumar.gocd.github.provider.github.GitHubProvider;
import in.ashwanthkumar.gocd.github.settings.scm.PluginConfigurationView;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GiteaProviderTest extends AbstractProviderTest {
   private static final Logger LOG = Logger.getLoggerFor(GiteaProviderTest.class);

   @Test
   @Ignore
   public void checkConnection() {
      GiteaProvider provider = new GiteaProvider();
      GitConfig gitConfig = new GitConfig("http://gitea.gitea.10.210.212.105.xip.io/helge/test.git");
      gitConfig.setUsername("gocd");
      gitConfig.setPassword("gocd");
      provider.checkConnection(gitConfig);
   }

   @Test
   public void testUpdatePRStatusUrl() throws Exception {
      GitConfig gitConfig = new GitConfig("http://gitea.gitea.10.210.212.105.xip.io/helge/test.git");
      gitConfig.setUsername("gocd");
      gitConfig.setPassword("gocd");
      LOG.info(gitConfig.getEffectiveUrl());
   }

   @Test
   public void shouldReturnCorrectScmSettingsTemplate() throws Exception {
      PluginConfigurationView scmConfigurationView = getScmView();

      assertThat(scmConfigurationView.templateName(), is("/views/scm.template.html"));;
   }

   @Test
   public void shouldReturnCorrectScmSettingsFields() throws Exception {
      PluginConfigurationView scmConfigurationView = getScmView();

      assertThat(scmConfigurationView.fields().keySet(),
              hasItems("url", "username", "password", "defaultBranch", "shallowClone")
      );
      assertThat(scmConfigurationView.fields().size(), is(5));
   }

   @Test
   public void shouldReturnCorrectGeneralSettingsTemplate() throws Exception {
      PluginConfigurationView generalConfigurationView = getGeneralView();

      assertThat(generalConfigurationView.templateName(), is(""));
      assertThat(generalConfigurationView.hasConfigurationView(), is(false));
   }

   @Override
   protected Provider getProvider() {
      return new GiteaProvider();
   }

   @Test
   public void populateRevisionData() throws Exception{
      GiteaProvider provider = new GiteaProvider();
      GitConfig gitConfig = new GitConfig("http://gitea.gitea.10.210.212.105.xip.io/helge/test.git");
      gitConfig.setUsername("gocd");
      gitConfig.setPassword("gocd");
      gitConfig.setBranch("master");
      provider.populateRevisionData(gitConfig,"17","7607faa4a72d150ec39f7e6aaa957eb978c37efa",new HashMap<>());
   }
}