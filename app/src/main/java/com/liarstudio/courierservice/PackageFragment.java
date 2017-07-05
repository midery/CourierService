package com.liarstudio.courierservice;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.BaseClasses.Person;

import java.sql.Date;
import java.util.ArrayList;


public class PackageFragment extends Fragment {
    private static final int layout = R.layout.activity_list_package;

    private ArrayList<Package> packages;
    private View view;

    public PackageFragment() {
        //this.packages = packages;

    }


    public void setPackages(ArrayList<Package> packages) {
        this.packages = packages;
    }
    public static PackageFragment getInstance() {
        //Bundle args = new Bundle();
        PackageFragment fragment = new PackageFragment();
        //fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        ListView listView = (ListView)view.findViewById(R.id.lwShipmentList);

        ListAdapter la = new ListAdapter(getContext(), packages);
        listView.setOnItemClickListener((parent, v, position, id) -> {
                Toast.makeText(getContext(), "working", Toast.LENGTH_SHORT).show();
            Gson gson = new GsonBuilder().create();
            String jsonPackage = gson.toJson(packages.get(position));

            Intent intent = new Intent(getContext(), PackageEdit.class);
            intent.putExtra("jsonPackage", jsonPackage);
            startActivity(intent);
        });

        listView.setAdapter(la);

        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }
}