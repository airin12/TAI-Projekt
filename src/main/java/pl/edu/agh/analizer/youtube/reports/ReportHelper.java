package pl.edu.agh.analizer.youtube.reports;

public class ReportHelper {
	private String title;
	private String channelId;
	private String analysisStartDate;
	private String analysisEndDate;
	private String analysis;
	
	public static final String VIEWS_OVER_TIME = "views_over_time";
	public static final String TOP_VIDEOS = "top_videos";
	public static final String TOP_VIDEOS_PLOT = "top_videos_plot";
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getAnalysisStartDate() {
		return analysisStartDate;
	}
	public void setAnalysisStartDate(String analysisStartDate) {
		this.analysisStartDate = analysisStartDate;
	}
	public String getAnalysisEndDate() {
		return analysisEndDate;
	}
	public void setAnalysisEndDate(String analysisEndDate) {
		this.analysisEndDate = analysisEndDate;
	}
	public String getAnalysis() {
		return analysis;
	}
	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}
	@Override
	public String toString() {
		return "ReportHelper [title=" + title + ", channelId=" + channelId + ", analysisStartDate=" + analysisStartDate + ", analysisEndDate=" + analysisEndDate + ", analysis="
				+ analysis + "]";
	}
	
	
}
