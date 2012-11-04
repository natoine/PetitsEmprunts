package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class UserAccount extends Model{
	
	@Id
	public Long id;
	
	@Required @Column(unique=true)
	public String nickname;
	
	@Required @Email
	public String email;
	
	@Required
	public String password;
	
	public static Finder<Long, UserAccount> find = new Finder<Long, UserAccount>(Long.class, UserAccount.class);
	
	public static List<UserAccount> findAll()
	{
		return find.all();
	}
	
	public static UserAccount findByEmail(String email)
	{
		return find.where().eq("email", email).findUnique();
	}
}
