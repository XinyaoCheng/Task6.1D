package com.example.new61d;

import android.net.Uri;

import java.util.Date;

public class OrderModel {
    private String receiver_name;
    private String sender_name;
    private String pick_up_date;
    private String pick_up_time;
    private String drop_off_location;
    private String good_type;
    private String weight,width,length,height;
    private String vehicle_type;
    private String order_iamge_name;
    private boolean finished;

    public OrderModel(){

    }

    public OrderModel(String receiver_name, String sender_name, String pick_up_date, String pick_up_time, String drop_off_location, String good_type, String weight, String width, String length, String height, String vehicle_type, String order_iamge_name, boolean finished) {
        this.receiver_name = receiver_name;
        this.sender_name = sender_name;
        this.pick_up_date = pick_up_date;
        this.pick_up_time = pick_up_time;
        this.drop_off_location = drop_off_location;
        this.good_type = good_type;
        this.weight = weight;
        this.width = width;
        this.length = length;
        this.height = height;
        this.vehicle_type = vehicle_type;
        this.order_iamge_name = order_iamge_name;
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getOrder_iamge_name() {
        return order_iamge_name;
    }

    public void setOrder_iamge_name(String order_iamge_name) {
        this.order_iamge_name = order_iamge_name;
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

    public String getDrop_off_location() {
        return drop_off_location;
    }

    public void setDrop_off_location(String drop_off_location) {
        this.drop_off_location = drop_off_location;
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

    @Override
    public String toString() {
        return sender_name +" deliver " +good_type+
                " on " + pick_up_date +
                " to " + drop_off_location+
                " by "   + vehicle_type ;
    }


    public String toString(int i) {
        return "OrderModel{" +
                "receiver_name='" + receiver_name + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", pick_up_date='" + pick_up_date + '\'' +
                ", pick_up_time='" + pick_up_time + '\'' +
                ", drop_off_location='" + drop_off_location + '\'' +
                ", good_type='" + good_type + '\'' +
                ", weight='" + weight + '\'' +
                ", width='" + width + '\'' +
                ", length='" + length + '\'' +
                ", height='" + height + '\'' +
                ", vehicle_type='" + vehicle_type + '\'' +
                ", order_iamge_name='" + order_iamge_name + '\'' +
                ", finished=" + finished +
                '}';
    }
}