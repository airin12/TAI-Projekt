package pl.edu.agh.analizer.youtube.report;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.edu.agh.analizer.youtube.reports.ReportHelper;

public class ReportHelperTest {

	private static ReportHelper reportHelper;
	
	@BeforeClass
	public static void setUp(){
		reportHelper = new ReportHelper();
	}
	
	@Test
	public void reportTitleTest(){
		String title = "test title";
		reportHelper.setTitle(title);
		
		assertEquals(title,reportHelper.getTitle());
	}
	
	@Test
	public void reportChannelIdTest(){
		String channelId = "exampleId";
		reportHelper.setChannelId(channelId);
		
		assertEquals(channelId, reportHelper.getChannelId());
	}
	
	@Test
	public void reportStartDateTest(){
		String startDate = "2014/12/02";
		reportHelper.setAnalysisStartDate(startDate);
		
		assertEquals(startDate, reportHelper.getAnalysisStartDate());
	}
	
	@Test
	public void reportEndDateTest(){
		String endDate = "2014/12/09";
		reportHelper.setAnalysisEndDate(endDate);
		
		assertEquals(endDate, reportHelper.getAnalysisEndDate());
	}
	
	@Test
	public void reportAnalysisDateTest(){
		String analysis1 = ReportHelper.TOP_VIDEOS_10;
		String analysis2 = ReportHelper.TOP_VIDEOS_ALL;
		String analysis3 = ReportHelper.VIEWS_OVER_TIME;
		
		reportHelper.setAnalysis(analysis1);
		assertEquals(analysis1, reportHelper.getAnalysis());
		
		reportHelper.setAnalysis(analysis2);
		assertEquals(analysis2, reportHelper.getAnalysis());
		
		reportHelper.setAnalysis(analysis3);
		assertEquals(analysis3, reportHelper.getAnalysis());
	}
	
	@AfterClass
	public static void cleanUp(){
		reportHelper=null;
	}
}
