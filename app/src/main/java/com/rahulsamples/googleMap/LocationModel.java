package com.rahulsamples.googleMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Admin on 5/15/2018.
 */

public class LocationModel implements ClusterItem {

    String name;
    String snippet;
    LatLng latlng;

    public LocationModel(String name, String snippet, LatLng latlng) {
        this.name = name;
        this.snippet = snippet;
        this.latlng = latlng;
    }

    @Override
    public LatLng getPosition() {
        return latlng;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
