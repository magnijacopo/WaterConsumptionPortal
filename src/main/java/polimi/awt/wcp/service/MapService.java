package polimi.awt.wcp.service;

import polimi.awt.wcp.model.ElaboratedUserData;
import polimi.awt.wcp.model.User;
import java.util.List;

public interface MapService {
	
	public List<ElaboratedUserData>  ObtainUserData();
	public boolean checkZipCode(String zipcodeInserted, User user);
	public List<String> addressDetailUser(User user);
	public ElaboratedUserData dataSingleUser(User user);
	public int countHouseholdAssociated(User user);
	public void updatePublicList(User user);
}
