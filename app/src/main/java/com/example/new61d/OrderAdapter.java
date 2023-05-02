package com.example.new61d;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private ArrayList<OrderModel> orderList;
    private Context context;

    public OrderAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new OrderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("/"+(position+1)+".jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.v("get image from firebase",uri.toString());
                Picasso.get().load(uri).into(holder.order_image);

            }
        });

        Log.v("position",String.valueOf(position));
        //holder.order_image.setImageURI(orderList.get(position).getOrder_iamge_Uri());
        holder.order_title.setText(orderList.get(position).getGood_type());
        holder.order_desc.setText(orderList.get(position).toString());

        //share button functionality
        String share_txt = orderList.get(position).toString();
        holder.share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,share_txt);
                context.startActivity(Intent.createChooser(shareIntent,"share with"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView order_image;
        TextView order_title, order_desc;
        ImageButton share_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_image = itemView.findViewById(R.id.recycle_order_image);
            order_title = itemView.findViewById(R.id.recycle_order_title);
            order_desc = itemView.findViewById(R.id.recycle_order_description);
            share_button = itemView.findViewById(R.id.share_button);
        }
    }
}