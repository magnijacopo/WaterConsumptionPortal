/**
 * 
 */
package polimi.awt.wcp.service;

import java.math.BigDecimal;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import polimi.awt.wcp.model.MeterReading;
import polimi.awt.wcp.model.User;
import polimi.awt.wcp.model.ElaboratedConsumptionData;
import polimi.awt.wcp.persistence.DataVisualizationRepository;

/**
 * @author Jacopo Magni
 * 
 * This class is used to interface the repository "DataVisualizationRepository" with the controller "VisualizationController".
 * It handles all the business logic to elaborate the data of the meter readings that will be shown in the consumptionPage.
 *
 */
@Service
@Transactional
public class ConsumptionServiceImpl implements ConsumptionService {

	//Autowired for DI with ConsumptionDataRepository interface
	@Autowired
	private DataVisualizationRepository dataRepository;

	/**
	 * The function takes the user's reads (of {@link MeterReading}) and it calculate
	 * the daily consumption. It takes the first read and the read after that one,
	 * and check if they have been read on the same day. If they are not, it takes the first read 
	 * of that day and the first of the next day and it makes the difference between them.
	 * @param user
	 * @return {@link ElaboratedConsumptionData} It returns a list of DAYS with date and total consumption.
	 */
	@Override
	public List<ElaboratedConsumptionData> DayVisualization(User user){

		Date dayDate;
		Date dayDateToCheck;
		Date firstRead = null;
		String dayToCheck;
		String day;
		BigDecimal daySum = new BigDecimal("0");
		int numberOfReads = 0;
		boolean setFirstRead = false;

		List<MeterReading> mrList = dataRepository.ConsumptionData(user);
		List<ElaboratedConsumptionData> dayDataToShow = new ArrayList<ElaboratedConsumptionData>();

		// Declaration of the date format
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		if(mrList.isEmpty()){
			// If the meter reading's list is empty return an empty list.
			return dayDataToShow;
		}
		else{
			for(int i = 0; i < mrList.size()-1; i++){
				// Transform the dates into string
				dayDate = mrList.get(i).getReadingDateTime();
				day = df.format(dayDate);
				dayDateToCheck = mrList.get(i+1).getReadingDateTime();
				dayToCheck = df.format(dayDateToCheck);

				// Setting the date of the day that is analyzed
				if(setFirstRead == false){
					firstRead = dayDate;
					setFirstRead = true;
				}
				
				// Check if the day is the same or it is changed.
				// The format is "yyyy/MM/dd HH:mm:ss". So the substring (0,10) is equal to "yyyy/MM/dd"
				if(day.substring(0, 10).equals(dayToCheck.substring(0, 10))){
					// Increments the number of reads in order to going back to the first value of the day.
					numberOfReads++;
				} else {
					// Calculate daySum as difference between the first value of the day after and the first value 
					// of the day and that is analyzed
					daySum = mrList.get(i).getTotalConsumptionAdjusted().subtract(mrList.get(i-numberOfReads).getTotalConsumptionAdjusted());
					// Create a new ElaboratedConsumptionData, set the date and the consumption, and add to a list
					ElaboratedConsumptionData ecd = new ElaboratedConsumptionData();
					ecd.setPeriod(firstRead);
					ecd.setConsumption(daySum);
					dayDataToShow.add(ecd);
					// Initialize variables for the day after
					setFirstRead = false;
					numberOfReads = 1;
				}
			}
			return dayDataToShow;
		}
	}
	
