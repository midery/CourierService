package com.liarstudio.courierservice;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ShipmentFragment extends Fragment {
    private static final int layout = R.layout.shipment_activity;

    private View view;

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
        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }
}
