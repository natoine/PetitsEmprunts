package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import controllers.MorphiaObject;


@Entity("Borrow")
public class Borrow 
{
	@Id
	private ObjectId id;
	
	@Required
	private User borrower;
	
	@Required
	private Exemplary exemplary;
	
	@Required
	private Date startingDate;
	
	private Date closingDate;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public User getBorrower() {
		return borrower;
	}

	public void setBorrower(User borrower) {
		this.borrower = borrower;
	}

	public Exemplary getExemplary() {
		return exemplary;
	}

	public void setExemplary(Exemplary exemplary) {
		this.exemplary = exemplary;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}
	
	public boolean hasBeenReturned()
	{
		if(closingDate == null) return false ;
		else return true ;
	}
	
	public static List<Borrow> all() {
		if (MorphiaObject.datastore != null) {
			return MorphiaObject.datastore.find(Borrow.class).asList();
		} else {
			return new ArrayList<Borrow>();
		}
	}
}