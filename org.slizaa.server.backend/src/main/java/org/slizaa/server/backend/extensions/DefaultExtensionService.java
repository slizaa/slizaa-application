package org.slizaa.server.backend.extensions;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Component
public class DefaultExtensionService implements IExtensionService {

  /* - */
  private MavenBasedExtension _neo4jExtension = new MavenBasedExtension("Neo4j Slizaa Backend", new Version(1, 0, 0))
      .withDependency("org.slizaa.neo4j:org.slizaa.neo4j.importer:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.neo4j:org.slizaa.neo4j.graphdbfactory:1.0.0-SNAPSHOT")
      .withExclusionPattern("*:org.slizaa.scanner.spi-api").withExclusionPattern("*:jdk.tools");

  /* - */
  private MavenBasedExtension _jtypeExtension = new MavenBasedExtension("JType Slizaa Extension", new Version(1, 0, 0))
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner.apoc:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.hierarchicalgraph:1.0.0-SNAPSHOT")
      .withExclusionPattern("*:org.slizaa.scanner.spi-api").withExclusionPattern("*:jdk.tools");

  /* - */
  private List<IExtension> _extensionList = Arrays.asList(_neo4jExtension, _jtypeExtension);

  /**
   * @return
   */
  @Override
  public List<IExtension> getAvailableExtensions() {
    return Collections.unmodifiableList(_extensionList);
  }
}
