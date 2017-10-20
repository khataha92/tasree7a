package com.tasree7a.Managers;

import com.tasree7a.Constants;
import com.tasree7a.Models.Bookings.BookingModel;
import com.tasree7a.Models.FavoriteModels.FavoriteResponseModel;
import com.tasree7a.Models.Login.LoginModel;
import com.tasree7a.Models.Login.LoginResponseModel;
import com.tasree7a.Models.PopularSalons.PopularSalonsResponseModel;
import com.tasree7a.Models.SalonBooking.AvailableTimesResponse;
import com.tasree7a.Models.SalonBooking.SalonServicesResponse;
import com.tasree7a.Models.SalonDetails.SalonDetailsResponseModel;
import com.tasree7a.Models.Signup.SignupModel;
import com.tasree7a.Models.Signup.SignupResponseModel;
import com.tasree7a.Models.UserBookingsResponse;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.interfaces.ServiceRequest;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

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
            public void onResponse(Call<SalonDetailsResponseModel> call, Response<SalonDetailsResponseModel> response) {

                if (response.isSuccessful() && response.body() != null) {

                    callback.onResult(true, response.body().getSalon());

                } else {

                    callback.onResult(false, null);
                }

            }


            @Override
            public void onFailure(Call<SalonDetailsResponseModel> call, Throwable t) {

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


    public void getUserBookings(String userId, final AbstractCallback callback) {

        Call<UserBookingsResponse> getBookings = request.getUserBookings(userId);

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


    public void getNearestSalons(double lat, double lng, final AbstractCallback callback) {

        Call<PopularSalonsResponseModel> call = request.getNearestSalons(lat, lng);

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


    public void register(SignupModel model, final AbstractCallback callback) {

        Call<SignupResponseModel> call = request.register(model.getFirstName(), model.getLastName(), model.getEmail(), model.getPassword(), model.getUsername(), model.isFbLogin() ? 1 : 0, model.isBuisness() ? 1 : 0, model.getFbId());

        call.enqueue(new Callback<SignupResponseModel>() {

            @Override
            public void onResponse(Call<SignupResponseModel> call, Response<SignupResponseModel> response) {

                if (response.body() != null && response.code() == 200 && response.body().getResponseCode().equalsIgnoreCase("200")) {

                    callback.onResult(true, response.body());

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


    public void addNewSalon(String userID,
                            String cityId,
                            String salonType,
                            String imageID,
                            String ownerName,
                            String ownerMobile,
                            String salonLat,
                            String salonLong,
                            String salonName) {

        Call<Object> get = request.addNewSalonInformation("",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "");

        get.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

            }


            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
}
