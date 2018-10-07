package com.dyna.dyna.ListUtility;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyna.dyna.R;

/**
 * Created by Kevin on 10/27/2017.
 */
//register link to cardViewItem
public class ListViewHolder extends RecyclerView.ViewHolder{
    Button btn2;
    ImageView image;
    TextView title;
    TextView tv_sell;
    TextView tv_buy;
    TextView tv_address;



    public ListViewHolder(View itemView) {
        super(itemView);
        btn2 = itemView.findViewById(R.id.btn_takeToMaps);
       // image = (ImageView) itemView.findViewById(R.id.image_listItem);
        title = itemView.findViewById(R.id.title_listItem);
        tv_sell = itemView.findViewById(R.id.tv_sell);
        tv_buy = itemView.findViewById(R.id.tv_buy);
        tv_address = itemView.findViewById(R.id.tv_address);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
