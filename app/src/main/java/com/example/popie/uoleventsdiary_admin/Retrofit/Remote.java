package com.example.popie.uoleventsdiary_admin.Retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Remote {

    private static Retrofit retrofit = null;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.159/uoleventsdiary/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
