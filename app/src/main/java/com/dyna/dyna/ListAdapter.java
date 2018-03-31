package com.dyna.dyna;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Kevin on 10/27/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    List <Store> storeList;

    public ListAdapter(List<Store> storeList) {
        this.storeList = storeList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_navigation_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.title.setText(storeList.get(position).getName());
        // holder.image.setImageResource(storeList.get(position).getImage());
        //holder.image.setImageResource(R.drawable.sample_store);
        holder.tv_sell.setText("Sell:  "+storeList.get(position).getSell());
        holder.tv_buy.setText("Buy:  "+storeList.get(position).getBuy());
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
}
