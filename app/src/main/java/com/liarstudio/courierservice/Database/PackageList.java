package com.liarstudio.courierservice.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.BaseClasses.Person;

import java.util.ArrayList;
import java.util.Calendar;


public class PackageList extends ArrayList<PackageDB> {

    private DBHelper dbHelper;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public PackageList() {}

    public PackageList(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }



    @Override
    public boolean add(PackageDB aPackage) {
        insertToDB(aPackage);
        return super.add(aPackage);
    }

    public boolean insert(PackageDB aPackage) {
        return super.add(aPackage);
    }


    @Override
    public void add(int index, PackageDB element) {

        insertToDB(element);
        super.add(index, element);
    }

    @Override
    public PackageDB set(int index, PackageDB element) {
        //updateInDB()
        updateInDB(element);
        return super.set(index, element);
    }

    public void loadFromDB() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cur = db.query(ConstantsPackage.TABLE_NAME, null, null, null, null, null, null);
        if (cur.moveToFirst()) {
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
                int senderID = cur.getInt(senderCol);
                int recipientID = cur.getInt(recipientCol);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(cur.getLong(dateCol));

                PackageDB pkg = new PackageDB(cur.getInt(idCol), cur.getInt(statusCol), senderID, recipientID,
                        cur.getString(nameCol), calendar, new double[]{cur.getDouble(xCol), cur.getDouble(yCol), cur.getDouble(zCol)},
                        cur.getDouble(weightCol), cur.getString(commentaryCol));
                pkg.setSender(loadPersonByID(db, senderID));
                pkg.setRecipient(loadPersonByID(db, recipientID));

                super.add(pkg);
            } while (cur.moveToNext());
        }
    }
    private Person loadPersonByID(SQLiteDatabase db, int id) {
        Cursor cur = db.query(ConstantsPerson.TABLE_NAME, null, "person_id="+id, null, null, null, null, null);
        if (cur.moveToFirst()) {
            int nameCol = cur.getColumnIndex(ConstantsPerson.NAME);
            int addressCol = cur.getColumnIndex(ConstantsPerson.ADDRESS);
            int emailCol = cur.getColumnIndex(ConstantsPerson.EMAIL);
            int phoneCol = cur.getColumnIndex(ConstantsPerson.PHONE);
            int typeCol = cur.getColumnIndex(ConstantsPerson.TYPE);
            int companyCol = cur.getColumnIndex(ConstantsPerson.COMPANY_NAME);
            int xCol = cur.getColumnIndex(ConstantsPerson.COORDINATE_X);
            int yCol = cur.getColumnIndex(ConstantsPerson.COORDINATE_Y);


            return new Person(cur.getInt(typeCol), cur.getString(nameCol),
                    cur.getString(phoneCol), cur.getString(emailCol),
                    cur.getString(addressCol), cur.getString(companyCol),
                    new double[]{cur.getDouble(xCol), cur.getDouble(yCol)});
        }
        return  null;
    }

    private int insertToDB(Person person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ConstantsPerson.NAME, person.getName());
        cv.put(ConstantsPerson.ADDRESS, person.getAddress());
        cv.put(ConstantsPerson.EMAIL, person.getEmail());
        cv.put(ConstantsPerson.PHONE, person.getPhone());
        cv.put(ConstantsPerson.TYPE, person.getType());
        cv.put(ConstantsPerson.COMPANY_NAME, person.getCompanyName());
        cv.put(ConstantsPerson.COORDINATE_X, person.getCoordinates()[0]);
        cv.put(ConstantsPerson.COORDINATE_Y, person.getCoordinates()[1]);

        return (int)db.insert(ConstantsPerson.TABLE_NAME, null, cv);

    }
    private int insertToDB(PackageDB pkg) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        double[] sizes = pkg.getSizesConst();
        cv.put(ConstantsPackage.SENDER, insertToDB(pkg.getSender()));
        cv.put(ConstantsPackage.RECIPIENT, insertToDB(pkg.getRecipient()));
        cv.put(ConstantsPackage.NAME, pkg.getName());
        cv.put(ConstantsPackage.STATUS, pkg.getStatus());
        cv.put(ConstantsPackage.SIZE_X, sizes[0]);
        cv.put(ConstantsPackage.SIZE_Y, sizes[1]);
        cv.put(ConstantsPackage.SIZE_Z, sizes[2]);
        cv.put(ConstantsPackage.WEIGHT, pkg.getWeightConst());
        cv.put(ConstantsPackage.DATE, pkg.getDate().getTimeInMillis());
        cv.put(ConstantsPackage.PRICE, pkg.getPrice());
        cv.put(ConstantsPackage.COMMENTARY, pkg.getCommentary());

        return (int)db.insert(ConstantsPackage.TABLE_NAME, null, cv);
    }

    private int updateInDB(int id, Person person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ConstantsPerson.NAME, person.getName());
        cv.put(ConstantsPerson.ADDRESS, person.getAddress());
        cv.put(ConstantsPerson.EMAIL, person.getEmail());
        cv.put(ConstantsPerson.PHONE, person.getPhone());
        cv.put(ConstantsPerson.TYPE, person.getType());
        cv.put(ConstantsPerson.COMPANY_NAME, person.getCompanyName());
        cv.put(ConstantsPerson.COORDINATE_X, person.getCoordinates()[0]);
        cv.put(ConstantsPerson.COORDINATE_Y, person.getCoordinates()[1]);

        return db.update(ConstantsPerson.TABLE_NAME, cv, "person_id=?",
                new String[] {Integer.toString(id)});

    }

    private int updateInDB(PackageDB pkg) {
        updateInDB(pkg.getSenderID(), pkg.getSender());
        updateInDB(pkg.getRecipientID(), pkg.getRecipient());


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        double[] sizes = pkg.getSizesConst();
        int id = pkg.getId();



        cv.put(ConstantsPackage.PACKAGE_ID, id);
        cv.put(ConstantsPackage.NAME, pkg.getName());
        cv.put(ConstantsPackage.STATUS, pkg.getStatus());
        cv.put(ConstantsPackage.SIZE_X, sizes[0]);
        cv.put(ConstantsPackage.SIZE_Y, sizes[1]);
        cv.put(ConstantsPackage.SIZE_Z, sizes[2]);
        cv.put(ConstantsPackage.WEIGHT, pkg.getWeightConst());
        cv.put(ConstantsPackage.DATE, pkg.getDate().getTimeInMillis());
        cv.put(ConstantsPackage.PRICE, pkg.getPrice());
        cv.put(ConstantsPackage.COMMENTARY, pkg.getCommentary());

        return db.update(ConstantsPackage.TABLE_NAME, cv, "package_id=?",
                new String[] {Integer.toString(id)});

    }

}
