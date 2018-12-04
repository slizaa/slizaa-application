package org.slizaa.server.graphql;

public class BackendExtensionId {

  private String identifier;

  private String version;

  public BackendExtensionId(String identifier, String version) {
    this.identifier = identifier;
    this.version = version;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getVersion() {
    return version;
  }
}