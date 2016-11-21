/**
 * 
 */
package polimi.awt.wcp.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import polimi.awt.wcp.model.MeterReading;
import polimi.awt.wcp.model.User;


/**
 * @author Jacopo Magni
 *
 * This class implements all the queries needed for visualize the data in the Consumption jsp page.
 */
@Repository
public class DataVisualizationRepositoryImpl implements DataVisualizationRepository {

	/**
	 * {@link EntityManager} object to access the DB.
	 */
	@PersistenceContext(unitName="AccountUnit")
	protected EntityManager em;

	/**
	 * It queries the DB returning Data of a specific user, ordering the results by date time.
	 * 
	 * @param {@link User} a given user
	 * @return List<MeterReading> list of neighbour
	 */
	@Override
	public List<MeterReading>  ConsumptionData(User user){

		TypedQuery<MeterReading> query = em.createQuery("SELECT mr FROM MeterReading mr, SmartMeter sm, "
				+ "Household h, NeutralUser nu, Building b "
				+ "WHERE nu.household = h.oid AND h.building = b.oid AND b.oid = sm.building "
				+ "AND sm.oid = mr.smartMeter AND nu.userOid = :userID "
				+ "ORDER BY mr.readingDateTime",
				MeterReading.class);
		query.setParameter("userID", user.getOid());
		return query.getResultList();
	}

	/**
	 * It queries the DB returning the list of the neighbours of the given user
	 * 
	 * @param {@link User} a given user
	 * @return List<User> list of neighbours
	 */
	@Override
	public List<User> neighbourList(User user){
		
		//The query returns the users, which have the same district oid of the user taken as parameter by the function.
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u, NeutralUser nu, Household h, Building b, District d "
				+ "WHERE u.oid = nu.userOid AND nu.household = h.oid AND h.building = b.oid AND b.district = d.oid "
				+ "AND d.oid IN ( SELECT d2.oid FROM NeutralUser nu2, Household h2, Building b2, District d2 "
				+ "WHERE nu2.household = h2.oid AND h2.building = b2.oid AND b2.district = d2.oid "
				+ "AND nu2.userOid = :userID)", User.class);
		query.setParameter("userID", user.getOid());
		return query.getResultList();        
	}

}
