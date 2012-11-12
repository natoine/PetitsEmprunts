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
public class Possession extends Model
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	
	@Required
	private Exemplaire exemplaire;
	
	@Required
	private UserAccount proprietaire;
	
	public static Finder<Long,Possession> find = new Finder( Long.class, Possession.class );

	public static List<Possession> findAll() 
	{
		return find.all();
	}
	
	 public static Map<String,String> options() {
	        List<Possession> possessions = findAll();
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(Possession pos: possessions) {
	            options.put(pos.id.toString(), pos.exemplaire.getItem().getLabel() + pos.exemplaire.getEtat());
	        }
	        return options;
	    }
	
	public static void delete(Long id) 
	{
		find.ref(id).delete();
	}
	
	public static void create(Possession possession) 
	{
		possession.save();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Exemplaire getExemplaire() {
		return exemplaire;
	}

	public void setExemplaire(Exemplaire exemplaire) {
		this.exemplaire = exemplaire;
	}

	public UserAccount getProprietaire() {
		return proprietaire;
	}

	public void setProprietaire(UserAccount proprietaire) {
		this.proprietaire = proprietaire;
	}
}
