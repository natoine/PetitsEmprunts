package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

import controllers.MorphiaObject;


@Entity("User")
public class User 
{
	@Id
	private ObjectId id;
	
	private String lastname;
	
	private String firstname;
	
	@Email @Required @Indexed(unique = true)
	private String email;
	
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNickname()
	{
		return firstname + " " + lastname ;
	}
	
	/**
	 * get all the borrows concerning our user (owner or borrower).
	 * @return List<Borrow>
	 */
	public List<Borrow> getBorrows()
	{
		List <Borrow> allBorrows = Borrow.all();
		List <Borrow> toReturn = new ArrayList<Borrow>();
		for(Borrow borrow : allBorrows)
		{
			if(borrow.getBorrower().getNickname().equals(this.getNickname())) toReturn.add(borrow);
			else if(borrow.getExemplary().getOwner().getNickname().equals(this.getNickname())) toReturn.add(borrow);
		}
		return toReturn ;
	}
	
	public static Map<String,String> options() {
		List<User> uas = all();
		LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
		for(User ua: uas) {
			options.put(ua.id.toString(), ua.getNickname());
		}
		return options;
	}
	
	public static List<User> all() {
		if (MorphiaObject.datastore != null) {
			return MorphiaObject.datastore.find(User.class).asList();
		} else {
			return new ArrayList<User>();
		}
	}
	
	public static User findById(String id)
	{
		return MorphiaObject.datastore.find(User.class).field("_id").equal(new ObjectId(id)).get();
	}
	
	public List<Exemplary> getPossessions()
	{
		return MorphiaObject.datastore.find(Exemplary.class).field("owner").equal(this).asList();
		//return Exemplary.all();
	}
}