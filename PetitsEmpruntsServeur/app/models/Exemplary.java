package models;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("Exemplary")
public class Exemplary
{
	@Id
	private ObjectId id;
	
	@Required
	private Thing thing;
	
	@Required
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
}
