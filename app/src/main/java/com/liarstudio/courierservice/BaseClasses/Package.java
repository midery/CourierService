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

    /*
    ****** FIELD AREA ******
    */



    @NotNull
    private int status;

    @NotNull
    private Person sender;

    @NotNull
    private Person recipient;

    @NotNull
    private String name;

    private double sizeX;
    private double sizeY;
    private double sizeZ;



    @NotNull
    private double weight;

    @NotNull
    private Calendar date;


    private int courierId;

    private double price = 0;

    private String commentary;

    @Ignore
    private transient double sizeCoefficient = 0.3937;
    @Ignore
    private transient double weightCoefficient = 2.2;

    /*
    ****** CONSTRUCTOR AREA ******
    */


    public Package()  {
        sender = new Person();
        recipient = new Person();
        sizeX = 0;
        sizeY = 0;
        sizeZ = 0;
        commentary = "";
    }
    public Package(int status, Person sender, Person recipient, String name, Calendar date){
        this.status = status;
        this.sender = sender;
        this.recipient = recipient;
        this.name = name;
        this.date = date;
        commentary = "";
    }



    public Package(int status, Person sender, Person recipient, String name, Calendar date, double[] sizes, double weight){
        this(status, sender, recipient, name, date);
        this.sizeX = sizes[0];
        this.sizeY = sizes[1];
        this.sizeZ = sizes[2];
        this.weight = weight;
    }
    public Package(int status, int courierId, Person sender, Person recipient, String name, Calendar date, double[] sizes, double weight){
        this(status, sender, recipient, name, date);
        this.sizeX = sizes[0];
        this.sizeY = sizes[1];
        this.sizeZ = sizes[2];
        this.weight = weight;
        this.courierId = courierId;
        commentary = "";
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
        return SIZE_PROGRAM_STATE == 0 ? new double[] {sizeX, sizeY, sizeZ} :
                new double[]{
                        round(sizeX *sizeCoefficient),
                        round(sizeY *sizeCoefficient),
                        round(sizeZ *sizeCoefficient)
        };
    }
    public void setSizes(double[] sizes) {
        if (SIZE_PROGRAM_STATE == 0 ) {
            this.sizeX = sizes[0];
            this.sizeY = sizes[1];
            this.sizeZ = sizes[2];
        }
        else {
            this.sizeX = round(sizes[0]/sizeCoefficient);
            this.sizeY = round(sizes[1]/sizeCoefficient);
            this.sizeZ = round(sizes[2]/sizeCoefficient);

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



        double v = sizeX * sizeY * sizeZ /(double)1000000;


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

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }



}
