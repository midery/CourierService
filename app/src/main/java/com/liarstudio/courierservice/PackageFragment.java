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
import com.liarstudio.courierservice.Activities.PackageFieldsActivity;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.Database.PackageList;



public class PackageFragment extends Fragment {
    private static final int layout = R.layout.activity_list_package;


    private View view;
    //Количество посылок в текущем фрагменте
    private PackageList packages;

    //Экземпляр adapter для поиска абсолютного положения
    PackageFragmentPageAdapter adapter;

    public PackageFragment() {

    }



    public void setPackages(PackageList packages) {
        this.packages = packages;
    }
    public void setAdapter(PackageFragmentPageAdapter adapter) { this.adapter = adapter;}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        ListView listView = (ListView)view.findViewById(R.id.lwShipmentList);


        //Устанавливаем
        PackageListAdapter la = new PackageListAdapter(getContext(), packages);

        listView.setOnItemClickListener((parent, v, position, id) -> {

            //работаем с выбранной посылкой

            Package pkg = packages.get(position);
            Package newPkg = Package.findById(Package.class, pkg.getId());


            //можно редактировать, если статус - "Активна"
            if (newPkg.getStatus() == 0) {
                //Проводим JSON-сериализацию для передачи в другую Activity
                Gson gson =  new GsonBuilder().create();
                String jsonPackage = gson.toJson(newPkg);


                Intent intent = new Intent(getContext(), PackageFieldsActivity.class);
                intent.putExtra("jsonPackage", jsonPackage);
                //Кладем не position, а AbsolutePosition, так как после завершения обновлять будем
                //посылку не из списка этого фрагмента, а из списка посылок целиком.

                getActivity().startActivityForResult(intent, MainActivity.REQUEST_ADD_OR_EDIT);
            }
            else {
                if (pkg.getStatus() != newPkg.getStatus()) {
                    pkg = newPkg;
                   adapter.notifyDataSetChanged();
                }
            }
        });

        listView.setAdapter(la);

        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }
}
