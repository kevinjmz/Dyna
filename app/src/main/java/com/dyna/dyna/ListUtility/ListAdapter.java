package com.dyna.dyna.ListUtility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyna.dyna.R;
import com.dyna.dyna.Utility.CalculatorManager;
import com.dyna.dyna.Utility.Store;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kevin on 10/27/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    ArrayList<Store> storeList;
    Activity activity;
    CalculatorManager calculatorManager;

    public ListAdapter(Activity activity,ArrayList<Store> storeList) {
        this.storeList = storeList;
        this.activity = activity;
        calculatorManager = new CalculatorManager(activity, storeList);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_navigation_list, parent, false);
        storeList = calculatorManager.computeDistancesToCities();
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        holder.title.setText(storeList.get(position).getName());
        // holder.image.setImageResource(storeList.get(position).getImage());
        //holder.image.setImageResource(R.drawable.sample_store);
        holder.tv_sell.setText("Sell: "+storeList.get(position).getSell());
        holder.tv_buy.setText("Buy: "+storeList.get(position).getBuy());
        holder.tv_address.setText(storeList.get(position).getAddress());
        Log.d("developer", String.valueOf(storeList.get(position).shortFloat(storeList.get(position).getDistanceToUser()) ) + " mi");
        holder.distance.setText(String.valueOf( storeList.get(position).shortFloat(storeList.get(position).getDistanceToUser()) ) + " mi");


        holder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Store S: storeList){
                    if(S.getName().equals(holder.title.getText())){
                        Uri gmmIntentUri = Uri.parse("google.navigation:"+S.getLatitude()+","+S.getLongitude());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
                            activity.startActivity(mapIntent);
                        }
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
}
