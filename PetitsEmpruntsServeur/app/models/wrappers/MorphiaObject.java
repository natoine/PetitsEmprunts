package models.wrappers;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

/**
 * Wrapper pour accéder rapidement aux objets liées à l'accès à la BDD
 * @author xandar
 *
 */
public class MorphiaObject 
{
	static public Mongo mongo;
	static public Morphia morphia;
	static public Datastore datastore;
}
