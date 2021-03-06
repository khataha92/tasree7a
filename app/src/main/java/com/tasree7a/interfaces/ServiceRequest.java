package com.tasree7a.interfaces;

import com.tasree7a.Models.FavoriteModels.FavoriteResponseModel;
import com.tasree7a.Models.Login.LoginResponseModel;
import com.tasree7a.Models.PopularSalons.PopularSalonsResponseModel;
import com.tasree7a.Models.SalonDetails.SalonDetailsResponseModel;
import com.tasree7a.Models.Signup.SignupResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by mac on 5/17/17.
 */

public interface ServiceRequest {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> login(@Field("username") String username, @Field("password") String password, @Field("fb_flag") boolean isFBLogin, @Field("isBusiness") boolean isBusiness);


    @FormUrlEncoded
    @POST("register")
    Call<SignupResponseModel> register(@Field("first_name") String firstName,
                                       @Field("last_name") String lastName,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("username") String userName,
                                       @Field("fb_flag") int fbFlag,
                                       @Field("isBusiness") boolean isBuisness);


    @FormUrlEncoded
    @POST("getUserFavoriteSalons")
    Call<FavoriteResponseModel> getUserFavorites(@Field("userId") String userName);


    @FormUrlEncoded
    @POST("changeFavorites")
    Call<SignupResponseModel> changeUserFavorite(@Field("userId") String userName,
                                                @Field("salonId") String salonId,
                                                @Field("action") String action);


    @FormUrlEncoded
    @POST("getNearestSalons")
    Call<PopularSalonsResponseModel> getNearestSalons(@Field("currentUserLat") double lat, @Field("currentUserLong") double lng);


    @FormUrlEncoded
    @POST("getSalonDetails")
    Call<SalonDetailsResponseModel> getSalonDetails(@Field("salonID") String salonId);
}
