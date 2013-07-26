package models.security;

import be.objectify.deadbolt.core.models.Role;

/**
 * Enum représentant les différentes roles d'un utilisateur
 * @author xandar
 *
 */
public enum Roles implements Role 
{
	User, Admin;

	@Override
	public String getName() 
	{
		return this.name();
	}

}
