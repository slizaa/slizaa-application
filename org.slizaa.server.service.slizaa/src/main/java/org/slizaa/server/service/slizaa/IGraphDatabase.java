package org.slizaa.server.service.slizaa;

import java.io.IOException;
import java.util.List;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.slizaa.internal.graphdatabase.GraphDatabaseTrigger;

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
  boolean hasContentDefinitionProvider();

  /**
   * @return
   */
  IContentDefinitionProvider<?> getContentDefinitionProvider();

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
  List<GraphDatabaseTrigger> getAllowedTrigger();
}
