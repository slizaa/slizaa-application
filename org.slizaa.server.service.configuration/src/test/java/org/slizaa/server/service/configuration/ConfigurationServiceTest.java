package org.slizaa.server.service.configuration;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestConfiguration
@EnableConfigurationModule
public class ConfigurationServiceTest {

	@Autowired
	private IConfigurationService _configurationService;

	@Test
	public void testConfigurationService() throws IOException {

		_configurationService.store("bumm", "asdasdasdasd");
		
		System.out.println(_configurationService.load("bumm", String.class));
	}
}
