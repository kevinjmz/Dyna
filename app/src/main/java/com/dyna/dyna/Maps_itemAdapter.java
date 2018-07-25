package com.dyna.dyna;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Kevin on 3/29/2018.
 */

public class Maps_itemAdapter extends RecyclerView.Adapter<Maps_itemViewHolder> implements Observer {

    List<Store> storeList;
    Context context;

    public Maps_itemAdapter(Context context, List<Store> storeList) {
        this.storeList = storeList;
        this.context = context;
    }

    @Override
    public Maps_itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_item, parent, false);
        return new Maps_itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Maps_itemViewHolder holder, int position) {
        holder.storeName.setText(storeList.get(position).getName());
        holder.sell.setText(storeList.get(position).getSell());
        holder.buy.setText(storeList.get(position).getBuy());

        holder.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Developer", "Go clicked @" +holder.storeName.getText());
                for(Store S: storeList){
                    if(S.getName().equals(holder.storeName.getText())){
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+S.getLatitude()+","+S.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
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

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof DatabaseManager) {
            DatabaseManager databaseManager = (DatabaseManager) o;
            for(Store S: storeList){
                ////////////////////////////////////
            }
        }
    }
}
