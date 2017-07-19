package com.liarstudio.courierservice.API;

/**
 * Created by M1DERY on 19.07.2017.
 */

public class User {

    private String email;
    private String password;

    private int id;
    private String name;
    private int role;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
