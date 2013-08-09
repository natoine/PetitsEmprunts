package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model;

/**
 * Classe servant lors de la récupération de mot de passe d'un utilisateur
 * @author xandar
 *
 */
@Entity
public class PwdRecoveryHolder extends Model
{	
	@Id
	public Long id;

	/**
	 * Référence vers l'utilisateur qui cherche à récupérer son mot de passe
	 */
	@OneToOne
	public UserAccount user;
	
	/**
	 * Date d'expiration de la récupération de mot de passe
	 */
	public Date expireDate;
	
	/**
	 * Hash présent dans l'URL générée qui est envoyée à l'utilisateur lors de la récupération
	 */
	public String randomHash;
	
	public static Finder<Long, PwdRecoveryHolder> find = new Finder<Long, PwdRecoveryHolder>(Long.class, PwdRecoveryHolder.class);
	
	
	public PwdRecoveryHolder()
	{
	}
	
	/**
	 * Retrouve une demande de récupération de mot de passe avec un hash donnée
	 * @param hash hash servant à l'identification de la demande de récupération de mot de passe
	 * @return null si n'existe pas
	 */
	public static PwdRecoveryHolder findByHash(String hash)
	{
		return find.where().eq("randomHash", hash).findUnique();
	}
	
	/**
	 * Vérifie si une demande de récupération de mot de passé existe déjà pour un utilisateur donnée
	 * @param user l'utilisateur à vérifier
	 * @return vrai si la demande existe déjà, faux sinon
	 */
	public static boolean alreadyExists(UserAccount user)
	{
		PwdRecoveryHolder usr = find.where().eq("user.id", user.id).findUnique();
		
		if(usr.getExpireDate().after(new Date()))
			return true;
		else
		{
			PwdRecoveryHolder.delete(usr.id);
			return false;
		}
	}
	
	/**
	 * Retourne la liste de toutes les demandes de récupération de mot de passe
	 * @return
	 */
	public static List<PwdRecoveryHolder> all() 
	{
		return find.all();
	}

	/**
	 * Crée une demande de récupération de mot de passe en BDD
	 * @param pwdRecovery
	 */
	public static void create(PwdRecoveryHolder pwdRecovery) 
	{
		Ebean.save(pwdRecovery);
	}
	
	/**
	 * Supprime une demande de récupération de mot de passe dans la BDD
	 * @param idToDelete
	 */
	public static void delete(Long idToDelete) 
	{
		PwdRecoveryHolder pwdRecovery = PwdRecoveryHolder.findById(idToDelete);
		Ebean.delete(pwdRecovery);
	}
	
	public void delete()
	{
		Ebean.delete(this);
	}
	
	public static PwdRecoveryHolder findById(Long id)
	{
		return find.byId(id);
	}

	////////////////
	// ACCESSEURS //
	////////////////
	
	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getRandomHash() {
		return randomHash;
	}

	public void setRandomHash(String randomHash) {
		this.randomHash = randomHash;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
