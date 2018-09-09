package com.tasree7a.managers;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tasree7a.utils.UserDefaultUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadersApiInterceptor implements Interceptor {

    private static final String AUTHORIZATION = "Authorization";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String authorizationToken = UserDefaultUtil.getUserToken();
        if (request != null
                && !TextUtils.isEmpty(authorizationToken)) {
            request = request.newBuilder()
                    .addHeader(AUTHORIZATION, authorizationToken)
                    .build();
        }

        return chain.proceed(request);
    }
}
