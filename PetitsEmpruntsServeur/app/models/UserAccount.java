package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

import com.avaje.ebean.Ebean;

import controllers.util.Secured;

/**
 * Classe représentant un utilisateur enregistré
 * @author xandar
 *
 */
@Entity
public class UserAccount extends Model implements Subject
{
	@Id
	public Long id;
	
	/**
	 * Login
	 */
	public String nickname ;
	
	/**
	 * Prénom
	 */
	public String firstname ;
	
	/**
	 * Nom
	 */
	public String lastname ;
	
	/**
	 * Email
	 */
	public String email ;
	
	/**
	 * Mot de passe hashé
	 */
	public String hashedPassword;
	
	/**
	 * Date de création de l'utilisateur
	 */
	public Date creationDate;
	
	/**
	 * Liste des roles possédés par l'utilisateur
	 */
	public List<UserRole> roles;
	
	public static Finder<Long, UserAccount> find = new Finder<Long, UserAccount>(Long.class, UserAccount.class);
	

	public UserAccount()
	{
		this.roles = new ArrayList<UserRole>();
		this.roles.add(UserRole.findByType(UserRole.Roles.User));
	}
	
	public String getNickname() 
	{
		return nickname;
	}

	public void setNickname(String nickname) 
	{
		this.nickname = nickname;
	}

	public String getFirstname() 
	{
		return firstname;
	}

	public void setFirstname(String firstname) 
	{
		this.firstname = firstname;
	}

	public String getLastname() 
	{
		return lastname;
	}

	public void setLastname(String lastname) 
	{
		this.lastname = lastname;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	/**
	 * Renvoi la liste de tous les utilisateurs enregistrés
	 * @return
	 */
	public static List<UserAccount> all() 
	{
		return find.all();
	}

	/**
	 * wtf ?
	 * @return
	 */
	/*public static Map<String,String> options() 
	{
		List<UserAccount> uas = all();
		LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
		for(UserAccount ua: uas) 
		{
			options.put(ua.id.toString(), ua.nickname);
		}
		return options;
	}*/
	
	/**
	 * Enregistre en base de données l'utilisateur passé en paramètre
	 * @param userAccount
	 */
	public static void create(UserAccount userAccount) 
	{
		Ebean.save(userAccount);
	}
	
	/**
	 * Met à jour dans la BDD l'utilisateur passé en paramètre
	 * @param userAccount
	 */
	public static void update(UserAccount userAccount)
	{
		Ebean.save(userAccount);
	}

	/**
	 * Récupère dans la BDD l'utilisateur dont l'identifiant a été passé en paramètre
	 * @param id
	 * @return
	 */
	public static UserAccount findById(String id)
	{
		return findById(Long.parseLong(id));
	}
	
	/**
	 * Récupère dans la BDD l'utilisateur dont l'identifiant a été passé en paramètre
	 * @param id
	 * @return
	 */
	public static UserAccount findById(Long id)
	{
		return find.byId(id);
	}

	public String getHashedPassword() 
	{
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) 
	{
		this.hashedPassword = hashedPassword;
	}
	
	/**
	 * Appelé lors du login de l'utilisateur. Vérifie que le mot de passe donné est bon.
	 * @param nickname
	 * @param password
	 * @return
	 */
	public static UserAccount authenticate(String nickname, String password)
	{
		UserAccount user = UserAccount.findByNickname(nickname);
		String hashPassword = Secured.hash(password);
		
		if(user != null && hashPassword != null)
		{
			if(user.getHashedPassword().equals(hashPassword))
			{
				return user;
			}
			else
				return null;
		}
		else
			return null;
	}
	
	/**
	 * Récupère l'utilisateur dans la base de données dont le mail est celui passé en paramètre
	 * @param mail
	 * @return
	 */
	public static UserAccount findByMail(String mail)
	{
		return find.where().eq("email", mail).findUnique();
	}
	
	/**
	 * Récupère dans la BDD l'utilisateur dont le nickname a été passé en paramètre
	 * @param nickname
	 * @return
	 */
	public static UserAccount findByNickname(String nickname)
	{
		return find.where().eq("nickname", nickname).findUnique();
	}
	
	/**
	 * Aucune idée d'a quoi ça sert
	 * @return
	 */
	public String validate()
	{
		return "ok";
	}
	
	/**
	 * Renvoi vrai si l'utilisateur passé en paramètre est identique, faux sinon
	 * @param user
	 * @return
	 */
	public boolean isSameUser(UserAccount user)
	{
		return user.id.equals(this.id);
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Plugin DeadBolt. Renvoi le champ qui sert à vérifier qu'un utilisateur est connecté.
	 */
	@Override
	public String getIdentifier() 
	{
		return this.nickname;
	}

	/**
	 * Renvoi la liste des permissions de l'utilisateur
	 */
	@Override
	public List<? extends Permission> getPermissions() 
	{
		// on renvoi une liste vide car pour l'instant les permissions ne nous intéressent pas
		return new ArrayList<Permission>(); 
	}

	/**
	 * Renvoi la liste des roles de l'utilisateur
	 */
	@Override
	public List<? extends Role> getRoles() 
	{
		List<Role> roles = new ArrayList<Role>();
		
		for(UserRole r : this.roles)
		{
			roles.add(r.role);
		}
		
		return roles;
	}
	
	public void setRoles(List<UserRole> roles)
	{
		this.roles = roles;
	}
	
	/**
	 * Ajoute un role a la liste des roles de l'utilisateur
	 * @param role
	 */
	public void addRole(UserRole role)
	{
		if(!this.roles.contains(role))
			this.roles.add(role);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}