package com.liarstudio.courierservice.BaseClasses;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Package {
    /*
    ****** PRIVATE FIELDS AREA ******
    */


    private int status;
    private Person sender;
    private Person recipient;


    private String name;
    private double[] sizes;
    private double weight;
    private Calendar date;



    private double price = 0;
    private String commentary;


    int weightOnCreationState;
    int sizeOnCreationState;
    private double sizeCoefficient = 0.3937;
    private double weightCoefficient = 2.2;

    /*
    ****** CONSTRUCTOR AREA ******
    */


    public Package()  {
        weightOnCreationState = WEIGHT_PROGRAM_STATE == 0 ? 0 : 1;
        sizeOnCreationState = SIZE_PROGRAM_STATE == 0 ? 0 : 1;
    }
    public Package(int status, Person sender, Person recipient, String name, Calendar date){
        this();
        this.status = status;
        this.sender = sender;
        this.recipient = recipient;
        this.name = name;
        this.date = date;
    }
    public Package(int status, Person sender, Person recipient, String name, Calendar date, double[] sizes, double weight){
        this(status, sender, recipient, name, date);
        setSizes(sizes);
        setWeight(weight);
        setPrice();
    }


    /*
    ****** STATIC AREA ******
    */
    public static int SIZE_PROGRAM_STATE = 1;
    public static int WEIGHT_PROGRAM_STATE = 1;

    public static double tariff = 200;

    //http://www.bagagesdumonde.com/en/lost-and-found/faq/79
    public static double cargoRate = 250;



    public static double calculatePrice(double width, double height, double depth, double weight, Calendar date) {
        width *= SIZE_PROGRAM_STATE; height *= SIZE_PROGRAM_STATE; depth *= SIZE_PROGRAM_STATE;
        weight *= WEIGHT_PROGRAM_STATE;
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

    public static String getStringDate(Calendar date, String format) {
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date.getTime());
    }
    public static String getStringDate(Calendar date) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date.getTime());
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
        return weightOnCreationState ==WEIGHT_PROGRAM_STATE ? weight :
                weightOnCreationState ==0 ? weight*weightCoefficient : weight/weightCoefficient;

    }

    public void setWeight(double weight) {
        this.weight = weightOnCreationState ==WEIGHT_PROGRAM_STATE ? weight :
                weightOnCreationState ==0 ? weight/weightCoefficient :weight*weightCoefficient;
    }

    public double[] getSizes() {
        if (sizeOnCreationState == SIZE_PROGRAM_STATE)
            return sizes;
        if (sizeOnCreationState == 0)
            return new double[] {sizes[0]* sizeCoefficient, sizes[1]* sizeCoefficient, sizes[2]* sizeCoefficient};
        else
            return new double[] {sizes[0]/ sizeCoefficient, sizes[1]/ sizeCoefficient, sizes[2]/ sizeCoefficient};
    }
    public void setSizes(double[] sizes) {
        if (sizeOnCreationState == SIZE_PROGRAM_STATE) {
            this.sizes = sizes;
            return;
        }
        if (sizeOnCreationState == 0)
            this.sizes = new double[]{sizes[0] / sizeCoefficient, sizes[1] / sizeCoefficient, sizes[2] / sizeCoefficient};
        else
            this.sizes = new double[]{sizes[0] * sizeCoefficient, sizes[1] * sizeCoefficient, sizes[2] * sizeCoefficient};


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



    public void setPrice() {



        double v = sizes[0]* sizes[1]* sizes[2]/(double)1000000;

        if (sizeOnCreationState == 1)
            v = v/(sizeCoefficient * sizeCoefficient * sizeCoefficient);

        double weightBalanced = weightOnCreationState == 0 ? weight : weight/weightCoefficient;

        double g = weightBalanced/v;


        double fastShippingMultiplier = 1;

        //Доставка за 2 дня
        Calendar day = Calendar.getInstance();
        if (day.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) - day.get(Calendar.DAY_OF_YEAR) < 2)
                fastShippingMultiplier *= 2;
        }


        if (g > 250)
            price = weightBalanced * tariff * fastShippingMultiplier;
        else
            price = v * 250 * tariff * fastShippingMultiplier;
    }
    public double getPrice() {return price;}


}
