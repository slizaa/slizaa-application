package org.slizaa.server.service.configuration.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slizaa.server.service.configuration.IConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConfigurationServiceImpl implements IConfigurationService {

	@Autowired
	private ConfigurationServiceProperties _configProps;

	@PostConstruct
	public void init() {
	}

	@Override
	public <T> void store(String configurationIdentifier, T configuration) throws IOException {

		//
		File file = new File(_configProps.getRootDirectoryAsFile(), configurationIdentifier + "/configuration.json");
		file.getParentFile().mkdirs();

		//
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enableDefaultTyping();
		objectMapper.writeValue(file, configuration);
	}

	@Override
	public <T> T load(String configurationIdentifier, Class<T> type) throws IOException {

		//
		File file = new File(_configProps.getRootDirectory(), configurationIdentifier + "/configuration.json");
		
		//
		if (file.exists()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enableDefaultTyping();
			return objectMapper.readValue(file, type);			
		}
		
		//
		return null;
	}
}
