package org.slizaa.server.service.slizaa.internal.graphdatabase;

import org.slizaa.server.service.slizaa.GraphDatabaseState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@EnableStateMachineFactory
public class GraphDatabaseStateMachine
    extends EnumStateMachineConfigurerAdapter<GraphDatabaseState, GraphDatabaseTrigger> {

  @Autowired
  private GraphDatabaseFactory _graphDatabaseFactory;

  @Override
  public void configure(StateMachineConfigurationConfigurer<GraphDatabaseState, GraphDatabaseTrigger> config)
      throws Exception {

    config.withConfiguration().taskExecutor(new SyncTaskExecutor()).autoStartup(false);
  }

  @Override
  public void configure(StateMachineStateConfigurer<GraphDatabaseState, GraphDatabaseTrigger> states) throws Exception {

    states
    // @formatter:off
        .withStates().initial(GraphDatabaseState.INITIAL).choice(GraphDatabaseState.PARSING)
        .states(EnumSet.allOf(GraphDatabaseState.class));
    // @formatter:on
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<GraphDatabaseState, GraphDatabaseTrigger> transitions)
      throws Exception {

    transitions
      // @formatter:off
      // INITIAL
      .withExternal()
        .source(GraphDatabaseState.INITIAL)
        .target(GraphDatabaseState.NOT_RUNNING)
        // TODO: CONTENT DEFINITION SET?
        .guard(guardWithCtx(ctx -> ctx.hasPopulatedDatabaseDirectory()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.INITIAL)
        .target(GraphDatabaseState.CONFIGURED)
        .event(GraphDatabaseTrigger.SET_CONTENT_DEFINITION)
        .action(actionWithCtx((stateCtx, ctx) -> {         
          String contentDefinitionFactoryId = stateCtx.getMessageHeaders().get(GraphDatabaseImpl.CONTENT_DEFINITION_FACTORY_ID, String.class);
          String contentDefinition =   stateCtx.getMessageHeaders().get(GraphDatabaseImpl.CONTENT_DEFINITION, String.class);
          ctx.setContentDefinitionProvider(contentDefinitionFactoryId, contentDefinition);
        }))
        .and()
      // CONFIGURED  
      .withExternal()
        .source(GraphDatabaseState.CONFIGURED)
        .target(GraphDatabaseState.PARSING)
        .event(GraphDatabaseTrigger.PARSE)
        .action(actionWithCtx((stateCtx, ctx) -> ctx.parse(stateCtx.getMessageHeaders().get(GraphDatabaseImpl.START_DATABASE_AFTER_PARSING, Boolean.class))))
        .and()
      // PARSING  
      .withChoice()
        .source(GraphDatabaseState.PARSING)
        .first(GraphDatabaseState.RUNNING, guardWithCtx(ctx -> ctx.isRunning()))
        .last(GraphDatabaseState.NOT_RUNNING)
        .and()
      // NOT_RUNNING
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.CONFIGURED)
        .event(GraphDatabaseTrigger.SET_CONTENT_DEFINITION)
        .action(actionWithCtx((stateCtx, ctx) -> {         
          String contentDefinitionFactoryId = stateCtx.getMessageHeaders().get(GraphDatabaseImpl.CONTENT_DEFINITION_FACTORY_ID, String.class);
          String contentDefinition =   stateCtx.getMessageHeaders().get(GraphDatabaseImpl.CONTENT_DEFINITION, String.class);
          ctx.setContentDefinitionProvider(contentDefinitionFactoryId, contentDefinition);
        }))
        .and()
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.PARSING)
        .event(GraphDatabaseTrigger.PARSE)
        .action(actionWithCtx((stateCtx, ctx) -> ctx.parse(stateCtx.getMessageHeaders().get(GraphDatabaseImpl.START_DATABASE_AFTER_PARSING, Boolean.class))))
        .and()
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.RUNNING)
        .event(GraphDatabaseTrigger.START)
        .action(actionWithCtx(ctx -> ctx.start()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      // RUNNING  
      .withExternal()
        .source(GraphDatabaseState.RUNNING)
        .target(GraphDatabaseState.NOT_RUNNING)
        .event(GraphDatabaseTrigger.STOP)
        .action(actionWithCtx(ctx -> ctx.stop()))
        .and()
      // TERMINATE
      .withExternal()
        .source(GraphDatabaseState.INITIAL)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.CONFIGURED)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.RUNNING)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()));
      // @formatter:on
  }

  private Action<GraphDatabaseState, GraphDatabaseTrigger> actionWithCtx(
      Consumer<GraphDatabaseStateMachineContext> consumer) {

    return new Action<GraphDatabaseState, GraphDatabaseTrigger>() {
      @Override
      public void execute(StateContext<GraphDatabaseState, GraphDatabaseTrigger> context) {
        consumer.accept(_graphDatabaseFactory.context(context.getStateMachine()));
      }
    };
  }

  private Action<GraphDatabaseState, GraphDatabaseTrigger> actionWithCtx(
      BiConsumer<StateContext<GraphDatabaseState, GraphDatabaseTrigger>, GraphDatabaseStateMachineContext> consumer) {

    return new Action<GraphDatabaseState, GraphDatabaseTrigger>() {
      @Override
      public void execute(StateContext<GraphDatabaseState, GraphDatabaseTrigger> context) {
        consumer.accept(context, _graphDatabaseFactory.context(context.getStateMachine()));
      }
    };
  }

  private Guard<GraphDatabaseState, GraphDatabaseTrigger> guardWithCtx(
      Function<GraphDatabaseStateMachineContext, Boolean> guard) {

    return new Guard<GraphDatabaseState, GraphDatabaseTrigger>() {
      @Override
      public boolean evaluate(StateContext<GraphDatabaseState, GraphDatabaseTrigger> context) {
        return guard.apply(_graphDatabaseFactory.context(context.getStateMachine()));
      }
    };

  }
}
