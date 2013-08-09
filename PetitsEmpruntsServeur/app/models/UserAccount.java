package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.security.Roles;
import models.wrappers.MorphiaObject;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

import controllers.util.Secured;

/**
 * Classe représentant un utilisateur enregistré
 * @author xandar
 *
 */
@Entity
public class UserAccount implements Subject
{
	@Id
	private ObjectId id;

	/**
	 * Login
	 */
	@Indexed(unique = true) @Required
	private String nickname ;

	/**
	 * Prénom
	 */
	private String firstname ;

	/**
	 * Nom
	 */
	private String lastname;

	/**
	 * Email
	 */
	@Email @Required @Indexed(unique = true)
	private String email;

	/**
	 * Mot de passe hashé
	 */
	@Required
	private String hashedPassword;

	/**
	 * Compte validé ou pas
	 */
	private boolean validated;

	/**
	 * Code de validation
	 */
	private String validationCode;

	/**
	 * Date de création de l'utilisateur
	 */
	private Date creationDate;

	/**
	 * Liste des roles possédés par l'utilisateur
	 */
	private List<Roles> roles;

	public UserAccount()
	{
		this.roles = new ArrayList<Roles>();
		this.roles.add(Roles.User);
	}
	
	public ObjectId getId() 
	{
		return id;
	}

	public void setId(ObjectId id) 
	{
		this.id = id;
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

	public boolean isValidated()
	{
		return this.validated;
	}

	public void setValidated(boolean validated)
	{
		this.validated = validated;
	}

	public String getValidationCode()
	{
		return this.validationCode;
	}

	public void setValidationCode(String validationCode)
	{
		this.validationCode = validationCode;
	}

	/**
	 * Renvoi la liste de tous les utilisateurs enregistrés
	 * @return
	 */
	public static List<UserAccount> all()
	{
		if (MorphiaObject.datastore != null)
		{
			return MorphiaObject.datastore.find(UserAccount.class).asList();
		} 
		else
		{
			return new ArrayList<UserAccount>();
		}
	}

	/**
	 * wtf ?
	 * @return
	 */
	public static Map<String,String> options()
	{
		List<UserAccount> uas = all();
		LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
		for(UserAccount ua: uas)
		{
			options.put(ua.id.toString(), ua.nickname);
		}
		return options;
	}

	/**
	 * Enregistre en base de données l'utilisateur passé en paramètre
	 * @param userAccount
	 */
	public static void create(UserAccount userAccount)
	{
		MorphiaObject.datastore.save(userAccount);
	}

	/**
	 * Met à jour dans la BDD l'utilisateur passé en paramètre
	 * @param userAccount
	 */
	public static void update(UserAccount userAccount)
	{
		create(userAccount);
	}

	/**
	 * Supprime de la BDD l'utilisateur passé en paramètre
	 * @param idToDelete
	 */
	public static void delete(String idToDelete)
	{
		UserAccount toDelete = MorphiaObject.datastore.find(UserAccount.class).field("_id").equal(new ObjectId(idToDelete)).get();
		if (toDelete != null)
		{
			MorphiaObject.datastore.delete(toDelete);
		}
	}

	/**
	 * Récupère dans la BDD l'utilisateur dont le nickname a été passé en paramètre
	 * @param nickname
	 * @return
	 */
	public static UserAccount findByNickname(String nickname)
	{
		return MorphiaObject.datastore.find(UserAccount.class).field("nickname").equal(nickname).get();
	}

	/**
	 * Récupère dans la BDD l'utilisateur dont l'identifiant a été passé en paramètre
	 * @param id
	 * @return
	 */
	public static UserAccount findById(String id)
	{
		return MorphiaObject.datastore.find(UserAccount.class).field("_id").equal(new ObjectId(id)).get();
	}

	/**
	 * Récupère dans la BDD l'utilisateur dont l'identifiant a été passé en paramètre
	 * @param id
	 * @return
	 */
	public static UserAccount findById(ObjectId id)
	{
		return MorphiaObject.datastore.find(UserAccount.class).field("_id").equal(id).get();
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
		UserAccount user = MorphiaObject.datastore.find(UserAccount.class).field("nickname").equal(nickname).get();
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
		return MorphiaObject.datastore.find(UserAccount.class).field("email").equal(mail).get();
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
		return user.getId().equals(this.getId());
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
		return this.roles;
	}

	public void setRoles(List<Roles> roles)
	{
		this.roles = roles;
	}

	/**
	 * Ajoute un role a la liste des roles de l'utilisateur
	 * @param role
	 */
	public void addRole(Roles role)
	{
		if(!this.roles.contains(role))
			this.roles.add(role);
	}
}