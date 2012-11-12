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
public class State extends Model
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	
	@Required
	private String label ;
	
	public static Finder<Long,State> find = new Finder( Long.class, State.class );

	public static List<State> findAll() 
	{
		return find.all();
	}
	
	 public static Map<String,String> options() {
	        List<State> states = findAll();
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(State s: states) {
	            options.put(s.id.toString(), s.label);
	        }
	        return options;
	    }
	
	public static void delete(Long id) 
	{
		find.ref(id).delete();
	}
	
	public static void create(State state) 
	{
		state.save();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}