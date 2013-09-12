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
import be.objectify.deadbolt.java.actions.SubjectPresent;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import controllers.util.Secured;

public class ContentController extends Controller
{

    /**
     * Displays user's activity
     */
    @SubjectPresent
    public static Result displayMyPage()
    {
        return ok(views.html.user.mypage.render());
    }

    /**
     * Displays user's stuff
     */
    @SubjectPresent
    public static Result displayMyStuff()
    {
        return ok(views.html.user.mystuff.render());
    }

    /**
     * Displays user's friends
     */
    @SubjectPresent
    public static Result displayMyFriends()
    {
        return ok(views.html.user.myfriends.render());
    }

    /**
     * Displays user's profile
     */
    @SubjectPresent
    public static Result displayUserProfile()
    {
        return ok(views.html.user.userProfile.render());
    }

}
