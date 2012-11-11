package com.momock.fastfood.db.model;

public class Person {
	int id;
	String name;
	String title;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

    @Override
    public String toString() {
        return this.title + " : " + this.name;
    }
}
