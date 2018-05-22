package com.example.akb05.project;

import java.util.Date;

/**
 * Created by akb05 on 2018-05-22.
 */

public class foodDTO{
    private String foodName;
    private String saledprice;
    private String price;
    private long duedate;
    private double lat;
    private double lng;
    private String storeName;

    public foodDTO() {}
    public foodDTO(String foodName,String saledprice,String price,long duedate,double lat,double lng,String storeName){
        this.foodName = foodName;
        this.saledprice = saledprice;
        this.price = price;
        this.duedate = duedate;
        this.lat = lat;
        this. lng = lng;
        this.storeName = storeName;
    }
    public void setFoodName(String foodName){
        this.foodName = foodName;
    }
    public void setSaledprice(String saledprice){
        this.saledprice = saledprice;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public void setDuedate(long duedate){
        this.duedate = duedate;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public void setLng(double lng){
        this.lng = lng;
    }
    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    public String getFoodName(){
        return foodName;
    }
    public String getSaledprice(){
        return saledprice;
    }
    public String getPrice(){
        return price;
    }
    public long getDuedate(){
        return duedate;
    }
    public double getLat(){
        return lat;
    }
    public double getLng(){
        return lng;
    }
    public String getStoreName(){
        return storeName;
    }
}