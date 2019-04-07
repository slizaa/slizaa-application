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
	 * @param identifier
	 * @return
	 */
	IHierarchicalGraph newHierarchicalGraph(String identifier);

	/**
	 *
	 * @param identifier
	 * @return
	 */
	IHierarchicalGraph getHierarchicalGraph(String identifier);

	/**
	 *
	 * @param identifier
	 */
	void removeHierarchicalGraph(String identifier);

	/**
	 *
	 * @return
	 */
	List<IHierarchicalGraph> getHierarchicalGraphs();

}
