/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Wouter
 */
//@Provider
public class GenericExceptionMapper {//implements ExceptionMapper{

//@Override
    public Response toResponse(Throwable ex) {
        Response.StatusType type = getStatusType(ex);

        Error error = new Error(
                type.getStatusCode(),
                type.getReasonPhrase(),
                ex.getLocalizedMessage());

        
        String message = error.getErrorMessage();
        if(message == null){
            message = error.getStatusDescription();
        }
        return Response.status(error.getStatusCode())
                .entity(message)
                .build();
                //.type(MediaType.APPLICATION_JSON)
    }

    private Response.StatusType getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return((WebApplicationException)ex).getResponse().getStatusInfo();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
    
}
