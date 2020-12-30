package com.example.vijaya.earthquakeapp;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Earthquake} objects.
     */
    public static List<Earthquake> fetchEarthquakeData2(String requestUrl) {
        // An empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();
        //  URL object to store the url for a given string
        // URL url = null;               <--- I don't think I need this
        // A string to store the response obtained from rest call in the form of string
        try {
            URL httpURL = new URL(requestUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) httpURL.openConnection();
            // InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream()); // I don't think I need this since the below code works fine.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            double mag;
            String place;
            long time;
            String url;

            String response;
            String fullResponse = "";

            boolean entry = true;
            boolean exit = false;
            while((response = reader.readLine()) != null){
                if (entry){
                    response = response.substring(270,response.length()-1);
                    entry = false;
                    exit = true;
                } else if (exit && response.contains("bbox")) {
                    response = response.substring(0,(response.indexOf("bbox")-3));
                } else if (exit) {
                    response = response.substring(0,response.length()-1);
                }
                System.out.println(response);
                // JSONObject earthquakeJSON = new JSONObject(jsonResponse);

                // earthquakes.add(new Earthquake( mag, place, time, url));
            }
            reader.close();
            //DONE: 1. Create a URL from the requestUrl string and make a GET request to it

            //TODO: 2. Read from the Url Connection and store it as a string(jsonResponse)
            /*TODO: 3. Parse the jsonResponse string obtained in step 2 above into JSONObject to extract the values of
                    "mag","place","time","url"for every earth quake and create corresponding Earthquake objects with them
                    Add each earthquake object to the list(earthquakes) and return it.
            */

            // Return the list of earthquakes

        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception:  ", e);
        }
        // Return the list of earthquakes
        return earthquakes;
    }
}
