package com.liarstudio.courierservice;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.liarstudio.courierservice.BaseClasses.Shipment;

import java.util.ArrayList;


public class ShipmentFragment extends Fragment {
    private static final int layout = R.layout.shipments_list_activity;

    private View view;
    Shipment shipment;

    public static ShipmentFragment getInstance() {
        Bundle args = new Bundle();
        ShipmentFragment fragment = new ShipmentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        ListView listView = (ListView)view.findViewById(R.id.lwShipmentList);

        ArrayList<String> kuk = new ArrayList<String>();
        kuk.add("kek");
        kuk.add("kak");

        ListAdapter la = new ListAdapter(getContext(), kuk);
        listView.setAdapter(la);

        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }
}
