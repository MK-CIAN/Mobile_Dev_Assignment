package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finding buttons by their IDs
        TextView title = findViewById(R.id.textView);
        Button apodButton = findViewById(R.id.apodButton);
        Button eventsButton = findViewById(R.id.eventsButton);
        Button userSavesButton = findViewById(R.id.userSaves);
        Button mapButton = findViewById(R.id.mapButton);

        // Setting trans values
        title.setTranslationX(-1500f);
        apodButton.setTranslationX(-1500f);
        eventsButton.setTranslationX(-1500f);
        userSavesButton.setTranslationX(-1500f);
        mapButton.setTranslationX(-1550f);

        // Animate the buttons to slide in
        animateButtonIn(title, 0);
        animateButtonIn(apodButton, 300);
        animateButtonIn(eventsButton, 400);
        animateButtonIn(userSavesButton, 500);
        animateButtonIn(mapButton, 600);
    }

    private void animateButtonIn(View view, long delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0);
        animator.setDuration(1000); // Duration of the animations
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Set the interpolation
        animator.setStartDelay(delay); // Set a delay for each button
        animator.start(); // Start the animation
    }

    //Starting Astronomy Picture of the Day Activity
    public void openApodActivity(View view) {
        Intent intent = new Intent(this, ApodActivity.class);
        startActivity(intent);
    }

    //Starting Meteor Shower List Activity
    public void openEventsList(View view) {
        Intent intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
    }

    //Starting User Saved Events Activity
    public void openUserSaves(View view) {
        Intent intent = new Intent(this, UserSavesActivity.class);
        startActivity(intent);
    }

    //Starting Dark Sky Finder Activity
    public void openMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}