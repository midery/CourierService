package com.liarstudio.courierservice.BaseClasses;


import com.orm.SugarRecord;

public class Person extends SugarRecord implements Cloneable{
    /*
    ****** FIELD AREA ******
    */

    private int type;

    private String address;
    private String name;

    private String email;

    private String phone;

    private String companyName;

    private double coordinatesX;
    private double coordinatesY;

    /*
    ****** CONSTRUCTOR AREA ******
    */


    public Person() { coordinatesX = 0; coordinatesY = 0; companyName = "";}

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

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    /*
    ****** GETTER/SETTER AREA ******
    */

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
