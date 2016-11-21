/**
 * 
 */
package polimi.awt.wcp.persistence;

import java.math.BigDecimal;
import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import polimi.awt.wcp.model.Building;
import polimi.awt.wcp.model.District;
import polimi.awt.wcp.model.MeterReading;
import polimi.awt.wcp.model.NeutralUser;
import polimi.awt.wcp.model.User;

/**
 * @author Jacopo Magni
 *
 *  This class implements all the queries needed for all the data requested in the map jsp page.
 */
@Repository
public class MapRepositoryImpl implements MapRepository {

	/**
	 * {@link EntityManager} object to access the DB.
	 */
	@PersistenceContext(unitName="AccountUnit")
	protected EntityManager em;

	/**
	 * Retrieve the zipCode of a user
	 * @param {@link User}
	 * @return {@link District.zipCode}
	 */
	@Override
	public String retrieveZipCode(User user){
		String zipcode;
		// The query return the district in which the user lives (performing joins between different tables)
		TypedQuery<District> query = em.createQuery("SELECT d FROM District d, "
				+ "Household h, NeutralUser nu, Building b "
				+ "WHERE nu.household = h.oid AND h.building = b.oid AND b.district = d.oid "
				+ "AND nu.userOid = :userID", District.class);
		query.setParameter("userID", user.getOid());
		// Get the zipcode from the district object returned by the query
		zipcode = query.getResultList().get(0).getZipcode();

		return zipcode;
	}

	/**
	 * The method returns the list of users that have the public column to 1.
	 * The users with public = 1 are those that have compiled the opt-in form
	 * and have agreed to show their data to the other users
	 * @return List<User> with public = 1
	 */
	@Override
	public List<User> retrievePublicUsers(){

		TypedQuery<User> query = em.createQuery("SELECT u FROM User u, NeutralUser nu "
				+ "WHERE u.neutralUser = nu.userOid AND nu.okPublic = :publicElement ", User.class);
		query.setParameter("publicElement", 1);

		return query.getResultList();
	}

	/**
	 * The method returns the object building associated with the user 
	 * @param {@link User}
	 * @return {@link Building}
	 */
	@Override
	public Building retrieveUserBuilding(User user){

		TypedQuery<Building> query = em.createQuery("SELECT b FROM Household h, "
				+ "NeutralUser nu, Building b "
				+ "WHERE nu.household = h.oid AND h.building = b.oid "
				+ "AND nu.userOid = :userID ", Building.class);
		query.setParameter("userID", user.getOid());
		


		return query.getResultList().get(0);
	}

	/**
	 * The method returns the object building associated with the user 
	 * @param {@link User}
	 * @return {@link District}
	 */
	@Override
	public District retrieveUserDistrict(User user){

		TypedQuery<District> query = em.createQuery("SELECT d "
				+ "FROM Household h, Building b, NeutralUser nu, District d "
				+ "WHERE nu.household = h.oid AND h.building = b.oid "
				+ "AND b.district = d.oid "
				+ "AND nu.userOid = :userID ",
				District.class);
		query.setParameter("userID", user.getOid());

		return query.getResultList().get(0);
	}

	/**
	 * The method returns the number of households associated with a building.
	 * If the count is greater than one it means that the smartmeter associated with that building 
	 * is common and not individual
	 * @param buildingID
	 * @return countHousehold, the number of households associated with the param building id
	 */
	@Override
	public int countHouseholdAssociated(int buildingID){

		int countH;

		TypedQuery<Long> query = em.createQuery("SELECT count(b) "
				+ "FROM Household h, Building b "
				+ "WHERE h.building = b.oid "
				+ "AND b.oid = :buildingID ",
				Long.class);
		query.setParameter("buildingID", buildingID);

		countH = (query.getSingleResult()).intValue();

		return countH;
	}

	/**
	 * The method returns the last meter reading of the user.
	 * @param user
	 * @return BigDecimal , the last reading inside the table meter readings
	 */
	@Override
	public BigDecimal  lastReading(User user){

		// The query selects an object meter reading. It orders the reads by reading date time
		// and in order to get the last one limit the result to 1
		TypedQuery<MeterReading> query = em.createQuery("SELECT mr FROM MeterReading mr, SmartMeter sm, "
				+ "Household h, NeutralUser nu, Building b "
				+ "WHERE nu.household = h.oid AND h.building = b.oid AND b.oid = sm.building "
				+ "AND sm.oid = mr.smartMeter AND nu.userOid = :userID "
				+ "ORDER BY mr.readingDateTime DESC ",
				MeterReading.class);
		query.setParameter("userID", user.getOid());
		query.setMaxResults(1);
		return query.getResultList().get(0).getTotalConsumptionAdjusted();
	}
	
	/**
	 * The method update to '1' the attribute 'public' in the table NeutralUser.
	 * @param {{@link User}} 
	 */
	@Override
	public void updatePublic(User user){
	
		 NeutralUser newPublicUser = em.find(NeutralUser.class, user.getOid());
		 newPublicUser.setOkPublic(1);
		 em.flush();
	}

}
