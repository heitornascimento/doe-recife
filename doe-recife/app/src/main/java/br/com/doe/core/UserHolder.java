/**
 * 
 */
package br.com.doe.core;

import br.com.doe.model.User;

/**
 * @author heitornsouza
 * 
 */
public class UserHolder {

	private static User instance;

	public static User getUserHolder() {

		return instance;
	}

	public static void setUserHolder(User user) {

		instance = user;
	}

}
