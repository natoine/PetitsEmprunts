package models.security;

import be.objectify.deadbolt.core.models.Permission;

/**
 * Enum représentation les différentes persmissions d'un utilisateur
 * @author xandar
 *
 */
public enum Permissions implements Permission 
{
	;

	@Override
	public String getValue() 
	{
		return this.name();
	}

}
