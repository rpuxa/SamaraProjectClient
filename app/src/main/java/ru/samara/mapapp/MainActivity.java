package ru.samara.mapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createMapView();
        addMarker();
    }

    private void createMapView() {
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.mapView)).getMap();
        if (null == googleMap) {
            Toast.makeText(getApplicationContext(),
                    "Error creating map", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMarker() {
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Вот сюда нам нужно")
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ap))

                    );
                }
            });
    }
}

