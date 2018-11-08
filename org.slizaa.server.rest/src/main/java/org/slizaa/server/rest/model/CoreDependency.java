package org.slizaa.server.rest.model;

import java.util.ListIterator;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slizaa.hierarchicalgraph.core.model.AbstractHGDependency;
import org.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import org.slizaa.hierarchicalgraph.core.model.HGNode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CoreDependency {

  /** - */
  @JsonProperty("from")
  private String _from;

  /** - */
  @JsonProperty("to")
  private String _to;
  
  /** - */
  @JsonProperty("type")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String _type;

  /**
   * <p>
   * Creates a new instance of type {@link CoreDependency}.
   * </p>
   *
   * @param dependency
   */
  public CoreDependency(AbstractHGDependency dependency) {

    //
    _from = path(dependency.getFrom());
    _to = path(dependency.getTo());
    
    //
    if (dependency instanceof HGCoreDependency) {
      _type = ((HGCoreDependency) dependency).getType();
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String getFrom() {
    return _from;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String getTo() {
    return _to;
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  protected String path(HGNode node) {

    StringBuilder builder = new StringBuilder();

    // Generate an iterator. Start just after the last element.
    ListIterator<HGNode> li = node.getPredecessors().listIterator(node.getPredecessors().size());

    // Iterate in reverse.
    while (li.hasPrevious()) {
      builder.append(li.previous().getIdentifier());
      builder.append("/");
    }

    //
    builder.append(node.getIdentifier());
    
    //
    return builder.toString();
  }
}
