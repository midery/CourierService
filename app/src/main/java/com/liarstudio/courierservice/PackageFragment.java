package com.liarstudio.courierservice;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liarstudio.courierservice.Activities.MainActivity;
import com.liarstudio.courierservice.Activities.PackageEdit;
import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.ArrayList;


public class PackageFragment extends Fragment {
    private static final int layout = R.layout.activity_list_package;

    private ArrayList<Package> packages;
    private View view;

    PackageFragmentPageAdapter adapter;

    public PackageFragment() {
        //this.packages = packages;

    }



    public void setPackages(ArrayList<Package> packages) {
        this.packages = packages;
    }
    public void setAdapter(PackageFragmentPageAdapter adapter) { this.adapter = adapter;}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        ListView listView = (ListView)view.findViewById(R.id.lwShipmentList);

        ListAdapter la = new ListAdapter(getContext(), packages);
        listView.setOnItemClickListener((parent, v, position, id) -> {
            Package pkg = packages.get(position);
            if (pkg.status == 0) {
                Gson gson =  new GsonBuilder().create();
                String jsonPackage = gson.toJson(pkg);


                Intent intent = new Intent(getContext(), PackageEdit.class);
                intent.putExtra("jsonPackage", jsonPackage);
                intent.putExtra("packagePosition", adapter.getAbsolutePosition(pkg));
                getActivity().startActivityForResult(intent, MainActivity.REQUEST_ADD_OR_EDIT);
            }
        });

        listView.setAdapter(la);

        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }
}
