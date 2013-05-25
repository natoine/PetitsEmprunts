package controllers.util;

import models.security.Permissions;
import play.Logger;
import play.mvc.Http.Context;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;

/**
 * Gestionnaire de permissions et de roles
 * @author xandar
 *
 */
public class SecurityResourceHandler implements DynamicResourceHandler 
{

	private static SecurityResourceHandler instance;
	
	/**
	 * Appelé lors de la vérification d'une permission. Permission != Role
	 */
	@Override
	public boolean checkPermission(String permission, DeadboltHandler handler, Context ctx) 
	{
		Subject subject = handler.getSubject(ctx);
		
		return subject.getRoles().contains(Permissions.valueOf(permission));
	}

	/**
	 * Appelé lors de la vérification d'un role. Pas encore compris comment ça marchait.
	 */
	@Override
	public boolean isAllowed(String name, String meta, DeadboltHandler handler, Context ctx) 
	{
		Logger.debug("SecurityResourceHandler : name = " + name);
		Logger.debug("SecurityResourceHandler : meta = " + meta);
		return false;
	}
	
	/**
	 * Singleton
	 * @return
	 */
	public static SecurityResourceHandler getInstance()
	{
		if(instance == null)
			instance = new SecurityResourceHandler();
		
		return instance;
	}

}
