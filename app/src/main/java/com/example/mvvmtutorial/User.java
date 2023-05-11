package com.example.mvvmtutorial;

import android.text.TextUtils;
import android.util.Patterns;

public class User {
    private String email;
    private String password;

    public User(String email, String password) {
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

    // kiểm tra chuỗi email có hợp lệ hay không
    public boolean isValidEmail() {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // kiểm tra chuỗi password có hợp lệ hay không
    public boolean isValidPassword() {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }
}
