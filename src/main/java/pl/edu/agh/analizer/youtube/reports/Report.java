package pl.edu.agh.analizer.youtube.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.api.services.youtubeAnalytics.model.ResultTable;
import com.google.api.services.youtubeAnalytics.model.ResultTable.ColumnHeaders;

public class Report {
	
	private final List<String> columnHeaders;
	
	private final Map<String, List<Long>> values;
	
	private final String title;
	
	private static final Report EMPTY = new Report();
	
	private final int rowCount;
	
	private final List<String> labels;
	
	private final List<Integer> valuesList;
	
	private final String DATE_HEADER = "DATE";
	
	private final String RAW_HEADER = "RAW_HEADER";
	
	private final String VALUE_HEADER = "VALUE_HEADER";
	
	private final String reportType;
	
	public Report(List<String> columnHeaders, Map<String, List<Long>> values, String title, String type) {
		System.out.println(columnHeaders);
		System.out.println(title);
		System.out.println(values);
		
		this.reportType=type;
		this.columnHeaders = new ArrayList<String>(columnHeaders);
		this.values = new HashMap<String, List<Long>>(values);
		this.title = title;
		
		if (columnHeaders.isEmpty()) {
			rowCount = 0;
		} else {
			rowCount = values.get(columnHeaders.get(0)).size();
		}
		
		
		valuesList = new LinkedList<Integer>();
		if (values.containsKey(VALUE_HEADER)) {
			for (Long value : values.get(VALUE_HEADER)) {
				valuesList.add(value.intValue());
			}
		} else {
			throw new RuntimeException("Report does not contain values");
		}
		
		labels = new LinkedList<String>();
		if (values.containsKey(DATE_HEADER)) {
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
			SimpleDateFormat format = new SimpleDateFormat("mm");
			for (Long l : values.get(DATE_HEADER)) {
				labels.add(format.format(new Date(l)));
			}
		} else if (values.containsKey(RAW_HEADER)) {
			for (Long l : values.get(RAW_HEADER)) {
				labels.add(l.toString());
			}
		} else {
			for (int i = 0; i < rowCount; i++) {
				labels.add(Integer.toString(i));
			}
		}
	}
	
	private Report() {
		this.columnHeaders = Collections.EMPTY_LIST;
		this.labels = Collections.EMPTY_LIST;
		this.values = Collections.EMPTY_MAP;
		this.valuesList = Collections.EMPTY_LIST;
		this.rowCount = 0;
		this.title = "EMPTY";
		this.reportType = null;
	}
	
	public static Report ofResultTable(ResultTable table, String title, String type) throws IllegalArgumentException{
		
		if (table.getRows() == null || table.getRows().isEmpty()) {
			//throw new IllegalArgumentException("No results found.");
			return EMPTY;
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
				long value = Long.parseLong((String)row.get(i));
				values.get(headerName).add(value);
			}
		}
		
		return new Report(headers, values, title,type);
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
	
	public List<String> getColumnHeaders() {
		return columnHeaders;
	}
	
	
	public List<String> getChartLabelsFromWeek(){

		System.out.println(labels);
		return labels;
	}

	public List<Integer> getChartDataFromWeek(){

		return valuesList;
	}
	
	public List<String> getChartLabelsFromDay(){

		System.out.println(labels);
		return labels.subList(0, 1);
	}

	public List<Integer> getChartDataFromDay(){

		return valuesList.subList(0, 1);
	}
	
	public Map<String,Integer> getViewsList(){
		Map <String,Integer> mock = new HashMap<String, Integer>();
		mock.put("super video",new Integer(121212));
		mock.put("super zw",new Integer(121));
		mock.put("best",new Integer(555212));
		return mock;
	}
	
	public Map<String,Integer> getTopViewsListDaily(){
		return new HashMap<String,Integer>();
	}
	
	public Map<String,Integer> getTopViewsListWeekly(){
		Map <String,Integer> mock = new HashMap<String, Integer>();
		mock.put("super video na ten tydzien",new Integer(121212));
		return mock;
	}
	
	public String getType(){
		return reportType;
	}
	
}
