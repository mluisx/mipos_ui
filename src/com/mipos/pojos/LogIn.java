package com.mipos.pojos;

public class LogIn {

	String user;
	boolean successful; 
	int id;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Data [user=" + user + ", id=" + id + "]";
	}
	 
}

