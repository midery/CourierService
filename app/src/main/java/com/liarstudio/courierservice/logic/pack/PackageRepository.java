package com.liarstudio.courierservice.logic.pack;

import android.support.annotation.NonNull;

import com.liarstudio.courierservice.entitiy.pack.Package;
import com.liarstudio.courierservice.entitiy.person.Person;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Collection;


public class PackageRepository extends ArrayList<Package> {


    public PackageRepository() {}

    public PackageRepository(@NonNull Collection<? extends Package> c) {
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
