package org.slizaa.server.service.svg.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.configuration.IConfigurationService;
import org.slizaa.server.service.svg.ISvgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Component
public class SvgServiceImpl implements ISvgService {

  public static final String    CONFIGURATION_ID = "org.slizaa.server.service.svg.impl";

  private static final String   DEFAULT_SVG_NODE = "<rect x=\"62.5\" y=\"63.502\" fill=\"#BF6363\" width=\"875.328\" height=\"874.994\"/>";

  @Autowired
  private IConfigurationService _configurationService;

  @Autowired
  private IBackendService       _backendService;

  private Map<String, String>   _key2ShortKeyMap;

  private Map<String, String>   _shortKey2XmlMap;

  @PostConstruct
  public void init() {

    _shortKey2XmlMap = new HashMap<>();
    _key2ShortKeyMap = new HashMap<>();

    try {
      Map<String, String> map = _configurationService.load(SvgServiceImpl.CONFIGURATION_ID, HashMap.class);
      if (map != null && !map.isEmpty()) {
        _key2ShortKeyMap.putAll(map);
      }
    } catch (Exception e) {
      // TODO: Logging
    }
  }

  @Override
  public String getKey(String main, String upperLeft, String upperRight, String lowerLeft, String lowerRight) {

    String key = getOrCreateShortKey(main, upperLeft, upperRight, lowerLeft, lowerRight);

    _shortKey2XmlMap.computeIfAbsent(key, k -> mergeSvg(main, upperLeft, upperRight, lowerLeft, lowerRight));

    return key;
  }

  @Override
  public String getMergedSvg(String shortKey) {
    return _shortKey2XmlMap.get(shortKey);
  }

  private String getOrCreateShortKey(String main, String upperLeft, String upperRight, String lowerLeft,
      String lowerRight) {

    // lookup for the key
    String key = ImageKey.longKey(main, upperLeft, upperRight, lowerLeft, lowerRight);

    // create a new one if the key does not exist
    if (!_key2ShortKeyMap.containsKey(key)) {

      // create...
      _key2ShortKeyMap.computeIfAbsent(key, ImageKey::shortKey);

      try {
        // ...and store
        _configurationService.store(CONFIGURATION_ID, _key2ShortKeyMap);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    // return the short key
    return _key2ShortKeyMap.get(key);
  }

  private String mergeSvg(String main, String upperLeft, String upperRight, String lowerLeft, String lowerRight) {

    checkNotNull(main);

    try {

      OverlaySvgIcon overlaySvgIcon = new OverlaySvgIcon();

      byte[] resource = _backendService.loadResourceFromExtensions(main);
      if (resource != null) {
        try {
          Document document = XMLWriterDOM.read(resource);
          overlaySvgIcon._mainNodes = document.getDocumentElement().getChildNodes();
        } catch (Exception e) {
          // TODO
        }
      }

      // DEFAULT_SVG_NODE

      if (upperLeft != null) {
        Document document = XMLWriterDOM.read(_backendService.loadResourceFromExtensions(upperLeft));
        if (document != null) {
          overlaySvgIcon._ulNodes = document.getDocumentElement().getChildNodes();
        }
      }

      if (upperRight != null) {
        Document document = XMLWriterDOM.read(_backendService.loadResourceFromExtensions(upperRight));
        if (document != null) {
          overlaySvgIcon._urNodes = document.getDocumentElement().getChildNodes();
        }
      }

      if (lowerLeft != null) {
        Document document = XMLWriterDOM.read(_backendService.loadResourceFromExtensions(lowerLeft));
        if (document != null) {
          overlaySvgIcon._llNodes = document.getDocumentElement().getChildNodes();
        }
      }

      if (lowerRight != null) {
        Document document = XMLWriterDOM.read(_backendService.loadResourceFromExtensions(lowerRight));
        if (document != null) {
          overlaySvgIcon._lrNodes = document.getDocumentElement().getChildNodes();
        }
      }

      //
      return overlaySvgIcon.create();
    }
    //
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
