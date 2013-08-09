package controllers;

import java.util.List;
import java.util.Map;

import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Controller;

public class AbstractController extends Controller {

	/**
	 * Renvoi vrai si l'utilisateur est connecté, faux sinon
	 * @return Renvoi vrai si l'utilisateur est connecté, faux sinon
	 */
	public static boolean isConnected()
	{
		if(session("nickname") != null)
			return true;
		else
			return false;
	}

	/**
	 * Renvoi une string HTML affichant les erreurs passées en paramètres
	 * @param errors liste des erreurs
	 * @return description HTML des erreurs
	 */
	public static String getHTMLReadableErrors(Map<String, List<ValidationError>> errors)
	{
		StringBuilder sb = new StringBuilder();

		for(String key : errors.keySet())
		{
			for(ValidationError error : errors.get(key))
			{
				if(key != null && !key.isEmpty())
				{
					sb.append(key);
					sb.append(" : ");
				}

				if(error.message() != null)
				{
					sb.append(Messages.get(error.message()));
					sb.append("<br />");
				}
			}
		}

		return sb.toString();
	}

}
