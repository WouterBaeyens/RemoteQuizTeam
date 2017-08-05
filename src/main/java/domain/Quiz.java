/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.AUTO;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 *
 * @author Wouter
 */
@Entity
@Table(name = "Quiz")
@NamedQueries({
    @NamedQuery(name="Quiz.getAll", query="select q from Quiz q"),
    @NamedQuery(name="Quiz.getInPeriod", query="select q from Quiz q where q.date > :start AND q.date < :end"),
})
public class Quiz implements Serializable {
 
   @Id @GeneratedValue(strategy=AUTO)
   @Column(name="quiz_id", nullable=false)
    private long id;
    
    private int minPeople;
    
    private String name;
    
    private String adress;
    
    private Date date;

    @ManyToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE}, fetch= FetchType.EAGER)
    @JoinTable(name="quiz_has_teams")
    private Set<Team> teams;
    
    public Quiz(){};
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the minPeople
     */
    public int getMinPeople() {
        return minPeople;
    }

    /**
     * @param minPeople the minPeople to set
     */
    public void setMinPeople(int minPeople) {
        this.minPeople = minPeople;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the adress
     */
    public String getAdress() {
        return adress;
    }

    /**
     * @param adress the adress to set
     */
    public void setAdress(String adress) {
        this.adress = adress;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    public void addTeam(Team team){
        teams.add(team);
    }
    
    public void removeTeam(Team team){
        teams.remove(team);
    }
    
    public void setTeams(Set<Team> teams){
        this.teams = teams;
    }
    
    public Set<Team> getTeams(){
        /*Team[] teams = new Team[1];
        Team team = new Team();
        team.setName("JOHN");
        teams[0] = team;*/
        return teams;
        //return teams.toArray(new Team[teams.size()]);
    }
    
}
