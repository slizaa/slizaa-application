package org.slizaa.server.service.configuration;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestConfiguration
@EnableConfigurationModule
public class ConfigurationServiceTest {

	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();

	@BeforeClass
	public static void before() throws Exception {
		System.setProperty("configuration.rootDirectory", folder.newFolder().getAbsolutePath());
	}

	@Autowired
	private IConfigurationService _configurationService;

	@Test
	public void testConfigurationService() throws IOException {

		// test id
		String testId = "testId";
		String testValue = "testValue";

		// store/restore
		_configurationService.store(testId, testValue);
		assertThat(_configurationService.load(testId, String.class)).isEqualTo(testValue);
	}
}
