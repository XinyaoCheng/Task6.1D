package com.example.new61d;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView order_image;
    TextView order_title, order_desc;
    ImageButton share_button;
    CardView order_item;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        order_image = itemView.findViewById(R.id.recycle_order_image);
        order_title = itemView.findViewById(R.id.recycle_order_title);
        order_desc = itemView.findViewById(R.id.recycle_order_description);
        share_button = itemView.findViewById(R.id.share_button);
        order_item = itemView.findViewById(R.id.recycle_order_item);

    }
}
