package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;


@Entity("User")
public class User 
{
	@Id
	private ObjectId id;
	
	private String lastname;
	
	private String firstname;
	
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
			if(! borrow.getBorrower().equals(this))
			{
				if(! borrow.getExemplary().getOwner().equals(this))
					toReturn.add(borrow);
			}
		}
		return toReturn ;
	}
}