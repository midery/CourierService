package com.liarstudio.courierservice.BaseClasses;

import java.util.Date;

public class Package {
    String name;
    int[] dimensions;
    double weight;
    Date date;

    public Package() {
        dimensions = new int[3];
    }

    public Package(String aName, int[] aDimensions, double aWeight, Date aDate) {
        name = aName;
        dimensions = aDimensions;
        weight = aWeight;
        date = aDate;
    }

}
