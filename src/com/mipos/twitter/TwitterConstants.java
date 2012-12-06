package com.mipos.twitter;

public class TwitterConstants {

	public static final String CONSUMER_KEY = "wAnvwfi3N1PUbB6v0tsQxQ";
	public static final String CONSUMER_SECRET= "HJnR9A0ZkXGVYykmT67c5aY91q8NVbx868dqhl7BhqE";
	
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	
	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

}

