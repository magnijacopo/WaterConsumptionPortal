package polimi.awt.wcp.persistence;

import polimi.awt.wcp.model.User;
import polimi.awt.wcp.model.Building;
import polimi.awt.wcp.model.District;

import java.util.List;
import java.math.BigDecimal;

public interface MapRepository {

	public String retrieveZipCode(User user);
	public List<User> retrievePublicUsers();
	public Building retrieveUserBuilding(User user);
	public int countHouseholdAssociated(int buildingID);
	public District retrieveUserDistrict(User user);
	public BigDecimal  lastReading(User user);
	public void updatePublic(User user);
	
}
