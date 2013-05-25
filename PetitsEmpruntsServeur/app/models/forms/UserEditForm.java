package models.forms;

import models.security.Roles;

/**
 * Wrapper pour la formulaire d'édition d'utilisateur dans l'administration
 * @author xandar
 *
 */
public class UserEditForm 
{
	public String nickname;
	public String firstname;
	public String lastname;
	public Roles role;
	
	/**
	 * Méthode appelée lors de la vérification des informations du formulaire. Renvoi null pour l'instant, faire quelques vérifications quand même.
	 * @return
	 */
	public String validate()
	{
		return null;
	}
}
