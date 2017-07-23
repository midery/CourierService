package com.liarstudio.courierservice.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
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
import com.liarstudio.courierservice.API.PackageAPI;
import com.liarstudio.courierservice.API.ApiUtils;
import com.liarstudio.courierservice.Activities.AuthActivity;
import com.liarstudio.courierservice.Activities.MainActivity;
import com.liarstudio.courierservice.Activities.PackageFieldsActivity;
import com.liarstudio.courierservice.Adapters.PagerAdapterNew;
import com.liarstudio.courierservice.BaseClasses.Person;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.Database.PackageList;
import com.liarstudio.courierservice.Adapters.PagerAdapterMy;
import com.liarstudio.courierservice.R;


import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.liarstudio.courierservice.API.ApiUtils.CURRENT_USER;
import static com.liarstudio.courierservice.API.ApiUtils.IS_ADMIN;
import static com.liarstudio.courierservice.Activities.MainActivity.ON_FIRST_LAUNCH;
import static com.liarstudio.courierservice.Activities.MainActivity.managerType;

public class HomeFragment extends Fragment {

    /*
    ****** FIELDS AREA ******
    */

    FragmentStatePagerAdapter manager;
    PackageAPI api;
    ProgressBar progressBar;

    /*
    ****** CREATION AREA ******
    */

    public HomeFragment() {

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



        onFirstLaunch();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        manager = managerType == 0 ?
                new PagerAdapterMy(getActivity().getSupportFragmentManager()) :
                new PagerAdapterNew(getActivity().getSupportFragmentManager()) ;


        viewPager.setAdapter(manager);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(PackageAPI.class);

        loadListFromServer();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

    /*
    ****** OPTIONS AREA ******
    */



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
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
                getActivity().startActivityForResult(addIntent, MainActivity.REQUEST_ADD_OR_EDIT);
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


    /*
    ****** DATABASE AREA ******
    */


    void addToDB() {

        List<Person> persons = new ArrayList<>();
        for (int i = 1; i <9 ; i++) {
            persons.add(new Person(1, "Person " + i, "(999)555-11-1"+i, "person" + i + "@mail.ru",
                    "Pushkina street, house #" + i, "", new double[]{51.65712, 39.18995}));
            persons.get(i-1).save();
        }
        Package pkg1 = new Package(0, 1, persons.get(0), persons.get(2), "Package #1", Calendar.getInstance(),
                new double[]{10, 10, 35}, 8.5);
        Package pkg2 = new Package(0, 1, persons.get(1), persons.get(2), "Package #2", new GregorianCalendar(2017, 06, 10),
                new double[]{50, 11, 20}, 14);
        Package pkg3 = new Package(1, 1, persons.get(2), persons.get(4), "Package #3", Calendar.getInstance(),
                new double[]{5, 5, 5}, 1);
        pkg1.save(); pkg2.save(); pkg3.save();
    }

    void onFirstLaunch() {
        SharedPreferences pref = getActivity().getPreferences(MODE_PRIVATE);


        if (!pref.getBoolean(ON_FIRST_LAUNCH, false)) {
            addToDB();
            pref.edit().putBoolean(ON_FIRST_LAUNCH, true).commit();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == MainActivity.REQUEST_ADD_OR_EDIT) {
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
        api.add(pkg).enqueue(
                new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                loadListFromServer();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:

                                Toast.makeText(getActivity(), "Произошла ошибка работы с базой данных.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:

                                Toast.makeText(getActivity(), "Произошла ошибка на стороне сервера.",
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "Время ожидание ответа от сервера истекло.",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }
        );
    }

    public void loadListFromServer() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<Package>> apiCall;
        if (IS_ADMIN)
            apiCall = api.loadForAdmin();
        else
            apiCall = api.loadForCourier(ApiUtils.CURRENT_USER.getId(), 1 - managerType);


        apiCall.enqueue(
                new Callback<List<Package>>() {
                    @Override
                    public void onResponse(Call<List<Package>> call, Response<List<Package>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:

                                PackageList pkgList = new PackageList(response.body());
                                pkgList.reloadAll();
                                manager.notifyDataSetChanged();

                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getActivity(), "Произошла ошибка работы с базой данных.",
                                        Toast.LENGTH_LONG).show();

                                break;
                            default:
                                Toast.makeText(getActivity(), "Произошла ошибка на стороне сервера.",
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<Package>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Время ожидание ответа от сервера истекло.",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);


                    }
                }
        );
    }

    void deleteFromServer(int id) {
        progressBar.setVisibility(View.VISIBLE);
        api.delete(id).enqueue(
                new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                loadListFromServer();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:

                                Toast.makeText(getActivity(), "Произошла ошибка работы с базой данных.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:

                                Toast.makeText(getActivity(), "Произошла ошибка на стороне сервера.",
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "Время ожидание ответа от сервера истекло.",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }
        );
    }

}
