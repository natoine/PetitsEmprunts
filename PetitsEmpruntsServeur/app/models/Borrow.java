package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Borrow extends Model
{
	@Id
	public Long id;
	
	@OneToOne
	public Person borrower;
	
	@OneToOne
	public Exemplary exemplary;
	
	public Date startingDate;
	
	public Date closingDate;
	
	public static Finder<Long, Borrow> find = new Finder<Long, Borrow>(Long.class, Borrow.class);
	

	public Borrow()
	{
	}
	
	public Person getBorrower() {
		return borrower;
	}

	public void setBorrower(Person borrower) {
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
	
	public static List<Borrow> all() 
	{
		return find.all();
	}
	
	public static void create(Borrow borrow) 
	{
		Ebean.save(borrow);
	}
	
	public boolean isConcernedByThisBorrow(Person user)
	{
		if(this.borrower.equals(user)) return true ;
		if(this.exemplary.getOwner().equals(user)) return true ;
		return false ;
	}
	
	public static Borrow findById(String id)
	{
		return findById(Long.parseLong(id));
	}
	
	public static Borrow findById(Long id)
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
