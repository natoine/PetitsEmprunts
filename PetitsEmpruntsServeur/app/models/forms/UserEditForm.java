package models.forms;

import play.data.validation.Constraints.EmailValidator;
import models.UserAccount;
import models.security.Roles;

/**
 * Wrapper pour la formulaire d'édition d'utilisateur dans l'administration
 * @author xandar
 *
 */
public class UserEditForm 
{
	// list of parameters
	public String id;
	public String nickname;
	public String firstname;
	public String lastname;
	public String email;
	public Roles role;

	// list of errors
	public static final String ERROR_ID_IS_NOT_REGISTERED = "edituser.error.idisnotregistered";
	public static final String ERROR_FIRSTNAME_IS_EMPTY = "edituser.error.firstnameisempty";
	public static final String ERROR_LASTNAME_IS_EMPTY = "edituser.error.lastnameisempty";
	public static final String ERROR_EMAIL_IS_EMPTY = "edituser.error.emailisempty";
	public static final String ERROR_EMAIL_IS_NOT_VALID = "edituser.error.emailisnotvalid";
	public static final String ERROR_EMAIL_IS_ALREADY_REGISTERED = "edituser.error.emailisalreadyregistered";
	public static final String ERROR_NICKNAME_IS_EMPTY = "edituser.error.nicknameisempty";
	public static final String ERROR_NICKNAME_IS_ALREADY_REGISTERED = "edituser.error.nicknameisalreadyregistered";
	
	/**
	 * Validates parameters of the form
	 * @return error code or null
	 */
	public String validate()
	{
		// check firstname is not empty
		if(this.firstname.isEmpty())
			return ERROR_FIRSTNAME_IS_EMPTY;

		// check lastname is not empty
		if(this.lastname.isEmpty())
			return ERROR_LASTNAME_IS_EMPTY;

		// check email is not empty
		if(this.email.isEmpty())
			return ERROR_EMAIL_IS_EMPTY;

		// check email is valid
		EmailValidator ev = new EmailValidator();
		if(!ev.isValid(this.email))
			return ERROR_EMAIL_IS_NOT_VALID;

		// check nickname is not empty
		if(this.nickname.isEmpty())
			return ERROR_NICKNAME_IS_EMPTY;

		// check id
		// /!\ little expensive, do only if everything else is ok
		UserAccount idAccount = UserAccount.findById(this.id);
		if(idAccount == null)
			return ERROR_ID_IS_NOT_REGISTERED;

		// check email is not already registered
		// /!\ expensive, do only if everything else is ok
		UserAccount mailAccount = UserAccount.findByMail(this.email);
		if(mailAccount != null
				&& !mailAccount.getId().equals(idAccount.getId()))
			return ERROR_EMAIL_IS_ALREADY_REGISTERED;

		// check nickname is not already registered
		// /!\ expensive, do only if everything else is ok
		UserAccount nicknameAccount = UserAccount.findByNickname(this.nickname);
		if(nicknameAccount != null
				&& !nicknameAccount.getId().equals(idAccount.getId()))
			return ERROR_NICKNAME_IS_ALREADY_REGISTERED;

		return null;
	}
}
