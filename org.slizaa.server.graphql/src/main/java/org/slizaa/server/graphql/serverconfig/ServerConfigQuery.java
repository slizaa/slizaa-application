package org.slizaa.server.graphql.serverconfig;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class ServerConfigQuery implements GraphQLQueryResolver {

  //
  @Autowired
  private ISlizaaService slizaaService;

  /**
   * @return
   */
  public boolean isBackendConfigured() {
    return slizaaService.isBackendConfigured();
  }

  public List<ServerExtension> installedServerExtensions() {
    return slizaaService.getInstalledExtensions().stream()
        .map(ext -> new ServerExtension(ext.getSymbolicName(), ext.getVersion().toString())).collect(
            Collectors.toList());
  }

  public List<ServerExtension> availableServerExtensions() {
    return slizaaService.getExtensionService().getExtensions().stream()
        .map(ext -> new ServerExtension(ext.getSymbolicName(), ext.getVersion().toString())).collect(
            Collectors.toList());
  }
}