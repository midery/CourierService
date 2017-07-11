package com.liarstudio.courierservice.Database;

import android.provider.BaseColumns;

public abstract class ConstantsPerson implements BaseColumns {
    public static final String TABLE_NAME = "person";
    public static final String ID = "person_id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String TYPE = "type";
    public static final String COMPANY_NAME = "company_name";
    public static final String COORDINATE_X = "coordinate_x";
    public static final String COORDINATE_Y = "coordinate_y";


    public static final String SQL_CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " text not null, " +
            ADDRESS + " text not null, " +
            EMAIL + " text, " +
            PHONE + " text not null, " +
            TYPE + " integer not null, " +
            COMPANY_NAME + " text, " +
            COORDINATE_X + " double not null, " +
            COORDINATE_Y + " double not null); ";


    public static final String SQL_DELETE_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

}
