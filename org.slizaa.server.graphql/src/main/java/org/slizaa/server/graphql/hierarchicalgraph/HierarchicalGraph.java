package org.slizaa.server.graphql.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

public class HierarchicalGraph {

	private String databaseIdentifier;

	private String hierarchicalGraphIdentifier;

	public HierarchicalGraph(String databaseIdentifier, String hierarchicalGraphIdentifier) {
		this.databaseIdentifier = checkNotNull(databaseIdentifier);
		this.hierarchicalGraphIdentifier = checkNotNull(hierarchicalGraphIdentifier);
	}

	public String getDatabaseIdentifier() {
		return databaseIdentifier;
	}

	public String getHierarchicalGraphIdentifier() {
		return hierarchicalGraphIdentifier;
	}
}
