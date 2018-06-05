package com.mehedicr.tourmate.Model;

public class User {

    private String name;
    private String mobile;
    private String email;

    public User() {
        //required for firebase
    }

    public User(String name, String mobile, String email) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }
}
