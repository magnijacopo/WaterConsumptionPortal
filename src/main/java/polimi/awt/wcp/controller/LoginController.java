/**
 * 
 */
package polimi.awt.wcp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import polimi.awt.wcp.model.User;
import polimi.awt.wcp.service.LoginService;

/**
 * @author Jacopo Magni
 * 
 *  This class manages the interaction between user and interface, mapping the URL 
 *  with the respective class. In particular this controller manages the login phase,
 *  taking in account the GET and POST request.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private LoginService service;
	
	/**
	 * Method that handles the GET request coming from the /login page.
	 * It creates an user in the model.
	 * @param Model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String formLogin(Model model) {
		
		model.addAttribute(new User());
		return "/login";
		
	}
	
	/**
	 * Method that handles the POST request coming from the /login page.
	 * It takes the user as parameter and verify if the login is ok, 
	 * if not it returns the error page.
	 * It redirects the user attribute to the next page, the Consumption page.
	 * @param Model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String loginAccount(@ModelAttribute("user")User user,RedirectAttributes redirectAttributes) {
		
		if(!service.validateLogin(user)){
			return "/loginFail";	
		}
		else
		{
			user.setOid(service.getUserID(user));
			redirectAttributes.addFlashAttribute("user", user);
			return "redirect:/consumptionPage";
		}
	}
	
}
