package in.ashwanthkumar.gocd.github.provider.gitea;

import in.ashwanthkumar.gocd.github.provider.github.GHUtils;

public class GiteaGHUtils extends GHUtils {

   public static int prIdFrom(String diffUrl) {
      return Integer.parseInt(diffUrl.substring(diffUrl.indexOf("/pulls/") + 7, diffUrl.length() - 5));
   }
}
