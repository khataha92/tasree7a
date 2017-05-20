package com.tasree7a.interfaces;

import com.tasree7a.Models.Login.LoginModel;
import com.tasree7a.Models.Login.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by mac on 5/17/17.
 */

public interface ServiceRequest {

    @POST("login")
    Call<LoginResponseModel> login(@Field("username") String username,@Field("password") String password, @Field("fb_flag") boolean isFBLogin);
}
