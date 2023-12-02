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

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        new LoadDarkSkyReservesTask().execute();
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
        for (DarkSkyReserve reserve : darkSkyReserves) {
            LatLng reserveLocation = new LatLng(reserve.getLatitude(), reserve.getLongitude());

            int markerColor = getColorFromString(reserve.getColor());

            myMap.addMarker(new MarkerOptions()
                    .position(reserveLocation)
                    .title(reserve.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
        }
    }

    private int getColorFromString(String colorString) {
        return Color.parseColor(colorString);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

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
        }
    }

    private List<DarkSkyReserve> getDarkSkyReservesFromDatabase(){
        DarkSkyDatabase skydb = Room.databaseBuilder(getApplicationContext(),
                DarkSkyDatabase.class, "darksky-database").build();

        return skydb.darkSkyReserveDao().getALL();
    }

    private void insertDarkSkyReservesFromCSV() {
        try {

        }
    }
}