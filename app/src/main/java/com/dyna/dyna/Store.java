package com.dyna.dyna;
import com.google.android.gms.maps.model.Marker;
import java.io.Serializable;

public class Store implements Serializable {
    private String name;
    private double latitude;
    private double longitude;
    private String sell;
    private String buy;
    private String city;
    private Marker marker=null;

    public Store(String name, double latitude, double longitude, String sell, String buy){
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.sell=sell;
        this.buy=buy;
    }
/*    public Store(String name, double latitude, double longitude){
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        buy=0.00;
        sell=0.00;
    }*/


    public void setName(String new_Name){
        name=new_Name;
    }
    public void setSell(String new_Sell){
        sell=new_Sell;
    }
    public void setBuy (String new_Buy){buy = new_Buy;}
    public void setMarker(Marker newM){marker = newM;}

    public String getName(){return name;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public String getSell(){return sell;}
    public String getBuy(){return buy;}
    public Marker getMarker(){return marker;}




}
