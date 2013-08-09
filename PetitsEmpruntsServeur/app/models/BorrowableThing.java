package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.Ebean;


@Entity
public class BorrowableThing extends Model
{
	@Id
	public Long id;
	
	
	public String label;
	
	public String description;
	
	public static Finder<Long, BorrowableThing> find = new Finder<Long, BorrowableThing>(Long.class, BorrowableThing.class);

	
	public BorrowableThing()
	{
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
	
	public static void create(BorrowableThing thing) 
	{
		Ebean.save(thing);
	}

	/*public static Map<String,String> options()
	{
		List<BorrowableThing> allThings = all();
		LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
		for(BorrowableThing thing: allThings) {
			options.put(thing.id.toString(), thing.getLabel());
		}
		return options;
	}*/
	
	public static List<BorrowableThing> all() 
	{
		return find.all();
	}
	
	public static BorrowableThing findByLabel(String label)
	{
		return find.where().eq("label", label).findUnique();
	}
	
	public static BorrowableThing findById(String id)
	{
		return findById(Long.parseLong(id));
	}
	
	public static BorrowableThing findById(Long id)
	{
		return find.byId(id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}