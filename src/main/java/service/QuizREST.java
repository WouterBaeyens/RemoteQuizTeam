/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.QuizRepositoryUI;
import domain.Quiz;
import domain.Team;
import filter.Authenticate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Wouter
 */
@Stateless
@Path("/quiz")
public class QuizREST {
    
    @EJB
    private QuizRepositoryUI repository;
    
    @GET
    @Path("/{param: all|allquizzes}")
            //@Produces("text/plain")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quiz> getAllQuizzes(){
      List<Quiz> quizzes = repository.getAllQuizzes();
      return quizzes;
    }

    @GET
    @Path("/{param: active|activequizzes}")
            //@Produces("text/plain")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quiz> getAllActiveQuizzes(){
        List<Quiz> quizzes = repository.getAllActiveQuizzes();
      return quizzes;
    }
    
        @GET
    @Path("/{quizId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuiz(@PathParam("quizId") long quizId) throws JsonProcessingException {
        if(repository.getQuiz(quizId) == null){
            return Response.status(404).entity("No quiz found for id: " + quizId).build();
            //throw new IllegalArgumentException("This quiz does not exist");
        }
        Quiz quiz = repository.getQuiz(quizId);
        String json = new ObjectMapper().writeValueAsString(quiz);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Quiz quiz) {
        if(repository.getQuiz(quiz.getId()) == null){
            repository.addQuiz(quiz);
            return Response.ok("New quiz added").build();
        } else{
            Quiz quizToUpdate = repository.getQuiz(quiz.getId());
            quizToUpdate.setAdress(quiz.getAdress());
            quizToUpdate.setDate(quiz.getDate());
            quizToUpdate.setMinPeople(quiz.getMinPeople());
            quizToUpdate.setName(quiz.getName());
            repository.mergeQuiz(quizToUpdate);
            return Response.ok("Quiz updated").build();
        }
    } 
        
    @DELETE
    @Path("/{quizId}")
    public Response removeQuiz(@PathParam("quizId") long quizId) {
        if(repository.getQuiz(quizId) == null){
            return Response.status(404).entity("No quiz found for id: " + quizId).build();
        }
        repository.removeQuiz(repository.getQuiz(quizId));
        return Response.ok("Quiz succesfully deleted").build();
    }
    
    @POST
    @Path("/{quizId}/subscribe")
    @Authenticate
    public Response addTeamToQuiz(@PathParam("quizId") long quizId, @BeanParam Team team) {
        Team storedTeam = null;
        if(team.getId() > 0){
            storedTeam = repository.getTeam(team.getId());
        } else if(team.getName() != null){
            storedTeam = repository.getTeamByName(team.getName());
        }
        if(repository.getQuiz(quizId) == null){
            return Response.status(404).entity("No quiz found for id: " + quizId).build();
        }
        if(storedTeam == null){
            return Response.status(404).entity("No team found for id: " + team.getId() + " (or name: " + team.getName() + ")").build();
        }
        Quiz storedQuiz = repository.getQuiz(quizId);
        storedQuiz.addTeam(storedTeam);
        repository.mergeQuiz(storedQuiz);
        return Response.ok("Team succesfully subscribed to quiz").build();
    }
    
    @POST
    @Path("/{quizId}/unsubscribe")
    @Authenticate
    public Response removeTeamFromQuiz(@BeanParam Team team, @PathParam("quizId") long quizId) {
        Team storedTeam = null;
        if(team.getId() > 0){
            storedTeam = repository.getTeam(team.getId());
        } else if(team.getName() != null){
            storedTeam = repository.getTeamByName(team.getName());
        }
        if(repository.getQuiz(quizId) == null){
            return Response.status(404).entity("No quiz found for id: " + quizId).build();
        }
        if(storedTeam == null){
            return Response.status(404).entity("No team found for id: " + team.getId() + " (or name: " + team.getName() + ")").build();
        }
        Quiz storedQuiz = repository.getQuiz(quizId);
        storedQuiz.removeTeam(storedTeam);
        repository.mergeQuiz(storedQuiz);
        return Response.ok("Team succesfully unsubscribed from quiz").build();
    }
}
