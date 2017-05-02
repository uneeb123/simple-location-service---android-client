package com.example.uneeb.simplemap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.*;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lng;
    private double lat;
    MarkerOptions marker;
    Marker m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        marker = new MarkerOptions().position(new LatLng(0.0,0.0));
        m = mMap.addMarker(marker);
        try {
            getLocations();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getLocations() throws JSONException {
        Client.get("location", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray locations) {
                JSONObject firstPos = null;
                lng = 0.0;
                lat = 0.0;
                try {
                    firstPos = locations.getJSONObject(0);
                    lng = firstPos.getDouble("longitude");
                    lat = firstPos.getDouble("latitude");
                    LatLng pos = new LatLng(lat,lng);
                    m.setPosition(pos);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, (float)10.0));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
