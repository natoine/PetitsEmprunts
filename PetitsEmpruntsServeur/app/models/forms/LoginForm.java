package models.forms;

import models.UserAccount;

/**
 * Classe servant à mapper les champs du formulaire de login
 * @author NaturalPad
 *
 */
public class LoginForm 
{
	// list of parameters
	public String nickname;
	public String password;

	// list of errors
	public static final String ERROR_NICKNAME_IS_EMPTY = "login.error.nicknameisempty";
	public static final String ERROR_PASSWORD_IS_EMPTY = "login.error.passwordisempty";
	public static final String ERROR_AUTHENTICATION_FAILED = "login.error.authenticationfailed";
	public static final String ERROR_ACCOUNT_NOT_VALIDATED = "login.error.accountnotvalidated";
	
	/**
	 * Méthode de vérification des infos du formulaire de connexion. Utilisée pour vérifier si l'utilisateur existe bien et que le mot de passe est bon
	 * @return
	 */
	public String validate()
	{
		// check nickname is not empty
		if(this.nickname.isEmpty())
			return ERROR_NICKNAME_IS_EMPTY;

		// check password is empty
		if(this.password.isEmpty())
			return ERROR_PASSWORD_IS_EMPTY;

		// check authentication
		// /!\ expensive (database request)
		UserAccount userAccount = UserAccount.authenticate(nickname, password);
		if(userAccount == null)
			return ERROR_AUTHENTICATION_FAILED;

		// check validated
		if(!userAccount.isValidated())
			return ERROR_ACCOUNT_NOT_VALIDATED;

		return null;
	}
}
