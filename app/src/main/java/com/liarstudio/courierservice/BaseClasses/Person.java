package com.liarstudio.courierservice.BaseClasses;


public class Person {
    int type;
    private String address;
    private String name;
    private String email;
    private String phone;
    private String companyName;
    private double[] coordinates;


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


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }



}
