package com.tasree7a.Managers;

import com.tasree7a.Constants;
import com.tasree7a.Models.Login.LoginModel;
import com.tasree7a.Models.Login.LoginResponseModel;
import com.tasree7a.Models.PopularSalons.PopularSalonsResponseModel;
import com.tasree7a.Models.SalonDetails.SalonDetailsResponseModel;
import com.tasree7a.Models.Signup.SignupModel;
import com.tasree7a.Models.Signup.SignupResponseModel;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.interfaces.ServiceRequest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mac on 5/17/17.
 */

public class RetrofitManager {

    private static RetrofitManager instance = null;

    private static final String SERVER_URL = Constants.SERVICE_API;

    ServiceRequest request;

    Retrofit retrofit;

    public static RetrofitManager getInstance(){

        if(instance == null){

            instance = new RetrofitManager();

        }

        return instance;

    }

    private RetrofitManager(){

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

    public void getSalonDetails(String salonId, final AbstractCallback callback){

        Call<SalonDetailsResponseModel> call = request.getSalonDetails(salonId);

        call.enqueue(new Callback<SalonDetailsResponseModel>() {

            @Override
            public void onResponse(Call<SalonDetailsResponseModel> call, Response<SalonDetailsResponseModel> response) {

                if(response.isSuccessful() && response.body() != null){

                    callback.onResult(true,response.body().getSalon());

                } else{

                    callback.onResult(false,null);
                }

            }

            @Override
            public void onFailure(Call<SalonDetailsResponseModel> call, Throwable t) {

                callback.onResult(false,null);

            }
        });

    }

    public void getNearestSalons(double lat, double lng, final AbstractCallback callback){

        Call<PopularSalonsResponseModel> call = request.getNearestSalons(lat,lng);

        call.enqueue(new Callback<PopularSalonsResponseModel>() {

            @Override
            public void onResponse(Call<PopularSalonsResponseModel> call, Response<PopularSalonsResponseModel> response) {

                if(response.isSuccessful() && response.body() != null){

                    callback.onResult(true,response.body());

                } else{

                    callback.onResult(false,null);
                }

            }

            @Override
            public void onFailure(Call<PopularSalonsResponseModel> call, Throwable t) {

                callback.onResult(false,null);

            }
        });

    }

    public void register(SignupModel model, final AbstractCallback callback){

        Call<SignupResponseModel> call = request.register(model.getFirstName(),model.getLastName(),model.getEmail(),model.getPassword(),model.getUsername(),model.isFbLogin()?1:0);

        call.enqueue(new Callback<SignupResponseModel>() {

            @Override
            public void onResponse(Call<SignupResponseModel> call, Response<SignupResponseModel> response) {

                if(response.body() != null && response.code() == 200 && response.body().getResponseCode().equalsIgnoreCase("200")){

                    callback.onResult(true,response.body());

                } else{

                    callback.onResult(false,null);

                }

            }

            @Override
            public void onFailure(Call<SignupResponseModel> call, Throwable t) {

                callback.onResult(false,null);

            }
        });
    }

    public void login(LoginModel loginModel, final AbstractCallback callback){

        Call<LoginResponseModel> call = request.login(loginModel.getUsername(),loginModel.getPassword(),loginModel.isFacebookLogin());

        call.enqueue(new Callback<LoginResponseModel>() {

            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {

                try {

                    LoginResponseModel result = response.body();

                    if(result != null){

                        callback.onResult(true,result);

                    } else{

                        callback.onResult(false,null);

                    }


                } catch (Exception e){

                    e.printStackTrace();

                    callback.onResult(false,null);
                }

            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

                callback.onResult(false,null);

            }
        });
    }

}
