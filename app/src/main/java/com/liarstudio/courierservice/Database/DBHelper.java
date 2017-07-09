package com.liarstudio.courierservice.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by M1DERY on 07.07.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "CourierService.db";



    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PersonDB.FeedPerson.SQL_CREATE_STATEMENT + PackageDB.FeedPackage.SQL_CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PersonDB.FeedPerson.SQL_DELETE_STATEMENT + PackageDB.FeedPackage.SQL_DELETE_STATEMENT);
        onCreate(db);
    }
}
