package com.momock.fastfood.multithread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import com.momock.util.Logger;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity implements
		JsonRemoteService.Callback {

	Button btnWeather;
	Button btnProgress;
	Button btnImage;
	ImageView ivImage;
	JsonRemoteService weatherRemoteService = new JsonRemoteService();
	Handler handler;
	ProgressBar progress;

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			Logger.debug("MyHandler.handleMessage : " + msg.arg1);
			progress.setProgress(msg.arg1);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.open("multithread.log", Logger.LEVEL_DEBUG);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		handler = new MyHandler();
		progress = (ProgressBar) findViewById(R.id.progress);
		ivImage = (ImageView) findViewById(R.id.ivImage);

		btnWeather = (Button) findViewById(R.id.btnWeather);
		btnWeather.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//String url = "http://www.weather.com.cn/data/sk/101020100.html";
				//String url = "http://10.0.2.2/momock/test/getdata";
				String url = "http://momock.com/test/getdata";
				weatherRemoteService.doGet(url, MainActivity.this);
			}
		});

		btnProgress = (Button) findViewById(R.id.btnProgress);
		btnProgress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i <= 100; i++) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							processMessageWithCallback(i);
							// processHandlerWithCallback(i);
							// processHandlerWithSubclass(i);
						}
					}
				};
				new Thread(runnable).start();
			}
		});

		btnImage = (Button) findViewById(R.id.btnImage);
		btnImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageDownloader downloader = new ImageDownloader(
						MainActivity.this, new ImageDownloader.Callback() {

							@Override
							public void onImageDownloaded(File imageFile) {
								FileInputStream fis;
								try {
									fis = new FileInputStream(
											imageFile);
									Bitmap bmp = null;
									try {
										bmp = BitmapFactory.decodeStream(fis, null,
												null);
										ivImage.setImageBitmap(bmp);
									} catch (OutOfMemoryError ome) {
										Logger.error(ome.getMessage());
									}
								} catch (FileNotFoundException e) {
									Logger.error(e.getMessage());
								}
							}
						});
				//downloader.execute("http://10.0.2.2/momock/data/test.jpg");
				downloader.execute("http://momock.com/data/test.jpg");
			}

		});
	}

	void processMessageWithCallback(final int value) {
		Message msg = Message.obtain(handler, new Runnable() {
			@Override
			public void run() {
				Logger.debug("Message callback " + value);
				progress.setProgress(value);
			}
		});
		handler.sendMessage(msg);
	}

	void processHandlerWithSubclass(final int value) {
		Message msg = handler.obtainMessage(0, value, 0);
		handler.sendMessage(msg);
	}

	void processHandlerWithCallback(final int value) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Logger.debug("processHandlerWithCallback " + value);
				progress.setProgress(value);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void processResponse(JSONObject json) {
		try {
			JSONObject wi = json.getJSONObject("weatherinfo");
			String weather = "上海今天气温：" + wi.get("temp") + "度，" + wi.get("WD")
					+ wi.get("WS") + "，湿度：" + wi.get("SD");
			Toast.makeText(this, weather, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Logger.error(e.getMessage());
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		weatherRemoteService.shutdown();
		Logger.close();
	}
}
