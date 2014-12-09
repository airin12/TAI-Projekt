package pl.edu.agh.analizer.youtube.controllers;

import java.io.IOException;
import java.security.Principal;
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
@RequestMapping("analyst")
public class AnalystPanelController {

	private List<String> userAnalysisList = null;

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public ModelAndView showAnalystPanel(ModelAndView modelAndView, Principal user) {
		userAnalysisList = DatabaseDao.getUsersReportsNames(user.getName());
		modelAndView.addObject("analysis", userAnalysisList);
		modelAndView.getModel().put("report", new ReportHelper());
		return modelAndView;
	}

	@RequestMapping(value = "/panel", method = RequestMethod.POST)
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
		} else if (report.getAnalysis().equals(ReportHelper.TOP_VIDEOS_PLOT)) {
			try {
				rs = analyticsReport.executeTopVideosQuery(report.getChannelId(), report.getAnalysisStartDate(), report.getAnalysisEndDate());
			} catch (IOException e) {
				e.printStackTrace();
			}

		if (rs != null) {
			youtubeReport = Report.ofResultTable(rs, report.getTitle());
			if (!youtubeReport.getTitle().equals("EMPTY"))
				DatabaseDao.addReport(youtubeReport, user.getName());
		}

		redirectAttributes.addFlashAttribute("status", "success");

		return new RedirectView("/analyst/panel", true);
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView removeAnalysis(@RequestParam(value = "title", required = true) String title, ModelAndView modelAndView, Principal user) {
		if(DatabaseDao.removeRaport(title))
			userAnalysisList=DatabaseDao.getUsersReportsNames(user.getName());
		ModelAndView newModelAndView = new ModelAndView("analyst/panel");
		newModelAndView.addObject("analysis", userAnalysisList);
		newModelAndView.getModel().put("report", new ReportHelper());
		return newModelAndView;
	}
	
}
