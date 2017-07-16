package com.liarstudio.courierservice.Database;

import android.support.annotation.NonNull;

import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.ArrayList;
import java.util.Collection;


public class PackageList extends ArrayList<Package> {


    public PackageList() {}

    public PackageList(@NonNull Collection<? extends Package> c) {
        super(c);
    }




    public boolean addLocally(Package aPackage) {
        int position = 0;

        for (Package pack : this)
        {
            if (aPackage.getDate().compareTo(pack.getDate()) < 0){
                break;
            }
            position++;

        }
        if ((position == this.size()-1) && (aPackage.getDate().compareTo(this.get(position).getDate()) > 0))
            return super.add(aPackage);
        else
            super.add(position, aPackage);
        return true;
    }


    @Override
    public boolean add(Package aPackage) {
        aPackage.save();
        return addLocally(aPackage);
    }

    @Override
    public void add(int index, Package element) {
        element.save();
        super.add(index, element);

    }

    public Package update(Package element) {

        element.save();

        this.remove(element.getId());

        addLocally(element);

        return element;
    }



    public void remove(Long id) {
        for (Package pack : this) {
            if (pack.getId() == id) {
                super.remove(pack);
                break;
            }
        }
    }




}
