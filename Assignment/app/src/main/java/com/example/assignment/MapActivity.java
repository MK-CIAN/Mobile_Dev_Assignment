package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    private DarkSkyDatabase darkSkyDatabase;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    private void placeMarkers(List<DarkSkyReserve> darkSkyReserves){

        if (myMap == null) {
            return;
        }
        for (DarkSkyReserve reserve : darkSkyReserves) {
            LatLng reserveLocation = new LatLng(reserve.getLatitude(), reserve.getLongitude());
            Log.d("Marker_Debug", "Adding marker at Lat: " + reserve.getLatitude() + ", Lng: " + reserve.getLongitude());
            int markerColor = getColorFromString(reserve.getColor());

            myMap.addMarker(new MarkerOptions()
                    .position(reserveLocation)
                    .title(reserve.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
        }
    }

    private int getColorFromString(String colorString) {
        try {
            // Check if the colorString starts with '#' and has a valid length
            if (colorString.startsWith("#") && (colorString.length() == 7 || colorString.length() == 9)) {
                // Parse the colorString as a hexadecimal string
                int color = (int) Long.parseLong(colorString.substring(1), 16);

                // If the colorString has alpha, remove it
                if (colorString.length() == 9) {
                    color &= 0x00FFFFFF;
                }

                // Convert the color to a hue value in the range [0.0, 360.0)
                return (int) ((color % 0x01000000) / 16777215.0 * 360.0);
            } else {
                // Return a default hue if the colorString is not in the expected format
                return 0;
            }
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails
            e.printStackTrace();
            return 0;
        }
    }


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

    private class LoadDarkSkyReservesTask extends AsyncTask<Void, Void, List<DarkSkyReserve>> {
        @Override
        protected List<DarkSkyReserve> doInBackground(Void... voids) {
            Log.d("AsyncTask_Debug", "doInBackground: Executing");
            List<DarkSkyReserve> darkSkyReserves = getDarkSkyReservesFromDatabase();
            Log.d("AsyncTask_Debug", "doInBackground: Completed, Loaded " + darkSkyReserves.size() + " Dark Sky Reserves");
            if (darkSkyReserves.isEmpty()) {
                insertDarkSkyReservesFromCSV();
                darkSkyReserves = getDarkSkyReservesFromDatabase();
            }
            return darkSkyReserves;
        }

        @Override
        protected void onPostExecute(List<DarkSkyReserve> darkSkyReserves) {
            super.onPostExecute(darkSkyReserves);

            Log.d("AsyncTask_Debug", "Dark Sky Reserves loaded: " + darkSkyReserves.size());
            placeMarkers(darkSkyReserves);


            if (darkSkyDatabase != null && darkSkyDatabase.isOpen()) {
                darkSkyDatabase.close();
            }
        }
    }

    private List<DarkSkyReserve> getDarkSkyReservesFromDatabase(){
        DarkSkyDatabase darkSkyDatabase = Room.databaseBuilder(getApplicationContext(),
                DarkSkyDatabase.class, "darksky-database").build();

        return darkSkyDatabase.darkSkyReserveDao().getALL();
    }

    private void insertDarkSkyReservesFromCSV() {
        Log.d("CSV_DEBUG", "Inserting Dark Sky Reserves from CSV");
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

                        // Log the values for debugging
                        Log.d("CSV_DEBUG", "Name: " + name + ", Color: " + color +
                                ", Latitude: " + latitude + ", Longitude: " + longitude);

                        DarkSkyReserve reserve = new DarkSkyReserve();
                        reserve.setName(name);
                        reserve.setColor(color);
                        reserve.setLatitude(latitude);
                        reserve.setLongitude(longitude);

                        // Insert the data into the database
                        darkSkyDatabase.darkSkyReserveDao().insert(reserve);
                    }
                } catch (NumberFormatException e) {
                    // Log the exception
                    Log.e("CSV_DEBUG", "Error parsing CSV line: " + line, e);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}