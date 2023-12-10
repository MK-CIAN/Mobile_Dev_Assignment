package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        readCsvFile(this, "meteorshowertest.csv");
        Log.d("ListView", "Meteor Showers List Size: " + meteorShowersList.size());
        for (MeteorShowers meteorShower : meteorShowersList) {
            Log.d("ListView", "Item: " + meteorShower.toString());
        }

        ListView listView = findViewById(R.id.listView);
        if (meteorShowersList != null && !meteorShowersList.isEmpty()) {
            MeteorShowersAdapter adapter = new MeteorShowersAdapter(this, meteorShowersList);
            Log.d("ListView", "Meteor Showers List Size: " + meteorShowersList.size());
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            // Handle the case when no data is available or an error occurred during reading
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {
            MeteorShowers selectedMeteorShower = meteorShowersList.get(position);
            insertMeteorShowerIntoDatabase(selectedMeteorShower);
        });

        listView.setTranslationX(-1500f);
        animateObjectIn(listView, 150);
    }

    private void animateObjectIn(View view, long delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0);
        animator.setDuration(1000); // Set the duration of the animation
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Set the interpolation
        animator.setStartDelay(delay); // Set a delay for each button
        animator.start(); // Start the animation
    }

    private void insertMeteorShowerIntoDatabase(MeteorShowers selectedMeteorShower) {
        MeteorShowersDatabase db = Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build();

        new Thread(() -> {
            // Check if the meteor shower already exists in the database
            MeteorShowers existingMeteorShower = db.meteorShowersDao().getMeteorShowerByName(selectedMeteorShower.getEvent());

            if (existingMeteorShower == null) {
                // If the meteor shower doesn't exist, insert it into the database
                db.meteorShowersDao().insert(selectedMeteorShower);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Meteor Shower added to database", Toast.LENGTH_SHORT).show();
                });
            } else {
                // If the meteor shower already exists, show a toast message
                runOnUiThread(() -> {
                    Toast.makeText(this, "Meteor Shower already exists in the database", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
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