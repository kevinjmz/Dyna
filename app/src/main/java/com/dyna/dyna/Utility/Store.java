package com.dyna.dyna.Utility;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class Store implements Serializable {
    private String Name;
    private double Latitude;
    private double Longitude;
    private String Sell;
    private String Buy;
    private String Address;
    private String City;
    private transient Marker marker = null;  //TODO need to check if transient causes any problems with anything, added it because it avoids crashing@onSaveInstanceState/MapsActivity
    private float distanceToUserInMiles;
    private float distanceToUserInMeters;
    private String Password;


    public Store(String Name, double Latitude, double Longitude, String Sell, String Buy) {
        this.Name = Name;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Sell = Sell;
        this.Buy = Buy;
    }

    public Store() {
    } //default constructor

    public String getName() {
        return Name;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public String getSell() {
        return Sell;
    }

    public String getBuy() {
        return Buy;
    }

    public Marker getMarker() {
        return marker;
    }

    public String getAddress() {
        return Address;
    }

    public String getPassword() {return Password;}

    public String getCity(){return City;}

    public float getDistanceToUser(){return distanceToUserInMiles;}

    public float getGetDistanceToUserInMeters() {return distanceToUserInMeters;}


    public void setName(String new_Name) {
        Name = new_Name;
    }

    public void setSell(String new_Sell) {
        Sell = new_Sell;
    }

    public void setBuy(String new_Buy) {
        Buy = new_Buy;
    }

    public void setMarker(Marker newM) {
        marker = newM;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPassword(String password){ Password = password; }

    public void setCity(String City){ City = City; }

    public void setDistanceToUserInMiles(float DistanceToUser){ distanceToUserInMiles = DistanceToUser; }

    public void setGetDistanceToUserInMeters(float DistanceToUser){distanceToUserInMeters = DistanceToUser; }

    public float shortFloat(float f){
        float ff= (float) (Math.round(f * 100.0)/ 100.0);
        return ff;
    }

}
