package pl.edu.agh.analizer.youtube.reports;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.youtubeAnalytics.model.ResultTable;
import com.google.api.services.youtubeAnalytics.model.ResultTable.ColumnHeaders;

public class Report {
	
	private final List<String> columnHeaders;
	
	private final Map<String, List<Long>> values;
	
	private final String title;
	
	private static final Report EMPTY = new Report(Collections.EMPTY_LIST, Collections.EMPTY_MAP, "Empty report");
	
	private final int rowCount;
	
	public Report(List<String> columnHeaders, Map<String, List<Long>> values, String title) {
		
		this.columnHeaders = new ArrayList<String>(columnHeaders);
		this.values = new HashMap<String, List<Long>>(values);
		this.title = title;
		
		if (columnHeaders.isEmpty()) {
			rowCount = 0;
		} else {
			rowCount = values.get(columnHeaders.get(0)).size();
		}
	}
	
	public static Report ofResultTable(ResultTable table, String title) {
		
		if (table.getRows() == null || table.getRows().isEmpty()) {
			throw new IllegalArgumentException("No results found.");
		}
		
		ArrayList<String> headers = new ArrayList<String>();
		HashMap<String, List<Long>> values = new HashMap<String, List<Long>>();
		
		for (ColumnHeaders header : table.getColumnHeaders()) {
			String headerName = header.getName();
			headers.add(headerName);
			values.put(headerName, new ArrayList<Long>());
		}
		
		for (List<Object> row : table.getRows()) {
			for (int i = 0; i < table.getColumnHeaders().size(); i++) {
				String headerName = table.getColumnHeaders().get(i).getName();
				long value = ((BigDecimal) row.get(i)).longValue();
				values.get(headerName).add(value);
			}
		}
		
		return new Report(headers, values, title);
	}
	
	public static Report empty() {
		return EMPTY;
	}
	
	public Map<String, List<Long>> getValues() {
		return values;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getRowCount() {
		return rowCount;
	}

}
