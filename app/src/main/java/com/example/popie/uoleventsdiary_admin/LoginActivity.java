package com.example.popie.uoleventsdiary_admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.popie.uoleventsdiary_admin.Activities.AddNewActivity;
import com.example.popie.uoleventsdiary_admin.Client.UserClient;
import com.example.popie.uoleventsdiary_admin.Models.Login;
import com.example.popie.uoleventsdiary_admin.Models.User;
import com.example.popie.uoleventsdiary_admin.NavigationDrawer.Navigation_Drawer;
import com.example.popie.uoleventsdiary_admin.Retrofit.Remote;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    String token; //token authentication
    Button login; //login button
    Retrofit retrofit; //retrofit object
    UserClient userClient; // userClient Object
    Transition transition; //for switiching between scenes
    Scene scene1, scene2;  //  scene1 is login screen, scene2 is loading screen
    ViewGroup viewGroup;   //
    String email, password; // to check if user logged out or not


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewGroup = findViewById(R.id.admin_login);

        transition = TransitionInflater.from(this).inflateTransition(R.transition.login_transition);

        scene1 = Scene.getSceneForLayout(viewGroup, R.layout.activity_login_1, this);
        scene2 = Scene.getSceneForLayout(viewGroup, R.layout.activity_login_2, this);

        scene1.enter();
        login = findViewById(R.id.buttonLogin);

        retrofit = Remote.getRetrofit();

        userClient = retrofit.create(UserClient.class);

        sharedPreferences = getSharedPreferences("MyPreferneces", MODE_PRIVATE);

        email = sharedPreferences.getString("email", "");
        password = sharedPreferences.getString("password", "");

        if (email != "" && password != "") {
            Intent intent = new Intent(LoginActivity.this, ManageEventsActivity.class);
            startActivity(intent);
            finish();

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    protected void login() {

        TransitionManager.go(scene2, transition);
        Login login = new Login("admin123@gmail.com", "admin123");

        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", "admin123@gmail.com");
                    editor.putString("password", "admin123");
                    editor.apply();
                    token = response.body().getData().getApiToken();
                    Intent intent = new Intent(LoginActivity.this, ManageEventsActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Welcome Admin", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Unsuccessful Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Try again when network is strong", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
