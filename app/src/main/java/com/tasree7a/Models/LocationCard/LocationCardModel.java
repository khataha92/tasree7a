package com.tasree7a.Models.LocationCard;


import com.tasree7a.Models.PopularSalons.SalonModel;

/**
 * Created by mac on 5/10/16.
 */
public class LocationCardModel {

    double latitude,longitude;

    String address,distanceFromAttraction;

    private boolean hasIndicator;

    public void setHasIndicator(boolean hasIndicator) {
        this.hasIndicator = hasIndicator;
    }

    public boolean isHasIndicator() {
        return hasIndicator;
    }

    public boolean hasIndicator() {
        return hasIndicator;
    }

    private SalonModel salonModel;

    // Clone hotel so it will not hold
    public void setSalonModel(SalonModel salonModel) {
       this.salonModel = salonModel;
    }

    public SalonModel getSalonModel() {
        return salonModel;
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof LocationCardModel){

            LocationCardModel cardModel = (LocationCardModel)o;

            if(cardModel.latitude == latitude){

                if(cardModel.longitude == longitude){

                    if(cardModel.address != null && address != null && cardModel.address.equalsIgnoreCase( address)){


                        if(cardModel.distanceFromAttraction !=null && distanceFromAttraction !=null)

                        if(cardModel.distanceFromAttraction.equalsIgnoreCase(distanceFromAttraction)){

                            return true;

                        }

                    }

                }

            }

        }
        return false;
    }

    public void setDistanceFromAttraction(String distanceFromAttraction) {
        this.distanceFromAttraction = distanceFromAttraction;
    }

    public String getDistanceFromAttraction() {
        return distanceFromAttraction;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
