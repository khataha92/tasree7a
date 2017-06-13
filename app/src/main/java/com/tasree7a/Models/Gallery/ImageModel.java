package com.tasree7a.Models.Gallery;

import java.io.Serializable;

/**
 * Created by mac on 6/14/17.
 */

public class ImageModel implements Serializable{

    String imagePath;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}
