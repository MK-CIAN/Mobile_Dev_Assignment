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

public class UserSavesActivity extends AppCompatActivity {
    private List<MeteorShowers> savedEventsList = new ArrayList<>();
    private MeteorShowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_saves);

        MeteorShowersDatabase db = Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build();

        retrieveSavedEventsFromRoom(db);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSavesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void removeMeteorShowerFromDatabase(MeteorShowers selectedMeteorShower) {
        MeteorShowersDatabase db = Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build();

        new Thread(() -> {
            db.meteorShowersDao().delete(selectedMeteorShower);
            runOnUiThread(() -> {
                Toast.makeText(this, "Meteor Shower Removed From Database", Toast.LENGTH_SHORT).show();
                // Update the dataset in the adapter and notify the change
                savedEventsList.remove(selectedMeteorShower);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            });
        }).start();
    }

    private void retrieveSavedEventsFromRoom(MeteorShowersDatabase db) {
        new Thread(() -> {
            savedEventsList = db.meteorShowersDao().getAllMeteorShowers();
            runOnUiThread(() -> {
                if (savedEventsList != null && !savedEventsList.isEmpty()) {
                    ListView savedEventsListView = findViewById(R.id.savedEventsListView);
                    adapter = new MeteorShowersAdapter(this, savedEventsList);
                    savedEventsListView.setAdapter(adapter);

                    // Set an onClickListener for each item in the ListView
                    savedEventsListView.setOnItemClickListener((parent, view, position, id) -> {
                        MeteorShowers selectedMeteorShower = savedEventsList.get(position);
                        // Call the method to remove the selected meteor shower
                        removeMeteorShowerFromDatabase(selectedMeteorShower);
                    });

                    savedEventsListView.setTranslationX(-1500f);
                    animateObjectIn(savedEventsListView, 150);

                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case when savedEventsList is null or empty
                    Toast.makeText(this, "No saved events available", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void animateObjectIn(View view, long delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0);
        animator.setDuration(1000); // Set the duration of the animation
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Set the interpolation
        animator.setStartDelay(delay); // Set a delay for each button
        animator.start(); // Start the animation
    }

    // Method to refresh the UI after a removal
    private void refreshUI() {
        savedEventsList.clear(); // Clear the list
        retrieveSavedEventsFromRoom(Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build());
    }
}

