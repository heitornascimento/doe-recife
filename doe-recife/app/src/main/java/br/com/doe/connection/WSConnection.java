package br.com.doe.connection;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author heitornsouza
 * Copyright 2013. All rights reserved
 */
public class WSConnection {

	private String mUrl;
	private static WSConnection instance;
	private DefaultHttpClient client;
	private JSONObject json;
	private HttpPost post;
	private HttpPut put;
	private HttpGet get;
	private JSONObject holder;
	private JSONObject userObj;
	
	private WSConnection(){
		setClienteHttp();
	}
	
	public static WSConnection getInstance(){
		if(instance == null){
			instance = new WSConnection();
		}
		return instance;
	}
	
	private void setUrl(String url){
		this.mUrl = url;
	}
	
	private void setClienteHttp(){
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
		client = new DefaultHttpClient(httpParams);
	}
	
	public void setPostAction(String url) throws JSONException{
		setUrl(url);
		post = new HttpPost(this.mUrl);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-Type", "application/json");
		setJsonObject();
	}
	
	public void setPutAction(String url) throws JSONException{
		setUrl(url);
		put = new HttpPut(this.mUrl);
		put.setHeader("Accept", "application/json");
		put.setHeader("Content-Type", "application/json");
		setJsonObject();
	}
	
	public void setGetAction(String url) throws JSONException{
		setUrl(url);
		get = new HttpGet(this.mUrl);
		get.setHeader("Accept", "application/json");
		get.setHeader("Content-Type", "application/json");
		setJsonObject();
	}
	
	public HttpPost getPostAction(){
		return this.post;
	}
	
	public HttpPut getPutAction(){
		return this.put;
	}
	
	public HttpGet getGETAction(){
		return this.get;
	}
	
	private void setJsonObject() throws JSONException{
		holder = new JSONObject();
		userObj = new JSONObject();
		json = new JSONObject();
		json.put("success", false);
		json.put("info", "Ops! Algums deu errada!");
	}
	
	public JSONObject getJsonObject(){
		return this.json;
	}
	
	public JSONObject getHolder(){
		return this.holder;
	}
	
	public JSONObject getUserObj(){
		return this.userObj;
	}
	
	public DefaultHttpClient getHttpCliente(){
		return this.client;
	}
	
	
	
	
	
	
}