	/** 
	 * The function takes the user's reads (of {@link MeterReading}) and it calculate
	 * the MONTHLY consumption. It takes the first read and the read after that one,
	 * and check if they have been read on the same month. If they are not, it takes the first read 
	 * of that month and the first of the next month and it makes the difference between them.
	 * @param user
	 * @return {@link ElaboratedConsumptionData} It returns a list of months with date and total consumption.
	 */
	@Override
	public List<ElaboratedConsumptionData> MonthVisualization(User user){

		Date monthDate;
		Date monthDateToCheck;
		Date firstRead = null;
		String monthToCheck;
		String month;
		BigDecimal daySum;
		int numberOfReads = 0;
		boolean setFirstRead = false;

		List<MeterReading> mrList = dataRepository.ConsumptionData(user);
		List<ElaboratedConsumptionData> dayDataToShow = new ArrayList<ElaboratedConsumptionData>();
		// Declaration of the date format
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		if(mrList.isEmpty()){
			// If the meter reading's list is empty return an empty list.
			return dayDataToShow;
		}
		else{
			for(int i = 0; i < mrList.size()-1; i++){
				// Transform the dates into string
				monthDate = mrList.get(i).getReadingDateTime();
				month = df.format(monthDate);
				monthDateToCheck = mrList.get(i+1).getReadingDateTime();
				monthToCheck = df.format(monthDateToCheck);

				// Setting the date of the day that is analyzed
				if(setFirstRead == false){
					firstRead = monthDate;
					setFirstRead = true;
				}

				// Check if the month is the same or it is changed.
				// The format is "yyyy/MM/dd HH:mm:ss". So the substring (0,7) is equal to "yyyy/MM"
				if(month.substring(0, 7).equals(monthToCheck.substring(0, 7))){
					// Increments the number of reads in order to going back to the first value of the day.
					numberOfReads++;
				} else {
					// Calculate daySum as difference between the first value of the day after and the first value 
					// of the day and that is analyzed
					daySum = mrList.get(i+1).getTotalConsumptionAdjusted().subtract(mrList.get(i-numberOfReads).getTotalConsumptionAdjusted());
					// Create a new ElaboratedConsumptionData
					ElaboratedConsumptionData ecd = new ElaboratedConsumptionData();
					ecd.setPeriod(firstRead);
					ecd.setConsumption(daySum);
					dayDataToShow.add(ecd);
					// Initialize variables for the day after
					setFirstRead = false;
					numberOfReads = 0;
				}
			}
			return dayDataToShow;
		}
	}

	/**
	 * This method is used to calculate the average of the of the user's consumptions.
	 * it work with day, week and month
	 * 
	 * @param listEcd
	 * @return it returns the average of all the readings
	 */
	@Override
	public float calculateAverage(List<ElaboratedConsumptionData> listEcd){

		BigDecimal sumTotal = new BigDecimal(0);
		float average = 0;

		// If the list is not empty it adds all the consumptions in sumTotal
		// that then is divided by the size of the consumptions list.
		if(!listEcd.isEmpty()){
			for(int i=0; i<listEcd.size(); i++){
				sumTotal = sumTotal.add(listEcd.get(i).getConsumption()); 
			}
			average = (float)sumTotal.intValue() / listEcd.size();
		}

		return average;



	}
	
	/**
	 * This method is used to calculate the average consumptions of the user's neighbourhood.
	 * It work with day, week and month
	 * @param listEcd
	 * @return
	 */
	@Override
	public List<Float> calculateAvgNeighbourhood(User user){

		List<ElaboratedConsumptionData> day;
		List<ElaboratedConsumptionData> week;
		List<ElaboratedConsumptionData> month;
		List<Float> average = new ArrayList<Float>();
		float averageD;
		float averageW;
		float averageM;
		float totalD=0;
		float totalW=0;
		float totalM=0;
		float averageTotalD;
		float averageTotalW;
		float averageTotalM;
		List<User> userList = dataRepository.neighbourList(user);
		
		// If the list is NOT empty compute averageTotal otherwise set averageTotal to 0
		if(!userList.isEmpty()){
			for(int i=0; i<userList.size(); i++){
				// For each user that lives in the same district (is a neighbour)
				// the consumption, and then the average is calculated, and then added to total.
				day = DayVisualization(userList.get(i));
				week = WeekVisualization(userList.get(i));
				month = MonthVisualization(userList.get(i));
				averageD = calculateAverage(day);
				averageW = calculateAverage(week);
				averageM = calculateAverage(month);
				totalD = totalD + averageD;
				totalW = totalW + averageW;
				totalM = totalM + averageM;
			}
			// The neighbour's average is computed as the total divided by the size of the list of neighbours
			averageTotalD = totalD / userList.size();
			averageTotalW = totalW / userList.size();
			averageTotalM = totalM / userList.size();
			average.add(averageTotalD);
			average.add(averageTotalW);
			average.add(averageTotalM);
		}
		else{
			averageTotalD = 0;
			averageTotalW = 0;
			averageTotalM = 0;
		}

		return average;
	}

