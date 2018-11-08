package org.slizaa.server.graphql;

/**
 *
 */
public class MapEntry {

  private String key;

  private String value;

  public MapEntry(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
