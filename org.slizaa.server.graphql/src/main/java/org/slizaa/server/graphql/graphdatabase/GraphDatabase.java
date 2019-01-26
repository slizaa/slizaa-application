package org.slizaa.server.graphql.graphdatabase;

import org.slizaa.server.service.slizaa.IGraphDatabase;

public class GraphDatabase {
	
	private String _identifier;

	private String _state;
	
	private int _port;

	private GraphDatabase(String identifier, String state, int port) {
		this._identifier = identifier;
		this._state = state;
		this._port = port;
	}

	public String getIdentifier() {
		return _identifier;
	}

	public String getState() {
		return _state;
	}
	
	public int getPort() {
		return _port;
	}
	
	public static GraphDatabase convert(IGraphDatabase database) {
		return new GraphDatabase(database.getIdentifier(), database.getState().name(), database.getPort());
	}
}
