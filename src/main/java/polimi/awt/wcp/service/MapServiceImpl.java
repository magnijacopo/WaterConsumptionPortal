package polimi.awt.wcp.service;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import polimi.awt.wcp.model.ElaboratedUserData;
import polimi.awt.wcp.model.User;
import polimi.awt.wcp.persistence.MapRepository;
import polimi.awt.wcp.model.Building;
import polimi.awt.wcp.model.District;
import polimi.awt.wcp.model.ElaboratedConsumptionData;
import polimi.awt.wcp.service.ConsumptionService;
/**
 * 
 * @author Jacopo Magni
 *
 * This class is used to interface the repository "MapRepository" with the controller "VisualizationController".
 * It handles all the business logic to decide what data user can be displayed, by marker, on the map.
 * 
 */
@Service
@Transactional
public class MapServiceImpl implements MapService {

	@Autowired
	private MapRepository mapRepository;
	@Autowired
	private ConsumptionService cs;

	/**
	 * This method, for each user in the public list, i.e. those who can be 
	 * visualized on the map, calls dataSingleUser(). Then returns
	 * a list of ElaboratedUserData that represents all the public user data.
	 */
	@Override
	public List<ElaboratedUserData> ObtainUserData(){

		List<User> userPublicList;
		List<ElaboratedUserData> eudList = new ArrayList<ElaboratedUserData>();

		userPublicList = mapRepository.retrievePublicUsers();

		for(int i = 0; i < userPublicList.size(); i++){
			ElaboratedUserData eud;
			eud = dataSingleUser(userPublicList.get(i));
			eudList.add(eud);
		}

		return eudList;
	}

	/**
	 * This method check if the zipcode associated with the address inserted by the user
	 * matches with the other one inserted at the registration time by the same user.
	 */
	@Override
	public boolean checkZipCode(String zipcodeInserted, User user){
		
		boolean zcMatch;
		
		String zipcode = mapRepository.retrieveZipCode(user);
		
		if(zipcodeInserted.equals(zipcode)){
			zcMatch = true;
		} else {
			zcMatch = false;
		}
		
		return zcMatch;
	}

	/**
	 * This method take a user as parameter. It sets all the data needed 
	 * for the pop-up and the ones of the address needed 
	 * to derive latitude and longitude, by calling the specific methods.
	 * It returns an ElaboratedUserData object.
	 */
	@Override
	public ElaboratedUserData dataSingleUser(User user){

		ElaboratedUserData eud = new ElaboratedUserData();
		int countHousehold;
		List<Float> consumptionDetailUserList = new ArrayList<Float>();
		List<String> addressDetailUserList;

	//----------- Setting the Consumption data details of the user ---------//
		consumptionDetailUserList = consumptionDetailUser(user);
		eud.setUsername(user.getUsername());
		eud.setLastReading(consumptionDetailUserList.get(0));
		eud.setAverageDay(consumptionDetailUserList.get(1));
		eud.setAverageWeek(consumptionDetailUserList.get(2));
		eud.setAverageMonth(consumptionDetailUserList.get(3));

	//----------- Setting the Address details of the user ---------//
		addressDetailUserList = addressDetailUser(user);
		eud.setCountry(addressDetailUserList.get(0));
		eud.setCity(addressDetailUserList.get(1));
		eud.setName(addressDetailUserList.get(2));
		eud.setStreet(addressDetailUserList.get(3));

	//----------- Setting the attribute for Common/Individual smartmeter ---------//
		countHousehold = countHouseholdAssociated(user);
		if(countHousehold == 1){
			eud.setIndividual(true);
		} else { eud.setIndividual(false);}

		return eud;
	}

	/**
	 * This method retrieve the building of the user taken as parameter.
	 * Then it calls the count household associated. It returns count households.
	 * If the return value is greater than 1 the Smart Meter of the user is common,
	 * otherwise it's individual. 
	 */
	@Override
	public int countHouseholdAssociated(User user){

		int countHousehold;
		Building building;

		building = mapRepository.retrieveUserBuilding(user);
		countHousehold = mapRepository.countHouseholdAssociated(building.getOid());

		return countHousehold;
	}

	/**
	 * This method is not exposed to the MapService interface.
	 * It elaborates all the consumption data of a single user by calling the methods 
	 * exposed in the ConsumptionService interface.
	 * @param user
	 * @return
	 */
	public List<Float> consumptionDetailUser(User user){

		List<Float> consumptionDetail = new ArrayList<Float>();
		List<ElaboratedConsumptionData> dayData;
		List<ElaboratedConsumptionData> weekData;
		List<ElaboratedConsumptionData> monthData;
		float dayAverage;
		float weekAverage;
		float monthAverage;
		float lastReadingFloat = 0;
		BigDecimal lastReading;

	//----------- Calculate the current total of the user ---------//
	
		lastReading = mapRepository.lastReading(user);
		lastReadingFloat = lastReading.floatValue();
		consumptionDetail.add(lastReadingFloat);


	//----------- Calculate the averages of the user ---------//
		dayData = cs.DayVisualization(user);
		dayAverage = cs.calculateAverage(dayData);
		consumptionDetail.add(dayAverage);

		weekData = cs.WeekVisualization(user);
		weekAverage = cs.calculateAverage(weekData);
		consumptionDetail.add(weekAverage);

		monthData = cs.MonthVisualization(user);
		monthAverage = cs.calculateAverage(monthData);
		consumptionDetail.add(monthAverage);

		return consumptionDetail;
	}

	/**
	 * 
	 * It elaborates all the address data of a single user by calling the methods 
	 * exposed in the MapRepository interface.
	 * @param user
	 * @return
	 */
	@Override
	public List<String> addressDetailUser(User user){
		List<String> userAddressDetail = new ArrayList<String>();
		Building building;
		District district;
		
		//----------- Compute the district of the user and the related data ---------//
		district = mapRepository.retrieveUserDistrict(user);
		userAddressDetail.add(district.getCountry());
		userAddressDetail.add(district.getCity());
		userAddressDetail.add(district.getName());

		//----------- Compute the building of the user and the related data ---------//
		building = mapRepository.retrieveUserBuilding(user);
		userAddressDetail.add(building.getAddress());
		
		return userAddressDetail;
	}

	/**
	 * This method calls the method exposed in MapRepository interface, 
	 * which is aimed to update the list of public user.
	 */
	@Override
	public void updatePublicList(User user){
		mapRepository.updatePublic(user);
	}

}
