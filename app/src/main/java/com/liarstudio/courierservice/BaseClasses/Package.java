package com.liarstudio.courierservice.BaseClasses;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Package {
    public int status;
    public Person sender;
    public Person recipient;

    public String name;
    public int[] dimensions;
    public double weight;
    public Calendar date;

    public int price;
    public String commentary;

    public Package()  {

    }
    public Package(int status, Person sender, Person recipient, String name, Calendar date){
        this.status = status;
        this.sender = sender;
        this.recipient = recipient;
        this.name = name;
        this.date = date;
    }

    public Package(int status, Person sender, Person recipient, String name, Calendar date, int[] dimensions, double weight){
        this.status = status;
        this.sender = sender;
        this.recipient = recipient;
        this.name = name;
        this.date = date;
        this.dimensions = dimensions;
        this.weight = weight;
    }

    public String getStringDate() {
        return getStringDate("dd/MM/yyyy");
    }
    public String getStringDate(String format)
    {
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date.getTime());

    }

    public static String getStringDate(Calendar date, String format) {
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date.getTime());
    }
    public static String getStringDate(Calendar date) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date.getTime());
    }

    public void reloadValues(Person sender, Person recipient, String name, Calendar date) {
        this.sender = sender;
        this.recipient = recipient;
        this.name = name;
        this.date = date;
    }
    public void reloadValues(Person sender, Person recipient, String name, Calendar date, int[] dimensions, double weight) {
        this.sender = sender;
        this.recipient = recipient;
        this.name = name;
        this.date = date;
        this.dimensions = dimensions;
        this.weight = weight;
    }

    public double calculatePrice() {
        double v = dimensions[1]*dimensions[2]*dimensions[3]/1000000;
        return v;
    }
}
