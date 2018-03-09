package ru.samara.mapapp;

import com.google.android.gms.maps.model.LatLng;

 public interface MapListener {
    void openLocation(LatLng latLng);

    void addMarker(LatLng latLng);
}
