package models;

import play.data.validation.Constraints.Email;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

@Entity("User")
public class UserNonActive extends User
{
	@Email @Indexed(unique = true)
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
