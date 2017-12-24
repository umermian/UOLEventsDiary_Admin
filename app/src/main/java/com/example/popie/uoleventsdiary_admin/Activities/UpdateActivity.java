package com.example.popie.uoleventsdiary_admin.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;

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
    EditText etName, etDataTime, etVenue, etPhone;
    Spinner spinner;
    Gson gson;
    Button btnUpdate;
    Retrofit retrofit;
    Call<Event> call;
    UserClient userClient;
    ImageView imageView;
    Bitmap bm;
    ArrayAdapter<CharSequence> arrayAdapter;
    int o_id;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etName = findViewById(R.id.etName);
        etDataTime = findViewById(R.id.etDateTime);
        etVenue = findViewById(R.id.etVenue);
        etPhone = findViewById(R.id.etPhone);
        imageView = findViewById(R.id.imageViewEvent);
        spinner = findViewById(R.id.spinnerUpdate);

        btnUpdate = findViewById(R.id.btnUpdate);

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.organizers, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        retrofit = Remote.getRetrofit();
        userClient = retrofit.create(UserClient.class);


        Intent intent = getIntent();
        String eventData = intent.getStringExtra("eventData");
        gson = new Gson();
        event = gson.fromJson(eventData, Event.class);


        position = event.getOId() - 12;
        etName.setText(event.getName());
        etDataTime.setText(event.getDateTime());
        etVenue.setText(event.getVenue());
        etPhone.setText(event.getPhone());
        spinner.setSelection(position);

        byte[] encodeByte = Base64.decode(event.getImage(), Base64.DEFAULT);
        bm = BitmapFactory.decodeByteArray(encodeByte, 0,
                encodeByte.length);

        imageView.setImageBitmap(bm);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                o_id = position + 12;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call = userClient.updateEvent(event.getId(), event.getImage(), etName.getText().toString(), etDataTime.getText().toString(), etVenue.getText().toString(), etPhone.getText().toString(), 12, o_id);
                call.enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
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
        });
    }

}
