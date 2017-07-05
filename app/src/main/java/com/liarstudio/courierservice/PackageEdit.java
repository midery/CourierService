package com.liarstudio.courierservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.liarstudio.courierservice.BaseClasses.Package;

public class PackageEdit extends AppCompatActivity {


    Package pkg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_edit);

        Intent intent = getIntent();
        String jsonPackage = intent.getStringExtra("jsonPackage");
        pkg = new Gson().fromJson(jsonPackage, Package.class);
    }
}
