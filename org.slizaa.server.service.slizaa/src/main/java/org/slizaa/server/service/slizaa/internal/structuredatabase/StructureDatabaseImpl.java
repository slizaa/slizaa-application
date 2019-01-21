package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.util.List;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.statemachine.StateMachine;

/**
 *
 */
public class StructureDatabaseImpl implements IStructureDatabase {

	private StateMachine<StructureDatabaseState, StructureDatabaseEvent> _stateMachine;

	private StructureDatabaseStateMachineContext _stateMachineContext;

	StructureDatabaseImpl(StateMachine<StructureDatabaseState, StructureDatabaseEvent> stateMachine,
			StructureDatabaseStateMachineContext stateMachineContext) {
		this._stateMachine = checkNotNull(stateMachine);
		this._stateMachineContext = checkNotNull(stateMachineContext);
	}

	@Override
	public String getIdentifier() {
		return _stateMachineContext.getIdentifier();
	}

	/**
	 * @param contentDefinitionProvider
	 */
	@Override
	public void setContentDefinitionProvider(IContentDefinitionProvider contentDefinitionProvider) {
		_stateMachineContext.setContentDefinitionProvider(contentDefinitionProvider);
	}

	/**
	 * @return
	 */
	@Override
	public boolean hasContentDefinitionProvider() {
		return _stateMachineContext.hasContentDefinitionProvider();
	}

	@Override
	public IHierarchicalGraph createNewHierarchicalGraph(String identifier) {
		checkState(StructureDatabaseState.RUNNING.equals(this._stateMachine.getState().getId()),
				"Database is not running:  %s", this._stateMachine.getState().getId());
		return null;
	}

	@Override
	public void disposeHierarchicalGraph(String identifier) {

	}

	@Override
	public List<IHierarchicalGraph> getHierarchicalGraphs() {
		return null;
	}

	@Override
	public void parse(boolean startDatabase) throws IOException {
		this._stateMachine.sendEvent(StructureDatabaseEvent.PARSE);
	}

	@Override
	public void start() {
		this._stateMachine.sendEvent(StructureDatabaseEvent.START);
	}

	@Override
	public void stop() {
		this._stateMachine.sendEvent(StructureDatabaseEvent.STOP);
	}
}
