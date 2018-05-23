package com.example.akb05.project;

import android.net.Uri;

import java.net.URI;
import java.util.Date;

/**
 * Created by akb05 on 2018-05-22.
 */

public class foodDTO{
    private Uri imguri;
    private String foodName;
    private String saledprice;
    private String price;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private double lat;
    private double lng;
    private String storekind;
    private String storeName;

    public foodDTO() {}
    public foodDTO(String foodName,String saledprice,String price,int year,int month,int day,int hour,int minute,double lat,double lng,String storekind,String storeName){
        //this.imguri = imguri;
        this.foodName = foodName;
        this.saledprice = saledprice;
        this.price = price;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.lat = lat;
        this.lng = lng;
        this.storekind = storekind;
        this.storeName = storeName;
    }
    //public void setImguri(Uri imguri){this.imguri=imguri;}
    public void setFoodName(String foodName){
        this.foodName = foodName;
    }
    public void setSaledprice(String saledprice){
        this.saledprice = saledprice;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public void setYear(int year){
        this.year = year;
    }
    public void setMonth(int month){
        this.month = month;
    }
    public void setDay(int day){
        this.day = day;
    }
    public void setHour(int hour){
        this.hour = hour;
    }
    public void setMinute(int minute){
        this.minute = minute;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public void setLng(double lng){
        this.lng = lng;
    }
    public void setStorekind(String storekind){
        this.storekind = storekind;
    }
    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    //public Uri getImguri(){return imguri;}
    public String getFoodName(){
        return foodName;
    }
    public String getSaledprice(){
        return saledprice;
    }
    public String getPrice(){
        return price;
    }
    public int getYear(){
        return year;
    }
    public int getMonth(){
        return month;
    }
    public int getDay(){
        return day;
    }
    public int getHour(){
        return hour;
    }
    public int getMinute(){
        return minute;
    }
    public double getLat(){
        return lat;
    }
    public double getLng(){
        return lng;
    }
    public String getStorekind(){
        return storekind;
    }
    public String getStoreName(){
        return storeName;
    }
}