package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
                if (result != null) {
                    try {
                        String imageString = extractValueFromKey(result, "url");
                        if(imageString != null) {
                                Log.d("Message", "Image URL: " + imageString);

                                titleTextView.setText(extractValueFromKey(result, "title"));
                                dateTextView.setText(extractValueFromKey(result, "date"));
                                new DownloadImageTask().execute(imageString);
                                explanationTextView.setText(extractValueFromKey(result, "explanation"));
                                imageUrlTextView.setText(imageString);
                        }
                        else {
                            Log.d("Message", "Image URL is null");
                            titleTextView.setText("Error loading APOD");
                            explanationTextView.setText("Error Occured While Fetching Data");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    titleTextView.setText("Error loading APOD");
                    explanationTextView.setText("Error Occured While Fetching Data");
                }
            }
        }.execute();
    }

    private String extractValueFromKey(String jsonString, String key) {
        //Manually parsing json
        try{
            int index = jsonString.indexOf("\"" + key + "\"");
            if (index != -1) {
                int startIndex = jsonString.indexOf("\"", index + key.length() + 2) + 1;
                int endIndex = jsonString.indexOf("\"", startIndex + 1);
                return jsonString.substring(startIndex, endIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                apodImageView.setImageBitmap(result);
            }
        }
    }
}
