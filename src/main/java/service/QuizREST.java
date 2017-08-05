/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

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
    public Quiz getQuiz(@PathParam("quizId") long quizId) {
        if(repository.getQuiz(quizId) == null){
            throw new IllegalArgumentException("This quiz does not exist");
        }
        return repository.getQuiz(quizId);
    }

    @POST
    @Path("/")
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
    @Path("/{quizId}")
    public void removeQuiz(@PathParam("quizId") long quizId) {
        if(repository.getQuiz(quizId) == null){
            throw new IllegalArgumentException("This quiz does not exist");
        }
        repository.removeQuiz(repository.getQuiz(quizId));
    }
    
    @POST
    @Path("/{quizId}/subscribe")
    @Authenticate
    public void addTeamToQuiz(@PathParam("quizId") long quizId, @BeanParam Team team) {
        System.out.println("STARTING SUBSCRIPTION");
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
    @Path("/{quizId}/unsubscribe")
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
}
