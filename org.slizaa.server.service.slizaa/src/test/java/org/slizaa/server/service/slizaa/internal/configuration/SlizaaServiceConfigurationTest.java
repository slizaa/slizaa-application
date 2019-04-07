package org.slizaa.server.service.slizaa.internal.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.slizaa.server.service.configuration.IConfigurationService;
import org.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;

public class SlizaaServiceConfigurationTest extends AbstractSlizaaServiceTest {

  private static final String   TEST_DIRECTORY_NAME = "test";

  private static final String   TEST_FILE_NAME      = "test.json";

  @Autowired
  private IConfigurationService _configurationService;

  @Test
  public void testConfiguration() throws IOException {

    setupConfigurationFile("src/test/resources/slizaaservicecfg/cfg_1.json");

    SlizaaServiceConfiguration slizaaServiceConfiguration = _configurationService.load(TEST_DIRECTORY_NAME,
        TEST_FILE_NAME, SlizaaServiceConfiguration.class);

    assertThat(slizaaServiceConfiguration.getGraphDatabases()).hasSize(1);
    assertThat(slizaaServiceConfiguration.getGraphDatabases().get(0).getContentDefinition()).isNotNull();
  }

  @Test
  public void testIncorrectConfiguration() throws IOException {

    setupConfigurationFile("src/test/resources/slizaaservicecfg/incorrect_cfg.json");

    SlizaaServiceConfiguration slizaaServiceConfiguration = _configurationService.load(TEST_DIRECTORY_NAME,
        TEST_FILE_NAME, SlizaaServiceConfiguration.class);

    assertThat(slizaaServiceConfiguration.getGraphDatabases()).hasSize(1);
    assertThat(slizaaServiceConfiguration.getGraphDatabases().get(0).getContentDefinition()).isNotNull();
  }
  
  @Test
  public void testInvalidPayloadConfiguration() throws IOException {

    setupConfigurationFile("src/test/resources/slizaaservicecfg/invaldPayload_cfg.json");

    SlizaaServiceConfiguration slizaaServiceConfiguration = _configurationService.load(TEST_DIRECTORY_NAME,
        TEST_FILE_NAME, SlizaaServiceConfiguration.class);

    assertThat(slizaaServiceConfiguration.getGraphDatabases()).hasSize(1);
    assertThat(slizaaServiceConfiguration.getGraphDatabases().get(0).getContentDefinition()).isNotNull();
  }

  /**
   * 
   * @param fileName
   * @throws IOException
   */
  private void setupConfigurationFile(String filePath) throws IOException {

    File targetFile = new File(getConfigurationRootDirectory(),
        TEST_DIRECTORY_NAME + File.separatorChar + TEST_FILE_NAME);

    if (!targetFile.getParentFile().exists()) {
      targetFile.getParentFile().mkdirs();
    }

    Files.copy(new File(filePath),
        new File(getConfigurationRootDirectory(), TEST_DIRECTORY_NAME + File.separatorChar + TEST_FILE_NAME));
  }
}
