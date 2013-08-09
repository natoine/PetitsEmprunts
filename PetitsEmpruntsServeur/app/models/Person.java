package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model;


@Entity
public class Person extends Model
{
	@Id
	public Long id;
	
	/**
	 * Prénom
	 */
	public String firstname ;
	
	/**
	 * Nom
	 */
	public String lastname ;

	public Person()
	{
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}	
	
	/**
	 * Enregistre en base de données la personne passée en paramètre
	 * @param person
	 */
	public static void create(Person person)
	{
		Ebean.save(person);
	}
}
