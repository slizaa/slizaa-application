package org.slizaa.server.service.slizaa;

import org.slizaa.hierarchicalgraph.core.model.HGRootNode;

public interface IHierarchicalGraph {

  /**
   * 	
   * @return
   */
  String getIdentifier();	
	
  /**
   * <p>
   * </p>
   *
   * @return
   */
  HGRootNode getRootNode();
  
  /**
   * 
   * @return
   */
  IHierarchicalGraphContainer getContainer();
}
