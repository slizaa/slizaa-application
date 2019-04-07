package org.slizaa.server.service.configuration.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slizaa.server.service.configuration.IConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
	  store(configurationIdentifier, "configuration.json", configuration);
	}

	@Override
	public <T> T load(String configurationIdentifier, Class<T> type) throws IOException {

		//
	  return load(configurationIdentifier, "configuration.json", type);
	}

  @Override
  public <T> void store(String configurationIdentifier, String fileName, T configuration) throws IOException {
    
    //
    File file = new File(_configProps.getRootDirectoryAsFile(), configurationIdentifier + File.separatorChar + fileName);
    file.getParentFile().mkdirs();

    //
    ObjectMapper objectMapper = new ObjectMapper();
    // TODO: Replace with @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    objectMapper.enableDefaultTyping();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    objectMapper.writeValue(file, configuration);
    
  }

  @Override
  public <T> T load(String configurationIdentifier, String fileName, Class<T> type) throws IOException {
    
    //
    File file = new File(_configProps.getRootDirectory(), configurationIdentifier  + File.separatorChar + fileName);
    
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
