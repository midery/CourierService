package com.liarstudio.courierservice.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.liarstudio.courierservice.API.ApiUtils;
import com.liarstudio.courierservice.Fragments.HomeFragment;
import com.liarstudio.courierservice.Fragments.SettingsFragment;
import com.liarstudio.courierservice.R;
import com.liarstudio.courierservice.BaseClasses.Package;


import java.util.List;

import static com.liarstudio.courierservice.API.ApiUtils.CURRENT_USER;

public class  MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    /*
    ****** STATIC CONSTANT AREA ******
    */

    public static final int REQUEST_ADD_OR_EDIT = 1;


    public static final String VOL_COEFFICIENT = "size_dimensions";
    public static final String WEIGHT_COEFFICIENT = "weight_dimensions";
    public static final String ON_FIRST_LAUNCH = "first_launch";



    /*
    ****** FIELDS AREA ******
    */


    //Тип менеджера - PagerAdapterMy, если 0, и PagerAdapterNew, если 1
    //Переменная для экономии ресурсов
    public static int managerType = 0;


    DrawerLayout drawer;
    Fragment fragment;

    /*
    ****** METHODS AREA ******
    */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadCoefficients();


        //Создаем новый HomeFragment с менеджером PagerAdapterMy
        managerType = 0;
        fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment).commit();


        //Назначаем toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Создаем navigationDrawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Меняем textView у заголовка drawer'a в соответствии с имененем пользователя
        View header = navigationView.getHeaderView(0);
        TextView textViewNavHeader = (TextView) header.findViewById(R.id.textViewNavHeader);
        textViewNavHeader.setText(CURRENT_USER.getName());


        //Меняем названия и доступность пунктов меню drawer'а в зависимости от того, является ли
        //пользователь администратором
        MenuItem nav_my = navigationView.getMenu().findItem(R.id.nav_my);
        MenuItem nav_new = navigationView.getMenu().findItem(R.id.nav_new);

        if (ApiUtils.IS_ADMIN) {
            nav_my.setTitle(R.string.nav_my_admin);
            nav_new.setVisible(false);
        } else {
            nav_my.setTitle(R.string.nav_my);
            nav_new.setVisible(true);
        }
    }


    /*
    * Функция, вызываемая после завершения activity, запущенной для получения результата
    * Вызываем onActivityResult с теми же параметрами у фрагментов
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode,resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        fragment.onActivityResult(requestCode, resultCode, data);

    }
    /*
    * Функция, вызываемая при выборе одного из элементов меню drawer'а
    */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_my:
                managerType = 0;
                fragment = new HomeFragment();
                break;
            case R.id.nav_new:
                managerType = 1;
                fragment = new HomeFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_logout:
                CURRENT_USER = null;
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                return true;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void replaceFragment(int type) {
        fragment = null;
        managerType = type;
        fragment = new HomeFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment).commit();
    }


    /*
    * Функция считывания коэффициентов из Preferences
     */
    void loadCoefficients() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Package.WEIGHT_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(WEIGHT_COEFFICIENT, "0"));
        Package.SIZE_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(VOL_COEFFICIENT, "0"));
    }

}
