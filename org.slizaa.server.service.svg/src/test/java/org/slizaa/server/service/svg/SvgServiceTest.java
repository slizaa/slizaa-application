package org.slizaa.server.service.svg;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slizaa.server.service.svg.impl.fwk.SvgServiceTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SvgServiceTestConfiguration.class)
public class SvgServiceTest {

  @ClassRule
  public static TemporaryFolder folder = new TemporaryFolder();

  @BeforeClass
  public static void before() throws Exception {
    System.setProperty("configuration.rootDirectory", folder.newFolder().getAbsolutePath());
  }

  @Autowired
  private ISvgService _svgService;

  @Test
  public void test() throws Exception {

    String key = _svgService.getKey("icons/class_obj.svg", null, "icons/abstract_ovr.svg", null, "icons/private_ovr.svg");
    assertThat(key).isNotNull();
    
    System.out.println(_svgService.getMergedSvg(key));
  }
}
