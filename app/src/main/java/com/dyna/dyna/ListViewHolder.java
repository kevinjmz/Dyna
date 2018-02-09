package com.dyna.dyna;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kevin on 10/27/2017.
 */
//register link to cardViewItem
public class ListViewHolder extends RecyclerView.ViewHolder{
    Button btn1;
    Button btn2;
    ImageView image;
    TextView title;
    TextView tv_sell;
    TextView tv_buy;



    public ListViewHolder(View itemView) {
        super(itemView);
        btn1 = (Button) itemView.findViewById(R.id.btn_takeToCalc);
        btn2 = (Button) itemView.findViewById(R.id.btn_takeToMaps);
       // image = (ImageView) itemView.findViewById(R.id.image_listItem);
        title = (TextView) itemView.findViewById(R.id.title_listItem);
        tv_sell = (TextView) itemView.findViewById(R.id.tv_sell);
        tv_buy = (TextView) itemView.findViewById(R.id.tv_buy);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
