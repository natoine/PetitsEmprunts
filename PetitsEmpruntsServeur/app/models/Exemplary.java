package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;

import models.wrappers.MorphiaObject;

@Entity("Exemplary")
public class Exemplary
{
	@Id
	private ObjectId id;
	
	@Required @Reference
	private BorrowableThing thing;
	
	@Required @Reference
	private Person owner;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public BorrowableThing getThing() {
		return thing;
	}

	public void setThing(BorrowableThing thing) {
		this.thing = thing;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}
	
	public static void create(Exemplary exemplary) 
	{
		MorphiaObject.morphia.map(Exemplary.class);
		MorphiaObject.datastore.save(exemplary);
	}
	
	public static Exemplary findByThingAndOwner(BorrowableThing thing, Person owner)
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