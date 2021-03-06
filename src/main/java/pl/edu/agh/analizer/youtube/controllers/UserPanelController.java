package pl.edu.agh.analizer.youtube.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.edu.agh.analizer.youtube.dao.ReportDao;
import pl.edu.agh.analizer.youtube.reports.Report;
import pl.edu.agh.analizer.youtube.reports.ReportHelper;
import pl.edu.agh.analizer.youtube.util.SortingUtil;

@Controller
@RequestMapping("user")
public class UserPanelController {

	private List<String> analysisList = null;
	private Report report = null;

	@Autowired
	private ReportDao reportDAO;
	
	/**
	 * Method that intercepts HTTP GET requests to user/panel.
	 * 
	 * @param modelAndView object that represents corresponding jsp page
	 * @return modelAndView object with filled model map
	 */
	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public ModelAndView showUserPanel(ModelAndView modelAndView) {
		analysisList = reportDAO.selectReportsNames();
		modelAndView.addObject("analysis", analysisList);
		return modelAndView;
	}

	/**
	 * Method that intercepts HTTP GET requests to user/analysis.
	 * Performs a dispatching to proper jsp page based on analysis type.
	 * 
	 * @param title analysis title
	 * @return object representing jsp page corresponding to analysis type
	 */
	@RequestMapping(value = "/analysis", method = RequestMethod.GET)
	public ModelAndView getAnalysisPage(@RequestParam(value = "title", required = true) String title) {
		report = reportDAO.selectReport(title);
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
			modelAndView.addObject("videos", SortingUtil.entriesSortedByValues(report.getTopViewsListWeekly()));
		} else if (type.equals(ReportHelper.TOP_VIDEOS_ALL)) {
			modelAndView.addObject("videos", SortingUtil.entriesSortedByValues(report.getViewsList()));
		}
	}

	/**
	 * Method that intercepts HTTP GET requests to user/views_over_time.
	 * Method loads data into model depend on users time choice.
	 * 
	 * @param time time range 
	 * @param modelAndView object that represents corresponding jsp page
	 * @return modelAndViem object with filled model map
	 */
	@RequestMapping(value = "/views_over_time", method = RequestMethod.GET)
	public ModelAndView showViewsAnalysis(@RequestParam(value = "time", required = true) String time, ModelAndView modelAndView) {
		if (time.equals("week")) {
			modelAndView.addObject("labels", report.getChartLabelsFromWeek());
			modelAndView.addObject("data", report.getChartDataFromWeek());
		} else if (time.equals("day")) {
			modelAndView.addObject("labels", report.getChartLabelsFromDay());
			modelAndView.addObject("data", report.getChartDataFromDay());
		}
		modelAndView.addObject("title", report.getTitle());
		return modelAndView;
	}

	/**
	 * Method that intercepts HTTP GET requests to user/top_videos_10.
	 * Method loads data into model depend on users time choice.
	 * 
	 * @param time time range 
	 * @param modelAndView object that represents corresponding jsp page
	 * @return modelAndViem object with filled model map
	 */
	@RequestMapping(value = "/top_videos_10", method = RequestMethod.GET)
	public ModelAndView showTopVideosAnalysis(@RequestParam(value = "time", required = true) String time, ModelAndView modelAndView) {
		if (time.equals("week")) {
			modelAndView.addObject("videos", SortingUtil.entriesSortedByValues(report.getTopViewsListWeekly()));
		} else if (time.equals("day")) {
			modelAndView.addObject("videos", SortingUtil.entriesSortedByValues(report.getTopViewsListDaily()));
		}
		modelAndView.addObject("title", report.getTitle());
		return modelAndView;
	}

}
