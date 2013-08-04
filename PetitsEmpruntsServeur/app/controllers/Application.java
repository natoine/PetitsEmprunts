package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.PwdRecoveryHolder;
import models.UserAccount;
import models.forms.LoginForm;
import models.forms.PasswordForm;
import models.forms.RecoveryForm;
import models.forms.RegistrationForm;
import models.wrappers.MorphiaObject;
import play.Logger;
import play.Play;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

import be.objectify.deadbolt.java.actions.SubjectNotPresent;
import be.objectify.deadbolt.java.actions.SubjectPresent;

import com.mongodb.MongoException.DuplicateKey;
import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import controllers.util.Secured;

public class Application extends Controller {
  
	/**
	 * Modèle du formulaire d'inscription
	 */
	
	static Form<RegistrationForm> registrationForm 	= Form.form(RegistrationForm.class);
	
	/**
	 * Modèle du formulaire de connexion
	 */
	static Form<UserAccount> userAccountForm 		= Form.form(UserAccount.class);
	
	
	/**
	 * Affiche l'index de l'application.
	 * @return
	 */
	public static Result index() 
	{
		return ok(views.html.index.render());
	}
  
	/**
	 * Affiche le formulaire de création de compte utilisateur
	 * @return
	 */
	@SubjectNotPresent
	public static Result register()
	{
		if(!Application.isConnected())
			return ok(views.html.register.render(registrationForm));
		else
			return redirect(routes.Application.index());
	}

	/**
	 * Appelé lors de la soumissions du formulaire de création d'un nouveau compte utilisateur. Crée un nouvel utilisateur dans la BDD s'il n'y a pas d'erreurs
	 * @return
	 */
	@SubjectNotPresent
	public static Result newUserAccount() 
	{
		Form<RegistrationForm> filledForm = registrationForm.bindFromRequest();
		Map<String, List<ValidationError>> errors = filledForm.errors();
		
		if(filledForm.hasErrors()) 
		{
			flash("status", Messages.get("errorform") + " :<br />" + Application.getHTMLReadableErrors(errors));
			flash("status-css", "status_error");
			return redirect(routes.Application.register());
		}
		else 
		{
			try
			{
				UserAccount user = new UserAccount();
				user.setEmail(filledForm.field("email").value());
				user.setNickname(filledForm.field("nickname").value());
				user.setFirstname(filledForm.field("firstname").value());
				user.setHashedPassword(Secured.hash(filledForm.field("password").value()));
				user.setLastname(filledForm.field("lastname").value());
				user.setCreationDate(new Date());
				
				UserAccount.create(user);
				
				flash("status", Messages.get("register.success"));
				flash("status-css", "status_success");
				
				return redirect(routes.Application.login());	
			}
			catch(DuplicateKey exception)
			{
				return badRequest();
			}
		}
	}
	
	/**
	 * Déconnexion d'un utilisateur
	 * @return
	 */
	@SubjectPresent
	public static Result logout()
	{
		if(Application.isConnected())
		{
			session().clear();
			return redirect(routes.Application.index());
		}
		return redirect(routes.Application.index());
	}
	
	/**
	 * Affiche le formulaire de connexion
	 * @return
	 */
	public static Result login()
	{
		if(!Application.isConnected())
		{
			Form<LoginForm> lform = Form.form(LoginForm.class);	
			return ok(views.html.login.render(lform));
		}
		else
		{
			return redirect(routes.Application.userProfile());
		}
	}
	
	/**
	 * Appelé lors de la connexion d'un utilisateur (vérification des informations de connexion)
	 * @return
	 */
	@SubjectNotPresent
	public static Result checkLoginInfos()
	{
		Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();
		Map<String, List<ValidationError>> errors = form.errors();
		
		if(form.hasErrors()) 
		{
			flash("status", Messages.get("errorform") + " :<br />" + Application.getHTMLReadableErrors(errors));
			flash("status-css", "status_error");
			
			return badRequest(views.html.login.render(form));
		}
		else 
		{
			session("nickname", form.field("nickname").value());
			
			flash("status", Messages.get("authentication.success"));
			flash("status-css", "status_success");
			
			Logger.info("Connection of " + form.field("nickname").value());
			return redirect(routes.Application.userProfile());
		}
	}
	
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
	
