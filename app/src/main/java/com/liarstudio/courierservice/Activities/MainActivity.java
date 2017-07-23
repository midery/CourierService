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
import com.liarstudio.courierservice.Adapters.PagerAdapterMy;
import com.liarstudio.courierservice.Adapters.PagerAdapterNew;
import com.liarstudio.courierservice.Fragments.HomeFragment;
import com.liarstudio.courierservice.Fragments.SettingsFragment;
import com.liarstudio.courierservice.Fragments.PackageFragment;
import com.liarstudio.courierservice.R;
import com.liarstudio.courierservice.BaseClasses.Package;

import org.w3c.dom.Text;

import static com.liarstudio.courierservice.API.ApiUtils.CURRENT_USER;

public class  MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    /*
    ****** STATIC CONSTANT AREA ******
    */

    public static final int REQUEST_ADD_OR_EDIT = 1;
    public static final int REQUEST_MAP = 2;


    public static final String VOL_COEFFICIENT = "size_dimensions"; //"vol_coefficient";
    public static final String WEIGHT_COEFFICIENT = "weight_dimensions";//"weight_coefficient";
    public static final String PREFERENCES_FILENAME = "preferences_data";
    public static final String ON_FIRST_LAUNCH = "first_launch";



    /*
    ****** FIELDS AREA ******
    */

    public static int managerType = 0;


    DrawerLayout drawer;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadCoefficients();

        managerType = 0;
        fragment = new HomeFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView textViewNavHeader = (TextView) header.findViewById(R.id.textViewNavHeader);
        textViewNavHeader.setText(CURRENT_USER.getName());



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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode,resultCode, data);
        //fragment.onActivityResult(requestCode, resultCode, data);

        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            frag.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
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

    //Считываем коэффициенты из Preferences
    void loadCoefficients() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Package.WEIGHT_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(WEIGHT_COEFFICIENT, "0"));
        Package.SIZE_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(VOL_COEFFICIENT, "0"));
    }

}
