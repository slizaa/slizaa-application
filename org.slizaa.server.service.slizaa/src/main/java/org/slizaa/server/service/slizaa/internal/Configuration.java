package org.slizaa.server.service.slizaa.internal;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {

	@JsonProperty("structureDatabases")
	private List<String> _structureDatabases = new ArrayList<>();

	public List<String> getStructureDatabases() {
		return _structureDatabases;
	} 
	
	
}
