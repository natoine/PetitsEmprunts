package models;

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
	
	public static Thing findByLabel(String label)
	{
		return MorphiaObject.datastore.find(Thing.class).field("label").equal(label).get();
	}
}
