package pl.edu.agh.analizer.youtube.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import pl.edu.agh.analizer.youtube.dao.DatabaseDao;
import pl.edu.agh.analizer.youtube.reports.AnalyticsReports;
import pl.edu.agh.analizer.youtube.reports.Report;
import pl.edu.agh.analizer.youtube.reports.ReportHelper;

import com.google.api.services.youtubeAnalytics.model.ResultTable;

@Controller
public class LoginController {

	private List<String> analysisList = null;
	private List<String> userAnalysisList = null;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
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

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public ModelAndView logIn(ModelAndView modelAndView) {
		analysisList = DatabaseDao.getReportsNames();
		modelAndView.addObject("analysis", analysisList);
		return modelAndView;
	}

	@RequestMapping(value = "/analyst", method = RequestMethod.GET)
	public ModelAndView showAnalystPanel(ModelAndView modelAndView, Principal user) {
		userAnalysisList = DatabaseDao.getUsersReportsNames(user.getName());
		modelAndView.addObject("analysis", userAnalysisList);
		modelAndView.getModel().put("report", new ReportHelper());
		return modelAndView;
	}

	@RequestMapping(value = "/analyst", method = RequestMethod.POST)
	public View addanalysis(ReportHelper report, RedirectAttributes redirectAttributes, Principal user) {
		ResultTable rs = null;
		AnalyticsReports analyticsReport = new AnalyticsReports();
		Report youtubeReport;
		System.out.println(report);
		System.out.println(user.getName());

		if (report.getAnalysis().equals(ReportHelper.VIEWS_OVER_TIME)) {
			try {
				rs = analyticsReport.executeViewsOverTimeQuery(report.getChannelId(), report.getAnalysisStartDate(), report.getAnalysisEndDate());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (report.getAnalysis().equals(ReportHelper.TOP_VIDEOS)) {
			try {
				rs = analyticsReport.executeTopVideosQuery(report.getChannelId(), report.getAnalysisStartDate(), report.getAnalysisEndDate());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (rs != null) {
			youtubeReport = Report.ofResultTable(rs, report.getTitle());
			if(!youtubeReport.getTitle().equals("EMPTY"))
				DatabaseDao.addReport(youtubeReport, user.getName());
		}

		redirectAttributes.addFlashAttribute("status", "success");

		return new RedirectView("/analyst", true);
	}

	@RequestMapping(value = "/403", method = RequestMethod.POST)
	public ModelAndView accesssDenied(Principal user) {

		ModelAndView model = new ModelAndView();

		if (user != null) {
			model.addObject("msg", "Hi " + user.getName() + ", you do not have permission to access this page!");
		} else {
			model.addObject("msg", "You do not have permission to access this page!");
		}

		model.setViewName("403");
		return model;

	}

	@RequestMapping(value = "/analysis", method = RequestMethod.GET)
	public ModelAndView getAnalysisPage(@RequestParam(value = "title", required = true) String title, ModelAndView modelAndView) {
		Report report = DatabaseDao.getReport(title);
		modelAndView.addObject("labels", report.getChartLabels());
		modelAndView.addObject("data", report.getChartData());
		return modelAndView;
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView removeAnalysis(@RequestParam(value = "title", required = true) String title, ModelAndView modelAndView, Principal user) {
		if(DatabaseDao.removeRaport(title))
			userAnalysisList=DatabaseDao.getUsersReportsNames(user.getName());
		ModelAndView newModelAndView = new ModelAndView("analyst");
		newModelAndView.addObject("analysis", userAnalysisList);
		newModelAndView.getModel().put("report", new ReportHelper());
		return newModelAndView;
	}
	
}
