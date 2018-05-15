package com.rahulsamples.googleMap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.rahulsamples.R;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LocationModel> locationModelList;
    private Button btn_marker_cluster, btn_multiple_marker, btn_heatMap;
    private ClusterManager<LocationModel> manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        prepareLocationList();

    }



    private void prepareLocationList() {
        locationModelList=new ArrayList<>();

        locationModelList.add(new LocationModel("Delhi", "Capital",new LatLng(28.704059,77.102490)));
        locationModelList.add(new LocationModel("Gurugram", "Haryana Border",new LatLng(28.459497,77.026638)));
        locationModelList.add(new LocationModel("Faridabaad", "A part of Haryana",new LatLng(28.408912,77.317789)));
        locationModelList.add(new LocationModel("Noida", "Up Border",new LatLng(28.535516,77.391026)));
        locationModelList.add(new LocationModel("Gaziabaad", "Up Border 2",new LatLng(28.669156,77.453758)));
        locationModelList.add(new LocationModel("Rohtak", "Haryana Part",new LatLng(28.895515,76.606611)));
        locationModelList.add(new LocationModel("Rohtak", "Haryana Part",new LatLng(28.895515,76.606611)));
        locationModelList.add(new LocationModel("Hardoi", "HomeTown",new LatLng(27.398635,80.131693)));
        locationModelList.add(new LocationModel("Lucknow", "UP Capital",new LatLng(26.846694,80.946166)));

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

        showMultipleMarker(btn_multiple_marker);
    }


    public void showMultipleMarker(View view) {

        for (int i = 0; i < locationModelList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locationModelList.get(i).latlng)
                    .title(locationModelList.get(i).name)
                    .snippet(locationModelList.get(i).snippet))
                    .showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationModelList.get(0).latlng));
    }

    public void showMarkerCluster(View view) {

         manager=new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(manager);
        mMap.setOnMarkerClickListener(manager);
        addItems();

    }

    private void addItems() {
        for (int i = 0; i < locationModelList.size(); i++) {
            manager.addItem(locationModelList.get(i));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationModelList.get(0).latlng, 10));

    }

    public void showHeatMap(View view) {


    }
}
