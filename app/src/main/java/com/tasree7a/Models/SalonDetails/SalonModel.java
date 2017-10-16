package com.tasree7a.Models.SalonDetails;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.tasree7a.Constants;
import com.tasree7a.Enums.FilterType;
import com.tasree7a.Enums.Gender;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.Models.MapView.GeoLocationModel;
import com.tasree7a.Models.PopularSalons.CityModel;
import com.tasree7a.Models.PopularSalons.RankModel;
import com.tasree7a.Models.SalonBooking.SalonService;
import com.tasree7a.Models.SalonDetails.SalonProduct;
import com.tasree7a.interfaces.Filterable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 7/4/17.
 */

public class
SalonModel implements Filterable, ClusterItem {

    @SerializedName("rank")
    RankModel rank;

    @SerializedName("city")
    CityModel salonCity;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("latitude")
    double lat;

    @SerializedName("longitude")
    double lng;

    @SerializedName("owner_name")
    String ownerName;

    @SerializedName("owner_mobile")
    String ownerMobileNumber;

    @SerializedName("distance")
    double distance;

    @SerializedName("salon_img")
    String image;

    @SerializedName("salon_barbers")
    List<SalonBarber> salonBarbers;

    @SerializedName("salon_images")
    List<ImageModel> gallery;

    @SerializedName("products")
    List<SalonProduct> products;

    @SerializedName("salon_type")
    Gender salonType;

    GeoLocationModel locationModel;

    @SerializedName("salon_services")
    List<SalonService> salonServices;

    boolean isBusiness = false;

    public List<SalonService> getSalonServices() {

        return salonServices;
    }

    public List<SalonBarber> getSalonBarbers() {
        return salonBarbers;
    }

    public String getImage() {
        return Constants.IMAGE_PREFIX+image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerMobileNumber() {
        return ownerMobileNumber;
    }

    public void setOwnerMobileNumber(String ownerMobileNumber) {
        this.ownerMobileNumber = ownerMobileNumber;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getSalonCity() {
        return salonCity.getName();
    }

    public CityModel getCity(){

        return salonCity;
    }

    public CityModel getCityModel(){

        return salonCity;
    }

    public List<ImageModel> getProductsImages(){

        List<ImageModel> productList =  new ArrayList<>();

        for(int i = 0 ; i < products.size() ; i ++){

            ImageModel imageModel = new ImageModel();

            imageModel.setImagePath(Constants.IMAGE_PREFIX + products.get(i).getUrl());

            productList.add(imageModel);

        }

        return productList;

    }

    public List<SalonProduct> getProducts() {

        return products;

    }

    public void setProducts(List<SalonProduct> products) {
        this.products = products;
    }

    public void setGallery(List<ImageModel> gallery) {
        this.gallery = gallery;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRank(RankModel rank) {
        this.rank = rank;
    }

    public RankModel getRank() {
        return rank;
    }

    public void setSalonCity(CityModel salonCity) {
        this.salonCity = salonCity;
    }

    public void setSalonServices(List<SalonService> salonServices) {
        this.salonServices = salonServices;
    }

    public List<ImageModel> getGallery() {

        if(gallery == null || gallery.isEmpty()){

            gallery = new ArrayList<>();

            int start = Integer.parseInt(id);

            for(int i = (start-1) * 9 ; i < UIUtils.images.length && i < start * 9 ; i++){

                ImageModel imageModel = new ImageModel();

                imageModel.setImagePath(UIUtils.images[i]);

                gallery.add(imageModel);
            }

        } else {

            for (int i = 0 ; i< gallery.size() ;i++){

                if(!gallery.get(i).getImagePath().startsWith("http")) {

                    gallery.get(i).setImagePath(Constants.IMAGE_PREFIX + gallery.get(i).getImagePath());

                }

            }
        }

        return gallery;
    }

    public int getRating() {

        return (int)rank.getRank();

    }

    @Override
    public boolean filterValue(FilterType filterType) {

        switch (filterType){

            case ALL:

                return true;

            case AVAILABLE:

                return true;

            case FAVORITE:

                return UserDefaultUtil.isSalonFavorite(this);

        }

        return false;

    }

    public void setLocationModel(GeoLocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public GeoLocationModel getLocationModel() {
        if(locationModel == null){

            locationModel = new GeoLocationModel();

            locationModel.setLat(lat);

            locationModel.setLng(lng);
        }

        return locationModel;
    }

    @Override
    public LatLng getPosition() {

        LatLng latLng = new LatLng(lat,lng);

        return latLng;
    }

    public boolean isFavorite(){

        return UserDefaultUtil.isSalonFavorite(this);

    }

    public Gender getSalonType() {
        return salonType;
    }

    public void setSalonType(Gender salonType) {
        this.salonType = salonType;
    }

    public void setSalonBarbers(List<SalonBarber> salonBarbers) {
        this.salonBarbers = salonBarbers;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public void setBusiness(boolean business) {
        isBusiness = business;
    }
}
