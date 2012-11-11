package com.momock.fastfood.multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import com.momock.util.Logger;

public class JsonRemoteService {
	public interface Callback {
		void processResponse(JSONObject json);
	}

	class HttpRequestTask extends AsyncTask<Void, Void, Void> {
		Callback callback;
		HttpRequestBase request;
		JSONObject responseJson = null;

		public HttpRequestTask(HttpRequestBase request, Callback callback) {
			this.request = request;
			this.callback = callback;
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				request.setHeader("Accept", "application/json");
				request.setHeader("Accept-Encoding", "gzip");

				final long t = System.currentTimeMillis();
				httpClient.execute(request, new ResponseHandler<Object>() {

					@Override
					public Object handleResponse(HttpResponse response) {

						Logger.info("HTTPResponse received in ["
								+ (System.currentTimeMillis() - t) + "ms]");

						HttpEntity entity = response.getEntity();
						if (entity != null) {
							try {
								InputStream instream = entity.getContent();
								Header contentEncoding = response
										.getFirstHeader("Content-Encoding");
								if (contentEncoding != null
										&& contentEncoding.getValue()
												.equalsIgnoreCase("gzip")) {
									instream = new GZIPInputStream(instream);
								}

								String resultString = convertStreamToString(instream);
								instream.close();
								Logger.debug(resultString);

								responseJson = new JSONObject(resultString);

							} catch (Exception e) {
								Logger.error(e.getMessage());
							}
						}
						return null;
					}
				});
			} catch (Exception e) {
				Logger.error(e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			callback.processResponse(responseJson);		
		}

	}

	AndroidHttpClient httpClient;

	public JsonRemoteService() {
		httpClient = AndroidHttpClient.newInstance("Android");
	}

	public void shutdown() {
		httpClient.close();
	}

	public void doGet(String url, Callback callback) {
		new HttpRequestTask(new HttpGet(url), callback).execute();
	}

	public void doPut(String url, Callback callback) {
		new HttpRequestTask(new HttpPut(url), callback).execute();
	}

	public void doDelete(String url, Callback callback) {
		new HttpRequestTask(new HttpDelete(url), callback).execute();
	}

	public void doPostJson(String url, JSONObject json, Callback callback) {
		HttpPost httpPostRequest = new HttpPost(url);
		StringEntity se;
		try {
			se = new StringEntity(json.toString());
			httpPostRequest.setEntity(se);
			httpPostRequest.setHeader("Content-type", "application/json");
			new HttpRequestTask(httpPostRequest, callback).execute();
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
			callback.processResponse(null);
		}
	}

	static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			Logger.error(e.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Logger.error(e.getMessage());
			}
		}
		return sb.toString();
	}
}
