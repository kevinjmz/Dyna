package com.dyna.dyna;

/**
 * Created by Ismael on 5/31/2017.
 */

public class List {
    private String Name;
    private String Buy;
    private String Sell;

    public List(){

    }

    public List(String name, String buy, String sell) {
        Name = name;
        Buy = buy;
        Sell = sell;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setBuy(String buy) {
        Buy = buy;
    }

    public void setSell(String sell) {
        Sell = sell;
    }

    public String getName() {

        return Name;
    }

    public String getBuy() {
        return Buy;
    }

    public String getSell() {
        return Sell;
    }

}
