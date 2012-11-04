package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;
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

}
