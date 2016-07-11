package br.com.doe.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

/**
 * @author heitornsouza
 * Copyright 2013. All rights reserved
 */
public class PropertiesManager {

	private static PropertiesManager instance;
	private Context ctx;
	private String apiUrl;

	private PropertiesManager(Context ctx) {
		this.ctx = ctx;
		loadProperties();
	}

	public static PropertiesManager getInstance(Context ctx) {
		if (instance == null) {
			instance = new PropertiesManager(ctx);
		}

		return instance;
	}

	private void loadProperties() {

		Properties properties = new Properties();
		Resources resource = ctx.getResources();
		AssetManager assetManager = resource.getAssets();
		try {
			InputStream is = assetManager.open("doe_ws.properties");
			properties.load(is);
			apiUrl = properties.getProperty("ApiEnpointUrl");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getApiUrl(){
		return this.apiUrl;
	}
}
