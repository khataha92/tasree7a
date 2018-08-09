package com.tasree7a.interfaces;

import com.tasree7a.models.UpdateGalleryResponseModel;
import com.tasree7a.models.UserBookingsResponse;
import com.tasree7a.models.favoritemodels.FavoriteResponseModel;
import com.tasree7a.models.login.LoginResponseModel;
import com.tasree7a.models.popularsalons.PopularSalonsResponseModel;
import com.tasree7a.models.salonbooking.AvailableTimesResponse;
import com.tasree7a.models.salonbooking.SalonServicesResponse;
import com.tasree7a.models.salondetails.AddNewSalonResponseModel;
import com.tasree7a.models.salondetails.SalonDetailsResponseModel;
import com.tasree7a.models.signup.SignupResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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


    @Multipart
    @POST("updateSalonImage")
    Call<UpdateGalleryResponseModel> updateSalonImages(@Part("operation") RequestBody operation,
                                                       @Part("salonId") RequestBody salonId,
                                                       @Part("imageId") RequestBody images,
                                                       @Part MultipartBody.Part image);


    @Multipart
    @POST("addSalonService")
    Call<Object> addSalonService(@Part("serviceName") RequestBody serviceName,
                                 @Part("servicePrice") RequestBody servicePrice,
                                 @Part("salonId") RequestBody salonId,
                                 @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("forgetPassword")
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
    @POST("updateBookingStatus")
    Call<Object> updateBookingStatus(@Field("bookId") String bookingID, @Field("bookStatus") String status);

    @FormUrlEncoded
    @POST("getUserBookings")
    Call<UserBookingsResponse> getUserBookings(@Field("userId") String userId, @Field("userType") String userType);


    @FormUrlEncoded
    @POST("changeFavorites")
    Call<SignupResponseModel> changeUserFavorite(@Field("userId") String userName,
                                                 @Field("salonId") String salonId,
                                                 @Field("action") String action);


    @FormUrlEncoded
    @POST("getNearestSalons")
    Call<PopularSalonsResponseModel> getNearestSalons(@Field("currentUserLat") double lat, @Field("currentUserLong") double lng, @Field("pageIndex") int pageIndex);


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


    @Multipart
    @POST("updateSalonProduct")
    Call<Object> updateSalonProduct(@Part("operation") RequestBody op,
                                    @Part("productName") RequestBody productName,
                                    @Part("productDescription") RequestBody prodDesc,
                                    @Part("productPrice") RequestBody price,
                                    @Part("salonId") RequestBody id,
                                    @Part("product_ids_list") RequestBody prodId,
                                    @Part MultipartBody.Part image);
    ;
    @Multipart
    @POST("addNewSalon")
    Call<AddNewSalonResponseModel> addNewSalonInformation(@Part("userId") RequestBody salonId,
                                                          @Part("cityId") RequestBody cityId,
                                                          @Part("salonType") RequestBody salonType,
                                                          @Part("ownerName") RequestBody ownerName,
                                                          @Part("ownerMobile") RequestBody ownerMobile,
                                                          @Part("salonLat") RequestBody salonLat,
                                                          @Part("salonLong") RequestBody salonLong,
                                                          @Part("salonName") RequestBody salonName,
                                                          @Part("start_work_on") RequestBody startWorkAt,
                                                          @Part("end_work_on") RequestBody endWorkAt,
                                                          @Part MultipartBody.Part image);

//		-img_file

    @Multipart
    @POST("updateSalonInformation")
    Call<AddNewSalonResponseModel> updateSalonDetails(@Part("salonId") RequestBody salonId,
                                                      @Part("salonCityId") RequestBody cityId,
                                                      @Part("salonType") RequestBody salonType,
                                                      @Part("ownerName") RequestBody ownerName,
                                                      @Part("ownerMobile") RequestBody ownerMobile,
                                                      @Part("salonLat") RequestBody salonLat,
                                                      @Part("salonLong") RequestBody salonLong,
                                                      @Part("salonName") RequestBody salonName,
                                                      @Part("start_work_on") RequestBody startWorkAt,
                                                      @Part("end_work_on") RequestBody endWorkAt,
                                                      @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("DeleteSalonService")
    Call<Void> deleteSalonService(@Field("salonId") String salonId,
                                  @Field("serviceId") String serviceId);

}