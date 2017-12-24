package com.example.popie.uoleventsdiary_admin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.popie.uoleventsdiary_admin.Client.UserClient;
import com.example.popie.uoleventsdiary_admin.Models.Event;
import com.example.popie.uoleventsdiary_admin.R;
import com.example.popie.uoleventsdiary_admin.Retrofit.Remote;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddNewActivity extends AppCompatActivity {

    final static int GET_FROM_GALLERY = 3;
    ImageView imageView;
    Button btnAdd;
    EditText etName, etDateTime, etVenue, etPhone;
    Spinner spinner;
    String image, name, dateTime, venue, phone;
    Retrofit retrofit;
    UserClient userClient;
    ArrayAdapter<CharSequence> arrayAdapter;
    int o_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        imageView = findViewById(R.id.imageView2);
        etName = findViewById(R.id.etName);
        etDateTime = findViewById(R.id.etDateTime);
        etVenue = findViewById(R.id.etVenue);
        etPhone = findViewById(R.id.etPhone);

        spinner = findViewById(R.id.spinner);

        btnAdd = findViewById(R.id.btnUpdate);

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.organizers, R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        retrofit = Remote.getRetrofit();

        userClient = retrofit.create(UserClient.class);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                o_id = position + 12;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
        phone = etPhone.getText().toString();


        Call<Event> call = userClient.saveEvent(image, name, dateTime, venue, phone, 12, o_id);

        call.enqueue(new Callback<Event>() {


            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etDateTime.setText("");
                    etVenue.setText("");
                    etPhone.setText("");
                    imageView.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    Toast.makeText(getApplicationContext(), "Response Failure, enter correct information and try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Try again when network is strong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] b = baos.toByteArray();
                image = Base64.encodeToString(b, Base64.NO_WRAP);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
