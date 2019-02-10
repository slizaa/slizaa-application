package org.slizaa.server.service.svg.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.configuration.IConfigurationService;
import org.slizaa.server.service.svg.ISvgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Component
public class SvgServiceImpl implements ISvgService {

  public static final String    CONFIGURATION_ID = "org.slizaa.server.service.svg";

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
        for (String key : map.keySet()) {
          ImageKey.DecodedKey decodedKey = ImageKey.decode(key);
          if (decodedKey.isOverlayImage) {
            createSvgAndReturnShortKey(decodedKey.main, decodedKey.upperLeft, decodedKey.upperRight,
                decodedKey.lowerLeft, decodedKey.lowerRight);
          } else {
            createSvgAndReturnShortKey(decodedKey.main);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      // TODO: Logging
    }
  }

  @Override
  public String createSvgAndReturnShortKey(String main) {

    String key = getOrCreateShortKey(false, main, null, null, null, null);

    _shortKey2XmlMap.computeIfAbsent(key, k -> {
      OverlaySvgIcon overlaySvgIcon = new OverlaySvgIcon(false);
      loadAndSetChildNodes(main, nodeList -> overlaySvgIcon.setMainNodes(nodeList));
      return overlaySvgIcon.create();
    });

    return key;
  }

  @Override
  public String createSvgAndReturnShortKey(String main, String upperLeft, String upperRight, String lowerLeft,
      String lowerRight) {
    checkNotNull(main);

    String key = getOrCreateShortKey(true, main, upperLeft, upperRight, lowerLeft, lowerRight);

    _shortKey2XmlMap.computeIfAbsent(key, k -> {
      OverlaySvgIcon overlaySvgIcon = new OverlaySvgIcon(true);
      loadAndSetChildNodes(main, nodeList -> overlaySvgIcon.setMainNodes(nodeList));
      loadAndSetChildNodes(upperLeft, nodeList -> overlaySvgIcon.setUpperLeftNodes(nodeList));
      loadAndSetChildNodes(upperRight, nodeList -> overlaySvgIcon.setUpperRightNodes(nodeList));
      loadAndSetChildNodes(lowerRight, nodeList -> overlaySvgIcon.setLowerRightNodes(nodeList));
      loadAndSetChildNodes(lowerLeft, nodeList -> overlaySvgIcon.setLowerLeftNodes(nodeList));
      return overlaySvgIcon.create();
    });

    return key;
  }

  @Override
  public String getSvg(String shortKey) {
    return _shortKey2XmlMap.get(shortKey);
  }

  private String getOrCreateShortKey(boolean isOverlayImage, String main, String upperLeft, String upperRight,
      String lowerLeft, String lowerRight) {

    // lookup for the key
    String key = ImageKey.longKey(isOverlayImage, main, upperLeft, upperRight, lowerLeft, lowerRight);

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

  private void loadAndSetChildNodes(String path, Consumer<NodeList> nodeListConsumer) {
    if (path != null) {
      byte[] bytes = _backendService.loadResourceFromExtensions(path);
      if (bytes != null) {
        try {
          Document document = XMLWriterDOM.read(bytes);
          if (document != null) {
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            if (nodeList != null && nodeList.getLength() > 0) {
              nodeListConsumer.accept(nodeList);
            }
          }
        } catch (Exception e) {
          // TODO: Log e.printStackTrace();
        }
      }
    }
  }
}
