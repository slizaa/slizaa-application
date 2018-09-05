package org.slizaa.server.rest.model;

import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregatedDependency extends CoreDependency {

  /** - */
  @JsonProperty("weight")
  private int                  _weight;

  @JsonProperty("coreDependencies")
  private List<CoreDependency> _coreDependencies;

  /**
   * <p>
   * Creates a new instance of type {@link AggregatedDependency}.
   * </p>
   *
   * @param dependency
   */
  public AggregatedDependency(HGAggregatedDependency dependency) {
    super(dependency);

    //
    _weight = (int) dependency.getAggregatedWeight();
    
    //
    _coreDependencies = dependency.getCoreDependencies().stream().map(dep -> new CoreDependency(dep)).collect(Collectors.toList());
  }
}
