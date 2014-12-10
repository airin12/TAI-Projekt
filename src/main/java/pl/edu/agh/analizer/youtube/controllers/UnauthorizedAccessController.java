package pl.edu.agh.analizer.youtube.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("error")
public class UnauthorizedAccessController {

	/**
	 * Method that intercepts HTTP GET to error/403. Renders custom 403 error page.
	 * 
	 * @param user logged in user data
	 * @return modelAndView objects with filled model map
	 */
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied(Principal user) {

		ModelAndView model = new ModelAndView();

		if (user != null) {
			model.addObject("msg", "Hi " + user.getName() + ", you do not have permission to access this page!");
		} else {
			model.addObject("msg", "You do not have permission to access this page!");
		}

		model.setViewName("error/403");
		return model;

	}
}
