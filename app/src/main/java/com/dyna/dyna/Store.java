package com.dyna.dyna;
import com.google.android.gms.maps.model.Marker;
import java.io.Serializable;

public class Store implements Serializable {
    private String Name;
    private double Latitude;
    private double Longitude;
    private String Sell;
    private String Buy;
  //  private String city;
    private Marker marker=null;

    public Store(String Name, double Latitude, double Longitude, String Sell, String Buy){
        this.Name=Name;
        this.Latitude=Latitude;
        this.Longitude=Longitude;
        this.Sell=Sell;
        this.Buy=Buy;
    }

    public Store (){} //default constructor


    public void setName(String new_Name){
        Name=new_Name;
    }
    public void setSell(String new_Sell){
        Sell=new_Sell;
    }
    public void setBuy (String new_Buy){Buy = new_Buy;}
    public void setMarker(Marker newM){marker = newM;}

    public String getName(){return Name;}
    public double getLatitude(){return Latitude;}
    public double getLongitude(){return Longitude;}
    public String getSell(){return Sell;}
    public String getBuy(){return Buy;}
    public Marker getMarker(){return marker;}




}
