package org.slizaa.server.graphql;

public class BackendExtension {

  private String identifier;

  private String version;

  public BackendExtension(String identifier, String version) {
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