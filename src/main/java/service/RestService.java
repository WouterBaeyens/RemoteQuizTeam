/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.QuizDb;
import database.QuizRepositoryUI;
import domain.Quiz;
import domain.Team;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("/")
public class RestService {
    
        @EJB
        private QuizRepositoryUI repository;
        //private QuizRepositoryUI repository = new QuizDb();
    
    	@GET
	@Path("/{param: allquizzes|quizzes}")
        	//@Produces("text/plain")
        @Produces(MediaType.APPLICATION_JSON)
        public List<Quiz> getAllQuizzes(){
          List<Quiz> quizzes = repository.getAllQuizzes();
          return quizzes;
        }
        
        @GET
        @Path("quiz/{quizId}")
        @Produces(MediaType.APPLICATION_JSON)
        public Quiz getQuiz(@PathParam("quizId") long quizId) {
            if(repository.getQuiz(quizId) == null){
                throw new IllegalArgumentException("This quiz does not exist");
            }
            return repository.getQuiz(quizId);
        }

        @POST
        @Path("quiz")
        @Consumes(MediaType.APPLICATION_JSON)
        public void update(Quiz quiz) {
            if(repository.getQuiz(quiz.getId()) == null){
                repository.addQuiz(quiz);
            } else{
                Quiz quizToUpdate = repository.getQuiz(quiz.getId());
                quizToUpdate.setAdress(quiz.getAdress());
                quizToUpdate.setDate(quiz.getDate());
                quizToUpdate.setMinPeople(quiz.getMinPeople());
                quizToUpdate.setName(quiz.getName());
                repository.mergeQuiz(quizToUpdate);
            }
        }       
        
        @DELETE
        @Path("quiz/{quizId}")
        public void removeQuiz(@PathParam("quizId") long quizId) {
            if(repository.getQuiz(quizId) == null){
                throw new IllegalArgumentException("This quiz does not exist");
            }
            repository.removeQuiz(repository.getQuiz(quizId));
        }
        
        @POST
        @Path("quiz/{quizId}/subscribe")
        public void addTeamToQuiz(@PathParam("quizId") long quizId, @BeanParam Team team) {
            Team storedTeam = null;
            if(team.getId() > 0){
                storedTeam = repository.getTeam(team.getId());
            } else if(team.getName() != null){
                storedTeam = repository.getTeamByName(team.getName());
            }
            if(repository.getQuiz(quizId) == null){
                throw new IllegalArgumentException("This quiz does not exist");
            }
            if(storedTeam == null){
                throw new IllegalArgumentException("This team is not found in the database");
            }
            Quiz storedQuiz = repository.getQuiz(quizId);
            storedQuiz.addTeam(storedTeam);
            repository.mergeQuiz(storedQuiz);
        }
        
        
        @POST
        @Path("quiz/{quizId}/unsubscribe")
        public void removeTeamFromQuiz(@BeanParam Team team, @PathParam("quizId") long quizId) {
            Team storedTeam = null;
            if(team.getId() > 0){
                storedTeam = repository.getTeam(team.getId());
            } else if(team.getName() != null){
                storedTeam = repository.getTeamByName(team.getName());
            }
            if(repository.getQuiz(quizId) == null){
                throw new IllegalArgumentException("This quiz does not exist");
            }
            if(storedTeam == null){
                throw new IllegalArgumentException("This team is not found in the database");
            }
            Quiz storedQuiz = repository.getQuiz(quizId);
            storedQuiz.removeTeam(storedTeam);
            repository.mergeQuiz(storedQuiz);
        }
        
                
        @POST
        @Path("team")
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
        @Path("team")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Produces(MediaType.APPLICATION_JSON)
        public Response update_FormEncoded(@BeanParam Team team) {
            return update(team);
        }
        
        @GET
        @Path("team/{teamId}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getTeam(@PathParam("teamId") long teamId) {
            Team storedTeam = repository.getTeam(teamId);
            return Response.ok(convertToJson(storedTeam), MediaType.APPLICATION_JSON).build();
        }
        
        @GET
        @Path("team/byName")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getTeamByName(@QueryParam("name") String teamName) {
            Team storedTeam = repository.getTeamByName(teamName);
            return Response.ok(convertToJson(storedTeam), MediaType.APPLICATION_JSON).build();
        }
        
        @GET
	@Path("/{param: allteams|teams}")
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
                Logger.getLogger(RestService.class.getName()).log(Level.SEVERE, null, ex);
                return "errors : error parsing json - " + ex.getLocalizedMessage();
            }
        }
}
