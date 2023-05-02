package com.example.new61d;

import android.net.Uri;

import java.util.Date;

public class OrderModel {
    private String receiver_name;
    private String sender_name;
    private String pick_up_date;
    private String pick_up_time;
    private String pick_up_location;
    private String good_type;
    private String weight,width,length,height;
    private String vehicle_type;
    private String order_iamge_Uri;

    public OrderModel(){

    }



    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getPick_up_date() {
        return pick_up_date;
    }

    public void setPick_up_date(String pick_up_date) {
        this.pick_up_date = pick_up_date;
    }

    public String getPick_up_time() {
        return pick_up_time;
    }

    public void setPick_up_time(String pick_up_time) {
        this.pick_up_time = pick_up_time;
    }

    public String getPick_up_location() {
        return pick_up_location;
    }

    public void setPick_up_location(String pick_up_location) {
        this.pick_up_location = pick_up_location;
    }

    public String getGood_type() {
        return good_type;
    }

    public void setGood_type(String good_type) {
        this.good_type = good_type;
    }



    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public Uri getOrder_iamge_Uri() {
        return order_iamge_Uri;
    }

    public void setOrder_iamge_Uri(Uri order_iamge_Uri) {
        this.order_iamge_Uri = order_iamge_Uri;
    }

    @Override
    public String toString() {
        return sender_name +" deliver " +good_type+
                " on " + pick_up_date +
                " by " + vehicle_type ;
    }
}