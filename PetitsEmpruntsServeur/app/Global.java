import java.net.UnknownHostException;
import java.util.ArrayList;

import models.*;
import models.security.Roles;
import models.wrappers.MorphiaObject;

import play.GlobalSettings;
import play.Logger;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

/**
 * Classe appelé au premier lancement de play.
 * @author xandar
 *
 */
public class Global extends GlobalSettings 
{
	@Override
	public void onStart(play.Application arg0) 
	{
		super.beforeStart(arg0);
		Logger.debug("** OnStart play2auth Application **");
		Logger.info("Connecting to MongoDB database at 127.0.0.1");
		
		// on essaye de se connecter a la base de données play2auth. On la cree si elle n'existe pas
		try 
		{
			MorphiaObject.mongo = new Mongo("127.0.0.1", 27017);
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
		
		MorphiaObject.morphia = new Morphia();
		MorphiaObject.datastore = MorphiaObject.morphia.createDatastore(MorphiaObject.mongo, "play2auth");
		
		MorphiaObject.morphia.map(UserAccount.class);
		
		MorphiaObject.datastore.ensureIndexes();
		MorphiaObject.datastore.ensureCaps();
		
		// on crée un admin par défaut. Login : admin / password : admin
		if(UserAccount.all().size() == 0)
		{
			UserAccount admin = new UserAccount();
			admin.setRoles(new ArrayList<Roles>());
			admin.addRole(Roles.Admin);
			admin.setEmail("admin@naturalpad.org");
			admin.setFirstname("Antoine");
			admin.setLastname("Seilles");
			admin.setNickname("admin");
			admin.setHashedPassword("0925e15d0ae6af196e6295923d76af02b4a3420f");
			
			UserAccount.create(admin);
		}
	}
}