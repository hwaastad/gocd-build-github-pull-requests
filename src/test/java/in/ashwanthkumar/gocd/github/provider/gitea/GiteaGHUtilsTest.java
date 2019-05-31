package in.ashwanthkumar.gocd.github.provider.gitea;

import in.ashwanthkumar.gocd.github.provider.github.GHUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class GiteaGHUtilsTest {

   @Test
   public void prIdFrom() throws Exception{
      String diffUrl = "http://gitea.gitea.10.210.212.105.xip.io/helge/test/pulls/163.diff";
      String diffUrl2 = "http://gitea.gitea.10.210.212.105.xip.io/helge/test/pulls/1.diff";
      String githubDiff="https://patch-diff.githubusercontent.com/raw/hwaastad/helm-charts-1/pull/17.diff";
      assertEquals(163,GiteaGHUtils.prIdFrom(diffUrl));
      assertEquals(1,GiteaGHUtils.prIdFrom(diffUrl2));
   }
}