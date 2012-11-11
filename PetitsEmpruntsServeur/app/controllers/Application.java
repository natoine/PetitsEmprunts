package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;
import models.Login;
import models.UserAccount;

import views.html.*;

public class Application extends Controller {
	
	static Form<UserAccount> userAccountForm = form(UserAccount.class);
  
  public static Result index() {
    return ok(index.render("Petits Emprunts entre Amis"));
  }
  
	public static Result createAccount()
	{
		Form<UserAccount> filledForm = userAccountForm.bindFromRequest();
		UserAccount user = UserAccount.findByEmail(filledForm.field("email").value());
		
		if(user != null)
		{
			filledForm.reject("email", "this email is already taken");
		}
		
		user = UserAccount.findByNickname(filledForm.field("nickname").value());
		
		if(user != null)
		{
			filledForm.reject("nickname", "this nickname is already taken");
		}
		
		if(filledForm.hasErrors())
		{
			return badRequest(subscribe.render(filledForm));
		}
		else
		{
			UserAccount.create(filledForm.get());
			return redirect(routes.Application.index());
		}
	}

	public static Result createAccountForm()
	{
		return ok(subscribe.render(userAccountForm));
	}
	
	public static Result loginForm()
	{
		if(session().get("nickname") != null)
		{
			session().clear();
			flash("success", "You've been logged out");
		}
		return ok(login.render(form(Login.class)));
	}
	
	/**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
	    String identifier = loginForm.get().identifier ;
		UserAccount currentUser ;
	if(identifier.contains("@"))
	{
		currentUser = UserAccount.findByEmail(identifier);
	}
	else currentUser = UserAccount.findByNickname(identifier);
            session("nickname", currentUser.getNickname());
            return redirect(
                routes.Application.index()
            );
        }
    }
    
    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.loginForm()
        );
    }

}
