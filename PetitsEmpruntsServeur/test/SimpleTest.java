package test;

import models.UserAccount;

import org.junit.*;

import controllers.Application;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class SimpleTest {

	@Test 
	public void testAccountCreation() 
	{
		Application.createAccount("login", "email@gmail.com", "mdp");
		UserAccount user = UserAccount.findByEmail("email@gmail.com");
		assertThat(user).isNotNull();
	}

	@Test
	public void goodRoute() 
	{
		Result result = routeAndCall(fakeRequest(GET, "/subscribe"));
		assertThat(result).isNotNull();
	}

}
