package com.example.akb05.project;

import android.net.Uri;

import java.net.URI;
import java.util.Date;

/**
 * Created by akb05 on 2018-05-22.
 */

public class foodDTO{
    private String title;
    private String foodName;
    private String saledprice;
    private String price;
    private String date;
    private double lat;
    private double lng;
    private String storekind;
    private String storeName;

    public foodDTO() {}
    public foodDTO(String title,String foodName,String saledprice,String price,String date,double lat,double lng,String storekind,String storeName){
        this.title = title;
        this.foodName = foodName;
        this.saledprice = saledprice;
        this.price = price;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
        this.storekind = storekind;
        this.storeName = storeName;
    }
    public void setTitle(String title){this.title=title;}
    public void setFoodName(String foodName){
        this.foodName = foodName;
    }
    public void setSaledprice(String saledprice){
        this.saledprice = saledprice;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public void setDate(String date){this.date = date;}
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

    public String getTitle(){return title;}
    public String getFoodName(){
        return foodName;
    }
    public String getSaledprice(){
        return saledprice;
    }
    public String getPrice(){
        return price;
    }
    public String getDate(){return date;}
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