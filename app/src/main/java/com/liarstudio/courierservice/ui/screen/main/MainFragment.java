package com.liarstudio.courierservice.ui.screen.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liarstudio.courierservice.R;
import com.liarstudio.courierservice.entitiy.pack.Package;
import com.liarstudio.courierservice.logic.ServerUtils;
import com.liarstudio.courierservice.logic.pack.PackageAPI;
import com.liarstudio.courierservice.logic.pack.PackageRepository;
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity;
import com.liarstudio.courierservice.ui.screen.main.my_packages.PagerAdapterMyPackages;
import com.liarstudio.courierservice.ui.screen.main.new_packages.PagerAdapterNewPackages;
import com.liarstudio.courierservice.ui.screen.pack.PackageFieldsActivity;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.liarstudio.courierservice.logic.ServerUtils.CURRENT_USER;
import static com.liarstudio.courierservice.logic.ServerUtils.IS_ADMIN;

public class MainFragment extends Fragment {

    /*
     ****** FIELDS AREA ******
     */

    private int REQUEST_ADD_OR_EDIT = 1;
    private MainTabType tabType;

    FragmentStatePagerAdapter adapter;
    ProgressBar progressBar;

    /*
     ****** CREATION AREA ******
     */

    public MainFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        tabType = (MainTabType) getArguments().get("extra first");

        switch (tabType) {
            case MY:
                adapter = new PagerAdapterMyPackages(getActivity().getSupportFragmentManager());
                break;
            case NEW:
                adapter = new PagerAdapterNewPackages(getActivity().getSupportFragmentManager());
                tabLayout.setVisibility(View.GONE);
                break;
        }

        viewPager.setAdapter(adapter);

        loadListFromServer();

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        //Скрываем кнопку добавления для администратора
        MenuItem item = menu.findItem(R.id.item_add);
        if (IS_ADMIN)
            item.setVisible(false);
        else
            item.setVisible(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                Intent addIntent = new Intent(getActivity(), PackageFieldsActivity.class);
                getActivity().startActivityForResult(addIntent, REQUEST_ADD_OR_EDIT);
                break;

            case R.id.item_refresh:
                loadListFromServer();
                break;
            case R.id.item_logout:
                CURRENT_USER = null;
                startActivity(new Intent(getActivity(), AuthActivity.class));
                getActivity().finish();

                break;
        }
        return true;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_OR_EDIT) {
            if (data.hasExtra("packageToAdd")) {
                String jsonPackage = data.getStringExtra("packageToAdd");
                Package pack = new Gson().fromJson(jsonPackage, Package.class);
                addToServer(pack);
            } else {
                if (data.hasExtra("packageToDelete")) {
                    String jsonPackage = data.getStringExtra("packageToDelete");
                    Package pack = new Gson().fromJson(jsonPackage, Package.class);
                    deleteFromServer(pack.getId().intValue());
                }
            }
        }
    }

    /*
     ****** SERVER AREA ******
     */


    void addToServer(Package pkg) {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUtils.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PackageAPI api = retrofit.create(PackageAPI.class);

        api.add(pkg).enqueue(
                new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:

                                //Если все ОК и посылка добавлена - загружаем список посылок
                                loadListFromServer();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:

                                Toast.makeText(getActivity(), R.string.error_add_http_not_found,
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:

                                Toast.makeText(getActivity(), R.string.error_db,
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), R.string.error_could_not_connect_to_server,
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }
        );
    }

    public void loadListFromServer() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUtils.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PackageAPI api = retrofit.create(PackageAPI.class);


        Call<List<Package>> apiCall;

        // Если пользователь - администратор, то загружаем "Новые"/"Отмененные"/"Завершенные"
        // Если курьер - то в зависимости от статуса(типа adapter'а) загружаем либо только новые,
        // либо "Назначенные"/"В процессе"/"Завершенные"

        if (IS_ADMIN)
            apiCall = api.loadAdminPackages();
        else
            apiCall = api.loadCourierPackages(ServerUtils.CURRENT_USER.getId(), 1 - tabType.ordinal());


        apiCall.enqueue(
                new Callback<List<Package>>() {
                    @Override
                    public void onResponse(Call<List<Package>> call, Response<List<Package>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:

                                // Если тело запроса не пусто,
                                // загружаем список посылок и перезагружаем локальные данные,


                                if (response.body() != null) {
                                    PackageRepository pkgList = new PackageRepository(response.body());
                                    pkgList.reloadAll();
                                    adapter.notifyDataSetChanged();
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
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<Package>> call, Throwable t) {
                        Toast.makeText(getActivity(), R.string.error_could_not_connect_to_server,
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);


                    }
                }
        );
    }

    void deleteFromServer(int id) {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUtils.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PackageAPI api = retrofit.create(PackageAPI.class);


        api.delete(id).enqueue(
                new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:

                                //Если все ОК и посылка удалена - загружаем список посылок
                                loadListFromServer();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:

                                Toast.makeText(getActivity(), R.string.error_add_http_not_found,
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:

                                Toast.makeText(getActivity(), R.string.error_db,
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), R.string.error_could_not_connect_to_server,
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }
        );
    }

}
