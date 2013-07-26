package controllers.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import controllers.routes;

public class Secured extends Security.Authenticator 
{
	/**
	 * Pas complètement sur de l'utilité de cette fonction mais sert a mon avis a indiquer où est stocké le nom de l'utilisateur connecté.
	 * (et permet donc de savoir si on est connecté ou pas)
	 */
	@Override
	public String getUsername(Context ctx) 
	{
	    return ctx.session().get("nickname");
	}
	
	/**
	 * Méthode appelée lorsqu'un utilisateur essaye d'accéder a un controleur protégé et qu'il n'est pas authentifié.
	 */
	@Override
	public Result onUnauthorized(Context ctx) 
	{
	    return redirect(routes.Application.login());
	}
	
	/**
	 * Renvoi le hash (SHA1 de MD5) d'une chaine
	 * @param toHash chaine a hasher
	 * @return la chaine hashe
	 */
	public static String hash(String toHash)
	{
		try 
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			
			byte[] array = sha1.digest(md5.digest(toHash.getBytes("UTF-8")));
			
			StringBuffer buffer = new StringBuffer();
			
			for(int i = 0; i < array.length; i++)
			{
				buffer.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			
			return buffer.toString();
			
		} catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
