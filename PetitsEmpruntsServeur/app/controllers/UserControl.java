package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;

import java.util.List ;
import models.Emprunt;
import models.UserAccount;

import views.html.*;

public class UserControl extends Controller 
{
	
	public static Result getEmprunts()
	{
		UserAccount currentUser = UserAccount.findByNickname(session().get("nickname"));
		List<Emprunt> eemprunts = Emprunt.findByOwner(currentUser);
		return ok(emprunts.render(eemprunts));
	}

	public static Result getPrets()
	{
		UserAccount currentUser = UserAccount.findByNickname(session().get("nickname"));
		List<Emprunt> pprets = Emprunt.findByEmprunteur(currentUser);
		return ok(prets.render(pprets));		
	}

}

