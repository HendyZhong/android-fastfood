package com.momock.fastfood.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.momock.dal.Database;
import com.momock.fastfood.db.model.Person;

public class PersonDao {
	static final String SQL_GET_BY_ID = "sql.person.get.by.id";
	static final String SQL_GET_ALL = "sql.person.get.all";
	Database db;
	public PersonDao(Database db)
	{
		this.db = db;
	}
	public Person getPersonById(int id)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return db.executeForBean(SQL_GET_BY_ID, params, Person.class);		
	}
	public List<Person> getAllPersons()
	{
		return db.executeForBeanList(SQL_GET_ALL, null, Person.class);
	}
}
