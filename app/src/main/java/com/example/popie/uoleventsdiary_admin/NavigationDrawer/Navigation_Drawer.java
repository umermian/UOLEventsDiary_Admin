package com.example.popie.uoleventsdiary_admin.NavigationDrawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popie.uoleventsdiary_admin.Activities.AddNewActivity;
import com.example.popie.uoleventsdiary_admin.Activities.ViewAllActivity;
import com.example.popie.uoleventsdiary_admin.Client.UserClient;
import com.example.popie.uoleventsdiary_admin.LoginActivity;
import com.example.popie.uoleventsdiary_admin.R;
import com.example.popie.uoleventsdiary_admin.Retrofit.Remote;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Navigation_Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Retrofit retrofit;
    UserClient userClient;
    Call<Void> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation__drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        retrofit = Remote.getRetrofit();
        userClient = retrofit.create(UserClient.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation__drawer, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addNew) {

            Intent intent = new Intent(getApplicationContext(), AddNewActivity.class);
            startActivity(intent);
        } else if (id == R.id.viewAll) {
            Intent intent = new Intent(getApplicationContext(), ViewAllActivity.class);
            startActivity(intent);

        } else if (id == R.id.help) {

            Toast.makeText(getApplicationContext(), "UOL Events Diary V1", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.logout) {

            call = userClient.logout();
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences.Editor editor = LoginActivity.sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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

        return true;
    }
}
