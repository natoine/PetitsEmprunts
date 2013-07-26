package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import models.wrappers.MorphiaObject;

@Entity("Thing")
public class BorrowableThing 
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
	
	public static void create(BorrowableThing thing) 
	{
		MorphiaObject.morphia.map(BorrowableThing.class);
		MorphiaObject.datastore.save(thing);
	}

	public static Map<String,String> options()
	{
		List<BorrowableThing> allThings = all();
		LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
		for(BorrowableThing thing: allThings) {
			options.put(thing.id.toString(), thing.getLabel());
		}
		return options;
	}
	
	public static List<BorrowableThing> all() {
		if (MorphiaObject.datastore != null) {
			return MorphiaObject.datastore.find(BorrowableThing.class).asList();
		} else {
			return new ArrayList<BorrowableThing>();
		}
	}
	
	public static BorrowableThing findByLabel(String label)
	{
		return MorphiaObject.datastore.find(BorrowableThing.class).field("label").equal(label).get();
	}
	
	public static BorrowableThing findById(String id)
	{
		return MorphiaObject.datastore.find(BorrowableThing.class).field("id").equal(new ObjectId(id)).get();
	}
}