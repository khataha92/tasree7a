package com.tasree7a.managers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tasree7a.Constants;
import com.tasree7a.enums.BookingStatus;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.interfaces.ServiceRequest;
import com.tasree7a.models.AddNewBarberRequestModel;
import com.tasree7a.models.AddNewServiceRequestModel;
import com.tasree7a.models.ApiError;
import com.tasree7a.models.UpdateGalleryResponseModel;
import com.tasree7a.models.UpdateProductRequestModel;
import com.tasree7a.models.UpdateSalonImagesRequestModel;
import com.tasree7a.models.UserBookingsResponse;
import com.tasree7a.models.favoritemodels.FavoriteResponseModel;
import com.tasree7a.models.login.LoginModel;
import com.tasree7a.models.login.LoginResponseModel;
import com.tasree7a.models.popularsalons.PopularSalonsResponseModel;
import com.tasree7a.models.salonbooking.AvailableTimesResponse;
import com.tasree7a.models.salonbooking.SalonServicesResponse;
import com.tasree7a.models.salondetails.AddNewSalonResponseModel;
import com.tasree7a.models.salondetails.SalonDetailsResponseModel;
import com.tasree7a.models.salondetails.SalonInformationRequestModel;
import com.tasree7a.models.signup.SignupResponseModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Part;

/**
 * Created by mac on 5/17/17.
 */

public class RetrofitManager {

    private static RetrofitManager instance = null;

    private static final String SERVER_URL = Constants.SERVICE_API;

    ServiceRequest request;

    Retrofit retrofit;

    public static RetrofitManager getInstance() {
        if (instance == null) {
            instance = new RetrofitManager();
        }
        return instance;
    }

    private RetrofitManager() {

        instance = this;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build();

        request = retrofit.create(ServiceRequest.class);
    }

