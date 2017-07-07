package com.liarstudio.courierservice.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.BaseClasses.Person;
import com.liarstudio.courierservice.PackageFragmentPageAdapter;
import com.liarstudio.courierservice.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class  MainActivity extends AppCompatActivity {


    public static final int REQUEST_ADD_OR_EDIT = 1;
    public static final int REQUEST_MAP = 2;

    Toolbar toolbar;
    TabLayout tabLayout;
    PackageFragmentPageAdapter manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        //setSupportActionBar(toolBar);



        ArrayList<Package> packages = loadPackages();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        manager = new PackageFragmentPageAdapter(getSupportFragmentManager(), packages);

        viewPager.setAdapter(manager);

        //viewPager.addView(findViewById(R.id.trShipments));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);


        //initRows();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAdd:
                Intent intent = new Intent(this, PackageEdit.class);
                startActivityForResult(intent, REQUEST_ADD_OR_EDIT);
                return true;
            case R.id.itemSettings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_OR_EDIT &&
                data.hasExtra("jsonPackageChild") && data.hasExtra("packageChildPosition")) {
            int position = data.getIntExtra("packageChildPosition", -1);
            String jsonPackage = data.getStringExtra("jsonPackageChild");
            Package pkg = new Gson().fromJson(jsonPackage, Package.class);
            manager.add(position, pkg);
        }
    }




        private ArrayList<Package> loadPackages() {

        ArrayList<Package> packages = new ArrayList<>();
        Package pkg = new Package(0,
                new Person("Kuk", "9204595911", "m@m.m", "Lorum Ipsum street"),
                new Person("Kuk", "9204505931", "m@m.m", "Lorum Ipsum street"), "Pkg 1",
                new GregorianCalendar(2017, 07, 06), new int[]{5,1,6}, 5);

        packages.add(pkg);
        pkg = new Package(1, new Person("Kuk", "920", "m@m.m", "Lorum Ipsum street"), new Person("Kuk", "14156", "m@m.m", "Dorum av."), "Pkg 2", Calendar.getInstance());
        packages.add(pkg);
        pkg = new Package(0, new Person("Kuk", "920", "m@m.m", "Lorum Ipsum street"), new Person("Kuk", "14156", "m@m.m", "Dorum av."), "Pkg 3", new GregorianCalendar(2017, 05, 12),
                new int[]{200,35,615}, 20);
        packages.add(pkg);
        return packages;
    }
}
