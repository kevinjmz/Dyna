package com.dyna.dyna.Slider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dyna.dyna.Utility.DatabaseManager;
import com.dyna.dyna.R;
import com.dyna.dyna.Utility.Store;
import com.dyna.dyna.showDetailsInterface;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Kevin on 3/29/2018.
 */

public class Maps_itemAdapter extends RecyclerView.Adapter<Maps_itemViewHolder> implements Observer{

    List<Store> storeList;
    Activity context;
    showDetailsInterface listener;
    GestureDetector mGestureDetector;
    private int clickPosition;

    public Maps_itemAdapter(Activity context, List<Store> storeList, showDetailsInterface listener) {
        this.storeList = storeList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public Maps_itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent moveEvent, MotionEvent downEvent, float velocityX, float velocityY) {
                Log.d("developer", "onFling: ");
                boolean result = false;
                float diffX = moveEvent.getX() - downEvent.getX();
                float diffY = moveEvent.getY() - downEvent.getY();

                if( Math.abs(diffX) > Math.abs(diffY)){
                    //swipe right or left
                    return true;
                }
                else {
                    //up and down swipe
                    if(Math.abs(diffY) > 200 && Math.abs(velocityY) > 200){
                        if (diffY>0){
                            onSwipeTop();
                        }
                        else{
                            onSwipeBottom();
                        }
                    }
                    return true;
                }
            }
            private void onSwipeTop() {
                Log.d("developer", "onSwipeTop ");
                listener.showDetails(storeList.get(clickPosition).getName());
            }

            private void onSwipeBottom() {
                Log.d("developer", "onSwipeBottom ");
                listener.hideDetails();
            }

        });
        return new Maps_itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Maps_itemViewHolder holder, final int position) {
        holder.storeName.setText(storeList.get(position).getName());
        holder.sell.setText(storeList.get(position).getSell());
        holder.buy.setText(storeList.get(position).getBuy());
        holder.address.setText(storeList.get(position).getAddress());
        holder.distance.setText(String.valueOf( storeList.get(position).shortFloat(storeList.get(position).getDistanceToUser()) ) + "mi");

        holder.storeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showDetails(storeList.get(position).getName());
            }
        });

        holder.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Developer", "Go clicked @" +holder.storeName.getText());
                for(Store S: storeList){
                    if(S.getName().equals(holder.storeName.getText())){
                        Uri gmmIntentUri = Uri.parse("google.navigation:"+S.getLatitude()+","+S.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }
                    }
                }
            }
        });
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clickPosition = position;
                return mGestureDetector.onTouchEvent(event);
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
