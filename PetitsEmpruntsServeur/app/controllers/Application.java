package controllers;

import java.util.List;
import java.util.Map;

import models.Login;
import models.RegistrationForm;
import models.UserActive;

import com.mongodb.MongoException.DuplicateKey;

import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller
{
	
	static Form<RegistrationForm> registrationForm = form(RegistrationForm.class);
	static Form<UserActive> userAccountForm = form(UserActive.class);
	
	public static Result index() throws Exception 
	{
		if(session("nickname") != null)
		{
			return redirect(routes.UserProfile.index());
		}
		else
			return redirect(routes.Application.login());
	}
	
	public static Result register()
	{
		return ok(views.html.register.render(registrationForm));
	}
 
	public static Result newUserAccount() 
	{
		
		Form<RegistrationForm> filledForm = registrationForm.bindFromRequest();
		
		Map<String, List<ValidationError>> errors = filledForm.errors();
		
		for(String key : errors.keySet())
		{
			for(ValidationError error : errors.get(key))
			{
				System.out.println("error (" + key + ") : " + error.message());
			}
		}
		
		if(filledForm.hasErrors()) 
		{
			Logger.error("There was an error in the registration form.");
			return badRequest(views.html.users.render(UserActive.all(), filledForm));
		} 
		else 
		{
			try
			{
				UserActive user = new UserActive();
				user.setEmail(filledForm.field("email").value());
				user.setNickname(filledForm.field("nickname").value());
				user.setFirstname(filledForm.field("firstname").value());
				user.setHashedPassword(Secured.hash(filledForm.field("password").value()));
				user.setLastname(filledForm.field("lastname").value());
				
				UserActive.create(user);
				return redirect(routes.Application.index());	
			}
			catch(DuplicateKey exception)
			{
				return badRequest(views.html.users.render(UserActive.all(), filledForm));
			}
		}
	}
	
	public static Result logout()
	{
		if(session("nickname") != null)
		{
			session().clear();
			return redirect(routes.Application.index());
		}
		return redirect(routes.Application.index());
	}
	
	public static Result login()
	{
		Form<Login> lform = form(Login.class);	
		return ok(views.html.login.render(lform));
	}
	
	public static Result checkLoginInfos()
	{
		Form<Login> form = form(Login.class).bindFromRequest();
		if(form.hasErrors()) 
		{
            return badRequest(views.html.login.render(form));
        }
		else 
		{
            session("nickname", form.field("nickname").value());
            Logger.info("Connection of " + form.field("nickname").value());
            return redirect(routes.UserProfile.index());
        }
	}
}
