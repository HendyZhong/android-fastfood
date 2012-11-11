package com.momock.fastfood.multithread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ImageDownloader extends AsyncTask<String, Integer, String> {

	public interface Callback {
		void onImageDownloaded(File imageFile);
	}

	Callback callback;
	Context context;
	ProgressDialog progressDialog;
	File imageFile = null;

	ImageDownloader(Context context, Callback callback) {
		this.context = context;
		this.callback = callback;

		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Downloading");
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

	}

	@Override
	protected String doInBackground(String... sUrl) {
		try {
			URL url = new URL(sUrl[0]);
			URLConnection connection = url.openConnection();
			connection.connect();
			int fileLength = connection.getContentLength();

			InputStream input = new BufferedInputStream(url.openStream());
			imageFile = File.createTempFile("image", "tmp");
			OutputStream output = new FileOutputStream(imageFile);

			byte data[] = new byte[1024];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress((int) (total * 100 / fileLength));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		progressDialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		callback.onImageDownloaded(imageFile);
	}
}