package controllers;

import models.Borrow;
import models.Exemplary;
import models.UserActive;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class UserProfile extends Controller{
	static Form<Borrow> borrowForm = form(Borrow.class);
	static Form<Exemplary> exemplaryForm = form(Exemplary.class);
	
	public static Result index()
	{
		UserActive currentUser = UserActive.findByNickname(session("nickname"));
		return ok(views.html.userprofile.render(currentUser.getBorrows(), borrowForm));
	}
	
	public static Result seeUserAccount(String nickname)
	{
		UserActive userAccount = UserActive.findByNickname(nickname);
		if(userAccount == null)
			return redirect(routes.Application.index());
		else 
		{
			if(session("nickname") != null)
			{
				if(session("nickname").equals(nickname))
				{
					return redirect(routes.UserProfile.index());
				}
			}
			return ok(views.html.user.render(userAccount));
		}
	}
	
	public static Result seeUserPossession(String nickname)
	{
		UserActive user = UserActive.findByNickname(nickname);
		if(user == null)
			return redirect(routes.Application.index());
		else
		{
			return ok(views.html.possessions.render(user.getPossessions(), exemplaryForm));
		}
	}
	
	
	public static Result newBorrow()
	{
		return redirect(routes.UserProfile.index());
	}
	
	public static Result newPossession()
	{
		return redirect(routes.UserProfile.index());
	}
	
}