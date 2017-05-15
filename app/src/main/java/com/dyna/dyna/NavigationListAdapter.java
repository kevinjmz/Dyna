package com.dyna.dyna;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;



import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class NavigationListAdapter extends RecyclerView.Adapter<NavigationListAdapter.ViewHolder> implements Serializable {
    private LayoutInflater inflater;
    private ArrayList<Drawable> icons;
    private ArrayList<String> labels;
    private OnItemClickListener mItemClickListener;

    public NavigationListAdapter(Context context, ArrayList<Drawable> icons, ArrayList<String> labels) {
        inflater = LayoutInflater.from(context);
        this.icons = icons;
        this.labels = labels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_navigation_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NavigationListAdapter.ViewHolder holder, final int position) {
        holder.navitem.setText(labels.get(position));
        //The following line will draw an icon on the left side of the Button's text.
        //This is simply to avoid using an extra view for no reason.
        holder.navitem.setCompoundDrawablesWithIntrinsicBounds(icons.get(position), null, null, null);

    }

    @Override
    public int getItemCount() {
        return icons.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        // ViewGroup container;
        AppCompatButton navitem;

        public ViewHolder(View itemView) {
            super(itemView);

            //container = (ViewGroup) itemView.findViewById(R.id.nav_list_container);
            navitem = (AppCompatButton) itemView.findViewById(R.id.btn_nav_item);
            navitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onNavigationItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onNavigationItemClick(int position);
    }

    public void setOnClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}