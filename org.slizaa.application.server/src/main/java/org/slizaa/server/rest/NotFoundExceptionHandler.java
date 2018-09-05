package org.slizaa.server.rest;

import org.slizaa.server.rest.model.ErrorMessage;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {

    @Context
    private HttpHeaders headers;

    public Response toResponse(NotFoundException ex){
        return Response.status(404).entity(new ErrorMessage(ex.getMessage())).type( getAcceptType()).build();
    }

    private String getAcceptType(){
//         List<MediaType> accepts = headers.getAcceptableMediaTypes();
//         if (accepts!=null && accepts.size() > 0) {
//             //choose one
//         }
//         else {
//             //return a default one like Application/json
//         }
      return MediaType.APPLICATION_JSON;
    }
}