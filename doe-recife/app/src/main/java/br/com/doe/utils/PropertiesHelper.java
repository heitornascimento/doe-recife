package br.com.doe.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by heitornascimento on 4/11/16.
 */
public class PropertiesHelper {

    private static final String DOE_WS_PROPERTIES = "doe_ws.properties";
    private static final String LOGIN_API_ENDPOINT_URL = "loginApiEndpointUrl";
    private static final String REGISTER_API_ENDPOINT_URL = "registerApiEndpointUrl";
    private static final String INSTITUTION_API_ENDPOINT = "institutionsApiEndpoint";
    private static final String BASE_URL = "ApiEnpointUrl";


    /**
     * Load the URL login in properties file.
     **/
    public static String readLoginUrlProperties(Context ctx) throws IOException {

        Properties properties = new Properties();
        Resources resource = ctx.getResources();
        AssetManager assetManager = resource.getAssets();
        InputStream is = assetManager.open(DOE_WS_PROPERTIES);
        properties.load(is);
        String loginApiUrl = properties.getProperty(LOGIN_API_ENDPOINT_URL);
        return loginApiUrl;
    }

    /**
     * Load the URL login in properties file.
     **/
    public static String readRegisterUrlProperties(Context ctx) throws IOException {

        Properties properties = new Properties();
        Resources resource = ctx.getResources();
        AssetManager assetManager = resource.getAssets();
        InputStream is = assetManager.open(DOE_WS_PROPERTIES);
        properties.load(is);
        String registerUrl = properties.getProperty(REGISTER_API_ENDPOINT_URL);
        return registerUrl;
    }

    /**
     * Load the URL login in properties file.
     **/
    public static String readInstituionsURLProperties(Context ctx) throws IOException {

        Properties properties = new Properties();
        Resources resource = ctx.getResources();
        AssetManager assetManager = resource.getAssets();
        InputStream is = assetManager.open(DOE_WS_PROPERTIES);
        properties.load(is);
        String registerUrl = properties.getProperty(INSTITUTION_API_ENDPOINT);
        return registerUrl;
    }

    /**
     * Load the base URL in properties file.
     **/
    public static String readbaseURLProperties(Context ctx) throws IOException {

        Properties properties = new Properties();
        Resources resource = ctx.getResources();
        AssetManager assetManager = resource.getAssets();
        InputStream is = assetManager.open(DOE_WS_PROPERTIES);
        properties.load(is);
        String baseUrl = properties.getProperty(BASE_URL);
        return baseUrl;
    }

}
