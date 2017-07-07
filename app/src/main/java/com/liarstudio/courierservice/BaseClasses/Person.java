package com.liarstudio.courierservice.BaseClasses;


public class Person {
    int type;
    public String address;
    public String name;
    public String email;
    public String phone;
    public String companyName;
    public double[] coordinates;

    public Person(int type, String name, String phone, String email, String address, String companyName) {
        this(type,name,phone,email, address, companyName, new double[]{0,0});
    }
    public Person(int type, String name, String phone, String email, String address) {
        this(type,name,phone,email, address, "");
    }
    public Person(String name, String phone, String email, String address) {
        this(0, name, phone, email, address);
    }

    public Person(int type, String name, String phone, String email, String address, String companyName, double[] coordinates) {
        this.name = name;
        this.type = type;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.companyName = companyName;
        this.coordinates = coordinates;
    }


}
