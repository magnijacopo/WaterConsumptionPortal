package polimi.awt.wcp.persistence;

import polimi.awt.wcp.model.User;

public interface LoginRepository {

	public String retrievePassword(String username);
	
	public int retrieveUserID(User user);
	
}
