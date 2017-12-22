package com.example.popie.uoleventsdiary_admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.popie.uoleventsdiary_admin.Activities.AddNewActivity;
import com.example.popie.uoleventsdiary_admin.Activities.ViewAllActivity;
import com.example.popie.uoleventsdiary_admin.Client.UserClient;
import com.example.popie.uoleventsdiary_admin.Retrofit.Remote;
import com.example.popie.uoleventsdiary_admin.Retrofit.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManageEventsActivity extends AppCompatActivity {


    Retrofit retrofit;
    UserClient userClient;
    Button add, viewAll, logOut;
    Intent intent;
    String msg; //token coming from LoginActivity

    Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);

        intent = getIntent();
        msg = intent.getStringExtra("token");

        Token.setToken(msg);

        add = findViewById(R.id.btnUpdate);
        viewAll = findViewById(R.id.buttonView);
        logOut = findViewById(R.id.buttonLogout);

        retrofit = Remote.getRetrofit();
        userClient = retrofit.create(UserClient.class);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });


        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAll();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    protected void add() {
        intent = new Intent(getApplicationContext(), AddNewActivity.class);
        startActivity(intent);
    }

    protected void viewAll() {
        intent = new Intent(getApplicationContext(), ViewAllActivity.class);
        startActivity(intent);
    }

    protected void logOut() {
        Call<Void> call = userClient.logout();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = LoginActivity.sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Response Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Try again when network is strong", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
