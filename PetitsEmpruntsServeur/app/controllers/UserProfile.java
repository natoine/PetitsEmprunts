package controllers;

import models.UserActive;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class UserProfile extends Controller{

	public static Result index()
	{
		UserActive currentUser = UserActive.findByNickname(session("nickname"));
		return ok(views.html.userprofile.render());
	}
}
