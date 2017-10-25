package com.tasree7a.interfaces;

import com.tasree7a.Models.FavoriteModels.FavoriteResponseModel;
import com.tasree7a.Models.Login.LoginResponseModel;
import com.tasree7a.Models.PopularSalons.PopularSalonsResponseModel;
import com.tasree7a.Models.SalonBooking.AvailableTimesResponse;
import com.tasree7a.Models.SalonBooking.SalonServicesResponse;
import com.tasree7a.Models.SalonDetails.AddNewSalonResponseModel;
import com.tasree7a.Models.SalonDetails.SalonDetailsResponseModel;
import com.tasree7a.Models.Signup.SignupResponseModel;
import com.tasree7a.Models.UpdateGalleryResponseModel;
import com.tasree7a.Models.UserBookingsResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

/**
 * Created by mac on 5/17/17.
 */

public interface ServiceRequest {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> login(@Field("username") String username, @Field("password") String password, @Field("fb_flag") int isFBLogin, @Field("isBusiness") int isBusiness);


    @FormUrlEncoded
    @POST("register")
    Call<SignupResponseModel> register(@Field("first_name") String firstName,
                                       @Field("last_name") String lastName,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("username") String userName,
                                       @Field("fb_flag") int fbFlag,
                                       @Field("isBusiness") int isBuisness,
                                       @Field("fb_id") String facebookId);


    @FormUrlEncoded
    @POST("getUserFavoriteSalons")
    Call<FavoriteResponseModel> getUserFavorites(@Field("userId") String userName);


    @FormUrlEncoded
    @POST("updateSalonImage")
    Call<UpdateGalleryResponseModel> updateSalonImages(@Field("operation") String operation,
                                                       @Field("salonId") String salonId,
                                                       @Field("salonImage") String base64Image,
                                                       @Field("imageId") String imageId);


    @FormUrlEncoded
    @POST("addSalonService")
    Call<Object> addSalonService(@Field("serviceName") String serviceName,
                                 @Field("servicePrice") String servicePrice,
                                 @Field("salonId") String salonId,
                                 @Field("serviceImage") String serviceImage);


    @FormUrlEncoded
    @POST("resetPassword")
    Call<Object> resetPassword(@Field("email") String email);


    @FormUrlEncoded
    @POST("addUserBooking")
    Call<Object> addUserBooking(@Field("barberId") String barberId,
                                @Field("salonId") String salonId,
                                @Field("services") int[] services,
                                @Field("userId") String userId,
                                @Field("book_date") String date,
                                @Field("book_time") String time);


    @FormUrlEncoded
    @POST("getUserBookings")
    Call<UserBookingsResponse> getUserBookings(@Field("userId") String userId);


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


    @FormUrlEncoded
    @POST("getSalonServices")
    Call<SalonServicesResponse> getSalonServices(@Field("salonId") String salonId);


    @FormUrlEncoded
    @POST("getAvailableBookingTime")
    Call<AvailableTimesResponse> getAvailableTime(@Field("date") String date, @Field("salonId") String salonId);


    @FormUrlEncoded
    @POST("addNewBarber")
    Call<Object> addNewBarber(@Field("salonId") String salonId,
                              @Field("lastName") String lastName,
                              @Field("email") String email,
                              @Field("username") String userName,
                              @Field("firstName") String firstName,
                              @Field("created_at") String createdAt,
                              @Field("password") String pass,
                              @Field("updated_at") String updatedAt,
                              @Field("startTime") String startTime,
                              @Field("endTime") String endTime);


    @FormUrlEncoded
    @POST("updateSalonProduct")
    Call<Object> updateSalonProduct(@Field("operation") String op,
                                    @Field("productName") String productName,
                                    @Field("productDescription") String prodDesc,
                                    @Field("productPrice") String price,
                                    @Field("productImage") String image,
                                    @Field("salonId") String id,
                                    @Field("productId") String prodId);


    @FormUrlEncoded
    @POST("addNewSalon")
    Call<AddNewSalonResponseModel> addNewSalonInformation(@Field("userId") String userID,
                                                          @Field("cityId") String cityId,
                                                          @Field("salonType") String salonType,
                                                          @Field("salonImageBase64") String salonImageBase64,
                                                          @Field("ownerName") String ownerName,
                                                          @Field("ownerMobile") String ownerMobile,
                                                          @Field("salonLat") String salonLat,
                                                          @Field("salonLong") String salonLong,
                                                          @Field("salonName") String salonName);
}