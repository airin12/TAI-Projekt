package pl.edu.agh.analizer.youtube.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.edu.agh.analizer.youtube.dao.DatabaseDao;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView prepareLoginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, ModelAndView modelAndView) {


		if (error != null) {
			modelAndView.addObject("error", "Invalid username and password!");
		}
 
		if (logout != null) {
			modelAndView.addObject("msg", "You've been logged out successfully.");
		}
		
		return modelAndView;
	}

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public ModelAndView logIn(ModelAndView modelAndView) {
        modelAndView.addObject("analysis", DatabaseDao.getAvailableAnalysis());
		return modelAndView;
	}
	
	@RequestMapping(value = "/analyst", method = RequestMethod.GET)
	public ModelAndView showAnalystPanel(ModelAndView modelAndView) {
		modelAndView.addObject("analysis", DatabaseDao.getUsersAnalysys(""));
		return modelAndView;
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.POST)
	public ModelAndView accesssDenied(Principal user) {
 
		ModelAndView model = new ModelAndView();
 
		if (user != null) {
			model.addObject("msg", "Hi " + user.getName() 
			+ ", you do not have permission to access this page!");
		} else {
			model.addObject("msg", 
			"You do not have permission to access this page!");
		}
 
		model.setViewName("403");
		return model;
 
	}
}
