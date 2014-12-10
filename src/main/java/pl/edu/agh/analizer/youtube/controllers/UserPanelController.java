package pl.edu.agh.analizer.youtube.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.edu.agh.analizer.youtube.dao.DatabaseDao;
import pl.edu.agh.analizer.youtube.reports.Report;
import pl.edu.agh.analizer.youtube.reports.ReportHelper;

@Controller
@RequestMapping("user")
public class UserPanelController {

	private List<String> analysisList = null;
	private Report report = null;

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public ModelAndView logIn(ModelAndView modelAndView) {
		analysisList = DatabaseDao.getReportsNames();
		modelAndView.addObject("analysis", analysisList);
		return modelAndView;
	}

	@RequestMapping(value = "/analysis", method = RequestMethod.GET)
	public ModelAndView getAnalysisPage(@RequestParam(value = "title", required = true) String title) {
		report = DatabaseDao.getReport(title);
		ModelAndView modelAndView = new ModelAndView("user/" + report.getType());
		modelAndView.addObject("title", report.getTitle());
		setModelObjects(modelAndView, report.getType());
		return modelAndView;
	}

	private void setModelObjects(ModelAndView modelAndView, String type) {
		if (type.equals(ReportHelper.VIEWS_OVER_TIME)) {
			modelAndView.addObject("labels", report.getChartLabelsFromWeek());
			modelAndView.addObject("data", report.getChartDataFromWeek());
		} else if (type.equals(ReportHelper.TOP_VIDEOS_10)) {
			modelAndView.addObject("videos", report.getTopViewsListWeekly());
		} else if (type.equals(ReportHelper.TOP_VIDEOS_ALL)) {
			modelAndView.addObject("videos", report.getViewsList());
		}
	}

	@RequestMapping(value = "/views_over_time", method = RequestMethod.GET)
	public ModelAndView showAnalysis(@RequestParam(value = "time", required = true) String time, ModelAndView modelAndView) {
		if(time.equals("week")){
			modelAndView.addObject("labels", report.getChartLabelsFromWeek());
			modelAndView.addObject("data", report.getChartDataFromWeek());
		} else if(time.equals("day")){
			modelAndView.addObject("labels", report.getChartLabelsFromDay());
			modelAndView.addObject("data", report.getChartDataFromDay());
		}
		modelAndView.addObject("title",report.getTitle());
		return modelAndView;
	}
}