	/**
	 * Affichage du formulaire de saisie du mail pour le changement de mot de passe
	 * @return
	 */
	public static Result passwordRecoveryForm()
	{
		return ok(views.html.passwordRecovery.render());
	}
	
	/**
	 * Vérification après la soumission du mail pour la récupération de mot de passe
	 * @return
	 */
	public static Result passwordRecovery()
	{
		Form<RecoveryForm> form = Form.form(RecoveryForm.class).bindFromRequest();
		Map<String, List<ValidationError>> errors = form.errors();
		
		if(form.hasErrors())
		{
			flash("status", Messages.get("errorform") + " :<br />" + Application.getHTMLReadableErrors(errors));
			flash("status-css", "status_error");
			return redirect(routes.Application.passwordRecoveryForm());
		}
		else
		{
			String email = form.field("email").value();
			UserAccount account = UserAccount.findByMail(email);
			
			String randomHash = Secured.hash(UUID.randomUUID().toString());
			
			MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
			mail.setSubject(Messages.get("recovery.mail.header"));
			mail.addRecipient(email);
			mail.addFrom("test@naturalpad.org");
			
			try 
			{
				String link = "http://" + Play.application().configuration().getString("application.baseURL") + ":" + Play.application().configuration().getString("application.http.port") + "/recover/" + URLEncoder.encode(randomHash, "UTF-8");
				mail.send(Messages.get("recovery.mail.content", account.getNickname(), link));
				PwdRecoveryHolder holder = new PwdRecoveryHolder();
				holder.setRandomHash(randomHash);
				holder.setExpireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60)); // 1 heure de temps d'expiration
				holder.setUser(account);
				
				PwdRecoveryHolder.create(holder);
				
				flash("status", Messages.get("recovery.status.success"));
				flash("status-css", "status_info");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			return redirect(routes.Application.login());
		}
	}
	
	/**
	 * Affichage du formulaire de changement de mot de passe pour la recovery
	 * @param hash
	 * @return
	 */
	public static Result recoveryForm(String hash)
	{
		PwdRecoveryHolder holder = PwdRecoveryHolder.findByHash(hash);
		
		if(holder != null && hash != null && !hash.isEmpty())
		{
			if(holder.getExpireDate().after(new Date()))
			{
				return ok(views.html.recoveryCheck.render(hash));
			}
			else
			{
				flash("status", Messages.get("recovery.status.expire"));
				flash("status-css", "status_error");
				return redirect(routes.Application.login());
			}
		}
		else
		{
			flash("status", Messages.get("recovery.status.usernotfound"));
			flash("status-css", "status_error");
			return redirect(routes.Application.login());
		}
	}
	
	/**
	 * Vérification du nouveau mot de passe
	 * @param hash
	 * @param userId
	 * @return
	 */
	public static Result recoveryCheck()
	{
		Form<PasswordForm> form = Form.form(PasswordForm.class).bindFromRequest();
		Map<String, List<ValidationError>> errors = form.errors();
		
		String hash = form.field("hash").value();
		
		if(form.hasErrors()) 
		{
			flash("status", Messages.get("errorform") + " :<br />" + Application.getHTMLReadableErrors(errors));
			flash("status-css", "status_error");
			return redirect(routes.Application.recoveryForm(hash));
		}
		else
		{
			String password = form.field("password").value();
			
			PwdRecoveryHolder holder = PwdRecoveryHolder.findByHash(hash);
			
			UserAccount account = UserAccount.findById(holder.user.getId());
			account.setHashedPassword(Secured.hash(password));
			MorphiaObject.datastore.save(account);
			
			PwdRecoveryHolder.delete(holder.getId());
			
			flash("status", Messages.get("recovery.status.changepassword"));
			flash("status-css", "status_success");
			
			return redirect(routes.Application.login());
		}
	}
	
	@SubjectPresent
	/**
	 * Page affichée lorsqu'un utilisateur est connecté. Page de profil.
	 * @return
	 */
	public static Result userProfile()
	{
		return ok(views.html.userProfile.render());
	}
  
}
