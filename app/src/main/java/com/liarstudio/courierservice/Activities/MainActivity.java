package com.liarstudio.courierservice.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.Database.ConstantsPackage;
import com.liarstudio.courierservice.Database.ConstantsPerson;
import com.liarstudio.courierservice.Database.DBHelper;
import com.liarstudio.courierservice.Database.PackageDB;
import com.liarstudio.courierservice.Database.PackageList;
import com.liarstudio.courierservice.PackageFragmentPageAdapter;
import com.liarstudio.courierservice.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class  MainActivity extends AppCompatActivity {

    /*
    ****** STATIC CONSTANT AREA ******
    */


    public static final int REQUEST_ADD_OR_EDIT = 1;
    public static final int REQUEST_MAP = 2;

    public static final String VOL_COEFFICIENT = "vol_coefficient";
    public static final String WEIGHT_COEFFICIENT = "weight_coefficient";
    public static final String PREFERENCES_FILENAME = "preferences_data";
    public static final String ON_FIRST_LAUNCH = "first_launch";

    /*
    ****** FIELDS AREA ******
    */


    DBHelper dbHelper;
    TabLayout tabLayout;
    PackageFragmentPageAdapter manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadCoefficients();
        dbHelper = new DBHelper(this);
        onFirstLaunch();



        PackageList packages = new PackageList(dbHelper);
        packages.loadFromDB();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        manager = new PackageFragmentPageAdapter(getSupportFragmentManager(), packages);

        viewPager.setAdapter(manager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

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
                Intent addIntent = new Intent(this, PackageEdit.class);
                startActivityForResult(addIntent, REQUEST_ADD_OR_EDIT);
                return true;
            case R.id.itemSettings:
                Intent settingsIntent  = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
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
            PackageDB pkg = new Gson().fromJson(jsonPackage, PackageDB.class);
            manager.add(position, pkg);
        }
    }


    /*
    ****** DATABASE AREA ******
    */

    //Наполнение базы данных несколькими посылками
    void addToDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();


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

        db.insert(ConstantsPackage.TABLE_NAME, null, cv);

    }


    //Просмотр таблицы людей
    void readPersonData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(ConstantsPerson.TABLE_NAME, null, null, null, null, null, null, null);
        if (cursor.moveToNext())
        {
            int idCol = cursor.getColumnIndex(ConstantsPerson.ID);
            int nameCol = cursor.getColumnIndex(ConstantsPerson.NAME);
            int addressCol = cursor.getColumnIndex(ConstantsPerson.ADDRESS);
            int emailCol = cursor.getColumnIndex(ConstantsPerson.EMAIL);
            int phoneCol = cursor.getColumnIndex(ConstantsPerson.PHONE);
            int typeCol  = cursor.getColumnIndex(ConstantsPerson.TYPE);
            int companyCol = cursor.getColumnIndex(ConstantsPerson.COMPANY_NAME);
            int xCol  = cursor.getColumnIndex(ConstantsPerson.COORDINATE_X);
            int yCol = cursor.getColumnIndex(ConstantsPerson.COORDINATE_Y);


            do {
                Log.d("-", "PERSON: " + cursor.getInt(idCol) + " ; " +
                        cursor.getString(nameCol) + " ; " + cursor.getString(emailCol) + " ; " +
                        cursor.getString(addressCol) + " ; " + cursor.getString(phoneCol) + " ; "
                        + cursor.getInt(typeCol) + " ; " + cursor.getString(companyCol)
                        + cursor.getDouble(xCol) + " ; "+ cursor.getDouble(yCol));
            } while (cursor.moveToNext());
        }

    }
    //Просмотр таблицы посылок
    void readPackageData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cur = db.query(ConstantsPackage.TABLE_NAME, null, null, null, null, null, null, null);
        if (cur.moveToNext())
        {
            int idCol = cur.getColumnIndex(ConstantsPackage.PACKAGE_ID);
            int nameCol = cur.getColumnIndex(ConstantsPackage.NAME);
            int statusCol = cur.getColumnIndex(ConstantsPackage.STATUS);
            int xCol = cur.getColumnIndex(ConstantsPackage.SIZE_X);
            int yCol = cur.getColumnIndex(ConstantsPackage.SIZE_Y);
            int zCol = cur.getColumnIndex(ConstantsPackage.SIZE_Z);
            int weightCol = cur.getColumnIndex(ConstantsPackage.WEIGHT);
            int dateCol = cur.getColumnIndex(ConstantsPackage.DATE);
            int commentaryCol = cur.getColumnIndex(ConstantsPackage.COMMENTARY);
            int senderCol = cur.getColumnIndex(ConstantsPackage.SENDER);
            int recipientCol = cur.getColumnIndex(ConstantsPackage.RECIPIENT);


            do {
                Log.d("-", "PACKAGE: " + cur.getInt(idCol) + " ; " +
                        cur.getString(nameCol) + " ; " + cur.getInt(statusCol) + " ; " +
                        cur.getInt(senderCol) + " ; " + cur.getInt(recipientCol) + " ; ");
            } while (cur.moveToNext());
        }
    }


    /*
    ****** PREFERENCE AREA ******
    */


    void onFirstLaunch() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        if (!pref.getBoolean(ON_FIRST_LAUNCH, false)) {
            addToDB();
            pref.edit().putBoolean(ON_FIRST_LAUNCH, true).commit();
        }
    }
    //Считываем коэффициенты из Preferences
    void loadCoefficients() {
        SharedPreferences sharedPref = getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE);
        Package.WEIGHT_PROGRAM_STATE = sharedPref.getInt(WEIGHT_COEFFICIENT, 0);
        Package.SIZE_PROGRAM_STATE = sharedPref.getInt(VOL_COEFFICIENT, 0);
    }
}
