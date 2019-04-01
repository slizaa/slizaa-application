package org.slizaa.server.graphql.graphdatabase;

import org.slizaa.server.service.slizaa.IGraphDatabase;

import java.util.List;

public class GraphDatabase {
	
	private String _identifier;

	private String _state;
	
	private int _port;

	private ContentDefinition _contentDefinition;

	private GraphDatabase(String identifier, String state, int port, ContentDefinition contentDefinition) {
		this._identifier = identifier;
		this._state = state;
		this._port = port;
		this._contentDefinition = contentDefinition;
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

	public ContentDefinition getContentDefinition() {
		return _contentDefinition;
	}

	public static GraphDatabase convert(IGraphDatabase database) {
		return new GraphDatabase(database.getIdentifier(), database.getState().name(), database.getPort());
	}
}
