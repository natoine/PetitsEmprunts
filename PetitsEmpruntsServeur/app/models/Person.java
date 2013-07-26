package models;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("Person")
public class Person 
{
	@Id
	private ObjectId id;
}
