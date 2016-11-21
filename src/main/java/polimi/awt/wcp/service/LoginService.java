package polimi.awt.wcp.service;

import polimi.awt.wcp.model.User;

public interface LoginService {

	public boolean validateLogin(User user);
	public int getUserID(User user);
	
}
