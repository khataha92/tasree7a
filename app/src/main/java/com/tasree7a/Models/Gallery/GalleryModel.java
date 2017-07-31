package com.tasree7a.Models.Gallery;

import com.tasree7a.Models.SalonDetails.SalonProduct;

import java.util.List;

/**
 * Created by mac on 7/18/17.
 */

public class GalleryModel {

    String title;

    List<ImageModel> imageModelList;

    List<SalonProduct> products;

    public String getTitle() {

        return title;
    }


    public void setTitle(String title) {

        this.title = title;
    }


    public List<ImageModel> getImageModelList() {

        return imageModelList;
    }


    public void setImageModelList(List<ImageModel> imageModelList) {

        this.imageModelList = imageModelList;
    }


    public List<SalonProduct> getProducts() {

        return products;
    }


    public void setProducts(List<SalonProduct> products) {

        this.products = products;
    }
}
