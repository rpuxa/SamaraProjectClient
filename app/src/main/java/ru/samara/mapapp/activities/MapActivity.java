package ru.samara.mapapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
    public static final String MAP_CHOSE_LOCATION = "chose_location";

    public static final int REQUEST_CODE_MAP_CHOSE_LOCATION = 258;


    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(googleMap -> {
            map = googleMap;
            int action = (int) getIntent().getExtras().get(ACTION);
            Object value = getIntent().getExtras().get(VALUE);
            LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.mapButtonsLayout);
            Button accept = (Button) findViewById(R.id.mapAccept);
            if (action == GOTO_LOCATION) {
                buttonLayout.setVisibility(View.GONE);
                LatLng location = (LatLng) value;
                openLocation(location);
                addMarker(location);
            } else if (action == GET_LOCATION) {
                buttonLayout.setVisibility(View.VISIBLE);
                accept.setEnabled(false);
                MarkerOptions marker = new MarkerOptions().draggable(true).title("Выберете место");
                final LatLng[] markerLocation = {null};
                map.setOnMapClickListener(location -> {
                    if (markerLocation[0] == null) {
                        markerLocation[0] = location;
                        googleMap.addMarker(marker);
                        accept.setEnabled(true);
                    }
                    marker.position(location);
                });
                accept.setOnClickListener(view -> {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    intent.putExtra(MAP_CHOSE_LOCATION, markerLocation[0]);
                });
            }
        });
        findViewById(R.id.mapCancel).setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
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

    public static void gotoLocation(Activity activity, LatLng location) {
        Intent intent = new Intent(activity, MapActivity.class);
        intent.putExtra(MapActivity.ACTION, MapActivity.GOTO_LOCATION);
        intent.putExtra(MapActivity.VALUE, location);
        activity.startActivity(intent);
    }

    public static void getLocation(Activity activity) {
        Intent intent = new Intent(activity, MapActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_MAP_CHOSE_LOCATION);
    }
}


