package com.liarstudio.courierservice.BaseClasses;


public class Person {
    int type;
    String name;
    String email;
    String phone;
    String companyName;
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
