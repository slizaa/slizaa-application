package org.slizaa.server.graphql.serverconfig;

public class ServerExtensionIdentifier {

  private String symbolicName;

  private String version;

  public ServerExtensionIdentifier(String symbolicName, String version) {
    this.symbolicName = symbolicName;
    this.version = version;
  }

  public String getSymbolicName() {
    return symbolicName;
  }

  public String getVersion() {
    return version;
  }
}