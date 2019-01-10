package org.slizaa.server.service.slizaa.internal.structuredatabase;

import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class StructureDatabaseStateMachineAdapter extends
    StateMachineListenerAdapter<StructureDatabaseState, StructureDatabaseEvent> {

  // the structure database
  private IInternalStructureDatabase _structureDatabase;

  /**
   *
   * @param structureDatabase
   */
  public StructureDatabaseStateMachineAdapter(IInternalStructureDatabase structureDatabase) {
    this._structureDatabase = checkNotNull(structureDatabase);
  }

  /**
   * @param transition
   */
  @Override
  public void transition(
      Transition<StructureDatabaseState, StructureDatabaseEvent> transition) {

    //
    if (isTransition(StructureDatabaseEvent.PARSE, transition)) {
      _structureDatabase._parse();
    }
    //
    else if (isTransition(StructureDatabaseEvent.START, transition)) {
      _structureDatabase._start();
    }
    //
    else if (isTransition(StructureDatabaseEvent.STOP, transition)) {
      _structureDatabase._stop();
    }
  }

  /**
   * @param event
   */
  @Override
  public void eventNotAccepted(Message<StructureDatabaseEvent> event) {
    super.eventNotAccepted(event);
  }

  /**
   * @param event
   * @param transition
   * @return
   */
  private boolean isTransition(StructureDatabaseEvent event,
      Transition<StructureDatabaseState, StructureDatabaseEvent> transition) {
    return event.equals(transition.getTrigger().getEvent());
  }
}
