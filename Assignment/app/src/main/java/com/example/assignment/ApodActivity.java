package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApodActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView explanationTextView;
    private TextView dateTextView;
    private TextView imageUrlTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);

        //Creating the UI Elements
        titleTextView = findViewById(R.id.titleTextView);
        explanationTextView = findViewById(R.id.explanationtextView);
        dateTextView = findViewById(R.id.dateTextView);
        imageUrlTextView = findViewById(R.id.imageUrlTextView);

        //Fetching the data for apod
        fetchApodData();
    }
    private void fetchApodData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}