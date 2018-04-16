package com.liarstudio.courierservice.ui.screen.main;

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

import com.liarstudio.courierservice.ui.screen.maps.MapsActivity;
import com.liarstudio.courierservice.logic.pack.PackageRepository;
import com.liarstudio.courierservice.entities.pack.Package;
import com.liarstudio.courierservice.R;

import static com.liarstudio.courierservice.logic.ServerUtils.IS_ADMIN;

public class MainElementAdapter extends BaseAdapter {


    /*
    ****** FIELDS AREA ******
    */

    private Context ctx;
    private LayoutInflater layoutInflater;
    private PackageRepository packages;


    public MainElementAdapter(Context context, PackageRepository aPackages) {
        ctx = context;
        this.packages = aPackages;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /*
    ****** OVERRIDDEN METHODS AREA ******
    */

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


    /*
    * Функция, отвечающая за отображение элемента списка посылок на ListView
     */
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


        //Раскрашиваем посылку в завимсимости от ее статуса:
        //Назначенная/Новая - белый. В процессе - зеленый. Отклоненная - красный. Завершенная - серый.
        switch (pkg.getStatus()) {
            case 0:
                rl.setBackgroundColor(Color.WHITE);
                break;
            case 1:
                if (IS_ADMIN)
                    rl.setBackgroundColor(Color.argb(50, 0, 0, 150));
                else {
                    rl.setBackgroundColor(Color.WHITE);
                }
                break;
            case 2:
                rl.setBackgroundColor(Color.argb(50, 0, 180, 0));
                break;
            case 3:
                rl.setBackgroundColor(Color.argb(50, 180, 0, 0));
                break;
            case 4:
                rl.setBackgroundColor(Color.LTGRAY);
        }

        //Если у посылки есть координаты, то снимаем readonly c кнопки
        double[] coordinates = pkg.getCoordinates();
        if (coordinates[0] != 0 && coordinates[1] !=0) {
            buttonShowOnMap.setEnabled(true);
            buttonShowOnMap.setOnClickListener(l -> {
                Intent mapIntent = new Intent(parent.getContext(), MapsActivity.class);
                mapIntent.putExtra("coordinates", coordinates);
                parent.getContext().startActivity(mapIntent);

            });
        }
        return view;
    }
}
