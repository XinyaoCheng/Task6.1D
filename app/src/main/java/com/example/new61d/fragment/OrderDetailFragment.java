package com.example.new61d.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.new61d.activity.MapsActivity;
import com.example.new61d.model.OrderModel;
import com.example.new61d.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    OrderModel detail_order;
    TextView sender_name_view,pick_up_time_view,receiver_name_view,drop_off_location,
             weight_view,length_view,width_view,height_view,
             good_type,vehicle_type;
    Button call_button;
    ImageView order_image;

    public OrderDetailFragment(OrderModel orderModel){
        this.detail_order = orderModel;

    }
    public OrderDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailFragment newInstance(String param1, String param2) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        receiver_name_view = rootView.findViewById(R.id.detail_receiver_name);
        pick_up_time_view = rootView.findViewById(R.id.detail_pick_up_time);
        sender_name_view = rootView.findViewById(R.id.detail_sender_name);
        drop_off_location = rootView.findViewById(R.id.detail_drop_off_location);
        order_image = rootView.findViewById(R.id.detail_order_image);
        weight_view = rootView.findViewById(R.id.detail_weight);
        length_view = rootView.findViewById(R.id.detail_length);
        width_view = rootView.findViewById(R.id.detail_width);
        height_view = rootView.findViewById(R.id.detail_height);
        good_type = rootView.findViewById(R.id.detail_good_type);
        vehicle_type = rootView.findViewById(R.id.detail_vehicle_type);
        call_button = rootView.findViewById(R.id.call_button);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference().child(detail_order.getOrder_iamge_name());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(order_image);
                Log.v("succuss to get image for order detail", uri.toString());
            }
        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("fail to get image of detail order",e.toString());
                            }
                        });
        sender_name_view.setText("From"+detail_order.getSender_name());
        pick_up_time_view.setText("Pick up time:"+detail_order.getPick_up_date()+" "+detail_order.getPick_up_time());
        receiver_name_view.setText("To "+detail_order.getReceiver_name());
        drop_off_location.setText("Drop off location: "+ detail_order.getDrop_off_location());
        weight_view.setText("weight: "+detail_order.getWeight()+"kg");
        length_view.setText("length: "+detail_order.getLength()+"m");
        width_view.setText("width: "+detail_order.getWidth()+"m");
        height_view.setText("height: "+detail_order.getHeight()+"m");
        good_type.setText("type: "+detail_order.getGood_type());
        vehicle_type.setText("vehicle type: "+detail_order.getVehicle_type());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_pref",MODE_PRIVATE);
        String login_name = sharedPreferences.getString("login_name","");
//        if((detail_order.isFinished())&&(detail_order.getSender_name().equals(login_name))){
//            call_button.setVisibility(View.VISIBLE);
//        }else{
//            call_button.setVisibility(View.INVISIBLE);
//        }

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("destination",detail_order.getDrop_off_location());
                startActivity(intent);
            }
        });
        return rootView;
    }
}