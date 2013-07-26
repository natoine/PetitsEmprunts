package models.forms;

import models.UserAccount;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;

/**
 * Classe servant à mapper les champs du formulaire de login
 * @author NaturalPad
 *
 */
public class LoginForm 
{
	/**
	 * Champ login
	 */
	@Required
	public String nickname;
	
	/**
	 * Champ password
	 */
	@Required
	public String password;
	
	
	/**
	 * Méthode de vérification des infos du formulaire de connexion. Utilisée pour vérifier si l'utilisateur existe bien et que le mot de passe est bon
	 * @return
	 */
	public String validate()
	{
		if(UserAccount.authenticate(nickname, password) == null)
		{
			return Messages.get("authentication.fail");
		}
		return null;
	}
}
