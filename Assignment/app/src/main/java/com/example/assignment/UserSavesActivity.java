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

    private void retrieveSavedEventsFromRoom(MeteorShowersDatabase db) {
        new Thread(() -> {
            savedEventsList = db.meteorShowersDao().getAllMeteorShowers();
            runOnUiThread(() -> {
                if (savedEventsList != null && !savedEventsList.isEmpty()) {
                    ListView savedEventsListView = findViewById(R.id.savedEventsListView);
                    adapter = new MeteorShowersAdapter(this, savedEventsList);
                    savedEventsListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case when savedEventsList is null or empty
                    Toast.makeText(this, "No saved events available", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}