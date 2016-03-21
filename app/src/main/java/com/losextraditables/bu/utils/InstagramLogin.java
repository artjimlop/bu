package com.losextraditables.bu.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.losextraditables.bu.login.InstagramDialog;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.inject.Inject;

public class InstagramLogin {

	private Context context;
	@Inject InstagramSession session;
	private InstagramDialog instagramDialog;
	private OAuthAuthenticationListener authAuthenticationListener;
	private ProgressDialog progressDialog;
	private HashMap<String, String> userInfo = new HashMap<String, String>();
	private String accessToken;

	private String clientId;
	private String clientSecret;

	static int WHAT_ERROR = 1;
	private static int WHAT_FETCH_INFO = 2;

	/**
	 * Callback url, as set in 'Manage OAuth Costumers' page
	 * (https://developer.github.com/)
	 */

	public static String callbackUrl = "";
	private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
	private static final String API_URL = "https://api.instagram.com/v1";

	private static final String TAG = "InstagramAPI";

	@Inject public InstagramLogin() {
	}

	public void initialize(Context context, String clientId, String clientSecret,
						String callbackUrl) {
		this.context = context;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		accessToken = session.getAccessToken(context);
		InstagramLogin.callbackUrl = callbackUrl;
		String authUrl = AUTH_URL
				+ "?client_id="
				+ clientId
				+ "&redirect_uri="
				+ InstagramLogin.callbackUrl
				+ "&response_type=code&display=touch&scope=likes+comments+relationships";

		InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
			@Override
			public void onComplete(String code) {
				getAccessToken(code);
			}

			@Override
			public void onError(String error) {
				authAuthenticationListener.onFail("Authorization failed");
			}
		};

		instagramDialog = new InstagramDialog(context, authUrl, listener);
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
	}

	private void getAccessToken(final String code) {
		progressDialog.setMessage("Getting access token ...");
		progressDialog.show();

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Getting access token");
				int what = WHAT_FETCH_INFO;
				try {
					URL url = new URL(TOKEN_URL);
					Log.i(TAG, "Opening Token URL " + url.toString());
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);
					OutputStreamWriter writer = new OutputStreamWriter(
							urlConnection.getOutputStream());
					writer.write("client_id=" + clientId + "&client_secret="
							+ clientSecret + "&grant_type=authorization_code"
							+ "&redirect_uri=" + callbackUrl + "&code=" + code);
					writer.flush();
					String response = Utils.streamToString(urlConnection
							.getInputStream());
					Log.i(TAG, "response " + response);
					JSONObject jsonObj = (JSONObject) new JSONTokener(response)
							.nextValue();

					accessToken = jsonObj.getString("access_token");
					Log.i(TAG, "Got access token: " + accessToken);

					String id = jsonObj.getJSONObject("user").getString("id");
					String user = jsonObj.getJSONObject("user").getString(
							"username");
					String name = jsonObj.getJSONObject("user").getString(
							"full_name");

					session.storeAccessToken(context, accessToken, id, user, name);

				} catch (Exception ex) {
					what = WHAT_ERROR;
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
			}
		}.start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == WHAT_ERROR) {
				progressDialog.dismiss();
				if (msg.arg1 == 1) {
					authAuthenticationListener.onFail("Failed to get access token");
				} else if (msg.arg1 == 2) {
					authAuthenticationListener.onFail("Failed to get user information");
				}
			} else if (msg.what == WHAT_FETCH_INFO) {
				progressDialog.dismiss();
				authAuthenticationListener.onSuccess();
			}
		}
	};

	public HashMap<String, String> getUserInfo() {
		return userInfo;
	}

	public boolean hasAccessToken() {
		return (accessToken == null) ? false : true;
	}

	public void setListener(OAuthAuthenticationListener listener) {
		authAuthenticationListener = listener;
	}

	public String getUserName() {
		return session.getUsername(context);
	}

	public String getId() {
		return session.getId(context);
	}

	public String getName() {
		return session.getName(context);
	}
	public String getTOken() {
		return session.getAccessToken(context);
	}
	public void authorize() {
		instagramDialog.show();
	}

	public void resetAccessToken() {
		if (accessToken != null) {
			session.resetAccessToken(context);
			accessToken = null;
		}
	}

	public interface OAuthAuthenticationListener {
		void onSuccess();

		void onFail(String error);
	}

	
}