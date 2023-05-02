package com.example.new61d;

import android.net.Uri;

public class UserModel {
    private String full_name;
    private String user_name;
    private String password;
    private String phone_num;

    private Uri imageUrl;

    public UserModel(String full_name, String user_name, String password, String phone_num, Uri imageUrl) {
        this.full_name = full_name;
        this.user_name = user_name;
        this.password = password;
        this.phone_num = phone_num;
        this.imageUrl = imageUrl;
    }


}
