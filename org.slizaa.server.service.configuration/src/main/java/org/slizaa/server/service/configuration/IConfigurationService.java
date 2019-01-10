package org.slizaa.server.service.configuration;

public interface IConfigurationService {

  <T> void store(String configurationIdentifier, T configuration);

  <T> T load(String configurationIdentifier, ClassLoader classLoader, Class<T> type);
}
