package models;

public class Login 
{

	public String identifier;
	public String password;
    
    public String validate() {
        if(
	UserAccount.authenticateMail(identifier, password) == null 
	&& 
	UserAccount.authenticateNickname(identifier, password) == null
	)
	{
            return "Invalid user or password";
        }
        return null;
    }

}
