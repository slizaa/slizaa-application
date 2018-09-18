package org.slizaa.server.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CoreDependencies {

  /** - */
  @JsonProperty("coreDependencies")
  private List<CoreDependency> _coreDependencies;

  /**
   * <p>
   *   Creates a new instance of type {@link AggregatedDependencies}.
   * </p>
   *
   * @param coreDependencies
   */
  public CoreDependencies(List<CoreDependency> coreDependencies) {
    _coreDependencies = checkNotNull(coreDependencies);
  }
}
