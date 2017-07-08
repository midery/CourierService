package com.liarstudio.courierservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liarstudio.courierservice.Activities.MainActivity;
import com.liarstudio.courierservice.Activities.MapsActivity;
import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by M1DERY on 05.07.2017.
 */

public class ListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater layoutInflater;
    ArrayList<Package> packages;
    ListAdapter(Context context, ArrayList<Package> aPackages) {
        ctx = context;
        this.packages = aPackages;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return packages.size();
    }

    @Override
    public Object getItem(int position) {
        return packages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = layoutInflater.inflate(R.layout.activity_list_element, parent, false);
        Package pkg = packages.get(position);

        TextView tvName = (TextView)view.findViewById(R.id.twName);
        tvName.setText(pkg.getName());

        TextView tvAddress = (TextView)view.findViewById(R.id.twAddress);
        tvAddress.setText(pkg.getRecipient().getAddress());

        TextView tvDate = (TextView)view.findViewById(R.id.twDate);
        tvDate.setText(pkg.getStringDate());

        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.lvCell);
        Button buttonShowOnMap = (Button)view.findViewById(R.id.buttonShowOnMap);
        buttonShowOnMap.setEnabled(false);

        if (pkg.getStatus()>0) {
            rl.setBackgroundColor(Color.LTGRAY);
        }   else    {
            rl.setBackgroundColor(Color.WHITE);
            double[] coordinates = pkg.getCoordinates();
            if (coordinates[0] != 0 && coordinates[1] !=0) {
                buttonShowOnMap.setEnabled(true);
                buttonShowOnMap.setOnClickListener(l -> {
                    Intent mapIntent = new Intent(parent.getContext(), MapsActivity.class);
                    mapIntent.putExtra("coordinates", coordinates);
                    parent.getContext().startActivity(mapIntent);

                });
            }
        }





        return view;
    }
}
