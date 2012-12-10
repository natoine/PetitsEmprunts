import play.*;
import play.libs.*;

import java.util.*;

import com.avaje.ebean.*;

import models.*;

public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
        InitialData.insert(app);
    }
    
    static class InitialData {
        
        public static void insert(Application app) {
            if(Ebean.find(UserAccount.class).findRowCount() == 0) {
                
                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

                // Insert users first
                Ebean.save(all.get("users"));
		//Insert items
		Ebean.save(all.get("items"));
		//Insert exemplaires
		Ebean.save(all.get("exemplaires"));
		//Insert possessions
		Ebean.save(all.get("possessions"));
		//Insert emprunts
		Ebean.save(all.get("emprunts"));
		
            }
        }
        
    }
    
}
