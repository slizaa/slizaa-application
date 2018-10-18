package org.slizaa.server.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AggregatedDependencies {

  /** - */
  @JsonProperty("aggregatedDependencies")
  private List<AggregatedDependency> _aggregatedDependencies;

  /**
   * <p>
   *   Creates a new instance of type {@link AggregatedDependencies}.
   * </p>
   *
   * @param aggregatedDependencies
   */
  public AggregatedDependencies(List<AggregatedDependency> aggregatedDependencies) {
    _aggregatedDependencies = checkNotNull(aggregatedDependencies);
  }
}
