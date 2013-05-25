package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.User;
import models.UserActive;
import models.forms.UserEditForm;
import models.security.Roles;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import be.objectify.deadbolt.java.actions.*;

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
		List<UserActive> users = UserActive.allActive();
		return ok(views.html.users.render(users));
	}
	
	/**
	 * Affiche le formulaire d'édition de l'utilisateur passé en paramètre.
	 * @param nickname
	 * @return
	 */
	public static Result editUser(String nickname)
	{
		User user = User.findByNickname(nickname);
		
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
			flash("status", Messages.get("errorform") + " :<br />" + Application.getHTMLReadableErrors(errors));
			flash("status-css", "status_error");
			return redirect(routes.Application.register());
		}
		else 
		{
			User user = User.findByNickname(filledForm.field("nickname").value());
			
			user.setFirstname(filledForm.field("firstname").value());
			user.setLastname(filledForm.field("lastname").value());
			user.setRoles(new ArrayList<Roles>());
			user.addRole(Roles.valueOf(filledForm.field("role").value()));
			
			User.update(user);
			
			return redirect(routes.Administration.editUser(user.getNickname()));
		}
	}
}
