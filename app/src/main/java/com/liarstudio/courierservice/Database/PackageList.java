package com.liarstudio.courierservice.Database;

import android.support.annotation.NonNull;

import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.BaseClasses.Person;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Collection;


public class PackageList extends ArrayList<Package> {


    public PackageList() {}

    public PackageList(@NonNull Collection<? extends Package> c) {
        super(c);
    }



    /*
    * Функция обновления данных на устройстве
    * Удаляет все данные из локальной БД и перезаписывает ее с посылками из текущего списка
     */
    public void reloadAll() {
        SugarRecord.deleteAll(Person.class);
        SugarRecord.deleteAll(Package.class);

        for (Package pack : this) {
            pack.getSender().save();
            pack.getRecipient().save();
            pack.save();
        }
    }
}
