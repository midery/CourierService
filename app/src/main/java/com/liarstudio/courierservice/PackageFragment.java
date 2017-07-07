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


    private View view;
    //Количество посылок в текущем фрагменте
    private ArrayList<Package> packages;

    //Экземпляр adapter для поиска абсолютного положения
    PackageFragmentPageAdapter adapter;

    public PackageFragment() {

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


        //Устанавливаем
        ListAdapter la = new ListAdapter(getContext(), packages);

        listView.setOnItemClickListener((parent, v, position, id) -> {

            //работаем с выбранной посылкой
            Package pkg = packages.get(position);

            //можно редактировать, если статус - "Активна"
            if (pkg.status == 0) {
                //Проводим JSON-сериализацию для передачи в другую Activity
                Gson gson =  new GsonBuilder().create();
                String jsonPackage = gson.toJson(pkg);


                Intent intent = new Intent(getContext(), PackageEdit.class);
                intent.putExtra("jsonPackage", jsonPackage);
                //Кладем не position, а AbsolutePosition, так как после завершения обновлять будем
                //посылку не из списка этого фрагмента, а из списка посылок целиком.
                intent.putExtra("packagePosition", adapter.getAbsolutePosition(pkg));
                getActivity().startActivityForResult(intent, MainActivity.REQUEST_ADD_OR_EDIT);
            }
        });

        listView.setAdapter(la);

        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }
}
