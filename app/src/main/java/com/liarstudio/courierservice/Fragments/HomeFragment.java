package com.liarstudio.courierservice.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.liarstudio.courierservice.BaseClasses.Person;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.PackageFragmentPageAdapter;
import com.liarstudio.courierservice.R;
import com.orm.SugarRecord;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.liarstudio.courierservice.Activities.MainActivity.ON_FIRST_LAUNCH;
import static com.liarstudio.courierservice.Activities.MainActivity.REQUEST_ADD_OR_EDIT;

public class HomeFragment extends Fragment {

    PackageFragmentPageAdapter manager;


    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        onFirstLaunch();

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        manager = new PackageFragmentPageAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(manager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }
    void addToDB() {

        /*SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues cv = new ContentValues();


        for (int i = 1; i <9 ; i++) {
            cv.put(ConstantsPerson.NAME, "Ivan #" + i);
            cv.put(ConstantsPerson.ADDRESS, "Pushkina Street, house " + i);
            cv.put(ConstantsPerson.EMAIL, "ivan" + i + "@gmail.com");
            cv.put(ConstantsPerson.PHONE, "(920)-555-593" + i);
            cv.put(ConstantsPerson.TYPE, 1);
            cv.put(ConstantsPerson.COMPANY_NAME, "");
            cv.put(ConstantsPerson.COORDINATE_X, 51.65712);
            cv.put(ConstantsPerson.COORDINATE_Y, 39.18995);
            db.insert(ConstantsPerson.TABLE_NAME, null, cv);
        }

        cv.clear();
        cv.put(ConstantsPackage.SENDER, 1);
        cv.put(ConstantsPackage.RECIPIENT, 2);
        cv.put(ConstantsPackage.NAME, "Package #1");
        cv.put(ConstantsPackage.STATUS, 0);
        cv.put(ConstantsPackage.SIZE_X, 50);
        cv.put(ConstantsPackage.SIZE_Y, 35);
        cv.put(ConstantsPackage.SIZE_Z, 40);
        cv.put(ConstantsPackage.WEIGHT, 4);
        cv.put(ConstantsPackage.DATE, Calendar.getInstance().getTimeInMillis());

        db.insert(ConstantsPackage.TABLE_NAME, null, cv);
        cv.clear();

        cv.put(ConstantsPackage.SENDER, 2);
        cv.put(ConstantsPackage.RECIPIENT, 4);
        cv.put(ConstantsPackage.NAME, "Package #2");
        cv.put(ConstantsPackage.STATUS, 0);
        cv.put(ConstantsPackage.SIZE_X, 200);
        cv.put(ConstantsPackage.SIZE_Y, 10);
        cv.put(ConstantsPackage.SIZE_Z, 55);
        cv.put(ConstantsPackage.WEIGHT, 15);
        cv.put(ConstantsPackage.DATE, new GregorianCalendar(2017, 8, 10).getTimeInMillis());

        db.insert(ConstantsPackage.TABLE_NAME, null, cv);
        cv.clear();

        cv.put(ConstantsPackage.SENDER, 5);
        cv.put(ConstantsPackage.RECIPIENT, 7);
        cv.put(ConstantsPackage.NAME, "Package #3");
        cv.put(ConstantsPackage.STATUS, 1);
        cv.put(ConstantsPackage.SIZE_X, 10);
        cv.put(ConstantsPackage.SIZE_Y, 10);
        cv.put(ConstantsPackage.SIZE_Z, 35);
        cv.put(ConstantsPackage.WEIGHT, 8.5);
        cv.put(ConstantsPackage.DATE, new GregorianCalendar(2017, 06, 10).getTimeInMillis());

        db.insert(ConstantsPackage.TABLE_NAME, null, cv);*/

        /*RuntimeExceptionDao<Person, Integer> personDao = dbHelper.getPersonDataDao();
        for (int i = 1; i <9 ; i++) {
            personDao.create(new Person(i, "Person " + i, "(999)-555-11-1"+i, "person" + i + "@mail.ru",
                    "Pushkina street, house #" + i, "", new double[]{51.65712, 39.18995}));
        }

        List<Person> persons = personDao.queryForAll();

        RuntimeExceptionDao<Package, Integer> packageDao = dbHelper.getPackageDataDao();

        packageDao.create(new Package(0, persons.get(0), persons.get(2), "Package #1", Calendar.getInstance(),
                new double[]{10, 10, 35}, 8.5));
        packageDao.create(new Package(0, persons.get(1), persons.get(2), "Package #2", new GregorianCalendar(2017, 06, 10),
                new double[]{50, 11, 20}, 14));
        packageDao.create(new Package(1, persons.get(2), persons.get(4), "Package #3", Calendar.getInstance(),
                new double[]{5, 5, 5}, 1));
        packageDao.queryForAll();

        OpenHelperManager.releaseHelper();*/

        List<Person> persons = new ArrayList<>();
        for (int i = 1; i <9 ; i++) {
            persons.add(new Person(1, "Person " + i, "(999)555-11-1"+i, "person" + i + "@mail.ru",
                    "Pushkina street, house #" + i, "", new double[]{51.65712, 39.18995}));
            persons.get(i-1).save();
        }
        Package pkg1 = new Package(0, persons.get(0), persons.get(2), "Package #1", Calendar.getInstance(),
                new double[]{10, 10, 35}, 8.5);
        Package pkg2 = new Package(0, persons.get(1), persons.get(2), "Package #2", new GregorianCalendar(2017, 06, 10),
                new double[]{50, 11, 20}, 14);
        Package pkg3 = new Package(1, persons.get(2), persons.get(4), "Package #3", Calendar.getInstance(),
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
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_OR_EDIT &&
                data.hasExtra("jsonPackageChild")) {
            String jsonPackage = data.getStringExtra("jsonPackageChild");
            Package pkg = new Gson().fromJson(jsonPackage, Package.class);
            manager.add(pkg);
        }
    }
}