	/** 
	 * The function takes the user's reads (of {@link MeterReading}) and it calculate
	 * the WEEKLY consumption. It takes the first read and the read after that one,
	 * and check if they have been read on the same week. If they are not, it takes the first read 
	 * of that week and the first of the next week and it makes the difference between them.
	 * @param user
	 * @return {@link ElaboratedConsumptionData} It returns a list of WEEKS with date and total consumption.
	 */
	public List<ElaboratedConsumptionData> WeekVisualization(User user){

		Date dayDate;
		Date dayDateToCheck;
		Date firstRead = null;
		String dayToCheck;
		String day;
		BigDecimal daySum;
		int numberOfReads = 0;
		int weekCounter = 0;
		int dayOfWeek = 0; 
		boolean setFirstRead = false;
		List<MeterReading> mrList = dataRepository.ConsumptionData(user);
		List<ElaboratedConsumptionData> dayDataToShow = new ArrayList<ElaboratedConsumptionData>();


		if(!mrList.isEmpty()){
			// Retrieve an instance of Calendar
			Calendar c = Calendar.getInstance();
		
			// Declaration of the date format
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// Setting the time of the calendar as the time of the first lecture in the meter readings
			c.setTime(mrList.get(0).getReadingDateTime());
			// Retrieving the day of the week. It returns 1 for Sunday and 7 for Saturday
			dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

			// The week starts from Monday so Sunday (=1) is shifted after Saturday and setted to be equal to 8
			if (dayOfWeek == 1){
				dayOfWeek = 8;
			}

		// This while is used to set the value of the first week, 
		// since the first read can be any day of the week. In the while condition we verify that weekCounter
		// is lesser than 8 minus dayOfWeek that stands for the length of the week
		int i =0;
		while(weekCounter <= 8 - dayOfWeek){
			// Transform the dates into string
			dayDate = mrList.get(i).getReadingDateTime();
			day = df.format(dayDate);
			dayDateToCheck = mrList.get(i+1).getReadingDateTime();
			dayToCheck = df.format(dayDateToCheck);

			// Setting the date of the week that is analyzed
			if(setFirstRead == false){
				firstRead = dayDate;
				setFirstRead = true;
			}

			// Check if the day is the same or it is changed.
			// The format is "yyyy/MM/dd HH:mm:ss". So the substring (0,10) is equal to "yyyy/MM/dd"
			if(!(day.substring(0, 10).equals(dayToCheck.substring(0, 10)))){
				// If the day is changed weekCounter is incremented to indicate that 
				// another day of the same week has been analyzed
				weekCounter++;
			}
			// At each cycle both numberOfReads and i are incremented. 
			// The first to go back to compute the week consumption. 
			// The second to iterate over the meter reading list.
			numberOfReads++;
			i++;
		}
		// The consumption of the fist week is computed here.
		daySum = mrList.get(i-1).getTotalConsumptionAdjusted().subtract(mrList.get(i-numberOfReads).getTotalConsumptionAdjusted());
		// A new ElaboratedConsumptionData object is created 
		ElaboratedConsumptionData ecd = new ElaboratedConsumptionData();
		// The value of date and consumption are setted
		ecd.setPeriod(firstRead);
		ecd.setConsumption(daySum);
		// The object is added to a list in which all the ElaboratedConsumptionData representing the weeks are added 
		dayDataToShow.add(ecd);
		// The variables are re-inizialized to compute all the other weeks
		setFirstRead = false;
		numberOfReads = 0;
		weekCounter = 0;
		i++;

		// The iteration over the meter readings list starts from the following value 
		// of the one that has been analyzed inside the above while
		for(int j = i; j < mrList.size()-1; j++){
			// Transform the dates into string
			dayDate = mrList.get(j).getReadingDateTime();
			day = df.format(dayDate);
			dayDateToCheck = mrList.get(j+1).getReadingDateTime();
			dayToCheck = df.format(dayDateToCheck);

			// Setting the date of the week that is analyzed
			if(setFirstRead == false){
				firstRead = dayDate;
				setFirstRead = true;
			}

			// Check if the day is the same or it is changed.
			// The format is "yyyy/MM/dd HH:mm:ss". So the substring (0,10) is equal to "yyyy/MM/dd"
			if(!(day.substring(0, 10).equals(dayToCheck.substring(0, 10)))){
				// If the day is changed weekCounter is incremented to indicate that 
				// another day of the same week has been analyzed
				weekCounter++;
			} 
			// At each cycle numberOfReads is incremented to go back to compute the week consumption.
			numberOfReads++;

			// If the weekCounter is equal to 7 it means that seven days have been analyzed 
			// A complete week has been analyzed so it can now compute the consumption of the week
			if(weekCounter == 7){

					daySum = mrList.get(j).getTotalConsumptionAdjusted().subtract(mrList.get(j-numberOfReads).getTotalConsumptionAdjusted());
					// A new ElaboratedConsumptionData object is created 
					ElaboratedConsumptionData ecd2 = new ElaboratedConsumptionData();
					// The value of date and consumption are setted
					ecd2.setPeriod(dayDate);
					ecd2.setConsumption(daySum);
					// The object is added to a list in which all the ElaboratedConsumptionData representing the weeks are added 
					dayDataToShow.add(ecd2);
					// The variables are re-inizialized to compute the next week
					setFirstRead = false;
					numberOfReads = -1;
					weekCounter = 0;
				}
			}// end "for"
		}
	
		return dayDataToShow;
	}


}
