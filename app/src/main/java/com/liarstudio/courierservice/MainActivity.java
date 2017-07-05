package com.liarstudio.courierservice;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.BaseClasses.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ArrayList<Package> packages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPackages();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PackageFragmentPageAdapter manager = new PackageFragmentPageAdapter(getSupportFragmentManager(), packages)   ;

        viewPager.setAdapter(manager);

        //viewPager.addView(findViewById(R.id.trShipments));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);


        //initRows();

    }

    void loadPackages() {

        packages = new ArrayList<Package>();
        Package pkg = new Package(0, new Person("Kuk", "920", "m@m.m", "Lorum Ipsum street"), new Person("Kuk", "920", "m@m.m", "Lorum Ipsum street"), "Pkg 1", new Date(25));

        packages.add(pkg);
        pkg = new Package(1, new Person("Kuk", "920", "m@m.m", "Lorum Ipsum street"), new Person("Kuk", "14156", "m@m.m", "Dorum av."), "Pkg 2", new Date(System.currentTimeMillis()));
        packages.add(pkg);
        pkg = new Package(0, new Person("Kuk", "920", "m@m.m", "Lorum Ipsum street"), new Person("Kuk", "14156", "m@m.m", "Dorum av."), "Pkg 3", new Date(10600));
        packages.add(pkg);



        //TableRow tr = (TableRow) findViewById(R.id.tr
        //TabLayout.Tab tab = tabLayout.newTab();
        //tab.setText("Активные");
        //tabLayout.addTab(tab);
    }
}
