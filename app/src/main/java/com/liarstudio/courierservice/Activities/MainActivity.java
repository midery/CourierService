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
import android.view.Menu;
import android.view.MenuItem;

import com.liarstudio.courierservice.Fragments.HomeFragment;
import com.liarstudio.courierservice.Fragments.SettingsFragment;
import com.liarstudio.courierservice.R;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.orm.SugarContext;

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


    DrawerLayout drawer;
    Fragment fragment;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadCoefficients();

        fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode,resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }




    //Считываем коэффициенты из Preferences
    void loadCoefficients() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Package.WEIGHT_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(WEIGHT_COEFFICIENT, "0"));
        Package.SIZE_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(VOL_COEFFICIENT, "0"));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_settings:
                //Intent addIntent = new Intent(this, PackageFieldsActivity.class);
                //startActivityForResult(addIntent, REQUEST_ADD_OR_EDIT);
                fragment = new SettingsFragment();
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
