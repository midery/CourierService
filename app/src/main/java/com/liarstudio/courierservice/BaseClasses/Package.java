package com.liarstudio.courierservice.BaseClasses;


import java.util.Date;

public class Package {
    public int status;
    public Person sender;
    public Person recipient;

    public String name;
    public int[] dimensions;
    public double weight;
    public Date date;

    public int price;

    public Package()  {

    }
    public Package(int status, Person sender, Person recipient, String name, Date date){
        this.status = status;
        this.sender = sender;
        this.recipient = recipient;
        this.name = name;
        this.date = date;
    }
}
