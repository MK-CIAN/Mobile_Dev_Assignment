package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
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


    }

    private void removeMeteorShowerFromDatabase(MeteorShowers selectedMeteorShower) {
        MeteorShowersDatabase db = Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build();

        new Thread(() -> {
            db.meteorShowersDao().delete(selectedMeteorShower);
            runOnUiThread(() -> {
                Toast.makeText(this, "Meteor Shower Removed From Database", Toast.LENGTH_SHORT).show();
                refreshUI();
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

                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case when savedEventsList is null or empty
                    Toast.makeText(this, "No saved events available", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    // Method to refresh the UI after a removal
    private void refreshUI() {
        savedEventsList.clear(); // Clear the list
        retrieveSavedEventsFromRoom(Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build());
    }
}

