package com.tasree7a.Managers;

import com.google.gson.Gson;
import com.tasree7a.Constants;
import com.tasree7a.Models.Login.LoginModel;
import com.tasree7a.Models.Login.LoginResponseModel;
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

    public void login(LoginModel loginModel, final AbstractCallback callback){

        Call<LoginResponseModel> call = request.login(loginModel.getUsername(),loginModel.getPassword(),loginModel.isFacebookLogin());

        call.enqueue(new Callback<LoginResponseModel>() {

            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {

                try {

                    LoginResponseModel result = response.body();




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
