package in.ashwanthkumar.gocd.github.provider.gitea;

import in.ashwanthkumar.gocd.github.settings.scm.DefaultScmPluginConfigurationView;
import in.ashwanthkumar.gocd.github.util.FieldFactory;

import java.util.HashMap;
import java.util.Map;

public class GiteaScmPluginConfigurationView extends DefaultScmPluginConfigurationView {

   @Override
   public String templateName() {
      return "/views/scm.template.gitea.html";
   }

   @Override
   public Map<String, Object> fields() {
      Map<String, Object> response = new HashMap<String, Object>();
    //  response.put("api", FieldFactory.createForScm("GITEA API", null, true, true, false, "0"));
      response.put("url", FieldFactory.createForScm("URL", null, true, true, false, "1"));
      response.put("username", FieldFactory.createForScm("Username", null, false, false, false, "2"));
      response.put("password", FieldFactory.createForScm("Password", null, false, false, true, "3"));
      response.put("defaultBranch", FieldFactory.createForScm("Default Branch", "master", false, false, false, "4"));
      response.put("shallowClone", FieldFactory.createForScm("Default Clone Behaviour", "false", false, false, false, "5"));
      return response;
   }
}
