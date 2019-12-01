package com.example.fillinggood.Boundary.DBboundary;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

public class DBmanager {
    private static DBmanager instance = new DBmanager();

    public static DBmanager getInstance(){
        return instance;
    }
    public DBmanager(){}

    private static String IP_ADDRESS = "";
    private static String phpName = "";

    public String Connect(){
        HttpURLConnection urlConnection = null;
        String address = IP_ADDRESS + "\\" + phpName + ".php";

        try {
            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
        } catch (Exception e){
            Log.d(phpName, "Connection Fail", e);
            return "Fail";
        }
        return "Connect";
    }
}
