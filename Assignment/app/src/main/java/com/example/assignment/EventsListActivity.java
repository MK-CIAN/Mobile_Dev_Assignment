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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends AppCompatActivity {
    // List to store MeteorShowers data
    private List<MeteorShowers> meteorShowersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        // Back button setup
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(EventsListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Read CSV file and populate meteorShowersList
        readCsvFile(this, "meteorshowertest.csv");

        // Log meteor showers list size and details
        Log.d("ListView", "Meteor Showers List Size: " + meteorShowersList.size());
        for (MeteorShowers meteorShower : meteorShowersList) {
            Log.d("ListView", "Item: " + meteorShower.toString());
        }

        // Set up ListView and adapter
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

        // Set click listener for ListView items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            MeteorShowers selectedMeteorShower = meteorShowersList.get(position);
            // Insert selected meteor shower into the database
            insertMeteorShowerIntoDatabase(selectedMeteorShower);
        });

        // Set initial translation and animate ListView
        listView.setTranslationX(-1500f);
        animateObjectIn(listView, 150);
    }

    // Animate the translation of a view
    private void animateObjectIn(View view, long delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0);
        animator.setDuration(1000); // Set the duration of the animation
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Set the interpolation
        animator.setStartDelay(delay); // Set a delay for each button
        animator.start(); // Start the animation
    }

    // Insert selected meteor shower into the Room database
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

    // Read CSV file and populate meteorShowersList
    private void readCsvFile(Context context, String csvFileName){
        AssetManager assetManager = context.getAssets();
        try {
            // Open and read the CSV file
            InputStream inputStream = assetManager.open(csvFileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // Skip the first line in the CSV, which contains column names
            bufferedReader.readLine();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                // Split CSV line into values and create MeteorShowers object
                String[] values = line.split(",");
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
            // Handle IOException (e.g., file not found)
            e.printStackTrace();
            Log.d("ListView", "Adapter set successfully");
        }
    }
}
