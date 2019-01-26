package org.slizaa.server.service.slizaa.internal;

import java.util.ArrayList;
import java.util.List;

import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.internal.hierarchicalgraph.HierarchicalGraph;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlizaaServiceConfiguration {

	public static class GraphDbCfg {

		private String _identifier;

		private boolean _isRunning;

		private int _port;

		@JsonProperty("hierarchicalGraphs")
		private List<HierarchicalGraphCfg> _hierarchicalGraphs = new ArrayList<>();

		protected GraphDbCfg() {
		}

		public GraphDbCfg(IGraphDatabase graphDatabase) {
			_identifier = graphDatabase.getIdentifier();
			_isRunning = graphDatabase.isRunning();
			_port = graphDatabase.getPort();

			for (IHierarchicalGraph hierarchicalGraph : graphDatabase.getHierarchicalGraphs()) {
				_hierarchicalGraphs.add(new HierarchicalGraphCfg(hierarchicalGraph.getIdentifier()));
			}
		}

		public String getIdentifier() {
			return _identifier;
		}

		public void setIdentifier(String identifier) {
			_identifier = identifier;
		}

		public boolean isRunning() {
			return _isRunning;
		}

		public void setRunning(boolean isRunning) {
			_isRunning = isRunning;
		}

		public int getPort() {
			return _port;
		}

		public void setPort(int port) {
			_port = port;
		}

		public List<HierarchicalGraphCfg> getHierarchicalGraphs() {
			return _hierarchicalGraphs;
		}
	}

	public static class HierarchicalGraphCfg {

		private String _identifier;

		public HierarchicalGraphCfg(String identifier) {
			_identifier = identifier;
		}

		public HierarchicalGraphCfg() {
		}

		public String getIdentifier() {
			return _identifier;
		}

		public void setIdentifier(String identifier) {
			_identifier = identifier;
		}
	}

	@JsonProperty("graphDatabases")
	private List<GraphDbCfg> _graphDatabases = new ArrayList<>();

	public List<GraphDbCfg> getGraphDatabases() {
		return _graphDatabases;
	}

}
