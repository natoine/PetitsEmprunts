package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import controllers.MorphiaObject;

@Entity("Thing")
public class Thing 
{
	@Id
	private ObjectId id;
	
	@Required
	private String label;
	
	private String description;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
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
	
	public static void create(Thing thing) 
	{
		MorphiaObject.morphia.map(Thing.class);
		MorphiaObject.datastore.save(thing);
	}

	public static Map<String,String> options()
	{
		List<Thing> allThings = all();
		LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
		for(Thing thing: allThings) {
			options.put(thing.id.toString(), thing.getLabel());
		}
		return options;
	}
	
	public static List<Thing> all() {
		if (MorphiaObject.datastore != null) {
			return MorphiaObject.datastore.find(Thing.class).asList();
		} else {
			return new ArrayList<Thing>();
		}
	}
	
	public static Thing findByLabel(String label)
	{
		return MorphiaObject.datastore.find(Thing.class).field("label").equal(label).get();
	}
}
