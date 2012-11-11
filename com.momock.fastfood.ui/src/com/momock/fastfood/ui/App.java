package com.momock.fastfood.ui;

import android.app.Application;

import com.momock.fastfood.ui.model.Message;

public class App extends Application{
	static App instance;
    static private Message[] messages = null;

	public App()
	{
		instance = this;
	}
	public static App getInstance()
	{
		return instance;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	public static Message[] getMessages() {
		if (messages == null)
		{
			messages = new Message[] {
	                new Message("Gas bill overdue", true),
	                new Message("Congratulations, you've won!", true),
	                new Message("I love you!", false),
	                new Message("Please reply!", false),
	                new Message("You ignoring me?", false),
	                new Message("Not heard from you", false),
	                new Message("Electricity bill", true),
	                new Message("Gas bill", true),
	                new Message("Holiday plans", false),
	                new Message("Marketing stuff", false),
	        };
		}
		return messages;
	}

}
