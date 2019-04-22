package org.slizaa.server.service.slizaa;

import java.util.List;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public interface IHierarchicalGraphContainer {

	/**
	 * 
	 *
	 * @return
	 */
	String getIdentifier();

	/**
	 *
	 * @param hierarchicalGraphId
	 * @return
	 */
	IHierarchicalGraph newHierarchicalGraph(String hierarchicalGraphId);

	/**
	 *
	 * @param identifier
	 * @return
	 */
	IHierarchicalGraph getHierarchicalGraph(String hierarchicalGraphId);

	/**
	 *
	 * @param identifier
	 */
	void disposeHierarchicalGraph(String hierarchicalGraphId);

	/**
	 *
	 * @return
	 */
	List<IHierarchicalGraph> getHierarchicalGraphs();

}
