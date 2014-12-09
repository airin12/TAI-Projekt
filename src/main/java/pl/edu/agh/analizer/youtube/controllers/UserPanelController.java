package pl.edu.agh.analizer.youtube.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.edu.agh.analizer.youtube.dao.DatabaseDao;
import pl.edu.agh.analizer.youtube.reports.Report;

@Controller
@RequestMapping("user")
public class UserPanelController {

	
	private List<String> analysisList = null;
	
	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public ModelAndView logIn(ModelAndView modelAndView) {
		analysisList = DatabaseDao.getReportsNames();
		modelAndView.addObject("analysis", analysisList);
		return modelAndView;
	}
	
	@RequestMapping(value = "/analysis", method = RequestMethod.GET)
	public ModelAndView getAnalysisPage(@RequestParam(value = "title", required = true) String title, ModelAndView modelAndView) {
		Report report = DatabaseDao.getReport(title);
		modelAndView.addObject("labels", report.getChartLabels());
		modelAndView.addObject("data", report.getChartData());
		return modelAndView;
	}
}
