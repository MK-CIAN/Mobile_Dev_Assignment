package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openApodActivity(View view) {
        Intent intent = new Intent(this, ApodActivity.class);
        startActivity(intent);
    }

    public void openEventsList(View view) {
        Intent intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
    }

    public void openUserSaves(View view) {
        Intent intent = new Intent(this, UserSavesActivity.class);
        startActivity(intent);
    }

}