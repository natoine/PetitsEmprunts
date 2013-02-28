package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;

import controllers.MorphiaObject;

@Entity("Exemplary")
public class Exemplary
{
	@Id
	private ObjectId id;
	
	@Required @Reference
	private Thing thing;
	
	@Required @Reference
	private User owner;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public static void create(Exemplary exemplary) 
	{
		MorphiaObject.morphia.map(Exemplary.class);
		MorphiaObject.datastore.save(exemplary);
	}
	
	public static Exemplary findByThingAndOwner(Thing thing, User owner)
	{
		return MorphiaObject.datastore.find(Exemplary.class).field("thing").equal(thing).field("owner").equal(owner).get();
	}
	
	public static List<Exemplary> all() {
		if (MorphiaObject.datastore != null) {
			return MorphiaObject.datastore.find(Exemplary.class).asList();
		} else {
			return new ArrayList<Exemplary>();
		}
	}
	
	public static Exemplary findById(String id)
	{
		return MorphiaObject.datastore.find(Exemplary.class).field("_id").equal(new ObjectId(id)).get();
	}
}
