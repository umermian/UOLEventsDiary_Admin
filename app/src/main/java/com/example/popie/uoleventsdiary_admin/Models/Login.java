package com.example.popie.uoleventsdiary_admin.Models;

/**
 * Created by popie on 11/3/2017.
 */

public class Login {

    String email, password;

    public Login(String email, String password) {

        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
