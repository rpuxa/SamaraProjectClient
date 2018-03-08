package ru.samara.mapapp;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map {
    MapFragment fragment;
    GoogleMap map;

    public Map() {

    }

    private void createMapView() {
   //    map = ((MapFragment) getFragmentManager().findFragmentById(
     //           R.id.mapView)).getMap();
    }

    private void addMarker() {
        map.setOnMapClickListener((latLng) -> map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Вот сюда нам нужно")
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sport))
        ));
    }
}
