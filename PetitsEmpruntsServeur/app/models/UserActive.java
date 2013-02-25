package models;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

@Entity("User")
public class UserActive extends User
{
	@Required @Indexed(unique = true)
	private String nickname;
	
	@Email @Required @Indexed(unique = true)
	private String email;
	
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
}
