package org.slizaa.server.service.slizaa.internal.graphdatabase;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.GraphDatabaseState;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

@Component
public class GraphDatabaseFactory {

	/**
	 * the spring generated state machine
	 */
	@Autowired
	private StateMachineFactory<GraphDatabaseState, GraphDatabaseTrigger> _stateMachineFactory;

	private Map<StateMachine<GraphDatabaseState, GraphDatabaseTrigger>, GraphDatabaseStateMachineContext> _stateMachine2StructureDatabaseContext = new HashMap<>();

	public IGraphDatabase newInstance(String id, File databaseDirectory, int port, SlizaaServiceImpl slizaaService) {

		checkNotNull(id);
		checkNotNull(databaseDirectory);
		checkNotNull(slizaaService);

		// create the database directory
		if (!databaseDirectory.exists()) {
			databaseDirectory.mkdirs();
		}

		// create the new state machine
		StateMachine<GraphDatabaseState, GraphDatabaseTrigger> statemachine = _stateMachineFactory
				.getStateMachine();

		// create the state machine context
		GraphDatabaseStateMachineContext stateMachineContext = new GraphDatabaseStateMachineContext(id,
				databaseDirectory, port,  slizaaService);

		// create the structure database
		GraphDatabaseImpl structureDatabase = new GraphDatabaseImpl(statemachine, stateMachineContext);

		// store the association
		_stateMachine2StructureDatabaseContext.put(statemachine, stateMachineContext);

		// now start...
		statemachine.start();

		// finally return the result
		return structureDatabase;
	}

	GraphDatabaseStateMachineContext context(
			StateMachine<GraphDatabaseState, GraphDatabaseTrigger> stateMachine) {
		return _stateMachine2StructureDatabaseContext.get(stateMachine);
	}
}
