package com.driver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        int hour = Integer.parseInt(deliveryTime.substring(0, 2));
        int minute = Integer.parseInt(deliveryTime.substring(3));
        this.deliveryTime = (hour*60) + minute;

    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
