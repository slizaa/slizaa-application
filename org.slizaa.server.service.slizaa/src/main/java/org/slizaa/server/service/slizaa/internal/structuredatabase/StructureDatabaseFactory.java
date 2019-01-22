package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

@Component
public class StructureDatabaseFactory {

	/**
	 * the spring generated state machine
	 */
	@Autowired
	private StateMachineFactory<StructureDatabaseState, StructureDatabaseTrigger> _stateMachineFactory;

	private Map<StateMachine<StructureDatabaseState, StructureDatabaseTrigger>, StructureDatabaseStateMachineContext> _stateMachine2StructureDatabaseContext = new HashMap<>();

	/**
	 * @param id
	 * @param databaseDirectory
	 * @param serverBackend
	 * @param boltClientFactory
	 * @return
	 */
	public IStructureDatabase newInstance(String id, File databaseDirectory, SlizaaServiceImpl slizaaService) {

		checkNotNull(id);
		checkNotNull(databaseDirectory);
		checkNotNull(slizaaService);

		// create the database directory
		if (!databaseDirectory.exists()) {
			databaseDirectory.mkdirs();
		}

		// create the new state machine
		StateMachine<StructureDatabaseState, StructureDatabaseTrigger> statemachine = _stateMachineFactory
				.getStateMachine();

		// create the state machine context
		StructureDatabaseStateMachineContext stateMachineContext = new StructureDatabaseStateMachineContext(id,
				databaseDirectory, slizaaService);

		// create the structure database
		StructureDatabaseImpl structureDatabase = new StructureDatabaseImpl(statemachine, stateMachineContext);

		// store the association
		_stateMachine2StructureDatabaseContext.put(statemachine, stateMachineContext);

		// now start...
		statemachine.start();

		// finally return the result
		return structureDatabase;
	}

	/**
	 * @param stateMachine
	 * @return
	 */
	StructureDatabaseStateMachineContext context(
			StateMachine<StructureDatabaseState, StructureDatabaseTrigger> stateMachine) {
		return _stateMachine2StructureDatabaseContext.get(stateMachine);
	}
}
