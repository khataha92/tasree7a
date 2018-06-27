package com.tasree7a.models.gallery;

import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.models.salondetails.SalonProduct;

import java.util.List;

/**
 * Created by mac on 7/18/17.
 */

public class GalleryModel {

    String title;

    List<ImageModel> imageModelList;

    List<SalonProduct> products;

    SalonModel salonModel;

    int type = 0;// 0-> gallery , 1-> product

    public int getType() {

        return type;
    }


    public void setType(int type) {

        this.type = type;
    }


    public SalonModel getSalonModel() {

        return salonModel;
    }


    public void setSalonModel(SalonModel salonModel) {

        this.salonModel = salonModel;
    }


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
