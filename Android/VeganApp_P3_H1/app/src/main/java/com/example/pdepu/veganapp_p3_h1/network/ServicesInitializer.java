package com.example.pdepu.veganapp_p3_h1.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pdepu on 1/08/2017.
 */

public class ServicesInitializer {

    public Service initializeService(){

//        .baseUrl("http://192.168.1.58:3000/api/") //.0.227
        //.baseUrl("http:/192.168.0.146:3000/api/")


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http:/192.168.1.58:3000/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);

        return service;
    }
}
