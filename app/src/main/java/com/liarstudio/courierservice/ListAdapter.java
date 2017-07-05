package com.liarstudio.courierservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.liarstudio.courierservice.BaseClasses.Shipment;

import java.util.ArrayList;

/**
 * Created by M1DERY on 05.07.2017.
 */

public class ListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater layoutInflater;
    ArrayList<String> shipments;
    ListAdapter(Context context, ArrayList<String> shipments) {
        ctx = context;
        this.shipments = shipments;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return shipments.size();
    }

    @Override
    public Object getItem(int position) {
        return shipments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = layoutInflater.inflate(R.layout.shipments_list_element, parent, false);

        return view;
    }
}
