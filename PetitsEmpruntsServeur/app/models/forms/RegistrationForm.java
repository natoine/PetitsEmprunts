package models.forms;

import models.UserAccount;
import play.data.validation.Constraints.EmailValidator;

/**
 * Classe servant à mapper les champs du formulaire de création d'utilisateur
 * @author NaturalPad
 * @author kraft
 */
public class RegistrationForm
{
	// list of parameters
	public String nickname;
	public String password;
	public String passwordRepeat;
	public String firstname;
	public String lastname;
	public String email;

	// list of errors
	public static final String ERROR_FIRSTNAME_IS_EMPTY = "register.error.firstnameisempty";
	public static final String ERROR_LASTNAME_IS_EMPTY = "register.error.lastnameisempty";
	public static final String ERROR_EMAIL_IS_EMPTY = "register.error.emailisempty";
	public static final String ERROR_EMAIL_IS_NOT_VALID = "register.error.emailisnotvalid";
	public static final String ERROR_EMAIL_IS_ALREADY_REGISTERED = "register.error.emailisalreadyregistered";
	public static final String ERROR_NICKNAME_IS_EMPTY = "register.error.nicknameisempty";
	public static final String ERROR_NICKNAME_IS_ALREADY_REGISTERED = "register.error.nicknameisalreadyregistered";
	public static final String ERROR_PASSWORD_IS_TOO_SHORT = "register.error.passwordistooshort";
	public static final String ERROR_PASSWORD_DOES_NOT_MATCH = "register.error.passworddoesnotmatch";

	// minimum size of password
	public static final int PASSWORD_MINIMUM_LENGTH = 4;


	/**
	 * Méthode de vérification des infos sur formulaire.
	 * Mettre ici toutes les contraintes sur l'inscription (unicité, passwords identiques, etc)
	 * @return message d'erreur s'il y en a un sinon null
	 */
	public String validate()
	{
		// check firstname is not empty
		if(this.firstname.isEmpty())
			return ERROR_FIRSTNAME_IS_EMPTY;

		// check lastname is not empty
		if(this.lastname.isEmpty())
			return ERROR_LASTNAME_IS_EMPTY;

		// check password is not too short
		if(this.password.length() < PASSWORD_MINIMUM_LENGTH)
			return ERROR_PASSWORD_IS_TOO_SHORT;

		// check if password and confirmation match
		if(!this.password.equals(this.passwordRepeat))
			return ERROR_PASSWORD_DOES_NOT_MATCH;

		// check email is not empty
		if(this.email.isEmpty())
			return ERROR_EMAIL_IS_EMPTY;

		// check email is valid
		EmailValidator ev = new EmailValidator();
		if(!ev.isValid(this.email))
			return ERROR_EMAIL_IS_NOT_VALID;

		// check email is not already registered
		// /!\ expensive, do only if everything else is ok
		if(UserAccount.findByMail(this.email) != null)
			return ERROR_EMAIL_IS_ALREADY_REGISTERED;

		// check nickname is not empty
		if(this.nickname.isEmpty())
			return ERROR_NICKNAME_IS_EMPTY;

		// check nickname is not already registered
		// /!\ expensive, do only if everything else is ok
		if(UserAccount.findByNickname(this.nickname) != null)
			return ERROR_NICKNAME_IS_ALREADY_REGISTERED;

		return null;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
