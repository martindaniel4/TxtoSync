package com.example.txtosync.data;

public class MeInfo {

	private User user;
	
	private Integer messages_count;
	
	private Integer stories_count;

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Integer getMessages() {
		return messages_count;
	}
	
	public void setMessages(Integer messages) {
		this.messages_count = messages;
	}
	public Integer getStories() {
		return stories_count;
	}
	
	public void setStories(Integer stories) {
		this.stories_count = stories;
	}
	
}
