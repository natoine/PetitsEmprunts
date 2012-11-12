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
public class Item extends Model
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Required
	private String label;
	
	private String description;
	
	public static Finder<Long,Item> find = new Finder( Long.class, Item.class );

	public static List<Item> findAll() 
	{
		return find.all();
	}
	
	 public static Map<String,String> options() {
	        List<Item> items = findAll();
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(Item i: items) {
	            options.put(i.id.toString(), i.label);
	        }
	        return options;
	    }
	
	public static void delete(Long id) 
	{
		find.ref(id).delete();
	}
	
	public static void create(Item item) 
	{
		item.save();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
