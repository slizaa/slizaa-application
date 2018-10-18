package org.slizaa.server.rest;

public class NotFoundException extends RuntimeException {

  /**
   *
   */
  public NotFoundException() {
  }

  /**
   * @param message
   */
  public NotFoundException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public NotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
