package com.liarstudio.courierservice;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liarstudio.courierservice.API.PackageAPI;
import com.liarstudio.courierservice.API.UrlUtils;
import com.liarstudio.courierservice.Activities.MainActivity;
import com.liarstudio.courierservice.Activities.PackageFieldsActivity;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.Database.PackageList;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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

    public void setAdapter(PackageFragmentPageAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        ListView listView = (ListView) view.findViewById(R.id.lwShipmentList);


        //Устанавливаем
        PackageListAdapter la = new PackageListAdapter(getContext(), packages);

        listView.setOnItemClickListener((parent, v, position, id) -> {

            //работаем с выбранной посылкой


            if (UrlUtils.TOGGLE_OFFLINE) {
                Package newPkg = Package.findById(Package.class, packages.get(position).getId());


                //Проводим JSON-сериализацию для передачи в другую Activity
                Gson gson = new GsonBuilder().create();
                String jsonPackage = gson.toJson(newPkg);


                Intent intent = new Intent(getContext(), PackageFieldsActivity.class);
                intent.putExtra("jsonPackage", jsonPackage);
                //Кладем не position, а AbsolutePosition, так как после завершения обновлять будем
                //посылку не из списка этого фрагмента, а из списка посылок целиком.

                getActivity().startActivityForResult(intent, MainActivity.REQUEST_ADD_OR_EDIT);
            } else {
                loadPackageFromServer(packages.get(position).getId().intValue());
            }
        });

        listView.setAdapter(la);

        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }

    void loadPackageFromServer(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PackageAPI api = retrofit.create(PackageAPI.class);

        api.loadOne(id).enqueue(
                new Callback<Package>() {
                    @Override
                    public void onResponse(Call<Package> call, Response<Package> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                Gson gson = new GsonBuilder().create();
                                String jsonPackage = gson.toJson(response.body());

                                Intent intent = new Intent(getContext(), PackageFieldsActivity.class);
                                intent.putExtra("jsonPackage", jsonPackage);

                                getActivity().startActivityForResult(intent, MainActivity.REQUEST_ADD_OR_EDIT);
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getActivity(), "Ошибка при работе с базой данных.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "Произошла ошибка на стороне сервера.",
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Package> call, Throwable t) {
                        Toast.makeText(getActivity(), "Время ожидание ответа от сервера истекло.",
                                Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}