package org.slizaa.server.graphql;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDescription {

  private String baseImagePath;

  private String topLeftImagePath;

  private String topRightImagePath;

  private String bottomLeftImagePath;

  private String bottomRightImagePath;

  public ImageDescription(String baseImagePath, String topLeftImagePath, String topRightImagePath,
      String bottomLeftImagePath, String bottomRightImagePath) {
    this.baseImagePath = baseImagePath;
    this.topLeftImagePath = topLeftImagePath;
    this.topRightImagePath = topRightImagePath;
    this.bottomLeftImagePath = bottomLeftImagePath;
    this.bottomRightImagePath = bottomRightImagePath;
  }

  public String getTopLeftImagePath() {
    return nullSafe(topLeftImagePath);
  }

  public String getTopRightImagePath() {
    return nullSafe(topRightImagePath);
  }

  public String getBottomLeftImagePath() {
    return nullSafe(bottomLeftImagePath);
  }

  public String getBottomRightImagePath() {
    return nullSafe(bottomRightImagePath);
  }

  public String getBaseImagePath() {
    return nullSafe(baseImagePath);
  }

  /**
   *
   * @return
   */
  protected static String nullSafe(String path) {
    return path != null ? getBaseEnvLinkURL() + path : null;
  }

  /**
   *
   * @return
   */
  protected static String getBaseEnvLinkURL() {

    // get the current request
    HttpServletRequest currentRequest =
        ((ServletRequestAttributes) RequestContextHolder.
            currentRequestAttributes()).getRequest();

    // lazy about determining protocol but can be done too
    String currentRequestUrl = currentRequest.getRequestURL().toString();

    try {
      URL url = new URL(currentRequestUrl);
      return url.getProtocol() + "://" + url.getHost() + ":" +
          (url.getPort() != -1 ? url.getPort() : url.getDefaultPort()) + "/static/";
    } catch (MalformedURLException e) {
      return "";
    }
  }
}

