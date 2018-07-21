package com.tasree7a.models.salonbooking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Constants;

import java.io.Serializable;

/**
 * Created by mac on 8/27/17.
 */

public class SalonService implements Serializable, Parcelable {

    String id;

    @SerializedName("picture")
    String imageUrl;

    String name;

    double price;

    boolean mIsSelected;

    protected SalonService(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        price = in.readDouble();
        mIsSelected = in.readByte() != 0;
    }

    public static final Creator<SalonService> CREATOR = new Creator<SalonService>() {
        @Override
        public SalonService createFromParcel(Parcel in) {
            return new SalonService(in);
        }

        @Override
        public SalonService[] newArray(int size) {
            return new SalonService[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        return this.getId().equals(((SalonService) obj).getId());
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {

//        return Constants.IMAGE_PREFIX + imageUrl;
        return imageUrl;

    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeByte((byte) (mIsSelected ? 1 : 0));
    }
}
