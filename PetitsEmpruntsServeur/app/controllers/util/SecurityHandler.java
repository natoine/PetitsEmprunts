package controllers.util;

import models.User;

import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;

/**
 * Plugin DeadBolt. Classe utilisée pour récupérer l'utilisateur courant, les évènements d'echec d'authentification etc
 * @author xandar
 *
 */
public class SecurityHandler implements DeadboltHandler 
{

	/**
	 * Appelé avant chaque vérification de droit par DeadBolt
	 */
	@Override
	public Result beforeAuthCheck(Context ctx) 
	{
		return null;
	}

	/**
	 * Renvoi le gestionnaire de droits et permissions
	 */
	@Override
	public DynamicResourceHandler getDynamicResourceHandler(Context ctx) 
	{
		return SecurityResourceHandler.getInstance();
	}

	/**
	 * Renvoi l'utilisateur courant (celui qui est connecté)
	 */
	@Override
	public Subject getSubject(Context ctx) 
	{
		String nickname = ctx.session().get("nickname");
		
		if(nickname != null)
		{
			return User.findByNickname(nickname);
		}
		
		return null;
	}

	/**
	 * Appelé a chaque violation de droit ou permission
	 */
	@Override
	public Result onAuthFailure(Context ctx, String arg1) 
	{
		Controller.flash("status", Messages.get("security.error"));
		Controller.flash("status-css", "status_error");
		return Results.redirect("/");
	}

}
