package com.dyna.dyna.Slider;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dyna.dyna.R;

/**
 * Created by Kevin on 3/29/2018.
 */

public class Maps_itemViewHolder extends RecyclerView.ViewHolder {
    TextView storeName;
    TextView address;
    TextView sell;
    TextView buy;
    Button go;

    public Maps_itemViewHolder(final View itemView) {
        super(itemView);

        storeName = itemView.findViewById(R.id.storeName);
        address = itemView.findViewById(R.id.address);
        sell = itemView.findViewById(R.id.sell);
        buy = itemView.findViewById(R.id.buy);
        go = itemView.findViewById(R.id.go);

    }
}
