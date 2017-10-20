package com.tasree7a.Models.Signup;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Models.Login.User;

import java.io.Serializable;

/**
 * Created by SamiKhleaf on 10/20/17.
 */

public class SignUpDetailsModel implements Serializable{

    @SerializedName("userModel")
    User user;

//    Cities

//    SalonTypes


    public User getUser() {

        return user;
    }


    public void setUser(User user) {

        this.user = user;
    }
}
