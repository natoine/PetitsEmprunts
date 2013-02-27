package controllers;

import java.util.List;
import java.util.Map;

import com.mongodb.MongoException.DuplicateKey;

import models.Borrow;
import models.Exemplary;
import models.NonActiveUserRegistrationForm;
import models.RegistrationForm;
import models.Thing;
import models.User;
import models.UserActive;
import models.UserNonActive;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class UserProfile extends Controller{
	static Form<Borrow> borrowForm = form(Borrow.class);
	static Form<Exemplary> exemplaryForm = form(Exemplary.class);
	static Form<NonActiveUserRegistrationForm> nonActiveUserRegistrationForm = form(NonActiveUserRegistrationForm.class);
	
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
		Map<String, String[]> requestData = request().body().asFormUrlEncoded() ;
		String borrowerId = requestData.get("borrower.id")[0] ;
		String ownerId = requestData.get("owner.id")[0] ;
		String thingLabel = requestData.get("thing.label")[0] ;
		
		User borrower = User.findById(borrowerId);
		User owner = User.findById(ownerId);
		Thing thing = Thing.findByLabel(thingLabel);
		Exemplary exemplary ;
		//La chose n'existe pas , il faut la créer
		if(thing == null)
		{
			thing = new Thing();
			thing.setLabel(thingLabel);
			Thing.create(thing);
			//System.out.println("Thing.id : " + thing.getId());
			//il va aussi falloir créer l'exemplaire
			exemplary = new Exemplary();
			exemplary.setOwner(owner);
			exemplary.setThing(thing);
			Exemplary.create(exemplary);
		}
		else
		{
			//il faut trouver l'exemplaire correspondant
			exemplary = Exemplary.findByThingAndOwner(thing, owner);
			//si thing existe mais est possédé par quelqu'un d'autre créer un nouvel exemplaire
			if(exemplary == null) 
			{
				exemplary = new Exemplary();
				exemplary.setOwner(owner);
				exemplary.setThing(thing);
				Exemplary.create(exemplary);
			}
		}
		Borrow borrow = new Borrow();
		borrow.setBorrower(borrower);
		borrow.setExemplary(exemplary);
		Borrow.create(borrow);
		return redirect(routes.UserProfile.index());
	}
	
	public static Result newPossession()
	{
		return redirect(routes.UserProfile.index());
	}
	
	public static Result nonactiveuserDeclaration()
	{
		return ok(views.html.registernonactiveuser.render(nonActiveUserRegistrationForm));
	}
	
	public static Result newNonActiveUserAccount()
	{
		
		Form<NonActiveUserRegistrationForm> filledForm = nonActiveUserRegistrationForm.bindFromRequest();
		
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
			return badRequest(views.html.registernonactiveuser.render(filledForm));
		} 
		else 
		{
			try
			{
				UserNonActive user = new UserNonActive();
				user.setEmail(filledForm.field("email").value());
				user.setFirstname(filledForm.field("firstname").value());
				user.setLastname(filledForm.field("lastname").value());
				UserNonActive.create(user);
				return redirect(routes.Application.index());	
			}
			catch(DuplicateKey exception)
			{
				return badRequest(views.html.registernonactiveuser.render(filledForm));
			}
		}
	}
}