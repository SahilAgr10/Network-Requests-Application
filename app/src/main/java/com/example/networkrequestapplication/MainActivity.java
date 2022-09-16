package com.example.networkrequestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    private WordAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);


        ArrayList<Word> earthquakes = QueryUtils.extractFeatureFromJson(USGS_REQUEST_URL);
        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView) findViewById(R.id.list);
        //WordAdapter adapter = new WordAdapter(this,earthquakes);
        mAdapter = new WordAdapter(this, new ArrayList<Word>());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(intent);

            }
        });
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Word>> {
        @Override
        protected List<Word> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Word> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        protected void onPostExecute(List<Word> data) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}