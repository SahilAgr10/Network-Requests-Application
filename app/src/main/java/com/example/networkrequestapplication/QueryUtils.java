package com.example.networkrequestapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2021-01-01&endtime=2021-12-01&minmagnitude=6";

    private QueryUtils() {
    }


    public static ArrayList<Word> extractFeatureFromJson(String earthquAKEJSON){

        ArrayList<Word> earthquakes = new ArrayList<>();

        try{


            JSONObject baseJsonResponse = new JSONObject(earthquAKEJSON);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < earthquakeArray.length(); i++) {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String place = properties.getString("place");
                String[] arr = place.split("of");
                String Arr1 = arr[0];
                String Arr2 = arr[1];
                String url = properties.getString("url");

                String time = properties.getString("time");

                long tome = Long.parseLong(time);
                long timeInMilliseconds = tome;
                Date dateObject = new Date(timeInMilliseconds);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy ");
                String dateToDisplay = dateFormatter.format(dateObject);

                Date datetime = new Date(timeInMilliseconds);
                SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
                String timeToShow = dateFormat.format(datetime);

                Word currentearthquakes = new Word(magnitude,Arr1,Arr2,dateToDisplay,timeToShow);
                earthquakes.add(currentearthquakes);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return earthquakes;
    }

    public static List<Word> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Word> earthquakes = extractFeatureFromJson(jsonResponse);
        return earthquakes;
    }

    private static URL createUrl(String usgsRequestUrl) {
        URL url = null;
        try {
            url = new URL(usgsRequestUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    }
