import java.net.UnknownHostException;

import models.Borrow;
import models.Exemplary;
import models.Thing;
import models.User;
import models.UserActive;
import models.UserNonActive;
import play.GlobalSettings;
import play.Logger;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

import controllers.MorphiaObject;


public class Global extends GlobalSettings 
{

	@Override
	public void onStart(play.Application arg0) {
		super.beforeStart(arg0);
		Logger.debug("** onStart **");
		try {
			MorphiaObject.mongo = new Mongo("127.0.0.1", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		MorphiaObject.morphia = new Morphia();
		MorphiaObject.datastore = MorphiaObject.morphia.createDatastore(MorphiaObject.mongo, "test");
		
		MorphiaObject.morphia.map(User.class);
		MorphiaObject.morphia.map(UserActive.class);
		MorphiaObject.morphia.map(UserNonActive.class);
		MorphiaObject.morphia.map(Borrow.class);
		MorphiaObject.morphia.map(Exemplary.class);
		MorphiaObject.morphia.map(Thing.class);
		
		MorphiaObject.datastore.ensureIndexes();
		MorphiaObject.datastore.ensureCaps();

		Logger.debug("** Morphia datastore: " + MorphiaObject.datastore.getDB());
	}
}
