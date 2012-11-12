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
public class Emprunt extends Model
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	
	@Required
	private Possession possession ;
	
	@Required
	private UserAccount emprunteur ;
	
	public static Finder<Long,Emprunt> find = new Finder( Long.class, Emprunt.class );

	public static List<Emprunt> findAll() 
	{
		return find.all();
	}
	
	 public static Map<String,String> options() {
	        List<Emprunt> emprunts = findAll();
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(Emprunt emprunt: emprunts) {
	            options.put(emprunt.id.toString(), emprunt.possession.getExemplaire().getItem() + " état :" + emprunt.possession.getExemplaire().getEtat() + " appartenant à : " + emprunt.possession.getProprietaire().getNickname());
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

	public Possession getPossession() {
		return possession;
	}

	public void setPossession(Possession possession) {
		this.possession = possession;
	}

	public UserAccount getEmprunteur() {
		return emprunteur;
	}

	public void setEmprunteur(UserAccount emprunteur) {
		this.emprunteur = emprunteur;
	}
}