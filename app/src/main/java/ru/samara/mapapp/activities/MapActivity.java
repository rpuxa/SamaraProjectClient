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
    public static final int GOTO_LOCATION = 0;
    public static final int GET_LOCATION = 1;

    public static final String ACTION = "name";
    public static final String VALUE = "value";

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(googleMap -> {
            map = googleMap;
            int action = (int) getIntent().getExtras().get(ACTION);
            Object value = getIntent().getExtras().get(VALUE);
            if (action == GOTO_LOCATION) {
                LatLng location = (LatLng) value;
                openLocation(location);
                addMarker(location);
            } else if (action == GET_LOCATION) {

            }
        });

    }

    public void openLocation(LatLng location) {
        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(location, 10)));
    }

    public void addMarker(LatLng location) {
        map.addMarker(new MarkerOptions()
                .position(location)
                .title("Вот сюда нам нужно")
                .draggable(false));
    }
}


