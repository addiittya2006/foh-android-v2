package org.fundsofhope.android.config;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import org.fundsofhope.android.util.ApplicationConstant;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by anip on 23/07/16.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = Environment.PRODUCTION.getBaseUrl();

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(setOkClient())
            .setLogLevel(RestAdapter.LogLevel.FULL);

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, "");
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Content-Type", "application/json");
//                    conn.setRequestProperty("Accept", "application/json");
                }
            });
        } else {
            Log.e("ServiceGenerator", "HEADER ERROR");
        }
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }

    private static OkClient setOkClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(ApplicationConstant.SERVER_TIMEOUT, TimeUnit.MINUTES);
        okHttpClient.setReadTimeout(ApplicationConstant.READ_TIMEOUT, TimeUnit.MINUTES);
        return new OkClient(okHttpClient);
    }


}
