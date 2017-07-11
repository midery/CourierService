package com.liarstudio.courierservice.Database;

import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.BaseClasses.Person;

import java.util.Calendar;

public class PackageDB extends Package {

    private int id;

    public int getId() {
        return id;
    }

    int senderID;
    int recipientID;

    public int getSenderID() {
        return senderID;
    }

    public int getRecipientID() {
        return recipientID;
    }

    public void setSenderID(int senderID) {this.senderID = senderID;}

    public void setRecipientID(int recipientID) {this.recipientID = recipientID;}






    public PackageDB() {super();}

    public PackageDB(int status, Person sender, Person recipient, String name, Calendar date) {
        super(status, sender, recipient, name, date);
    }

    public PackageDB(int status, Person sender, Person recipient, String name, Calendar date, double[] sizes, double weight){
        super(status, sender, recipient, name, date);
        this.sizes = sizes;
        this.weight = weight;
    }


    public PackageDB(int id, int status, Person sender, Person recipient, String name, Calendar date, double[] sizes, double weight){
        this(status, sender, recipient, name, date, sizes, weight);
        this.id = id;
    }
    public PackageDB(int id, int status, int senderID, int recipientID, String name, Calendar date, double[] sizes, double weight, String commentary){
        this.id = id;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.status = status;
        this.name = name;
        this.date = date;
        this.commentary = commentary;
        this.sizes = sizes;
        this.weight = weight;
        //setSizes(sizes);
        //setWeight(weight);
    }


}
