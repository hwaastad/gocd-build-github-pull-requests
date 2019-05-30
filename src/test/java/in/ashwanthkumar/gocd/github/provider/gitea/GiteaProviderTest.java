package in.ashwanthkumar.gocd.github.provider.gitea;

import com.tw.go.plugin.model.GitConfig;
import in.ashwanthkumar.gocd.github.provider.AbstractProviderTest;
import in.ashwanthkumar.gocd.github.provider.Provider;
import in.ashwanthkumar.gocd.github.settings.scm.PluginConfigurationView;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GiteaProviderTest extends AbstractProviderTest {

   @Test
   public void checkConnection() {
      GiteaProvider provider = new GiteaProvider();
      GitConfig gitConfig = new GitConfig("http://gitea.gitea.10.210.212.105.xip.io/helge/test");
      gitConfig.setUsername("gocd");
      gitConfig.setPassword("gocd");
      provider.checkConnection(gitConfig);
   }

   @Test
   public void shouldReturnCorrectScmSettingsTemplate() throws Exception {
      PluginConfigurationView scmConfigurationView = getScmView();

      assertThat(scmConfigurationView.templateName(), is("/views/scm.template.gitea.html"));;
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
}