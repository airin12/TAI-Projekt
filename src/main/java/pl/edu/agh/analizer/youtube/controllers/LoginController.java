package pl.edu.agh.analizer.youtube.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("login")
public class LoginController {

	/**
	 * Method that intercepts HTTP GET requests to /login.
	 * Renders login page.
	 * 
	 * @param error login error message (optional)
	 * @param logout logout message (optional)
	 * @param modelAndView object that represents corresponding jsp page
	 * @return modelAndView object with filled model map
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView prepareLoginPage(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout,
			ModelAndView modelAndView) {

		if (error != null) {
			modelAndView.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			modelAndView.addObject("msg", "You've been logged out successfully.");
		}

		return modelAndView;
	}	
}
