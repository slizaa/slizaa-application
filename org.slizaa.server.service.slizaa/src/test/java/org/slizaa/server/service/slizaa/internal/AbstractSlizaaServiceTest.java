package org.slizaa.server.service.slizaa.internal;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slizaa.server.service.backend.EnableBackendServiceModule;
import org.slizaa.server.service.configuration.EnableConfigurationModule;
import org.slizaa.server.service.extensions.EnableExtensionsModule;
import org.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import org.slizaa.server.service.svg.EnableSvgServiceModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Configuration
@EnableBackendServiceModule
@EnableExtensionsModule
@EnableConfigurationModule
@EnableSlizaaServiceModule
@EnableSvgServiceModule
public abstract class AbstractSlizaaServiceTest {

	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();
	
	private static File configurationRootDirectory;
	
	private static File databaseRootDirectory;
	
	@Autowired
	private SlizaaServiceImpl _slizaaService;

	@BeforeClass
	public static void beforeClass() throws Exception {
		
		configurationRootDirectory = folder.newFolder();
		databaseRootDirectory = folder.newFolder();
		
		System.setProperty("configuration.rootDirectory", configurationRootDirectory.getAbsolutePath());
		System.setProperty("database.rootDirectory", databaseRootDirectory.getAbsolutePath());
	}

	public SlizaaServiceImpl slizaaService() {
		return _slizaaService;
	}

	
}