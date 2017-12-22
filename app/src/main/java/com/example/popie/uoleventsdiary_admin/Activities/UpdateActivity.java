package com.example.popie.uoleventsdiary_admin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.popie.uoleventsdiary_admin.Client.UserClient;
import com.example.popie.uoleventsdiary_admin.Models.Event;
import com.example.popie.uoleventsdiary_admin.R;
import com.example.popie.uoleventsdiary_admin.Retrofit.Remote;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateActivity extends AppCompatActivity {

    Event event;
    EditText etName, etDataTime, etVenue, etOrganizer, etPhone;
    Gson gson;
    Button btnUpdate;
    Retrofit retrofit;
    Call<Event> call;
    UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etName = findViewById(R.id.etName);
        etDataTime = findViewById(R.id.etDateTime);
        etVenue = findViewById(R.id.etVenue);
        etOrganizer = findViewById(R.id.etOrganizer);
        etPhone = findViewById(R.id.etPhone);

        btnUpdate = findViewById(R.id.btnUpdate);

        retrofit = Remote.getRetrofit();
        userClient = retrofit.create(UserClient.class);


        Intent intent = getIntent();
        String eventData = intent.getStringExtra("eventData");
        gson = new Gson();
        event = gson.fromJson(eventData, Event.class);

        etName.setText(event.getName());
        etDataTime.setText(event.getDateTime());
        etVenue.setText(event.getVenue());
        etOrganizer.setText(event.getOName());
        etPhone.setText(event.getPhone());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
