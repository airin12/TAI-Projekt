package pl.edu.agh.analizer.youtube.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
	
	private final List<Long> labels;
	
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
		
		labels = new LinkedList<Long>();
		if (values.containsKey(DATE_HEADER)) {
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
			SimpleDateFormat format = new SimpleDateFormat("mm");
			for (Long l : values.get(DATE_HEADER)) {
				labels.add(l);  //-- pomysl zeby wrzucac longa z data
			}
		} else if (values.containsKey(RAW_HEADER)) {
			for (Long l : values.get(RAW_HEADER)) {
				labels.add(l); //-- pomysl zeby wrzucac longa z data
			}
		} else {
			for (int i = 0; i < rowCount; i++) {
				labels.add(new Long(i));
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
	
	private int getIndexOfLastDay() {
		long greatest = 0;
		int index = 0;
		
		for (int i = 0; i < rowCount; i++) {
			if (values.get(DATE_HEADER).get(i) > greatest) {
				greatest = values.get(DATE_HEADER).get(i);
				index = i;
			}
		}
		
		return index;
	}
	
	//TODO labelki do wykresu z ostatniego tygodnia, data jako long
	public List<Long> getChartLabelsFromWeek(){

		System.out.println(labels);
		return labels;
	}

	//TODO dane do wykresu z ostatniego tygodnia
	public List<Integer> getChartDataFromWeek(){
		return valuesList;
	}
	
	//TODO labelki do wykresu z ostatniego dnia, data jako long
	public List<Long> getChartLabelsFromDay(){
		
		if (!values.containsKey(DATE_HEADER)) {
			throw new IllegalStateException("Report does not contain date column");
		}
		
		return Collections.singletonList(values.get(DATE_HEADER).get(getIndexOfLastDay()));

//		System.out.println(labels);
//		return labels.subList(0, 1);
	}

	//TODO dane do wykresu z ostatniego dnia
	public List<Integer> getChartDataFromDay(){

		if (!values.containsKey(VALUE_HEADER)) {
			throw new IllegalStateException("Report does not contain date column");
		}
		
		return Collections.singletonList(values.get(VALUE_HEADER).get(getIndexOfLastDay()).intValue());
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
		
		List<String> titles = new LinkedList<String>();
		for (String s : columnHeaders) {
			if (!s.equals(DATE_HEADER) && !s.equals(RAW_HEADER) && !s.equals(VALUE_HEADER)) {
				titles.add(s);
			}
		}
		
		for (String title : titles) {
			views.put(title, sum(values.get(title)));
		}
		
		return views;
	}
	
	//TODO pobranie listy wyswietlen dla 10 najlepszych filmow, mapa<nazwa, wyswietlenia>
	// pobranie dla ostatniego dnia
	public Map<String,Integer> getTopViewsListDaily(){
//		Map <String,Integer> mock = new HashMap<String, Integer>();
//		mock.put("super video na ten tydzien",new Integer(12));
//		mock.put("inne video na ten tydzien",new Integer(12312));
//		mock.put("hit",new Integer(12121));
//		return mock;
		
		//Powinno dzia³aæ, poniewa¿ w zapytaniu bêdzie ograniczenie do 10 filmów
		return getViewsList();
	}
	
	//TODO pobranie listy wyswietlen dla 10 najlepszych filmow, mapa<nazwa, wyswietlenia>
		// pobranie dla ostatniego tygodnia
	public Map<String,Integer> getTopViewsListWeekly(){
//		Map <String,Integer> mock = new HashMap<String, Integer>();
//		mock.put("super video na ten tydzien",new Integer(121212));
//		mock.put("inne video na ten tydzien",new Integer(12212));
//		mock.put("hit",new Integer(1212122));
//		return mock;
		
		//Powinno dzia³aæ, poniewa¿ zapytanie bêdzie w sobie zawieraæ informacjê, ze jest dla ostatniego tygodnia
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
	
	
	
}