    public void addBooking(String barberId, String salonId, int[] services, String userId, String date, String time, final AbstractCallback callback) {

        Call<Object> addBooking = request.addUserBooking(barberId, salonId, services, userId, date, time);

        addBooking.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (callback != null) {

                    callback.onResult(response.isSuccessful() && response.body().toString().indexOf("Booking_Added_Successfully") != -1, response.body());

                }

            }


            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                if (callback != null) {

                    callback.onResult(false, t);

                }

            }
        });

    }


    public void resetPassword(String emailAddress, final AbstractCallback callback) {

        Call<Object> reset = request.resetPassword(emailAddress);

        reset.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful() && response.body() != null) {

                    callback.onResult(true, response.body());

                } else {

                    callback.onResult(false, null);
                }
            }


            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


    public void getSalonDetails(String salonId, final AbstractCallback callback) {
        Call<SalonDetailsResponseModel> call = request.getSalonDetails(salonId);
        call.enqueue(new Callback<SalonDetailsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SalonDetailsResponseModel> call, @NonNull Response<SalonDetailsResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body() != null) {
                        callback.onResult(true, response.body().getSalon());
                    }
                } else {
                    callback.onResult(false, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SalonDetailsResponseModel> call, @NonNull Throwable t) {
                callback.onResult(false, null);
            }
        });
    }

    public void updateSalonProducts(UpdateProductRequestModel model, File selectedFile, final AbstractCallback callback) {

        RequestBody reqFile;
        MultipartBody.Part body = null;
        if (selectedFile != null) {
            reqFile = RequestBody.create(MediaType.parse("image/*"), selectedFile);
            body = MultipartBody.Part.createFormData("img_file", selectedFile.getName(), reqFile);
        }

        StringBuilder ids = new StringBuilder();
        if (model.getProductId() != null && !model.getProductId().isEmpty()) {
            for (String id : model.getProductId()) {
                ids.append(id).append("~");
            }
        }
        /////////

        Call<Object> call = request.updateSalonProduct(RequestBody.create(MediaType.parse("multipart/form-data"), model.getOperation()),
                model.getProductName() != null ? RequestBody.create(MediaType.parse("multipart/form-data"), model.getProductName()) : null,
                model.getProductDescription() != null ? RequestBody.create(MediaType.parse("multipart/form-data"), model.getProductDescription()) : null,
                model.getProductPrice() != null ? RequestBody.create(MediaType.parse("multipart/form-data"), model.getProductPrice()) : null,
                model.getSalonId() != null ? RequestBody.create(MediaType.parse("multipart/form-data"), model.getSalonId()) : null,
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(ids)),
                body);

        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                callback.onResult(true, response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                callback.onResult(false, null);
            }
        });
    }

    public void addNewBarber(AddNewBarberRequestModel model, final AbstractCallback callback) {

        Call<Object> call = request.addNewBarber(model.getSalonId(),
                model.getLastName(),
                model.getEmail(),
                model.getUserName(),
                model.getFirstName(),
                model.getCreatedAt(),
                model.getPass(),
                model.getUpdatedAt(),
                model.getStartTime(),
                model.getEndTime());

        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                callback.onResult(true, response.body());
            }


            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                callback.onResult(false, null);

            }
        });


    }


    public void getUserFavoriteSalons(String userName, final AbstractCallback callback) {

        Call<FavoriteResponseModel> call = request.getUserFavorites(userName);

        call.enqueue(new Callback<FavoriteResponseModel>() {

            @Override
            public void onResponse(Call<FavoriteResponseModel> call, Response<FavoriteResponseModel> response) {

                if (response.isSuccessful() && response.body() != null) {

                    callback.onResult(true, response.body());

                } else {

                    callback.onResult(false, null);

                }
            }


            @Override
            public void onFailure(Call<FavoriteResponseModel> call, Throwable t) {

                callback.onResult(false, null);

            }
        });
    }


    public void getUserBookings(String userId, String userType, final AbstractCallback callback) {

        Call<UserBookingsResponse> getBookings = request.getUserBookings(userId, userType);

        getBookings.enqueue(new Callback<UserBookingsResponse>() {

            @Override
            public void onResponse(Call<UserBookingsResponse> call, Response<UserBookingsResponse> response) {

                if (callback != null) {

                    callback.onResult(response.body() != null, response.body());

                }

            }


            @Override
            public void onFailure(Call<UserBookingsResponse> call, Throwable t) {

                if (callback != null) {

                    callback.onResult(false, null);

                }
            }
        });
    }


    public void changeSalonToUserFavorite(String salonId, String userId, String action, final AbstractCallback callback) {

        Call<SignupResponseModel> call = request.changeUserFavorite(userId, salonId, action);

        call.enqueue(new Callback<SignupResponseModel>() {

            @Override
            public void onResponse(Call<SignupResponseModel> call, Response<SignupResponseModel> response) {

                if (response.isSuccessful() && response.body() != null) {

                    callback.onResult(true, null);

                } else {

                    callback.onResult(false, null);

                }

            }


            @Override
            public void onFailure(Call<SignupResponseModel> call, Throwable t) {

                callback.onResult(false, null);

            }
        });
    }


    public void getNearestSalons(double lat, double lng, int pageIndex, final AbstractCallback callback) {
        Call<PopularSalonsResponseModel> call = request.getNearestSalons(lat, lng, pageIndex);
        call.enqueue(new Callback<PopularSalonsResponseModel>() {
            @Override
            public void onResponse(Call<PopularSalonsResponseModel> call, Response<PopularSalonsResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(true, response.body());
                } else {
                    callback.onResult(false, null);
                }
            }

            @Override
            public void onFailure(Call<PopularSalonsResponseModel> call, Throwable t) {
                callback.onResult(false, null);
            }
        });
    }

    public void register(com.tasree7a.models.signup.SignupModel model, final AbstractCallback callback) {

        Call<SignupResponseModel> call = request.register(model.getFirstName(), model.getLastName(), model.getEmail(), model.getPassword(), model.getUsername(), model.isFbLogin() ? 1 : 0, model.isBuisness() ? 1 : 0, model.getFbId());

        call.enqueue(new Callback<SignupResponseModel>() {

            @Override
            public void onResponse(@NonNull Call<SignupResponseModel> call, @NonNull Response<SignupResponseModel> response) {

                if (response.body() != null && response.code() == 200 && response.body().getResponseCode().equalsIgnoreCase("200")) {
                    callback.onResult(true, response.body());
                } else {
                    if (response.errorBody() != null) {
                        try {
                            callback.onResult(false, new ApiError().setmErrorMessage(response.errorBody().string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponseModel> call, Throwable t) {

                callback.onResult(false, null);

            }
        });
    }


    public void login(LoginModel loginModel, final AbstractCallback callback) {

        Call<LoginResponseModel> call = request.login(loginModel.getUsername(), loginModel.getPassword(), loginModel.isFacebookLogin() ? 1 : 0, loginModel.isBusiness() ? 1 : 0);

        call.enqueue(new Callback<LoginResponseModel>() {

            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {

                try {

                    LoginResponseModel result = response.body();

                    if (result != null) {

                        callback.onResult(true, result);

                    } else {

                        callback.onResult(false, null);

                    }


                } catch (Exception e) {

                    e.printStackTrace();

                    callback.onResult(false, null);
                }

            }


            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

                callback.onResult(false, null);

            }
        });
    }


    public void getSalonServices(String salonId, final AbstractCallback callback) {

        Call<SalonServicesResponse> getServices = request.getSalonServices(salonId);

        getServices.enqueue(new Callback<SalonServicesResponse>() {

            @Override
            public void onResponse(Call<SalonServicesResponse> call, Response<SalonServicesResponse> response) {

                if (callback != null) {

                    callback.onResult(response.isSuccessful(), response.body());
                }

            }


            @Override
            public void onFailure(Call<SalonServicesResponse> call, Throwable t) {

                if (callback != null) {

                    callback.onResult(false, null);

                }

            }
        });
    }


    public void getAvailableTimes(String salonId, String date, final AbstractCallback callback) {

        Call<AvailableTimesResponse> get = request.getAvailableTime(date, salonId);

        get.enqueue(new Callback<AvailableTimesResponse>() {

            @Override
            public void onResponse(Call<AvailableTimesResponse> call, Response<AvailableTimesResponse> response) {

                if (callback != null) {

                    callback.onResult(response.isSuccessful(), response.body());

                }

            }


            @Override
            public void onFailure(Call<AvailableTimesResponse> call, Throwable t) {

                if (callback != null) {

                    callback.onResult(false, null);

                }

            }
        });
    }

    public void updateBookingStatus(String BookingId, BookingStatus bookingStatus, final AbstractCallback callback) {

        Call<Object> call = request.updateBookingStatus(BookingId, bookingStatus.value + "");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(true, response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("FAILED_RESPONSE", t.getMessage());
            }
        });

    }

    public void updateSalonImages(UpdateSalonImagesRequestModel model, File selectedFile, final AbstractCallback callback) {
        RequestBody reqFile = null;
        MultipartBody.Part body = null;
        if (selectedFile != null) {
            reqFile = RequestBody.create(MediaType.parse("image/*"), selectedFile);
            body = MultipartBody.Part.createFormData("img_file", selectedFile.getName(), reqFile);
        }

        ////////////////////////////////////////////////////////////////

//        List<MultipartBody.Part> images = new ArrayList<>();
//        for (String imageId : model.getImageId()) {
//            images.add(RequestBody.create(MediaType.parse("multipart/form-data"), imageId));
//        }
//
//        Map<String, RequestBody> imageIds = new HashMap<>();
//        RequestBody requestFile;
//        for (int i = 0; i < model.getImageId().size(); i++) {
//            requestFile = RequestBody.create(MultipartBody.FORM, model.getImageId().get(i));
//            imageIds.put("imageId[" + i + "]", requestFile);
//        }
        StringBuilder imageIds = null;
        if (model.getImageId() != null && !model.getImageId().isEmpty()) {
            imageIds = new StringBuilder();
            for (String id : model.getImageId()) {
                imageIds.append(id).append("~");
            }
        }
        ////////////////////////////////////////////////////////////////

        Call<UpdateGalleryResponseModel> get = request.updateSalonImages(RequestBody.create(MediaType.parse("multipart/form-data"), model.getOperation()),
                RequestBody.create(MediaType.parse("multipart/form-data"), model.getSalonId()),
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(imageIds)),
                body);

        get.enqueue(new Callback<UpdateGalleryResponseModel>() {

            @Override
            public void onResponse(@NonNull Call<UpdateGalleryResponseModel> call, @NonNull Response<UpdateGalleryResponseModel> response) {
                if (callback != null) {
                    callback.onResult(response.isSuccessful(), response.body());
                }

            }

            @Override
            public void onFailure(@NonNull Call<UpdateGalleryResponseModel> call, @NonNull Throwable t) {
                if (callback != null) {
                    callback.onResult(false, null);
                }
            }
        });
    }


    public void addSalonService(AddNewServiceRequestModel model, File file, final AbstractCallback callback) {

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("img_file", file.getName(), reqFile);

        Call<Object> get = request.addSalonService(RequestBody.create(MediaType.parse("multipart/form-data"), model.getServiceName()),
                RequestBody.create(MediaType.parse("multipart/form-data"), model.getServicePrice()),
                RequestBody.create(MediaType.parse("multipart/form-data"), model.getSalonId()),
                body);

        get.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (callback != null) {

                    callback.onResult(response.isSuccessful(), response.body());

                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                if (callback != null) {

                    callback.onResult(false, null);

                }

            }
        });
    }

    public void updateSalonDetails(SalonInformationRequestModel salonInformationRequestModel, File file, final AbstractCallback abstractCallback) {

        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("img_file", file.getName(), reqFile);

        }
        Call<AddNewSalonResponseModel> updateSalonDetailsCall = request.
                updateSalonDetails(RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonId()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getCityID()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonType()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getOwnerName()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getOwnerMobile()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonLat()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonLong()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonName()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getStartAt()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getCloseAt()),
                        body);

        updateSalonDetailsCall.enqueue(new Callback<AddNewSalonResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AddNewSalonResponseModel> call, @NonNull Response<AddNewSalonResponseModel> response) {
                abstractCallback.onResult(response.isSuccessful(), response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AddNewSalonResponseModel> call, @NonNull Throwable t) {
                abstractCallback.onResult(false, t);
            }
        });
    }

    public void addNewSalon(SalonInformationRequestModel salonInformationRequestModel, File file, final AbstractCallback abstractCallback) {
        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("img_file", file.getName(), reqFile);

        }

        Call<AddNewSalonResponseModel> get = request.addNewSalonInformation(RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonId()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getCityID()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonType()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getOwnerName()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getOwnerMobile()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonLat()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonLong()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getSalonName()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getStartAt()),
                RequestBody.create(MediaType.parse("multipart/form-data"), salonInformationRequestModel.getCloseAt()),
                body);

        get.enqueue(new Callback<AddNewSalonResponseModel>() {

            @Override
            public void onResponse(@NonNull Call<AddNewSalonResponseModel> call, @NonNull Response<AddNewSalonResponseModel> response) {

                abstractCallback.onResult(true, response.body());
            }


            @Override
            public void onFailure(@NonNull Call<AddNewSalonResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    public void deleteSalonService(String salonId, String serviceId, final AbstractCallback callback) {
        Call<Void> deleteSalonService = request.deleteSalonService(salonId, serviceId);

        deleteSalonService.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                callback.onResult(response.isSuccessful(), response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onResult(false, t);
            }
        });
    }
}
