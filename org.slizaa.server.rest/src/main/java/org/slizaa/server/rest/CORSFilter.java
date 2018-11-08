package org.slizaa.server.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 *
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

   /**
    *
    * @param containerRequestContext
    * @param containerResponseContext
    * @throws IOException
    */
   @Override
   public void filter(final ContainerRequestContext containerRequestContext,
                      final ContainerResponseContext containerResponseContext) throws IOException {
      containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
      containerResponseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
      containerResponseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
      containerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
      containerResponseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
   }
}