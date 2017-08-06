/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Quiz;
import domain.Team;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;


/**
 *
 * @author Wouter
 */
@Stateless
public class QuizDb implements QuizRepositoryUI {
    
    private Logger logger = Logger.getLogger(QuizDb.class);
    
    /*@Resource
    UserTransaction ut;*/
    @PersistenceContext(unitName="LosFlippos_unit", type=PersistenceContextType.TRANSACTION)
    private EntityManager em;
    
    @Override
    public Team addTeam(Team team) {
        Team t = getTeam(team.getId());
        if (t != null) {
            throw new DbException("[Err. already persisted: old " + t + " | new " + team + "] ");
        }
        try {
            //ut.begin();
            em.persist(team);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
        return team;
    }
    
    @Override
    public Team getTeam(long id){
        return em.find(Team.class, id);
    }
    
    @Override
    public Team getTeamByName(String name){
        try {
            Query query = em.createNamedQuery("Team.getByName");
            query.setParameter("name", name);
            return ((Team) query.getSingleResult());
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            logger.error("em=" + em + "..." + e.getLocalizedMessage());
            throw new DbException(e.getMessage(), e);
        }
    }
    
    @Override
    public List<Team> getAllTeams() {
        try {
            Query query = em.createNamedQuery("Team.getAll");
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("em=" + em + "..." + e.getLocalizedMessage());
            throw new DbException(e.getMessage(), e);
        }
    }
    
    @Override
    public void mergeTeam(Team team) {
        Team p = getTeam(team.getId());
        if (p == null) {
            throw new DbException("[Err. to-be-update person is not yet in the databas: " + team + "] ");
        }
        try {
            //ut.begin();
            em.merge(team);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void removeTeam(Team team) {
        Team t = getTeam(team.getId());
        if (t == null) {
            throw new DbException("[Err. to-be-removed person is not yet in the database: " + team + "] ");
        }
        try {
            //ut.begin();
            em.remove(t);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void addQuiz(Quiz quiz) {
        Quiz q = getQuiz(quiz.getId());
        if (q != null) {
            throw new DbException("[Err. already persisted: old " + q + " | new " + quiz + "] ");
        }
        try {
            //ut.begin();
            em.persist(quiz);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public Quiz getQuiz(long id){
        return em.find(Quiz.class, id);
    }
        
    @Override
        public List<Quiz> getAllQuizzes() {
        try {
            Query query = em.createNamedQuery("Quiz.getAll");
            List<Quiz> quizzes = query.getResultList();
            return quizzes;
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }
        
    //A quiz is considered active if it has not passed, and it occurs less than 
    //a year from now
    @Override
        public List<Quiz> getAllActiveQuizzes(){
        try{       
            Query query = em.createNamedQuery("Quiz.getInPeriod");
            LocalDate now = LocalDate.now();
            LocalDate inOneYear = LocalDate.now().plusYears(1);
            query.setParameter("start", Date.valueOf(now));
            query.setParameter("end", Date.valueOf(inOneYear));
            //date.
            List<Quiz> quizzes = query.getResultList();
            return quizzes;
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }
    
    @Override
    public void mergeQuiz(Quiz quiz) {
        Quiz q = getQuiz(quiz.getId());
        if (q == null) {
            throw new DbException("[Err. to-be-update person is not yet in the databas: " + quiz + "] ");
        }
        try {
            //ut.begin();
            em.merge(quiz);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void removeQuiz(Quiz quiz) {
        Quiz q = getQuiz(quiz.getId());
        if (q == null) {
            throw new DbException("[Err. to-be-removed person is not yet in the database: " + quiz + "] ");
        }
        try {
            //ut.begin();
            em.remove(q);
            //ut.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            logger.error(e.getMessage());
        }
    }
}
