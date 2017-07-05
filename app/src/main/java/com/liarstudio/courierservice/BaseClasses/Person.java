package com.liarstudio.courierservice.BaseClasses;


public class Person {
    int type;
    public String address;
    public String name;
    public String email;
    public String phone;
    public String companyName;
    //координаты

    public Person(int aType, String aName, String aPhone, String aEmail, String aCompanyName) {
        name = aName;
        type = aType;
        email = aEmail;
        phone = aPhone;
        companyName = aCompanyName;
    }
    public Person(int aType, String aName, String aPhone, String aEmail) {
        this(aType,aName,aPhone,aEmail, "");
    }
    public Person(String aName, String aPhone, String aEmail) {
        this(0, aName, aPhone, aEmail);
    }


}
