package com.example.popie.uoleventsdiary_admin.Client;


import android.widget.ImageView;

import com.example.popie.uoleventsdiary_admin.Models.Event;
import com.example.popie.uoleventsdiary_admin.Models.Login;
import com.example.popie.uoleventsdiary_admin.Models.Organizer;
import com.example.popie.uoleventsdiary_admin.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface UserClient {

    //for admin login
    @POST("login")
    Call<User> login(@Body Login login);

    @POST("logout")
    Call<Void> logout();

    //to retrieve all the organizers
    @GET("organizers")
    Call<List<Organizer>> getOrganizers();

    //to retrieve all the events
    @GET("events")
    Call<List<Event>> getEvents();

    //to add a new event
    @POST("events")
    @FormUrlEncoded
    Call<Event> saveEvent(@Field("image") String image,
                          @Field("name") String name,
                          @Field("date_time") String dateTime,
                          @Field("venue") String venue,
                          @Field("phone") String phone,
                          @Field("u_id") int u_id,
                          @Field("o_id") int o_id);


    //to update a current event
    @PUT("events/{event}")
    @FormUrlEncoded
    Call<Event> updateEvent(@Path("event") int id,
                            @Field("image") String image,
                            @Field("name") String name,
                            @Field("date_time") String dateTime,
                            @Field("venue") String venue,
                            @Field("phone") String phone,
                            @Field("u_id") int u_id,
                            @Field("o_id") int o_id);

    //to delete an event
    @DELETE("events/{event}")
    Call<Void> deleteEvent(@Path("event") int id);


}


