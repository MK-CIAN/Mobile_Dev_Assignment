package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

//Room database derived off of Lab 6 Android Room Database in Java
public class UserSavesActivity extends AppCompatActivity {
    private List<MeteorShowers> savedEventsList = new ArrayList<>();
    private MeteorShowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_saves);

        // Initialize Room database
        MeteorShowersDatabase db = Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build();

        // Load saved events from Room
        retrieveSavedEventsFromRoom(db);

        // Set up back button functionality
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(UserSavesActivity.this, MainActivity.class));
            finish();
        });
    }

    // Remove selected meteor shower from the Room database
    private void removeMeteorShowerFromDatabase(MeteorShowers selectedMeteorShower) {
        MeteorShowersDatabase db = Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build();

        new Thread(() -> {
            db.meteorShowersDao().delete(selectedMeteorShower);
            runOnUiThread(() -> {
                Toast.makeText(this, "Meteor Shower Removed", Toast.LENGTH_SHORT).show();
                // Update adapter dataset and notify change
                savedEventsList.remove(selectedMeteorShower);
                if (adapter != null) adapter.notifyDataSetChanged();
            });
        }).start();
    }

    // Retrieve saved events from Room database
    private void retrieveSavedEventsFromRoom(MeteorShowersDatabase db) {
        new Thread(() -> {
            savedEventsList = db.meteorShowersDao().getAllMeteorShowers();
            runOnUiThread(() -> {
                if (savedEventsList != null && !savedEventsList.isEmpty()) {
                    // Set up ListView and adapter
                    ListView savedEventsListView = findViewById(R.id.savedEventsListView);
                    adapter = new MeteorShowersAdapter(this, savedEventsList);
                    savedEventsListView.setAdapter(adapter);

                    // Set click listener for each item in the ListView
                    savedEventsListView.setOnItemClickListener((parent, view, position, id) ->
                            removeMeteorShowerFromDatabase(savedEventsList.get(position)));

                    savedEventsListView.setTranslationX(-1500f);
                    animateObjectIn(savedEventsListView, 150);

                    adapter.notifyDataSetChanged();
                } else {
                    // Handle case when savedEventsList is null or empty
                    Toast.makeText(this, "No saved events", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    // Animate translation of a view
    private void animateObjectIn(View view, long delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(delay);
        animator.start();
    }

    // Refresh UI after removal
    private void refreshUI() {
        savedEventsList.clear();
        retrieveSavedEventsFromRoom(Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build());
    }
}
