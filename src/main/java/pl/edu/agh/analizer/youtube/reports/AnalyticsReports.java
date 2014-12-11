package pl.edu.agh.analizer.youtube.reports;

import java.io.IOException;
import java.util.List;

import pl.edu.agh.analizer.youtube.oauth.OAuthLoginUtil;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtubeAnalytics.YoutubeAnalytics;
import com.google.api.services.youtubeAnalytics.YoutubeAnalytics.Reports.Query;
import com.google.api.services.youtubeAnalytics.model.ResultTable;
import com.google.common.collect.Lists;



public class AnalyticsReports {
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private static YouTube youTube;
	
	private static YoutubeAnalytics analytics;
	
	private final static List<String> SCOPES = Lists.newArrayList(
			"https://www.googleapis.com/auth/yt-analytics.readonly",
	        "https://www.googleapis.com/auth/youtube.readonly");
	
	private final static String APPLICATION_NAME = "youtube.analizer";
	
	public AnalyticsReports() {
		
		try {
			Credential credential = OAuthLoginUtil.authorize(SCOPES);
			
			youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
			
			analytics = new YoutubeAnalytics.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					credential)
				.setApplicationName(APPLICATION_NAME)
				.build();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<Channel> getMineChannelsList() throws IOException {
		
		YouTube.Channels.List channelRequest = youTube.channels().list(
				"id,snippet");
		channelRequest.setMine(true);
		channelRequest.setFields("items(id,snippet/title)");
		ChannelListResponse channels = channelRequest.execute();
		
		List<Channel> listOfChannels = channels.getItems();
		
		return listOfChannels;
	}
	
//	private InputStream getClientSecretsInputStream() {
//		
//		String clientSecretsString = "{" +
//				"\"installed\": {" +
//				"\"client_id\": \"1093585424035-cbjeep3epbmpk4e70liof6hi28eg0t26.apps.googleusercontent.com\"," +
//				"\"client_secret\": \"JY29cHhOBRwm_YOP_QIfT3bD\""+
//				"}"+
//				"}";
//		InputStream is = new ByteArrayInputStream(clientSecretsString.getBytes());
//		
//		return is;
////		AnalyticsReports.class.getResourceAsStream("/client_secrets.json")
//		
//	}
	
//	private Credential authorize(List<String> scopes) throws IOException {
//		
//		//System.out.println(AnalyticsReports.class.getResourceAsStream("/client_secrets.json"));
//		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
//				getClientSecretsInputStream());
//		
//		FileCredentialStore credentialStore = new FileCredentialStore(
//				new File(System.getProperty("user.home"),
//				".credentials/youtube-analytics-api-report.json"),
//				JSON_FACTORY);
//
//		GoogleAuthorizationCodeFlow flow = 
//				new GoogleAuthorizationCodeFlow(HTTP_TRANSPORT, JSON_FACTORY,
//						clientSecrets.getDetails().getClientId(),
//						clientSecrets.getDetails().getClientSecret(), scopes);
//		
//		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//	}
	
	
	private Query prepareQuery(YoutubeAnalytics analytics, String channelID, String startDate, String endDate, String metrics, String dimensios, String sortBy) throws IOException {
		
		return analytics.reports().query("channel==" + channelID, startDate, endDate, metrics)
				.setDimensions(dimensios)
				.setSort(sortBy);
	}
	
	
	public ResultTable executeViewsOverTimeQuery(String channelID, String startDate, String endDate) throws IOException {
		
		Query query = prepareQuery(analytics, channelID, startDate, endDate, "views,uniques", "day", "day");
		return query.execute();
	}
	
	public ResultTable executeTopVideosWeeklyQuery(String channelID, String startDate, String endDate) throws IOException {
		
		Query query = prepareQuery(analytics, channelID, startDate, endDate, "views,subscribersGained,subscribersLost", "video", "-views");
		return query.setMaxResults(10).execute();
	}
	
	public ResultTable executeTopVideosDailyQuery(String channelID, String startDate, String endDate) throws IOException {
		
		Query query = prepareQuery(analytics, channelID, endDate, endDate, "views,subscribersGained,subscribersLost", "video", "-views");
		return query.setMaxResults(10).execute();
	}

	public ResultTable executeAllVideosQuery(String channelID, String startDate, String endDate) throws IOException {
		
		Query query = prepareQuery(analytics, channelID, startDate, endDate, "views,subscribersGained,subscribersLost", "video", "-views");
		return query.setMaxResults(10).execute();
	}
	
}
