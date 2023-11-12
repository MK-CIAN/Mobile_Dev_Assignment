package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.squareup.picasso.Picasso;

public class ApodActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView explanationTextView;
    private TextView dateTextView;
    private TextView imageUrlTextView;

    private ImageView apodImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);

        //Creating the UI Elements
        titleTextView = findViewById(R.id.titleTextView);
        explanationTextView = findViewById(R.id.explanationTextView);
        dateTextView = findViewById(R.id.dateTextView);
        imageUrlTextView = findViewById(R.id.imageUrlTextView);
        apodImageView = findViewById(R.id.apodImageView);

        fetchApodData();
    }

    private void fetchApodData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApodApi apodApi = retrofit.create(ApodApi.class);

        Call<ApodResponse> call = apodApi.getApod("DEMO_KEY");

        call.enqueue(new Callback<ApodResponse>() {
            @Override
            public void onResponse(Call<ApodResponse> call, Response<ApodResponse> response) {
                if (response.isSuccessful()) {
                    ApodResponse apodData = response.body();
                    String imageString = apodData.getUrl();
                    if (imageString != null) {
                        Log.d("Message", "Image URL: " + imageString);

                        titleTextView.setText(apodData.getTitle());
                        explanationTextView.setText(apodData.getExplanation());
                        dateTextView.setText(apodData.getDate());
                        imageUrlTextView.setText(apodData.getUrl());

                        Picasso.get().load(imageString).into(apodImageView);
                    } else {
                        Log.d("Message", "Image URL is null");
                        titleTextView.setText("Error loading APOD");
                        explanationTextView.setText("Error Occurred While Fetching Data.");
                    }
                } else {
                    titleTextView.setText("Error loading APOD");
                    explanationTextView.setText("Error Occurred While Fetching Data.");
                }
            }

            @Override
            public void onFailure(Call<ApodResponse> call, Throwable t) {
                titleTextView.setText("Failed to connect to APOD service");
                explanationTextView.setText("Please check your internet connection.");
            }
        });
    }
}
