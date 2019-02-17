package com.dyna.dyna.Utility;

import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Kevin on 11/2/2017.
 */

 public class DatabaseManager extends Observable {

     private static DatabaseManager INSTANCE = null;

     public DatabaseManager(){
         createStores();
     }

     public static DatabaseManager getInstance(){
         if(INSTANCE == null){
             INSTANCE = new DatabaseManager();
         }
         return INSTANCE;
     }

    ArrayList <Store> storeList = new ArrayList<>();

    public ArrayList<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(ArrayList<Store> storeList) {
        this.storeList = storeList;
    }

    Firebase rootRef;

    //Retrieves information from Firebase,Creates Store objects and saves each store on arraylist "StoreList"
    //After the list of stores is created, it sends the list to addMarkers(storeList) to create a marker to each store at the map
    protected  void createStores() {
        FirebaseDatabase.getInstance().getReference().child("ExchangeHouses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Store store = snapshot.getValue(Store.class);
                    storeList.add(store);
                 //   Log.d("Developer", " @DatabaseObserver storelist add:  "+store.getName());
                }
                checkDBchanges(storeList);//send list to create a child event listener for each one

                //after change
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Developer","Error in Database method createStores");
            }
        });
    }

    //to add a store programatically
    public Store addStore(String name, double latitude, double longitude, String sell, String buy) {
        saveOnFirebase(name, latitude, longitude, sell, buy);
        Store NS = new Store(name, latitude, longitude, sell, buy);
        return NS;
    }


    //Saves data of new Stores in Firebase
    public void saveOnFirebase(String name, double latitude, double longitude, String sell, String buy) {

        // Firebase mRef = new Firebase("https://dyna-ba42b.firebaseio.com/ExchangeHouses");
        Firebase mRefChild_house = rootRef.child(name);
        Firebase mRefChild_name = mRefChild_house.child("Name");
        mRefChild_name.setValue(name);
        Firebase mRefChild_latitude = mRefChild_house.child("Latitude");
        mRefChild_latitude.setValue(latitude);
        Firebase mRefChild_longitude = mRefChild_house.child("Longitude");
        mRefChild_longitude.setValue(longitude);
        Firebase mRefChild_sell = mRefChild_house.child("Sell");
        mRefChild_sell.setValue(sell);
        Firebase mRefChild_buy = mRefChild_house.child("Buy");
        mRefChild_buy.setValue(buy);
    }


    //Check changes on server
    //Adds a childEventListener that is constantly checking database changes
    // and sets the snippet when changed using the Child event listener
    protected void checkDBchanges(List<Store> List) {

        Firebase mRef = new Firebase("https://dyna-ba42b.firebaseio.com/ExchangeHouses");
        for(Store S:List){
        //    Log.d("Developer", "CEV created for: "+S.getName());
            mRef.child("/"+S.getName()).addChildEventListener(new Action_Listener(S).createCEV());
        }
    }
}
