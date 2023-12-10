package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

//Map functionality referenced from Google Maps SDK for android Documentation
//https://developers.google.com/maps/documentation/android-sdk/map
//Location gathering is derived off of Lab 9
//Room database derived off of Lab 6 Android Room Database in Java
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    private DarkSkyDatabase darkSkyDatabase;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize UI elements and request location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // Set up back button functionality
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, MainActivity.class));
            finish();
        });
    }

    // Retrieve the last known location
    private void getLastLocation() {
        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        // Retrieve last location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapActivity.this);
            }
        });
    }

    // Place markers on the map for Dark Sky Reserves
    private void placeMarkers(List<DarkSkyReserve> darkSkyReserves) {
        if (myMap == null || darkSkyReserves == null) {
            return;
        }
        for (DarkSkyReserve reserve : darkSkyReserves) {
            LatLng reserveLocation = new LatLng(reserve.getLatitude(), reserve.getLongitude());
            int markerColor = Color.parseColor(reserve.getColor());
            myMap.addMarker(new MarkerOptions()
                    .position(reserveLocation)
                    .title(reserve.getName())
                    .icon(getColoredMarker(markerColor)));
        }
    }

    // Generate colored marker icon
    private BitmapDescriptor getColoredMarker(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        Bitmap bitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.HSVToColor(hsv));
        canvas.drawCircle(24, 24, 24, paint);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Get color hue from a string representing a color
    private int getColorFromString(String colorString) {
        try {
            if (colorString.startsWith("#") && (colorString.length() == 7 || colorString.length() == 9)) {
                int color = (int) Long.parseLong(colorString.substring(1), 16);
                if (colorString.length() == 9) {
                    color &= 0x00FFFFFF;
                }
                return (int) ((color % 0x01000000) / 16777215.0 * 360.0);
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Callback when the map is ready
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        new LoadDarkSkyReservesTask().execute();
        myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng loc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        myMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        myMap.getUiSettings().setZoomControlsEnabled(true);
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location Permission Denied, Please Allow to Access Map", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // AsyncTask to load Dark Sky Reserves data
    private class LoadDarkSkyReservesTask extends AsyncTask<Void, Void, List<DarkSkyReserve>> {
        @Override
        protected List<DarkSkyReserve> doInBackground(Void... voids) {
            // Initialize the database if null and load Dark Sky Reserves
            if (darkSkyDatabase == null) {
                darkSkyDatabase = Room.databaseBuilder(getApplicationContext(),
                        DarkSkyDatabase.class, "darksky-database").build();
            }

            List<DarkSkyReserve> darkSkyReserves = getDarkSkyReservesFromDatabase();
            if (darkSkyReserves.isEmpty()) {
                insertDarkSkyReservesFromCSV();
                darkSkyReserves = getDarkSkyReservesFromDatabase();
            }
            return darkSkyReserves;
        }

        @Override
        protected void onPostExecute(List<DarkSkyReserve> darkSkyReserves) {
            super.onPostExecute(darkSkyReserves);
            placeMarkers(darkSkyReserves);
            if (darkSkyDatabase != null && darkSkyDatabase.isOpen()) {
                darkSkyDatabase.close();
            }
        }
    }

    // Retrieve Dark Sky Reserves from the database
    private List<DarkSkyReserve> getDarkSkyReservesFromDatabase() {
        DarkSkyDatabase darkSkyDatabase = Room.databaseBuilder(getApplicationContext(),
                DarkSkyDatabase.class, "darksky-database").build();
        return darkSkyDatabase.darkSkyReserveDao().getALL();
    }

    // Insert Dark Sky Reserves from CSV file into the database
    private void insertDarkSkyReservesFromCSV() {
        try {
            InputStream inputStream = getAssets().open("DarkSkyReserve.csv");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    String[] values = line.split(",");
                    if (values.length == 4) {
                        String name = values[0].trim();
                        String color = values[1].trim();
                        double latitude = Double.parseDouble(values[2].trim());
                        double longitude = Double.parseDouble(values[3].trim());

                        //Inserting into db
                        DarkSkyReserve reserve = new DarkSkyReserve();
                        reserve.setName(name);
                        reserve.setColor(color);
                        reserve.setLatitude(latitude);
                        reserve.setLongitude(longitude);
                        darkSkyDatabase.darkSkyReserveDao().insert(reserve);
                    }
                } catch (NumberFormatException e) {
                    Log.e("CSV_DEBUG", "Error parsing CSV line: " + line, e);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
