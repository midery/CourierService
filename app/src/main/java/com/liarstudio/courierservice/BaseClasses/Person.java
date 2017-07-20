package com.liarstudio.courierservice.BaseClasses;


import com.orm.SugarRecord;

public class Person extends SugarRecord{

    protected int type;

    protected String address;
    protected String name;

    protected String email;

    protected String phone;

    protected String companyName;

    protected double coordinatesX;
    protected double coordinatesY;


    public Person() { coordinatesX = 0; coordinatesY = 0;}

    public Person(int type, String name, String phone, String email, String address, String companyName, double[] coordinates) {
        this.name = name;
        this.type = type;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.companyName = companyName;
        this.coordinatesX = coordinates[0];
        this.coordinatesY = coordinates[1];
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
        return new double[]{coordinatesX, coordinatesY};
    }

    public void setCoordinates(double[] coordinates) {
        coordinatesX = coordinates[0];
        coordinatesY = coordinates[1];
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }



}
