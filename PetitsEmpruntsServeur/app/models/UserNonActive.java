package models;

import com.google.code.morphia.annotations.Entity;

import controllers.MorphiaObject;

@Entity("User")
public class UserNonActive extends User
{

	public static void create(UserNonActive userNonActive) {
		MorphiaObject.datastore.save(userNonActive);
	}
}
