package pl.edu.agh.analizer.youtube.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.edu.agh.analizer.youtube.dao.ReportDao;
import pl.edu.agh.analizer.youtube.reports.AnalyticsReports;
import pl.edu.agh.analizer.youtube.reports.Report;
import pl.edu.agh.analizer.youtube.reports.ReportHelper;

import com.google.api.services.youtubeAnalytics.model.ResultTable;

@Controller
@RequestMapping("analyst")
public class AnalystPanelController {

	private List<String> userAnalysisList = null;
	
	@Autowired
	private ReportDao reportDAO;

	/**
	 * Method that intercepts HTTP GET requests to /analyst/panel. Fills model with necessary objects.
	 * 
	 * @param modelAndView object that represents corresponding jsp page
	 * @param user logged in user data
	 * @return modelAndView object with necessary objects in model map
	 */
	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public ModelAndView showAnalystPanel(ModelAndView modelAndView, Principal user) {
		userAnalysisList = reportDAO.selectUsersReportsNames(user.getName());
		modelAndView.addObject("analysis", userAnalysisList);
		modelAndView.getModel().put("report", new ReportHelper());
		return modelAndView;
	}

	/**
	 * Method that intercepts HTTP POST requests to analyst/panel.
	 * Adds new analysis to database and performs redirect to analyst/panel.
	 * 
	 * @param report report object filled with values from corresponding jsp form.
	 * @param bindingResult result of validation on report object
	 * @param user logged in user data
	 * @return redirect new modelAndView object with updated data
	 */
	@RequestMapping(value = "/panel", method = RequestMethod.POST)
	public ModelAndView addanalysis(@Valid ReportHelper report, BindingResult bindingResult, Principal user) {
        if (bindingResult.hasErrors()) {
        	ModelAndView modelAndView = new ModelAndView("analyst/panel");
        	modelAndView.addObject("analysis", userAnalysisList);
    		modelAndView.getModel().put("report", report);
    		
    		for(FieldError error : bindingResult.getFieldErrors())
        		modelAndView.addObject(error.getField()+"Error",error.getDefaultMessage());

    		return modelAndView;
        }
        
		ResultTable rs = null;
		AnalyticsReports analyticsReport = new AnalyticsReports();
		Report youtubeReport;

		if (report.getAnalysis().equals(ReportHelper.VIEWS_OVER_TIME)) {
			try {
				rs = analyticsReport.executeViewsOverTimeQuery(report.getChannelId(), report.getAnalysisStartDate(), report.getAnalysisEndDate());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (report.getAnalysis().equals(ReportHelper.TOP_VIDEOS_ALL)) {
			try {
				rs = analyticsReport.executeTopVideosQuery(report.getChannelId(), report.getAnalysisStartDate(), report.getAnalysisEndDate());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (report.getAnalysis().equals(ReportHelper.TOP_VIDEOS_10)) {
			try {
				rs = analyticsReport.executeTopVideosQuery(report.getChannelId(), report.getAnalysisStartDate(), report.getAnalysisEndDate());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (rs != null) {
			youtubeReport = Report.ofResultTable(rs, report.getTitle(),report.getAnalysis());
			if (!youtubeReport.getTitle().equals("EMPTY"))
				reportDAO.insertReport(youtubeReport, user.getName());
		}

		return new ModelAndView("analyst/panel");
	}
	
	/**Method that intercepts HTTP GET requests to analyst/remove.
	 * Deletes chosen analysis.
	 * 
	 * @param title title of analysis
	 * @param modelAndView object that represents corresponding jsp page
	 * @param user logged in user data
	 * @return new modelAndView object that with updated data
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView removeAnalysis(@RequestParam(value = "title", required = true) String title, ModelAndView modelAndView, Principal user) {	
		reportDAO.deleteReport(title);
		userAnalysisList=reportDAO.selectUsersReportsNames(user.getName());
		ModelAndView newModelAndView = new ModelAndView("analyst/panel");
		newModelAndView.addObject("analysis", userAnalysisList);
		newModelAndView.getModel().put("report", new ReportHelper());
		return newModelAndView;
	}
	
}
