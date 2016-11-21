/**
 * 
 */
package polimi.awt.wcp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import polimi.awt.wcp.persistence.LoginRepository;
import polimi.awt.wcp.model.User;

/**
 * @author Jacopo Magni
 * 
 * This class is used to interface the repository "LoginRepository" with the controller "LoginController".
 * It handles all the business logic.
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private LoginRepository repo;
	
	
	/**
	 * It calls the repository method [@link repository.getUserPassword} 
	 * to retrieve the password and compare it to the one inserted by the user. 
	 */
	@Override
	public boolean validateLogin(User user){
		
		boolean result;
		String username = user.getUsername();
		String passwordInserted = user.getPassword();
		String passwordDatabase = repo.retrievePassword(username);
		
		if(passwordInserted.equals(passwordDatabase)){
			result = true;
		} else { result = false; }
		
		return result;
		
	}
	
	/**
	 * It calls the repository method to retrieve the user ID of the user passed as parameter.
     * @param {@link User}
	 */
	@Override
	public int getUserID(User user){
		
		int userID = repo.retrieveUserID(user);
		
		return userID;
		
	}

}
