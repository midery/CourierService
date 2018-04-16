package com.liarstudio.courierservice.Fragments;

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
import com.liarstudio.courierservice.API.ApiUtils;
import com.liarstudio.courierservice.ui.screen.main.MainActivity;
import com.liarstudio.courierservice.ui.screen.pack.PackageFieldsActivity;
import com.liarstudio.courierservice.entities.Package;
import com.liarstudio.courierservice.Database.PackageList;
import com.liarstudio.courierservice.ui.screen.main.MainElementAdapter;
import com.liarstudio.courierservice.R;

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

    public PackageFragment() {

    }


    public void setPackages(PackageList packages) {
        this.packages = packages;
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

        MainElementAdapter la = new MainElementAdapter(getContext(), packages);
        listView.setAdapter(la);


        listView.setOnItemClickListener((parent, v, position, id) ->
                loadPackageFromServer(packages.get(position).getId().intValue()));


        return view;//super.onCreateView(inflater, container, savedInstanceState);


    }

    void loadPackageFromServer(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PackageAPI api = retrofit.create(PackageAPI.class);

        api.loadOne(id).enqueue(
                new Callback<Package>() {
                    @Override
                    public void onResponse(Call<Package> call, Response<Package> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:

                                // Если тело запроса не пустое, сериализуем его в JSON
                                // и добавляем в новую Activity
                                if (response.body() != null) {
                                    Gson gson = new GsonBuilder().create();
                                    String jsonPackage = gson.toJson(response.body());

                                    Intent intent = new Intent(getContext(), PackageFieldsActivity.class);
                                    intent.putExtra("jsonPackage", jsonPackage);

                                    getActivity().startActivityForResult(intent, MainActivity.REQUEST_ADD_OR_EDIT);
                                }
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getActivity(), R.string.error_http_not_found,
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), R.string.error_db,
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Package> call, Throwable t) {
                        Toast.makeText(getActivity(), R.string.error_could_not_connect_to_server,
                                Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}