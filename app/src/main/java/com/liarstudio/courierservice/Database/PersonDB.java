package com.liarstudio.courierservice.Database;

import android.provider.BaseColumns;

import com.liarstudio.courierservice.BaseClasses.Person;

/**
 * Created by M1DERY on 08.07.2017.
 */

public class PersonDB extends Person {

    private int ID;

    public int getID() {    return ID;  }

    public static abstract class FeedPerson implements BaseColumns {
        public static final String TABLE_NAME = "person";
        public static final String COLUMN_ID = "person_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COMPANY_NAME = "company_name";
        public static final String COLUMN_COORDINATE_X = "coordinate_x";
        public static final String COLUMN_COORDINATE_Y = "coordinate_y";

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ", ";

        public static final String SQL_CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " text not null, " +
                COLUMN_ADDRESS + " text not null, " +
                COLUMN_EMAIL + " text, " +
                COLUMN_PHONE + " text not null, " +
                COLUMN_TYPE + " integer not null, " +
                COLUMN_COMPANY_NAME + " text, " +
                COLUMN_COORDINATE_X + " double not null, " +
                COLUMN_COORDINATE_Y + " double not null);";


        public static final String SQL_DELETE_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
