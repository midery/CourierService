package com.liarstudio.courierservice;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
        listView.setAdapter(la);

        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }
}
