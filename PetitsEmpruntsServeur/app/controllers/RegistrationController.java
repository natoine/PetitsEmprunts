package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.Person;
import models.UserAccount;
import models.forms.RegistrationForm;
import play.Logger;
import play.Play;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import be.objectify.deadbolt.java.actions.SubjectNotPresent;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import controllers.util.Secured;

@SubjectNotPresent
public class RegistrationController extends Controller {

	/**
	 * Modèle du formulaire d'inscription
	 */

	static Form<RegistrationForm> registrationForm = Form
			.form(RegistrationForm.class);

	/**
	 * Display form for registration.
	 */
	public static Result query() {
		if (!AbstractController.isConnected())
			return ok(views.html.register.render(registrationForm));
		else
			return redirect(routes.Application.index());
	}

	/**
	 * Process registration form. Creates a new user account in database
	 */
	public static Result process() {
		Form<RegistrationForm> filledForm = registrationForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			Map<String, List<ValidationError>> errors = filledForm.errors();
			flash("status", Messages.get("errorform") + " :<br />"
					+ AbstractController.getHTMLReadableErrors(errors));
			flash("status-css", "status_error");
			return redirect(routes.RegistrationController.query());
		} else {
			// create user account
			UserAccount user = new UserAccount();
			Person person = new Person();
			user.setEmail(filledForm.field("email").value());
			user.setNickname(filledForm.field("nickname").value());
			person.setFirstname(filledForm.field("firstname").value());
			person.setLastname(filledForm.field("lastname").value());
			user.setPerson(person);
			Person.create(person);
			//user.setFirstname(filledForm.field("firstname").value());
			user.setHashedPassword(Secured.hash(filledForm.field("password")
					.value()));
			user.setValidated(false);
			user.setValidationCode(Secured.hash(UUID.randomUUID().toString()));
			//user.setLastname(filledForm.field("lastname").value());
			user.setCreationDate(new Date());

			// persist user account
			UserAccount.create(user);

			// send email
			MailerAPI mail = play.Play.application().plugin(MailerPlugin.class)
					.email();
			mail.setSubject(Messages.get("register.mail.header",
					Messages.get("application.shortname")));
			mail.addRecipient(user.getEmail());
			mail.addFrom(Play.application().configuration()
					.getString("smtp.user"));

			try {
				// create link to validate account
				String link = "http://"
						+ Play.application().configuration()
								.getString("application.baseURL")
						+ ":"
						+ Play.application().configuration()
								.getString("application.http.port")
						+ "/register/validate/"
						+ URLEncoder.encode(user.getValidationCode(), "UTF-8");
				String nickname = user.getNickname();
				String website = Messages.get("application.name");
				mail.send(Messages.get("register.mail.content", nickname,
						website, link));
			} catch (UnsupportedEncodingException e) {
				// TODO handle failures
				e.printStackTrace();
				return redirect(routes.RegistrationController.query());
			}

			flash("status", Messages.get("register.success"));
			flash("status-css", "status_success");

			return redirect(routes.Application.login());
		}
	}

	/**
	 * Validates registration.
	 */
	public static Result validate(String hash) {
		// get user account
		UserAccount account = UserAccount.findByValidationCode(hash);

		// check if account found
		if (account == null) {
			// log warning
			Logger.warn("Failed to validate account with hash " + hash
					+ " because hash is not valid.");
			// display error
			flash("status",
					Messages.get("registration.validate.error.hashisnotvalid"));
			flash("status-css", "status_error");
			return redirect(routes.Application.login());
		}

		// validate account
		account.setValidated(true);
		account.setValidationCode(null);
		UserAccount.update(account);
		Logger.info("Validated account " + account.getId() + " with hash "
				+ hash);

		// display info
		flash("status", Messages.get("register.validate.success"));
		flash("status-css", "status_success");
		return redirect(routes.Application.login());
	}

}
