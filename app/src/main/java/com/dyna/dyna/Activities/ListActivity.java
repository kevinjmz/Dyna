package com.dyna.dyna.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.dyna.dyna.ListUtility.ListAdapter;
import com.dyna.dyna.R;
import com.dyna.dyna.Utility.Store;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {

    private ArrayList <Store> storeList = new ArrayList<>();
    private RecyclerView list_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        retrieveStores();
    }


    private void retrieveStores(){
        FirebaseDatabase.getInstance().getReference().child("ExchangeHouses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Store store = snapshot.getValue(Store.class);
                  //  Log.d("Developer", store.getName() + "  Sell" + store.getSell() + "  Buy  " + store.getBuy() + "In ListActivity");//for debugging purpuses
                    saveInList(store);
                }
                setUpList();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Developer","Error in Database method createStores");
            }
        });
    }

    public void saveInList(Store S) {
        storeList.add(S);
    }
    public void setUpList(){
        list_container = findViewById(R.id.store_list);
        list_container.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        list_container.setAdapter(new ListAdapter(this , storeList));
        list_container.setLayoutManager(layout);
    }
}