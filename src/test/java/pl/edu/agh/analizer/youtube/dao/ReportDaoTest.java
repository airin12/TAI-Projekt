package pl.edu.agh.analizer.youtube.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.analizer.youtube.model.User;
import pl.edu.agh.analizer.youtube.model.User.UserRole;
import pl.edu.agh.analizer.youtube.reports.Report;

import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/main-context.xml")
@Transactional
@TransactionConfiguration(defaultRollback=true)
public class ReportDaoTest {

	@Autowired
	private ReportDao reportDao;
	
	@Autowired
	private UserDao userDao;
	
	private static User user = new User("NAME", "PASS", UserRole.ANALYST);
	
	private static Report report; 
	
	@BeforeClass
	public static void initReport() {
		
		List<String> columnHeaders = Lists.newArrayList("day", "views");
		String title = "TEST";
		String type = "TESTTYPE";
		Map<String, List<Long>> values = new HashMap<String, List<Long>>();
		
		values.put("day", Lists.newArrayList(1l,2l,3l,4l));
		values.put("views", Lists.newArrayList(5l,6l,7l,8l));
		
		report = new Report(columnHeaders, values, title, type);
	}
	
	@Before
	public void init() {
		userDao.insertUser(user);
	}
	
	@Test
	public void selectNoReportsTest() {
		
		List<String> reportsNames = reportDao.selectUsersReportsNames(user.getName());
		Assert.assertEquals(0, reportsNames.size());
	}
	
	@Test
	public void insertAndGetOneAnalyseName() {
		
		reportDao.insertReport(report, user.getName());
		String name = reportDao.selectUsersReportsNames(user.getName()).get(0);
		
		Assert.assertEquals(report.getTitle(), name);
	}
	
	@Test
	public void insertGetNameGetReportTest() {
		
		reportDao.insertReport(report, user.getName());
		String name = reportDao.selectUsersReportsNames(user.getName()).get(0);
		Report selected = reportDao.selectReport(name);
		
		Assert.assertEquals(report, selected);
	}
	
	@Test
	public void insertDeleteSelectTest() {
		
		reportDao.insertReport(report, user.getName());
		String name = reportDao.selectUsersReportsNames(user.getName()).get(0);
		reportDao.deleteReport(name);
		Report selected = reportDao.selectReport(name);
		
		Assert.assertNull(selected);
	}
	
}
