/**
 * 
 */
package polimi.awt.wcp.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import polimi.awt.wcp.model.User;

/**
 * @author Jacopo Magni
 *
 * This class implements all the queries needed for the login phase.
 */
@Repository
public class LoginRepositoryImpl implements LoginRepository {

	/**
	 * {@link EntityManager} object to access the DB.
	 */
	@PersistenceContext(unitName="AccountUnit")
	protected EntityManager em;
	
	/** 
	 * It takes the username of the user and retrieve his password.
	 * @param user username
	 * @return user password
	 */
	@Override
	public String retrievePassword(String username) {

		TypedQuery<User> query = em.createQuery("SELECT u FROM User u "
				+ "WHERE u.username = :username", User.class);
		query.setParameter("username", username);
		
		List<User> rl = query.getResultList();
        String password = rl.get(0).getPassword();
        
        return password;
	}

	/**
	 * It takes the user and retrieve his id.
	 * @param user
	 * @return user oid
	 */
	@Override
	public int retrieveUserID(User user) {
		
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u "
				+ "WHERE u.username = :nickname", User.class);
		query.setParameter("nickname", user.getUsername());

		List<User> rl = query.getResultList();
		
		return rl.get(0).getOid();
	}

}
