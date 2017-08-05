/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import filter.AuthenticationFilter;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Wouter
 */
//@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig {//extends ResourceConfig{
    public ApplicationConfig(){
        //packages("filter");
        //register(filter.AuthenticationFilter.class);
    }
}
