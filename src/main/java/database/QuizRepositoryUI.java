/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Quiz;
import domain.Team;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author Wouter
 */
@Remote
public interface QuizRepositoryUI {

    void addQuiz(Quiz quiz);

    Team addTeam(Team team);

    List<Quiz> getAllQuizzes();

    //A quiz is considered active if it has not passed, and it occurs less than 
    //a year from now
    public List<Quiz> getAllActiveQuizzes();
    
    List<Team> getAllTeams();

    Quiz getQuiz(long id);

    Team getTeam(long id);

    Team getTeamByName(String name);
    
    void mergeQuiz(Quiz quiz);

    void mergeTeam(Team team);

    void removeQuiz(Quiz quiz);

    void removeTeam(Team team);
    
}
