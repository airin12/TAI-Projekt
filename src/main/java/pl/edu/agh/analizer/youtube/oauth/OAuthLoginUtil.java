package pl.edu.agh.analizer.youtube.oauth;

import java.io.File;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class OAuthLoginUtil {

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	public static Credential authorize(List<String> scopes) throws Exception {

		// Load client secrets.
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, OAuthLoginUtil.class
						.getResourceAsStream("/client_secrets.json"));

		// Checks that the defaults have been replaced (Default =
		// "Enter X here").
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret()
						.startsWith("Enter ")) {
			System.err
					.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=youtube"
							+ "into youtube-analytics-cmdline-report-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}

		// Set up file credential store.
		FileCredentialStore credentialStore = new FileCredentialStore(new File(
				System.getProperty("user.home"),
				".credentials/youtube-analytics-api-report.json"), JSON_FACTORY);

		// Set up authorization code flow.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)
				.setCredentialStore(credentialStore).build();

		// Authorize.
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver.Builder().setPort(9009).build()).authorize("user");
	}

}
