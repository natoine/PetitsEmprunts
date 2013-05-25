package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.wrappers.MorphiaObject;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;

/**
 * Classe servant lors de la récupération de mot de passe d'un utilisateur
 * @author xandar
 *
 */
@Entity
public class PwdRecoveryHolder 
{	
	@Id
	private ObjectId id;

	/**
	 * Référence vers l'utilisateur qui cherche à récupérer son mot de passe
	 */
	@Reference
	public User user;
	
	/**
	 * Date d'expiration de la récupération de mot de passe
	 */
	public Date expireDate;
	
	/**
	 * Hash présent dans l'URL générée qui est envoyée à l'utilisateur lors de la récupération
	 */
	public String randomHash;
	
	
	/**
	 * Retrouve une demande de récupération de mot de passe avec un hash donnée
	 * @param hash hash servant à l'identification de la demande de récupération de mot de passe
	 * @return null si n'existe pas
	 */
	public static PwdRecoveryHolder findByHash(String hash)
	{
		return MorphiaObject.datastore.find(PwdRecoveryHolder.class).field("randomHash").equal(hash).get();
	}
	
	/**
	 * Vérifie si une demande de récupération de mot de passé existe déjà pour un utilisateur donnée
	 * @param user l'utilisateur à vérifier
	 * @return vrai si la demande existe déjà, faux sinon
	 */
	public static boolean alreadyExists(User user)
	{
		PwdRecoveryHolder usr = MorphiaObject.datastore.find(PwdRecoveryHolder.class).field("user").equal(user).get();
		
		if(usr.getExpireDate().after(new Date()))
			return true;
		else
		{
			PwdRecoveryHolder.delete(usr.getId());
			return false;
		}
	}
	
	/**
	 * Retourne la liste de toutes les demandes de récupération de mot de passe
	 * @return
	 */
	public static List<PwdRecoveryHolder> all() 
	{
		if (MorphiaObject.datastore != null) 
		{
			return MorphiaObject.datastore.find(PwdRecoveryHolder.class).asList();
		}
		else
		{
			return new ArrayList<PwdRecoveryHolder>();
		}
	}

	/**
	 * Crée une demande de récupération de mot de passe en BDD
	 * @param pwdRecovery
	 */
	public static void create(PwdRecoveryHolder pwdRecovery) 
	{
		MorphiaObject.datastore.save(pwdRecovery);
	}
	
	/**
	 * Supprime une demande de récupération de mot de passe dans la BDD
	 * @param idToDelete
	 */
	public static void delete(ObjectId idToDelete) 
	{
		PwdRecoveryHolder toDelete = MorphiaObject.datastore.find(PwdRecoveryHolder.class).field("_id").equal(idToDelete).get();
		if (toDelete != null) 
		{
			MorphiaObject.datastore.delete(toDelete);
		}
	}

	/**
	 * Supprime une demande de récupération de mot de passe dans la BDD
	 * @param idToDelete
	 */
	public static void delete(String idToDelete) 
	{
		PwdRecoveryHolder toDelete = MorphiaObject.datastore.find(PwdRecoveryHolder.class).field("_id").equal(new ObjectId(idToDelete)).get();
		if (toDelete != null) 
		{
			MorphiaObject.datastore.delete(toDelete);
		}
	}

	////////////////
	// ACCESSEURS //
	////////////////
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getRandomHash() {
		return randomHash;
	}

	public void setRandomHash(String randomHash) {
		this.randomHash = randomHash;
	}
	
}
