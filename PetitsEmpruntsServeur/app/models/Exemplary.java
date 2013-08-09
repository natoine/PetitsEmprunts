package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;


@Entity
public class Exemplary extends Model
{
	@Id
	public Long id;
	
	@OneToOne
	public BorrowableThing thing;
	
	@OneToOne
	public Person owner;
	
	public static Finder<Long, Exemplary> find = new Finder<Long, Exemplary>(Long.class, Exemplary.class);
	
	public Exemplary()
	{
	}

	public BorrowableThing getThing() 
	{
		return thing;
	}

	public void setThing(BorrowableThing thing) 
	{
		this.thing = thing;
	}

	public Person getOwner() 
	{
		return owner;
	}

	public void setOwner(Person owner) 
	{
		this.owner = owner;
	}
	
	public static void create(Exemplary exemplary) 
	{
		Ebean.save(exemplary);
	}
	
	public static Exemplary findByThingAndOwner(BorrowableThing thing, Person owner)
	{
		return find.where().eq("thing", thing).eq("person.id", owner.id).findUnique();
	}
	
	public static List<Exemplary> all() 
	{
		return find.all();
	}
	
	public static Exemplary findById(String id)
	{
		return findById(Long.parseLong(id));
	}
	
	public static Exemplary findById(Long id)
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