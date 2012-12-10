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
		if(session().get("nickname") != null)
		{
			UserAccount currentUser = UserAccount.findByNickname(session().get("nickname"));
			System.out.println("user : " + currentUser);
			List<Emprunt> eemprunts = Emprunt.findByOwner(currentUser);//.findAll();
			System.out.println("userListEmprunts size : " + eemprunts.size());
			return ok(emprunts.render(eemprunts));
		}
		else 
		{
			flash("error", "Vous n'avez pas le droit d'accéder à cette page");
			return redirect(routes.AccountControl.index());
		}
	}

	public static Result getPrets()
	{
		if(session().get("nickname") != null)
		{		
			UserAccount currentUser = UserAccount.findByNickname(session().get("nickname"));
			List<Emprunt> pprets = Emprunt.findByEmprunteur(currentUser);
			return ok(prets.render(pprets));
		}
		else 
		{
			flash("error", "Vous n'avez pas le droit d'accéder à cette page");
			return redirect(routes.AccountControl.index());
		}	
	}

}

