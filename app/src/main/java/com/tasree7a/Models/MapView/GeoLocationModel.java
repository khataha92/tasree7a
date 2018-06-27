package com.tasree7a.models.mapview;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by MohammadTaweel on 1/20/16.
 */

public class GeoLocationModel implements Serializable{

    private static final String TAG = GeoLocationModel.class.getSimpleName();

    private String lat;

    private String lng;


    public GeoLocationModel() {
    }

    public GeoLocationModel(String lat, String lng) {

        this.lat = lat;

        this.lng = lng;
    }

    public GeoLocationModel(double lat, double lng) {

        this.lat = String.valueOf(lat);

        this.lng = String.valueOf(lng);
    }


    public Double getLat() {

        Double res = 0.0;

        try {
            res = Double.parseDouble(lat);
        }catch (Throwable t){
            Log.e(TAG, "converting lat to double",t);
        }

        return res;
    }

    public void setLat(Double lat) {
        if(lat != null) this.lat = String.valueOf(lat);
    }

    public Double getLng() {
        Double res = 0.0;

        try {
            res = Double.parseDouble(lng);
        }catch (Throwable t){
            Log.e(TAG, "converting lng to double: "+lng,t);
        }

        return res;
    }

    public double distanceTo(GeoLocationModel locationModel){

        double distance = 0;

        if (locationModel != null) {

            return distance = distance(getLat(),getLng(),locationModel.getLat(),locationModel.getLng())/1000;

        }

        return 0;

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {

        float[] distanceArr = new float[1];

        Location.distanceBetween(lat1, lon1, lat2,lon2, distanceArr);

        return distanceArr[0];

    }


    public LatLng getLatLng() {

        return new LatLng(getLat(), getLng());

    }


    public void setLng(Double lng) {

        if(lng != null) this.lng = String.valueOf(lng);

    }
}
