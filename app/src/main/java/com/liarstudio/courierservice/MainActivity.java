package com.liarstudio.courierservice;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        ShipmentFragmentPagerAdapter manager = new ShipmentFragmentPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(manager);

        //viewPager.addView(findViewById(R.id.trShipments));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);


        //initRows();

    }

    void initRows() {



        //TableRow tr = (TableRow) findViewById(R.id.tr
        //TabLayout.Tab tab = tabLayout.newTab();
        //tab.setText("Активные");
        //tabLayout.addTab(tab);
    }
}
