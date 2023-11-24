package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class UserSavesActivity extends AppCompatActivity {
    private List<MeteorShowers> savedEventsList;
    private MeteorShowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_saves);

        MeteorShowersDatabase db = Room.databaseBuilder(getApplicationContext(),
                MeteorShowersDatabase.class, "meteor-showers-db").build();

        retrieveSavedEventsFromRoom(db);

        ListView savedEventsListView = findViewById(R.id.savedEventsListView);
        adapter = new MeteorShowersAdapter(this, savedEventsList);
        savedEventsListView.setAdapter(adapter);

        if (savedEventsListView != null && !savedEventsList.isEmpty()) {
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No saved events available", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveSavedEventsFromRoom(MeteorShowersDatabase db) {
        new Thread(() -> {
            savedEventsList = db.meteorShowersDao().getAllMeteorShowers();
            runOnUiThread(() -> {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            });
        }).start();
    }
}