package org.slizaa.server.service.slizaa.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import javax.annotation.PostConstruct;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
@Component
@SuppressWarnings("rawtypes")
public class ContentDefinitionProviderFactoryService {

  /** the IContentDefinitionProviderFactories */
  private Map<String, IContentDefinitionProviderFactory> _contentDefinitionProviderFactories;

  @PostConstruct
  public void initialize() throws Exception {

    _contentDefinitionProviderFactories = new HashMap<String, IContentDefinitionProviderFactory>();

    ServiceLoader<IContentDefinitionProviderFactory> serviceLoader = ServiceLoader
        .load(IContentDefinitionProviderFactory.class);
    serviceLoader.forEach(fact -> _contentDefinitionProviderFactories.put(fact.getClass().getName(), fact));
  }

  public boolean containsContentDefinitionProviderFactory(String key) {
    return _contentDefinitionProviderFactories.containsKey(key);
  }

  public IContentDefinitionProviderFactory getContentDefinitionProviderFactory(String key) {
    return _contentDefinitionProviderFactories.get(key);
  }

}
