package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids){
                try {
                    URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null)
            }
        }

    }
}
