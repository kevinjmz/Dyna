package com.dyna.dyna;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kevin on 3/6/2018.
 */

public class Maps_item {
    TextView storeName;
    TextView address;
    TextView sell;
    TextView buy;
    Button go;


    public Maps_item(TextView storeName, TextView address, TextView sell, TextView buy, Button go) {
        this.storeName = storeName;
        this.address = address;
        this.sell = sell;
        this.buy = buy;
        this.go = go;
    }

    public TextView getStoreName() {
        return storeName;
    }

    public void setStoreName(TextView storeName) {
        this.storeName = storeName;
    }

    public TextView getDescription() {
        return address;
    }

    public void setDescription(TextView description) {
        this.address = address;
    }
    public TextView getAddress() {
        return address;
    }

    public void setAddress(TextView address) {
        this.address = address;
    }

    public TextView getSell() {
        return sell;
    }

    public void setSell(TextView sell) {
        this.sell = sell;
    }

    public TextView getBuy() {
        return buy;
    }

    public void setBuy(TextView buy) {
        this.buy = buy;
    }


}
