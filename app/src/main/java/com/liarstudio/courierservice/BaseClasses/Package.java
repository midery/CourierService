package com.liarstudio.courierservice.BaseClasses;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Package {
    public int status;
    public Person sender;
    public Person recipient;

    public String name;
    public int[] dimensions;
    public double weight;
    public Calendar date;

    public static double tariff = 200;

    //http://www.bagagesdumonde.com/en/lost-and-found/faq/79
    public static double cargoRate = 250;



    public double price = 0;
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

    public void calculatePrice() {

        double v = dimensions[0]*dimensions[1]*dimensions[2]/(double)1000000;
        double g = weight/v;

        double fastShippingMultiplier = 1;

        //Доставка за 2 дня
        Calendar day = Calendar.getInstance();
        if (day.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) - day.get(Calendar.DAY_OF_YEAR) < 2)
                fastShippingMultiplier *= 2;
        }


        if (g > 250)
            price = weight * tariff * fastShippingMultiplier;
        else
            price = v * 250 * tariff * fastShippingMultiplier;
    }

    public static double calculatePrice(int width, int height, int depth, double weight, Calendar date) {
        double v = width*height*depth/(double)1000000;
        double g = weight/v;
        double fastShippingMultiplier = 1;

        //Доставка за 2 дня
        Calendar day = Calendar.getInstance();
        if (day.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) - day.get(Calendar.DAY_OF_YEAR) < 2)
                fastShippingMultiplier *= 2;
        }

        //1m3 = 250kg
        if (g > cargoRate)
            return weight * tariff * fastShippingMultiplier;
        else
            return v * cargoRate * tariff * fastShippingMultiplier;
    }
}
