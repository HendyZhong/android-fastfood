package com.momock.fastfood.ui.model;

public class Message {

    private String subject;
    private boolean unread;

    public Message(String subject, boolean unread) {
        this.subject = subject;
        this.unread = unread;
    }

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

}
