package com.dyna.dyna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ListActivity extends AppCompatActivity {

    private RecyclerView mList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mList = (RecyclerView) findViewById(R.id.blog_list);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));


        mDatabase = FirebaseDatabase.getInstance().getReference().child("ExchangeHouses");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<List,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<List, ViewHolder>(
                List.class, R.layout.store_row,ViewHolder.class,mDatabase
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, List model, int position) {

            }
        };

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ViewHolder(View itemView){
            super(itemView);
            itemView = mView;
        }

        public void setName(String newName){
            TextView name = (TextView) mView.findViewById(R.id.store_name);
            name.setText(newName);
        }
        public void setSell (String newSell){
            TextView sell = (TextView) mView.findViewById(R.id.store_sell);
            sell.setText(newSell);
        }
        public void setBuy (String newBuy){
            TextView buy = (TextView) mView.findViewById(R.id.store_buy);
            buy.setText(newBuy);
        }
    }
}