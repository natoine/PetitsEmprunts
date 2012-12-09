package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Exemplaire extends Model
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	
	@Required
	private Item item ;
	
	private State etat ;
	
	public enum State {
		NEW, NEARLYNEW, CORRECT, BAD, VERYBAD
	}

	public static Finder<Long,Exemplaire> find = new Finder( Long.class, Exemplaire.class );

	public static List<Exemplaire> findAll() 
	{
		return find.all();
	}
	
	 public static Map<String,String> options() {
	        List<Exemplaire> exemplaires = findAll();
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(Exemplaire ex: exemplaires) {
	            options.put(ex.id.toString(), ex.item.getLabel() + " " + ex.etat);
	        }
	        return options;
	    }
	
	public static void delete(Long id) 
	{
		find.ref(id).delete();
	}
	
	public static void create(Exemplaire exemplaire) 
	{
		exemplaire.save();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public State getEtat() {
		return etat;
	}

	public void setEtat(State etat) {
		this.etat = etat;
	}
}
