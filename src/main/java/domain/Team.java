/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.AUTO;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.ws.rs.FormParam;

/**
 *
 * @author Wouter
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Team.getAll", query="select t from Team t"),
    @NamedQuery(name="Team.getByName", query="select t from Team t where t.name = :name")
})
public class Team implements Serializable {
    
    @FormParam("id")
    @Id @GeneratedValue(strategy=AUTO)
    private long id;
    
    @FormParam("name")
    @Column(unique=true)
    private String name;
    
    public Team(){}

    
    
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
    
        @Override
    public boolean equals(Object other){
        if (other == null) return false;
    if (other == this) return true;
    if (!(other instanceof Team))return false;
    Team otherMyClass = (Team)other;
    return (this.getId() == otherMyClass.getId() && this.getName().equals(otherMyClass.getName()));
    }
    
      @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
