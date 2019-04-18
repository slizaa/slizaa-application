package org.slizaa.server.service.slizaa;

import java.io.IOException;
import java.util.List;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

/**
 * <p>
 * </p>
 */
public interface IGraphDatabase extends IHierarchicalGraphContainer {

  /**
   * 
   *
   * @return
   */
  String getIdentifier();

  /**
   * 
   * @return
   */
  GraphDatabaseState getState();

  /**
   * 
   * @return
   */
  int getPort();

  /**
   *
   * @return
   */
  boolean hasContentDefinition();

  /**
   * @return
   */
  IContentDefinitionProvider<?> getContentDefinition();

  /**
   * 
   * @param contentDefinitionFactoryId
   * @param contentDefinition
   */
  void setContentDefinition(String contentDefinitionFactoryId, String contentDefinition);

  /**
   * <p>
   * </p>
   *
   * @throws IOException
   */
  void parse(boolean startDatabase) throws IOException;

  /**
   *
   */
  void start();

  /**
   *
   */
  void stop();

  /**
   *
   * @return
   */
  boolean isRunning();

  /**
   *
   */
  void terminate();

  /**
   * 
   * @return
   */
  List<GraphDatabaseAction> getAvailableActions();

  /**
   * Possible actions that can executed.
   * 
   * @author Gerd W&uuml;therich (gw@code-kontor.io)
   */
  public enum GraphDatabaseAction {

    SET_CONTENT_DEFINITION("setContentDefinition"), PARSE("parse"), START("start"), STOP("stop"), TERMINATE(
        "terminate");

    public String getName() {
      return _name;
    }

    private GraphDatabaseAction(String name) {
      _name = name;
    }

    private String _name;
  }
}
