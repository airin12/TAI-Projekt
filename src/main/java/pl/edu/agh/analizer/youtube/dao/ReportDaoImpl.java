package pl.edu.agh.analizer.youtube.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.analizer.youtube.reports.Report;

public class ReportDaoImpl implements ReportDao {
	
	private DataSourceTransactionManager transactionManager;
	
	private JdbcTemplate jdbcTemplate;
	
	public ReportDaoImpl() {
		super();
	}
	
	public void setTransactionManager(DataSourceTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
		DataSource dataSource = transactionManager.getDataSource();
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<String> selectReportsNames() {

		String sql = "SELECT TITLE FROM ANALYSIS";
		final List<String> names = new LinkedList<String>();
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				names.add(rs.getString("TITLE"));
			}
		});
		
		return names;
	}

	@Override
	public List<String> selectUsersReportsNames(String username) {

		String sql = "SELECT TITLE FROM ANALYSIS WHERE USERNAME = ?";
		final Object[] params = new Object[]{username};
		final List<String> names = new LinkedList<String>();
		
		jdbcTemplate.query(sql, params, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				names.add(rs.getString("TITLE"));
			}
		});
		
		return names;
	}
	
	private List<String> getColumnNames(String reportName) {

		final List<String> columnNames = new LinkedList<String>();
		String sql = "SELECT AC.TITLE FROM ANALYSIS_COLUMN AC "
				+ "INNER JOIN ANALYSIS AN ON AC.ANALYSISID = AN.ID "
				+ "WHERE AN.TITLE = ?";
		
		final Object[] params = new Object[]{reportName};
		jdbcTemplate.query(sql, params, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {

				columnNames.add(rs.getString("TITLE"));
				
			}
		});
		
		return columnNames;
	}

	@Override
	public Report selectReport(String reportName) {

		final List<String> columnNames = getColumnNames(reportName);
		final Map<String, List<Long>> values = new HashMap<String, List<Long>>();

		for (String columnName : columnNames) {
			values.put(columnName, new LinkedList<Long>());
		}

		String sql = "SELECT V.VALUE AS VALUE, R.ROW_NUMBER AS ROW_NUMBER, C.TITLE AS TITLE FROM ANALYSIS AN "
				+ "INNER JOIN ANALYSIS_ROW R ON R.ANALYSISID = AN.ID "
				+ "INNER JOIN ANALYSIS_COLUMN C ON C.ANALYSISID = AN.ID "
				+ "INNER JOIN ANALYSIS_VALUE V ON V.ROWID = R.ID AND V.COLUMNID = C.ID "
				+ "WHERE AN.TITLE = ? " + "ORDER BY 2";

		final Object[] params = new Object[] { reportName };
		jdbcTemplate.query(sql, params, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				values.get(rs.getString("TITLE")).add(rs.getLong("VALUE"));

			}
		});
		
		final LinkedList<String> reportType = new LinkedList<String>();
		sql = "SELECT ANALYSIS_TYPE FROM ANALYSIS WHERE TITLE = ?";
		jdbcTemplate.query(sql,  params, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				
				reportType.add(rs.getString("ANALYSIS_TYPE"));
			}
		});

		return new Report(columnNames, values, reportName, reportType.getFirst());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteReport(String reportName) {

		String deleteValuesSQL = "DELETE FROM ANALYSIS_VALUE "
				+ "WHERE COLUMNID IN (SELECT C.ID FROM ANALYSIS_COLUMN C "
				+ "INNER JOIN ANALYSIS AN ON AN.ID = C.ANALYSISID "
				+ "WHERE AN.TITLE = ?)";
		String deleteRowsSql = "DELETE FROM ANALYSIS_ROW "
				+ "WHERE ANALYSISID IN (SELECT ID FROM ANALYSIS "
				+ "WHERE TITLE = ?)";
		String deleteColumnsSql = "DELETE FROM ANALYSIS_COLUMN "
				+ "WHERE ANALYSISID IN (SELECT ID FROM ANALYSIS "
				+ "WHERE TITLE = ?)";
		String deleteAnalysisSql = "DELETE FROM ANALYSIS WHERE TITLE = ?";

		Object[] params = new Object[]{reportName};
		
		jdbcTemplate.update(deleteValuesSQL, params);
		jdbcTemplate.update(deleteRowsSql, params);
		jdbcTemplate.update(deleteColumnsSql, params);
		jdbcTemplate.update(deleteAnalysisSql, params);
	}
	
	private long insertAnalysisAndGetId(Report report, String username) {
		
		final String sql = "INSERT INTO ANALYSIS(TITLE, USERNAME, ANALYSIS_TYPE) VALUES(?,?,?)";
		final String[] params = new String[]{report.getTitle(), username, report.getType()};
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection)
					throws SQLException {
				
				PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
				ps.setString(1, params[0]);
				ps.setString(2, params[1]);
				return ps;
			}
		}, keyHolder);
		
		return keyHolder.getKey().longValue();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertReport(Report report, String username) {
		
		long analysisID = insertAnalysisAndGetId(report, username);
		Map<Long, Long> rowIDs = insertRowsAndGetIDs(report, analysisID);
		Map<String, Long> columnIDs = insertColumnsAndGetIDs(report, analysisID);
		insertValues(rowIDs, columnIDs, report);
		return 1;
	}

	private void insertValues(Map<Long, Long> rowIDs,
			Map<String, Long> columnIDs, Report report) {

		String sql = "INSERT INTO ANALYSIS_VALUE(VALUE, ROWID, COLUMNID) VALUES(?, ?, ?)";
		int[] types = new int[]{Types.BIGINT, Types.BIGINT, Types.BIGINT};
		
		for (Map.Entry<String, List<Long>> e : report.getValues().entrySet()) {
			for (int i = 0; i < e.getValue().size(); i++) {
				Object[] params = new Object[]{e.getValue().get(i), rowIDs.get((long) i), columnIDs.get(e.getKey())};
				jdbcTemplate.update(sql, params, types);
			}
		}
		
	}

	private Map<String, Long> insertColumnsAndGetIDs(Report report,
			final long analysisID) {

		Map<String, Long> ids = new HashMap<String, Long>();
		final String sql = "INSERT INTO ANALYSIS_COLUMN(ANALYSISID, TITLE) VALUES(?, ?)";
		final String[] generatedIDPath = new String[]{"ID"};
		
		for (final String columnName : report.getColumnHeaders()) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection connection)
						throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sql, generatedIDPath);
					ps.setLong(1, analysisID);
					ps.setString(2, columnName);
					return ps;
				}
			}, keyHolder);
			
			ids.put(columnName, keyHolder.getKey().longValue());
		}
		
		return ids;
	}

	private Map<Long, Long> insertRowsAndGetIDs(Report report, final long analysisID) {

		Map<Long, Long> ids = new HashMap<Long, Long>();
		final String sql = "INSERT INTO ANALYSIS_ROW(ANALYSISID, ROW_NUMBER) VALUES(?, ?)";
		final String[] generatedIDPath = new String[]{"ID"};
		
		for (int i = 0; i < report.getRowCount(); i++) {
			final int index = i;
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection connection)
						throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sql, generatedIDPath);
					ps.setLong(1, analysisID);
					ps.setLong(2, index);
					return ps;
				}
			}, keyHolder);
			
			ids.put((long) i, keyHolder.getKey().longValue());
		}
		
		return ids;
	}

}
