package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends AppCompatActivity {
    private List<MeteorShowers> meteorShowersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);


        readCsvFile(this,"meteorshowertest.csv");
        Log.d("ListView", "Meteor Showers List Size: " + meteorShowersList.size());
        for (MeteorShowers meteorShower : meteorShowersList) {
            Log.d("ListView", "Item: " + meteorShower.toString());
        }

        if (meteorShowersList != null && !meteorShowersList.isEmpty()) {
            ListView listView = findViewById(R.id.listView);
            MeteorShowersAdapter adapter = new MeteorShowersAdapter(this, meteorShowersList);
            Log.d("ListView", "Meteor Showers List Size: " + meteorShowersList.size());
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            // Handle the case when no data is available or an error occurred during reading
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        }
    }

    private void readCsvFile(Context context, String csvFileName){

        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(csvFileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //To skip the first line in the csv, which is just the column names
            bufferedReader.readLine();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                System.out.println(values);
                System.out.println(line);// Assuming your values are separated by commas
                if (values.length > 0) {
                    MeteorShowers meteorShower = new MeteorShowers(
                            values[0],
                            values.length > 1 ? values[1] : "",
                            values.length > 2 ? values[2] : "",
                            values.length > 3 ? values[3] : "",
                            values.length > 4 ? values[4] : ""
                    );
                    meteorShowersList.add(meteorShower);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ListView", "Adapter set successfully");
        }
    }
}