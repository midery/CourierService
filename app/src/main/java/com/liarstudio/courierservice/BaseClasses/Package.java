package com.liarstudio.courierservice.BaseClasses;


import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Package extends SugarRecord {


    @NotNull
    private int status;

    @NotNull
    private Person sender;

    @NotNull
    private Person recipient;

    @NotNull
    private String name;

    private double size_x;
    private double size_y;
    private double size_z;



    @NotNull
    private double weight;

    @NotNull
    private Calendar date;


    private double price = 0;

    private String commentary;

    @Ignore
    private double sizeCoefficient = 0.3937;
    @Ignore
    private double weightCoefficient = 2.2;

    /*
    ****** CONSTRUCTOR AREA ******
    */


    public Package()  {
        sender = new Person();
        recipient = new Person();
        size_x = 0;
        size_y = 0;
        size_z = 0;
    }
    public Package(int status, Person sender, Person recipient, String name, Calendar date){
        this.status = status;
        this.sender = sender;
        this.recipient = recipient;
        this.name = name;
        this.date = date;
    }



    public Package(int status, Person sender, Person recipient, String name, Calendar date, double[] sizes, double weight){
        this(status, sender, recipient, name, date);
        this.size_x = sizes[0];
        this.size_y = sizes[1];
        this.size_z = sizes[2];
        this.weight = weight;
        commentary = "";
    }


    public Package(int status, Person sender, Person recipient,String name, Calendar date, double[] sizes, double weight, String commentary){
        this.sender = sender;
        this.recipient = recipient;
        this.status = status;
        this.name = name;
        this.date = date;
        this.commentary = commentary;
        this.size_x = sizes[0];
        this.size_y = sizes[1];
        this.size_z = sizes[2];
        this.weight = weight;
    }

    /*
    ****** STATIC AND HELP METHODS AREA ******
    */

    @Ignore
    public static int SIZE_PROGRAM_STATE = 1;

    @Ignore
    public static int WEIGHT_PROGRAM_STATE = 1;
    @Ignore
    public static double tariff = 200;

    //http://www.bagagesdumonde.com/en/lost-and-found/faq/79
    @Ignore
    public static double cargoRate = 250;




    public static String getStringDate(Calendar date, String format) {
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date.getTime());
    }
    public static String getStringDate(Calendar date) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date.getTime());
    }



    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double round(double value) {
        return round(value, 2);
    }




    /*
    ****** GETTER/SETTER AREA ******
    */




    public int getStatus() {return status;}
    public void setStatus(int status) {this.status = status;}


    public Person getSender() {return sender;}
    public void setSender(Person sender) {this.sender = sender;}

    public Person getRecipient() {return recipient;}
    public void setRecipient(Person recipient) {this.recipient = recipient;}


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getCommentary() {return commentary;}
    public void setCommentary(String commentary) {this.commentary = commentary;}

    public double getWeight() {
        return WEIGHT_PROGRAM_STATE == 0 ? weight :  round(weight*weightCoefficient);

    }


    public void setWeight(double weight) {
        this.weight = WEIGHT_PROGRAM_STATE == 0 ? weight :  round(weight/weightCoefficient);
    }

    public double[] getSizes() {
        return SIZE_PROGRAM_STATE == 0 ? new double[] {size_x, size_y, size_z} :
                new double[]{
                        round(size_x*sizeCoefficient),
                        round(size_y*sizeCoefficient),
                        round(size_z*sizeCoefficient)
        };
    }
    public void setSizes(double[] sizes) {
        if (SIZE_PROGRAM_STATE == 0 ) {
            this.size_x = sizes[0];
            this.size_y = sizes[1];
            this.size_z = sizes[2];
        }
        else {
            this.size_x = round(sizes[0]/sizeCoefficient);
            this.size_y = round(sizes[1]/sizeCoefficient);
            this.size_z = round(sizes[2]/sizeCoefficient);

        }

    }

    public Calendar getDate() { return date; }
    public void setDate(Calendar date) {this.date = date;}

    public String getStringDate() { return getStringDate("dd/MM/yyyy");    }
    public String getStringDate(String format)
    {
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date.getTime());

    }

    public double[] getCoordinates() {
        return recipient.getCoordinates();
    }
    public void setCoordinates(double[] coordinates) { recipient.setCoordinates(coordinates);}


    public void setPrice() {



        double v = size_x* size_y * size_z /(double)1000000;


        double g = weight/v;


        double fastShippingMultiplier = 1;

        //Доставка за 2 дня
        Calendar day = Calendar.getInstance();
        if (day.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) - day.get(Calendar.DAY_OF_YEAR) < 2)
                fastShippingMultiplier *= 2;
        }


        if (g > cargoRate)
            price = weight * tariff * fastShippingMultiplier;
        else
            price = v * cargoRate * tariff * fastShippingMultiplier;
    }
    public double getPrice() {return price;}




}
