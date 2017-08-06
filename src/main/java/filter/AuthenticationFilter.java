/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Wouter
 */
@Provider
@Authenticate
public class AuthenticationFilter implements ContainerRequestFilter {
 
    private static final Map<String, String> USERNAME_PASSWORD_COMBINATIONS;
    static{
        USERNAME_PASSWORD_COMBINATIONS = new HashMap<>();
        USERNAME_PASSWORD_COMBINATIONS.put("Agata", "pass");
    }
    
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    
    //private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
    //                                                    .entity("Invalid authorization").build();
    
    @Override
    public void filter(ContainerRequestContext req) throws IOException {
        String authCredentials = req.getHeaderString("Authorization");
        System.out.println("AUTH");
            //Get request headers
            final MultivaluedMap<String, String> headers = req.getHeaders();
              
            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
              
            //If no authorization information present; block access
            if(authorization == null || authorization.isEmpty())
            {
                req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid authorization").build());
                return;
            }
            if(!isUserNamePasswordCombinationValid(authCredentials)){
                req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid authorization").build());
            }
            System.out.println("SUCCES");
    }

    public boolean isUserNamePasswordCombinationValid(String  basicEncoded){
        String base64Credentials = basicEncoded.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));
        // credentials = username:password
        final String[] values = credentials.split(":",2);
        return USERNAME_PASSWORD_COMBINATIONS.get(values[0]) != null && USERNAME_PASSWORD_COMBINATIONS.get(values[0]).equals(values[1]);
    }

    
}
