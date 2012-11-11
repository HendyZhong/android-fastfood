package com.momock.fastfood.db;

import java.util.List;

import android.app.Application;
import android.content.res.Configuration;

import com.momock.dal.Database;
import com.momock.fastfood.db.dao.PersonDao;
import com.momock.fastfood.db.model.Person;
import com.momock.util.Logger;

public class App extends Application{
	static App app = null;
	public static App getInstance()
	{
		return app;
	}
	Database db;
	PersonDao personDao;
	
	public App()
	{
		app = this;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		Logger.open("db.log", Logger.LEVEL_DEBUG);
		Logger.debug("onCreate");
		db = new Database(this);
		personDao = new PersonDao(db);		
		
		List<Person> ps = personDao.getAllPersons();
		for(int i = 0; i < ps.size(); i++)
		{
			Person p = ps.get(i);
			Logger.debug("name = " + p.getName() + " , title = " + p.getTitle());
		}
		
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	public PersonDao getPersonDao() {
		return personDao;
	}

}
