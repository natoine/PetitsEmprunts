import java.util.List;
import java.util.Map;

import models.UserAccount;
import play.GlobalSettings;
import play.libs.Yaml;

import com.avaje.ebean.Ebean;


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
		/**if(Ebean.find(UserAccount.class).findRowCount() == 0) 
		{
			Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
			
			Ebean.save(all.get("roles"));
			Ebean.save(all.get("persons"));
			Ebean.save(all.get("users"));
			for(Object user : all.get("users"))
            {
            	Ebean.saveManyToManyAssociations(user, "roles");
            	Ebean.saveAssociation(user, "person");
            }
		}**/
	}
}