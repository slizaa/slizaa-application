package org.slizaa.server.rest.model;

/**
 * Class to map application related exceptions
 */
public class ErrorMessage {

  /**
   * detailed error description for developers
   */
  String developerMessage;

  public ErrorMessage(
      String developerMessage) {

    //
    this.developerMessage = developerMessage;
  }

  public ErrorMessage() {
  }

  public String getDeveloperMessage() {
    return developerMessage;
  }

  public void setDeveloperMessage(String developerMessage) {
    this.developerMessage = developerMessage;
  }
}
