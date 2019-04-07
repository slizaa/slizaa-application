package org.slizaa.server.service.slizaa.internal.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

public class HierarchicalGraphDefinition {

  private String _identifier;
  
  public HierarchicalGraphDefinition(String identifier) {
    _identifier = checkNotNull(identifier);
  }

  public String getIdentifier() {
    return _identifier;
  } 
}
