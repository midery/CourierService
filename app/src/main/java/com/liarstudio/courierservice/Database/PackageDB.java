package com.liarstudio.courierservice.Database;

import android.provider.BaseColumns;

import com.liarstudio.courierservice.BaseClasses.Package;

/**
 * Created by M1DERY on 08.07.2017.
 */

public class PackageDB extends Package {

    private int id;

    public int getId() {
        return id;
    }

    public PackageDB() {

    }

    public static abstract class FeedPackage implements BaseColumns {
        public static final String TABLE_NAME = "package";
        public static final String COLUMN_ID = "package_id";
        public static final String COLUMN_SENDER = "sender";
        public static final String COLUMN_RECIPIENT = "recipient";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_SIZE_X = "size_x";
        public static final String COLUMN_SIZE_Y = "size_y";
        public static final String COLUMN_SIZE_Z = "size_z";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_COMMENTARY = "commentary";


        public static final String SQL_CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SENDER + " integer not null, " +
                COLUMN_RECIPIENT + " integer not null, " +
                COLUMN_NAME + " text not null, " +
                COLUMN_STATUS + " integer not null, " +
                COLUMN_SIZE_X + " double not null, " +
                COLUMN_SIZE_Y + " double not null, " +
                COLUMN_SIZE_Z + " double not null, " +
                COLUMN_WEIGHT + " double not null, " +
                COLUMN_DATE + " datetime not null, " +
                COLUMN_PRICE + " double not null, " +
                COLUMN_COMMENTARY + " text not null, " +
                "FOREIGN KEY(" + COLUMN_SENDER + ") REFERENCES " + PersonDB.FeedPerson.TABLE_NAME +
                "(" + PersonDB.FeedPerson.COLUMN_ID + ") " +
                "FOREIGN KEY(" + COLUMN_RECIPIENT + ") REFERENCES " + PersonDB.FeedPerson.TABLE_NAME +
                "(" + PersonDB.FeedPerson.COLUMN_ID + "));";

        public static final String SQL_DELETE_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


}
