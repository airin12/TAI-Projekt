package pl.edu.agh.analizer.youtube.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import com.google.api.services.youtubeAnalytics.model.ResultTable;
import com.google.api.services.youtubeAnalytics.model.ResultTable.ColumnHeaders;

public class Report {
	
	private final List<String> columnHeaders;
	
	private final Map<String, List<Long>> values;
	
	private final String title;
	
	private static final Report EMPTY = new Report();
	
	private final int rowCount;
	
	private final List<Long> labels;
	
	private final List<Integer> valuesList;
	
	private final String DATE_HEADER = "DATE";
	
	private final String RAW_HEADER = "RAW_HEADER";
	
	private final String VALUE_HEADER = "VALUE_HEADER";
	
	private static String labelsHeader;
	private static String valuesHeader;
	
	private static List<String> videosIds = new LinkedList<String>();
	
	private Report additionalReport;
	
	private final String reportType;
	
	public Report(List<String> columnHeaders, Map<String, List<Long>> values, String title, String type) {
		System.out.println(columnHeaders);
		System.out.println(title);
		System.out.println(values);
		
		if(type.equals(ReportHelper.VIEWS_OVER_TIME)){
			labelsHeader="day";
			valuesHeader="views";
		}
		
		if(type.equals(ReportHelper.TOP_VIDEOS_10) || type.equals(ReportHelper.TOP_VIDEOS_ALL)){
			labelsHeader="videos";
			valuesHeader="views";
		}
		
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
		if (values.containsKey(valuesHeader)) {
			for (Long value : values.get(valuesHeader)) {
				valuesList.add(value.intValue());
			}
		} else {
			throw new RuntimeException("Report does not contain values");
		}
		
		labels = new LinkedList<Long>();
		if (values.containsKey(labelsHeader)) {
			for (Long l : values.get(labelsHeader)) {
				labels.add(l);  //-- pomysl zeby wrzucac longa z data
			}
		} 
		
		if(labelsHeader.equals("video"))
			;
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
		
		if(type.equals(ReportHelper.VIEWS_OVER_TIME)){
			labelsHeader="day";
			valuesHeader="views";
		}
		
		if(type.equals(ReportHelper.TOP_VIDEOS_ALL)){
			labelsHeader="videos";
			valuesHeader="views";
		}
		
		ArrayList<String> headers = new ArrayList<String>();
		HashMap<String, List<Long>> values = new HashMap<String, List<Long>>();
		
		System.out.println(headers.size());
		for (ColumnHeaders header : table.getColumnHeaders()) {
			String headerName = header.getName();
			headers.add(headerName);
			values.put(headerName, new ArrayList<Long>());
		}
		
		for (List<Object> row : table.getRows()) {
			for (int i = 0; i < table.getColumnHeaders().size(); i++) {
				String headerName = table.getColumnHeaders().get(i).getName();
				System.out.println(headerName);
				long value = 0;
				if(headerName.equals("day")){
					String date = (String) row.get(i);
					SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
					Date d = new Date();
					try {
						d = f.parse(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					value = d.getTime();
				}else if(headerName.equals("video")){
					videosIds.add((String)row.get(i));
				}else
					value = ((BigDecimal) row.get(i)).longValue();
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
	
	private int getIndexOfLastDay() {
		long greatest = 0;
		int index = 0;
		
		for (int i = 0; i < rowCount; i++) {
			if (values.get(labelsHeader).get(i) > greatest) {
				greatest = values.get(labelsHeader).get(i);
				index = i;
			}
		}
		
		return index;
	}
	
	public List<Long> getChartLabelsFromWeek(){

		System.out.println(labels);
		return labels;
	}

	public List<Integer> getChartDataFromWeek(){
		return valuesList;
	}
	

	public List<Long> getChartLabelsFromDay(){
		
		if (!values.containsKey(labelsHeader)) {
			throw new IllegalStateException("Report does not contain date column");
		}
		
		return Collections.singletonList(values.get(labelsHeader).get(getIndexOfLastDay()));

	}

	public List<Integer> getChartDataFromDay(){

		if (!values.containsKey(valuesHeader)) {
			throw new IllegalStateException("Report does not contain date column");
		}
		
		return Collections.singletonList(values.get(valuesHeader).get(getIndexOfLastDay()).intValue());
	}
	
	private int sum(List<Long> ll) {
		int s = 0;
		for (long l : ll) {
			s += (int)l;
		}
		return s;
	}
	
	public Map<String,Integer> getViewsList() {
		
		Map <String,Integer> views = new HashMap<String, Integer>();
		
		List<Long> values = (List<Long>) this.values.get(valuesHeader);
		for(int i = 0 ;i<values.size();i++){
			views.put(videosIds.get(i), values.get(i).intValue());
		}
		
		return views;
	}
	
	public Map<String,Integer> getViewsListDaily() {
		
		Map <String,Integer> views = new HashMap<String, Integer>();
		
		List<Long> values = (List<Long>) this.values.get(valuesHeader);
		for(int i = 0 ;i<values.size();i++){
			views.put(videosIds.get(i), values.get(i).intValue());
		}
		
		return views;
	}
	

	public Map<String,Integer> getTopViewsListDaily(){
		return getViewsListDaily();
	}
	
	public Map<String,Integer> getTopViewsListWeekly(){
		return getViewsList();
	}
	
	public String getType(){
		return reportType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnHeaders == null) ? 0 : columnHeaders.hashCode());
		result = prime * result
				+ ((reportType == null) ? 0 : reportType.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Report other = (Report) obj;
		if (columnHeaders == null) {
			if (other.columnHeaders != null)
				return false;
		} else if (!columnHeaders.equals(other.columnHeaders))
			return false;
		if (reportType == null) {
			if (other.reportType != null)
				return false;
		} else if (!reportType.equals(other.reportType))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}
	
	public void setAdditionalReport(Report report){
		this.additionalReport=report;
	}
	
}
