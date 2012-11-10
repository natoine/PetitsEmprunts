package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class UserAccount extends Model{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Required @Column(unique=true)
	private String nickname;
	
	@Required @Email @Column(unique=true)
	private String email;
	
	@Required
	private String password;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long _id)
	{
		id = _id; 
	}

	public String getNickname()
	{
		return nickname ;
	}

	public void setNickname(String _nickname)
	{
		nickname = _nickname ;
	}

	public String getEmail()
	{
		return email ;
	}

	public void setEmail(String _email)
	{
		email = _email ;
	}
	
	public String getPassword()
	{
		return password ;
	}

	public void setPassword(String _password)
	{
		password = _password ;
	}

	public static Finder<Long, UserAccount> find = new Finder<Long, UserAccount>(Long.class, UserAccount.class);
	
	public static List<UserAccount> findAll()
	{
		return find.all();
	}
	
	public static UserAccount findByEmail(String email)
	{
		return find.where().eq("email", email).findUnique();
	}
	
	public static UserAccount findByNickname(String nickname)
	{
		return find.where().eq("nickname", nickname).findUnique();
	}
	
	public static void create(UserAccount ua)
	{
		ua.save();
	}
}
