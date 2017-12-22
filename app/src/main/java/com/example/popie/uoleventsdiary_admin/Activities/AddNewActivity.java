package com.example.popie.uoleventsdiary_admin.Activities;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddNewActivity extends AppCompatActivity {

    Button btnAdd;
    EditText etName, etDateTime, etVenue, etOrganizer, etPhone;
    String name, dateTime, venue, organizer, phone;
    Retrofit retrofit;
    UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        etName = findViewById(R.id.etName);
        etDateTime = findViewById(R.id.etDateTime);
        etVenue = findViewById(R.id.etVenue);
        etOrganizer = findViewById(R.id.etOrganizer);
        etPhone = findViewById(R.id.etPhone);

        btnAdd = findViewById(R.id.btnUpdate);

        retrofit = Remote.getRetrofit();

        userClient = retrofit.create(UserClient.class);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    protected void add() {

        name = etName.getText().toString();
        dateTime = etDateTime.getText().toString();
        venue = etVenue.getText().toString();
        organizer = etOrganizer.getText().toString();
        phone = etPhone.getText().toString();

        Call<Event> call = userClient.saveEvent("abcdef", name, dateTime, venue, phone, 12, 17);

        call.enqueue(new Callback<Event>() {


            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etDateTime.setText("");
                    etVenue.setText("");
                    etOrganizer.setText("");
                    etPhone.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Response Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Try again when network is strong", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
