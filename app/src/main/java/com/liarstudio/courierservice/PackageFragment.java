package com.liarstudio.courierservice;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.ArrayList;


public class PackageFragment extends Fragment {
    private static final int layout = R.layout.activity_list_package;

    private View view;

    public static PackageFragment getInstance() {
        Bundle args = new Bundle();
        PackageFragment fragment = new PackageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        ListView listView = (ListView)view.findViewById(R.id.lwShipmentList);

        ArrayList<Package> packages = new ArrayList<Package>();


        Package pkg = new Package();
        pkg.name = "good boy";
        packages.add(pkg);


        ListAdapter la = new ListAdapter(getContext(), packages);
        listView.setAdapter(la);

        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }
}
