package ru.samara.mapapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.samara.mapapp.R;

public class MapActivity extends AppCompatActivity {
    public static LatLng gotoLocation;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            map = googleMap;
            openLocation();
            addMarker();
        });

    }

    public void openLocation() {
        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(gotoLocation, 10)));
    }

    public void addMarker() {
        map.addMarker(new MarkerOptions()
                .position(gotoLocation)
                .title("Вот сюда нам нужно")
                .draggable(false));
    }
}


