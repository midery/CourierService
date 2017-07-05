package com.liarstudio.courierservice.BaseClasses;


import java.util.Date;

public class Package {
    public int type;
    public Person sender;
    public Person recipient;

    public String name;
    public int[] dimensions;
    public double weight;
    public Date date;

    public int price;

    public Package()  {

    }
}
