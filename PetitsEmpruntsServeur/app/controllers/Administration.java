package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.UserAccount;
import models.forms.UserEditForm;
import models.security.Roles;
import be.objectify.deadbolt.java.actions.*;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controleur protégé par identification : toutes les méthodes sont accessibles uniquement
 * aux utilisateurs ayant le role "Admin"
 * @author NaturalPad
 *
 */
@Restrict({@Group("Admin")})
public class Administration extends Controller 
{
	static Form<UserEditForm> userEditForm = Form.form(UserEditForm.class);
	
	/**
	 * Page d'accueil de l'administration
	 * @return
	 */
	public static Result index()
	{
		return ok(views.html.admin.render());
	}
	
	/**
	 * Affiche la liste des utilisateurs enregistrés
	 * @return
	 */
	public static Result users()
	{
		List<UserAccount> users = UserAccount.all();
		return ok(views.html.users.render(users));
	}
	
	/**
	 * Affiche le formulaire d'édition de l'utilisateur passé en paramètre.
	 * @param nickname
	 * @return
	 */
	public static Result editUser(String nickname)
	{
		UserAccount user = UserAccount.findByNickname(nickname);
		
		Roles[] roles = Roles.values();
		
		return ok(views.html.editUser.render(user, roles));
	}
	
	/**
	 * Modifie l'utilisateur passé en paramètre a partir des données du formulaire envoyé
	 * @return
	 */
	public static Result confirmEditUser()
	{
		Form<UserEditForm> filledForm = userEditForm.bindFromRequest();
		Map<String, List<ValidationError>> errors = filledForm.errors();
		
		if(filledForm.hasErrors()) 
		{
			flash("status", Messages.get("errorform") + " :<br />" + AbstractController.getHTMLReadableErrors(errors));
			flash("status-css", "status_error");
			return redirect(routes.Administration.users());
		}
		else 
		{
			// update account
			UserAccount user = UserAccount.findById(filledForm.field("id").value());
			user.setNickname(filledForm.field("nickname").value());
			user.setFirstname(filledForm.field("firstname").value());
			user.setLastname(filledForm.field("lastname").value());
			user.setEmail(filledForm.field("email").value());
			user.setValidated(filledForm.field("validated").value().equals("on"));
			user.setRoles(new ArrayList<Roles>());
			user.addRole(Roles.valueOf(filledForm.field("role").value()));
			
			UserAccount.update(user);
			
			return redirect(routes.Administration.editUser(user.getNickname()));
		}
	}
}
