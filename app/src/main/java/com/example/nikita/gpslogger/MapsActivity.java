package com.example.nikita.gpslogger;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String[] markerLocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        markerLocations = intent.getStringExtra("markerLocations").split("\n");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        System.out.println("length of locations received: " + (markerLocations.length - 1));
        for(int i = 1; i < markerLocations.length; i++) {
            String[] temp = markerLocations[i].split(",");
            Double _lat = Double.parseDouble(temp[0]);
            Double _long = Double.parseDouble(temp[1]);
            System.out.println("lat: " + _lat + " long: " + _long);
            MarkerOptions marker = new MarkerOptions();
            LatLng latLng = new LatLng(_lat, _long);
            marker.position(latLng);
            marker.title("Hello there");
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }
}
