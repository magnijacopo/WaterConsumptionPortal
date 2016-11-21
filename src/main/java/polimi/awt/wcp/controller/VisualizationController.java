package polimi.awt.wcp.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import polimi.awt.wcp.service.MapService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import polimi.awt.wcp.service.ConsumptionService;
import polimi.awt.wcp.model.ElaboratedConsumptionData;
import polimi.awt.wcp.model.ElaboratedUserData;
import polimi.awt.wcp.model.User;

/**
 * @author rezzo
 *
 *  This class manages the interaction between user and interface, mapping the URL 
 *  with the respective class. In particular this controller manages the Consumption and the map 
 *  pages, taking in account the GET and POST request of both of them.
 */
@Controller
public class VisualizationController {

	@Autowired 
	private ConsumptionService ConsumptionService;
	@Autowired
	private MapService MapService;


	List<ElaboratedConsumptionData> DailyData;
	List<ElaboratedConsumptionData> WeeklyData;
	List<ElaboratedConsumptionData> MonthlyData;
	List<ElaboratedUserData> PublicUserData;
	ElaboratedUserData LoggedUser = new ElaboratedUserData();
	float AverageDay;
	float AverageWeek;
	float AverageMonth;
	float AverageNeighD;
	float AverageNeighW;
	float AverageNeighM;
	List<Float> averageNeight = new ArrayList<Float>();


	/**
	 * Method that handles the GET request coming from the /consumptionPage page.
	 * It creates the elaborated data of the logged-in user and pass it to the view.
	 * @param Model,{@link User}
	 * @return
	 */
	@RequestMapping(value = "/consumptionPage", method = RequestMethod.GET)
	public String DataVisualization(Model model,@ModelAttribute("user")User user){

		//Computes the consumption data at the three different granularity calling the dedicated services
		DailyData = ConsumptionService.DayVisualization(user);
		WeeklyData = ConsumptionService.WeekVisualization(user);
		MonthlyData = ConsumptionService.MonthVisualization(user);
		
		//Verifies if the consumption data lists are empty or not; if they are 
		//not empty, it computes the averages. Otherwise it sets them to zero. 
		if(!DailyData.isEmpty() && !MonthlyData.isEmpty()){
			//Calls the services to compute the averages (daily, weekly and monthly)
			AverageDay = ConsumptionService.calculateAverage(DailyData);
			AverageWeek = ConsumptionService.calculateAverage(WeeklyData);
			AverageMonth = ConsumptionService.calculateAverage(MonthlyData);
			//Calls the service aimed to compute the daily, weekly and monthly neighbour average 
			averageNeight = ConsumptionService.calculateAvgNeighbourhood(user);
			AverageNeighD = averageNeight.get(0);
			AverageNeighW = averageNeight.get(1);
			AverageNeighM = averageNeight.get(2);
			
		}
		else
		{
			AverageDay = 0;
			AverageMonth = 0;
			ElaboratedConsumptionData ecd = new ElaboratedConsumptionData();
			ecd.setPeriod(new Date());
			ecd.setConsumption(new BigDecimal("0"));
			DailyData.add(ecd);
			MonthlyData.add(ecd);
		}
		//Bind the attribute of the model, to be visualized in the jsp page 
		model.addAttribute("DailyData",DailyData);
		model.addAttribute("WeeklyData",WeeklyData);
		model.addAttribute("MonthlyData",MonthlyData);
		model.addAttribute("averageDay",AverageDay);
		model.addAttribute("averageWeek",AverageWeek);
		model.addAttribute("averageMonth",AverageMonth);
		model.addAttribute("averageNeighD",AverageNeighD);
		model.addAttribute("averageNeighW",AverageNeighW);
		model.addAttribute("averageNeighM",AverageNeighM);

		return "consumptionPage";
	}
	
	/**
	 * 
	 * Method that handles the POST request coming from the /consumptionPage.
	 * As consequence it redirects the users to the maps jsp page.
	 * @param user
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/consumptionPage",method=RequestMethod.POST)
	public String sendUserToMap(@ModelAttribute("user")User user,RedirectAttributes redirectAttributes){
		
	redirectAttributes.addFlashAttribute("user", user);
	return "redirect:/maps";
		
	}

	/**
	 * Method that handles the GET request coming from the /maps page.
	 * @param Model
	 * @return
	 */
	@RequestMapping(value="/maps", method=RequestMethod.GET)
	public String viewMap(Model model,@ModelAttribute("user")User user) {
		
		PublicUserData = MapService.ObtainUserData();
		LoggedUser = MapService.dataSingleUser(user);
	
		model.addAttribute("publicUserData",PublicUserData);
		model.addAttribute("loggedUser",LoggedUser);
		model.addAttribute("elabUser", new ElaboratedUserData());
		return "/maps";
	}

	/**
	 * 
	 * Method that handles the POST request coming from the /maps.
	 * It verifies the correctness of the data inserted by the user.
	 * If data are wrong it redirects to /maps jsp page without any consequence.
	 * @param user
	 * @param elabUser
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/maps", method=RequestMethod.POST)
	public String setPublic(@ModelAttribute("user")User user,@ModelAttribute("elabUser")ElaboratedUserData elabUser,RedirectAttributes redirectAttributes) {
		
		boolean zipCodeOk = MapService.checkZipCode(elabUser.getZipCode(),user);
		//Update the list of public users if the data inserted by logged user are correct
		if(zipCodeOk == true){
			MapService.updatePublicList(user);
		}
		redirectAttributes.addFlashAttribute("user", user);
		redirectAttributes.addFlashAttribute("elabUser", elabUser);
		return "redirect:/maps";
	}




}
