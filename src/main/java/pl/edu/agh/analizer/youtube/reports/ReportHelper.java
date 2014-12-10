package pl.edu.agh.analizer.youtube.reports;

public class ReportHelper {
	private String title;
	private String channelId;
	private String analysisStartDate;
	private String analysisEndDate;
	private String analysis;
	
	public static final String VIEWS_OVER_TIME = "views_over_time";
	public static final String TOP_VIDEOS_10 = "top_videos_10";
	public static final String TOP_VIDEOS_ALL = "top_videos_all";
	
	
	/**
	 * Getter for title field
	 * 
	 * @return title of analysis
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Setter for title field
	 * 
	 * @param title title od analysis
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Getter for channelId field
	 * 
	 * @return id of youtube channel
	 */
	public String getChannelId() {
		return channelId;
	}
	
	/**
	 * Setter for channelId field
	 * 
	 * @param channelId id of youtube channel
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	/**
	 * Getter for analysisStartDate field
	 * 
	 * @return analysis start date
	 */
	public String getAnalysisStartDate() {
		return analysisStartDate;
	}
	
	/**
	 * Setter for analysisStartDate field
	 * 
	 * @param analysisStartDate analysis start date
	 */
	public void setAnalysisStartDate(String analysisStartDate) {
		this.analysisStartDate = analysisStartDate;
	}
	
	/**
	 * Getter for analysisEndDate
	 * 
	 * @return analysis end date
	 */
	public String getAnalysisEndDate() {
		return analysisEndDate;
	}
	
	/**
	 * Setter for analysisEndDate field
	 * 
	 * @param analysisEndDate analysis end date
	 */
	public void setAnalysisEndDate(String analysisEndDate) {
		this.analysisEndDate = analysisEndDate;
	}
	
	/**
	 * Getter for analysis field
	 * 
	 * @return type of analysis
	 */
	public String getAnalysis() {
		return analysis;
	}
	
	/**
	 * Setter for analysis field
	 * 
	 * @param analysis type of analysis
	 */
	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportHelper [title=" + title + ", channelId=" + channelId + ", analysisStartDate=" + analysisStartDate + ", analysisEndDate=" + analysisEndDate + ", analysis="
				+ analysis + "]";
	}
	
	
}
