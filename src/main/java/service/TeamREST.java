/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.QuizRepositoryUI;
import domain.Team;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Wouter
 */
@Stateless
@Path("/team")
public class TeamREST {
 
    @EJB
    private QuizRepositoryUI repository;
    
            @POST
        @Path("/")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response update(Team team) {
            Team storedTeam = repository.getTeam(team.getId());
            if(storedTeam == null){
                storedTeam = repository.addTeam(team);
            } else {
            storedTeam.setName(team.getName());
            repository.mergeTeam(storedTeam);
            }
            return Response.ok(convertToJson(storedTeam), MediaType.APPLICATION_JSON).build();
        }
        
        @POST
        @Path("/")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Produces(MediaType.APPLICATION_JSON)
        public Response update_FormEncoded(@BeanParam Team team) {
            return update(team);
        }
        
                @GET
        @Path("/{teamId}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getTeam(@PathParam("teamId") long teamId) {
            Team storedTeam = repository.getTeam(teamId);
            return Response.ok(convertToJson(storedTeam), MediaType.APPLICATION_JSON).build();
        }
        
        @GET
        @Path("/byName")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getTeamByName(@QueryParam("name") String teamName) {
            Team storedTeam = repository.getTeamByName(teamName);
            return Response.ok(convertToJson(storedTeam), MediaType.APPLICATION_JSON).build();
        }
        
                @GET
	@Path("/{param: all|allteams}")
        	//@Produces("text/plain")
        @Produces(MediaType.APPLICATION_JSON)
        public List<Team> getAllTeams(){
          return repository.getAllTeams();
        }      
        
        private String convertToJson(Object o){
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(o);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(UNUSEDRestService.class.getName()).log(Level.SEVERE, null, ex);
                return "errors : error parsing json - " + ex.getLocalizedMessage();
            }
        }
}
