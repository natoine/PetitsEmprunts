package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import play.db.ebean.Model;
import be.objectify.deadbolt.core.models.Role;


/**
 * Classe de merde créée parce que Ebean ne sait pas faire de mapping sur une liste d'enum. Obligé de stocker ça comme une entité.
 * @author xandar
 *
 */
@Entity
public class UserRole extends Model
{
	@Id
	public String name;
	
	@Enumerated
	public Roles role;
	
	public static Finder<Long, UserRole> find = new Finder<Long, UserRole>(Long.class, UserRole.class);
	
	public UserRole()
	{
	}
	
	public UserRole(Roles type)
	{
		this.role = type;
		this.name = type.getName();
	}
	
	public static UserRole findByType(Roles type)
	{
		return find.where().eq("role", type).findUnique();
	}
	
	public String toString()
	{
		return "Role : " + this.role.getName();
	}
	
	public static List<UserRole> all()
	{
		return find.all();
	}
	
	/**
	 * Enum représentant les différentes roles d'un utilisateur
	 * @author xandar
	 *
	 */
	public enum Roles implements Role 
	{
		Admin, User;

		@Override
		public String getName() 
		{
			return this.name();
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}
}

