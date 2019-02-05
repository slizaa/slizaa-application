package org.slizaa.server.service.svg.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.configuration.IConfigurationService;
import org.slizaa.server.service.svg.ISvgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SvgServiceImpl implements ISvgService {

  public static final String    CONFIGURATION_ID = "org.slizaa.server.service.svg.impl";

  @Autowired
  private IConfigurationService _configurationService;

  @Autowired
  private IBackendService       _backendService;

  private Map<String, String>   _key2ShortendKeyMap;

  @PostConstruct
  public void init() {

    _key2ShortendKeyMap = new HashMap<>();

    try {
      Map<String, String> map = _configurationService.load(SvgServiceImpl.CONFIGURATION_ID, HashMap.class);
      if (map != null && !map.isEmpty()) {
        _key2ShortendKeyMap.putAll(map);
      }
    } catch (Exception e) {
      // TODO: Logging
    }
  }

  @Override
  public String getMergedSvg(String shortendIdentifier) {
    return null;
  }

  public String getOrCreateKey(String main, String upperLeft, String upperRight, String lowerLeft, String lowerRight) {
    String key = ImageKey.key(main, upperLeft, upperRight, lowerLeft, lowerRight);
    if (!_key2ShortendKeyMap.containsKey(key)) {
      _key2ShortendKeyMap.computeIfAbsent(key, ImageKey::shortedKey);
      try {
        _configurationService.store(CONFIGURATION_ID, _key2ShortendKeyMap);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return _key2ShortendKeyMap.get(key);
  }
  
  public void mergeSvg() {
    _backendService.loadResourceFromExtensions("");
  }
}
