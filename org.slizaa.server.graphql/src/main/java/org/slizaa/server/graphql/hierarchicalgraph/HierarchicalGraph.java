package org.slizaa.server.graphql.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

public class HierarchicalGraph {

	private String _databaseIdentifier;
	
	private String _hierarchicalGraphIdentifier;

	public HierarchicalGraph(String databaseIdentifier, String hierarchicalGraphIdentifier) {
		this._databaseIdentifier = checkNotNull(databaseIdentifier);
		this._hierarchicalGraphIdentifier = checkNotNull(hierarchicalGraphIdentifier);
	}

	public String getDatabaseIdentifier() {
		return _databaseIdentifier;
	}

	public String getIdentifier() {
		return _hierarchicalGraphIdentifier;
	}
	
	public String getGlobalIdentifier() {
		return _databaseIdentifier + "/" +_hierarchicalGraphIdentifier;
	}
}
