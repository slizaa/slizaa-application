package org.slizaa.server.service.slizaa;

import java.util.List;

public interface IHierarchicalGraphContainer {
	

	
    /**
    *
    * @param identifier
    * @return
    */
   IHierarchicalGraph createNewHierarchicalGraph(String identifier);

   /**
    *
    * @param identifier
    */
   void disposeHierarchicalGraph(String identifier);

   /**
    *
    * @return
    */
   List<IHierarchicalGraph> getHierarchicalGraphs();

}
