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

        // Find your buttons by their IDs
        TextView title = findViewById(R.id.textView);
        Button apodButton = findViewById(R.id.apodButton);
        Button eventsButton = findViewById(R.id.eventsButton);
        Button userSavesButton = findViewById(R.id.userSaves);

        // Set initial translation values for buttons (outside the screen)
        title.setTranslationX(-1500f);
        apodButton.setTranslationX(-1500f);
        eventsButton.setTranslationX(-1500f);
        userSavesButton.setTranslationX(-1500f);

        // Animate the buttons to slide in
        animateButtonIn(title, 0);
        animateButtonIn(apodButton, 300);
        animateButtonIn(eventsButton, 400);
        animateButtonIn(userSavesButton, 500);
    }

    private void animateButtonIn(View view, long delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0);
        animator.setDuration(1000); // Set the duration of the animation
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Set the interpolation
        animator.setStartDelay(delay); // Set a delay for each button
        animator.start(); // Start the animation
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

    public void openMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}