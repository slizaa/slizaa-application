package org.slizaa.server.graphql.hierarchicalgraph;

import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

/**
 *
 */
@Component
public class HierarchicalGraphQuery implements GraphQLQueryResolver {

  //
  @Autowired
  private ISlizaaService slizaaService;

  /**
   * @return
   */
  public String svg(String identifier) {
    return slizaaService.getSvgService().getSvg(identifier);
  }
}