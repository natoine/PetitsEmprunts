package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import play.Logger;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

import controllers.MorphiaObject;
import controllers.Secured;

@Entity("User")
public class UserActive extends User
{
	@Required @Indexed(unique = true)
	private String nickname;
	
	@Email @Required @Indexed(unique = true)
	private String email;
	
	@Required
	private String hashedPassword;
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	
	public static UserActive authenticate(String nickname, String password)
	{
		UserActive user = MorphiaObject.datastore.find(UserActive.class).field("nickname").equal(nickname).get();
		String hashPassword = Secured.hash(password);
		
		if(user != null && hashPassword != null)
		{
			if(user.getHashedPassword().equals(hashPassword))
			{
				return user;
			}
			else
				return null;
		}
		else
			return null;
	}
	
	public String validate()
	{
		Logger.info("Validate in UserActive");
		return "ok";
	}
	
	public boolean isSameUser(UserActive user)
	{
		return user.getId().equals(this.getId());
	}
	
	public static List<UserActive> all() {
		if (MorphiaObject.datastore != null) {
			return MorphiaObject.datastore.find(UserActive.class).asList();
		} else {
			return new ArrayList<UserActive>();
		}
	}

	public static Map<String,String> options() {
		List<UserActive> uas = all();
		LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
		for(UserActive ua: uas) {
			options.put(ua.getId().toString(), ua.nickname);
		}
		return options;
	}
	
	public static void create(UserActive userActive) {
		MorphiaObject.datastore.save(userActive);
	}

	public static void delete(String idToDelete) {
		UserActive toDelete = MorphiaObject.datastore.find(UserActive.class).field("_id").equal(new ObjectId(idToDelete)).get();
		if (toDelete != null) {
			//Logger.info("toDelete: " + toDelete);
			MorphiaObject.datastore.delete(toDelete);
		} else {
			//Logger.debug("ID No Found: " + idToDelete);
		}
	}
	
	public static UserActive findByNickname(String nickname)
	{
		return MorphiaObject.datastore.find(UserActive.class).field("nickname").equal(nickname).get();
	}
	
	public List<Exemplary> getPossessions()
	{
		return MorphiaObject.datastore.find(Exemplary.class).field("owner").equal(this).asList();
	}
}