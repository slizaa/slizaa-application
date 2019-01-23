package org.slizaa.server.service.slizaa.internal.structuredatabase;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.statemachine.StateMachine;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 *
 */
public class StructureDatabaseImpl implements IStructureDatabase {

    /**
     * the state machine
     **/
    private StateMachine<StructureDatabaseState, StructureDatabaseTrigger> _stateMachine;

    /**
     * the state machine context
     **/
    private StructureDatabaseStateMachineContext _stateMachineContext;

    /**
     * @param stateMachine
     * @param stateMachineContext
     */
    StructureDatabaseImpl(StateMachine<StructureDatabaseState, StructureDatabaseTrigger> stateMachine,
                          StructureDatabaseStateMachineContext stateMachineContext) {

        this._stateMachine = checkNotNull(stateMachine);
        this._stateMachineContext = checkNotNull(stateMachineContext);
        this._stateMachineContext.setStructureDatabase(this);
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

        return _stateMachineContext.createHierarchicalGraph(identifier);
    }

    @Override
    public void disposeHierarchicalGraph(String identifier) {

        checkState(StructureDatabaseState.RUNNING.equals(this._stateMachine.getState().getId()),
                "Database is not running:  %s", this._stateMachine.getState().getId());

        _stateMachineContext.disposeHierarchicalGraph(identifier);
    }

    @Override
    public List<IHierarchicalGraph> getHierarchicalGraphs() {

        checkState(StructureDatabaseState.RUNNING.equals(this._stateMachine.getState().getId()),
                "Database is not running:  %s", this._stateMachine.getState().getId());

        return _stateMachineContext.getHierarchicalGraphs();
    }

    @Override
    public void parse(boolean startDatabase) throws IOException {
        trigger(StructureDatabaseTrigger.PARSE);
    }

    @Override
    public void start() {
        trigger(StructureDatabaseTrigger.START);
    }

    @Override
    public void stop() {
        trigger(StructureDatabaseTrigger.STOP);
    }

    @Override
    public boolean isRunning() {
        return StructureDatabaseState.RUNNING.equals(this._stateMachine.getState().getId());
    }

    @Override
    public void dispose() {
        if (!StructureDatabaseState.TERMINATED.equals(this._stateMachine.getState().getId())) {
            trigger(StructureDatabaseTrigger.TERMINATE);
        }
    }

    private void trigger(StructureDatabaseTrigger trigger) {

        if (!this._stateMachine.sendEvent(trigger)) {
            // TODO
            throw new RuntimeException(String.format("Trigger '%s' not accepted in state '%s'.",
                    trigger, this._stateMachine.getState().getId().name()));
        }
    }
}
