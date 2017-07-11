package com.liarstudio.courierservice.Database;

import android.provider.BaseColumns;

/**
 * Created by M1DERY on 10.07.2017.
 */

public abstract class ConstantsPackage  implements BaseColumns {
    public static final String TABLE_NAME = "package";
    public static final String PACKAGE_ID = "package_id";
    public static final String SENDER = "sender";
    public static final String RECIPIENT = "recipient";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String SIZE_X = "size_x";
    public static final String SIZE_Y = "size_y";
    public static final String SIZE_Z = "size_z";
    public static final String WEIGHT = "weight";
    public static final String DATE = "date";
    public static final String PRICE = "price";
    public static final String COMMENTARY = "commentary";


    public static final String SQL_CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "(" +
            PACKAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SENDER + " integer not null, " +
            RECIPIENT + " integer not null, " +
            NAME + " text not null, " +
            STATUS + " integer not null, " +
            SIZE_X + " double not null, " +
            SIZE_Y + " double not null, " +
            SIZE_Z + " double not null, " +
            WEIGHT + " double not null, " +
            DATE + " datetime not null, " +
            PRICE + " double, " +
            COMMENTARY + " text, " +
            "FOREIGN KEY(" + SENDER + ") REFERENCES " + ConstantsPerson.TABLE_NAME +
            "(" + ConstantsPerson.ID + ") " +
            "FOREIGN KEY(" + RECIPIENT + ") REFERENCES " + ConstantsPerson.TABLE_NAME +
            "(" + ConstantsPerson.ID + ")); ";

    public static final String SQL_DELETE_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";
}

