package org.slizaa.server.service.slizaa;

import java.util.List;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
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
