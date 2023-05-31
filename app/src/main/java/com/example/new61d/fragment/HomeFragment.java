package com.example.new61d.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new61d.model.OrderModel;
import com.example.new61d.R;
import com.example.new61d.activity.NewOrderActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String login_name;
    private RecyclerView home_recycleView;

    private FirebaseRecyclerAdapter orderAdapter;
    private RecyclerView.LayoutManager home_layoutManager;
    private FloatingActionButton add_order_button;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        add_order_button = rootView.findViewById(R.id.add_order_button);
        home_recycleView = rootView.findViewById(R.id.current_recycleView);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        login_name = sharedPreferences.getString("login_name", "");
        setRecycleView();
        home_layoutManager = new GridLayoutManager(getContext(),1);
        home_recycleView.setLayoutManager(home_layoutManager);
        home_recycleView.setAdapter(orderAdapter);

        //add_order button clicked
        add_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newOrderIntent = new Intent(getActivity(), NewOrderActivity.class);
                startActivity(newOrderIntent);
            }
        });
        return rootView;
    }

    private void setRecycleView() {
        Query query;

        if(getActivity().getIntent().getIntExtra("my_order_id",0)==2){

                // if user choose my order option and sender name equels to login name,
                //then only show login name's order
                query = FirebaseDatabase.getInstance().getReference().child("orderlist")
                        .orderByChild("sender_name")
                        .equalTo(login_name);
            }else // for which is not login name's order, just show all
                query = FirebaseDatabase.getInstance().getReference().child("orderlist");

        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>()
                        .setQuery(query, new SnapshotParser<OrderModel>() {
                            @NonNull
                            @Override
                            public OrderModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                OrderModel orderModel = new OrderModel(snapshot.child("receiver_name").getValue().toString(),
                                        snapshot.child("sender_name").getValue().toString(),
                                        snapshot.child("pick_up_date").getValue().toString(),
                                        snapshot.child("pick_up_time").getValue().toString(),
                                        snapshot.child("drop_off_location").getValue().toString(),
                                        snapshot.child("good_type").getValue().toString(),
                                        snapshot.child("weight").getValue().toString(),
                                        snapshot.child("width").getValue().toString(),
                                        snapshot.child("length").getValue().toString(),
                                        snapshot.child("height").getValue().toString(),
                                        snapshot.child("vehicle_type").getValue().toString(),
                                        snapshot.child("order_iamge_name").getValue().toString(),
                                        Boolean.getBoolean(snapshot.child("finished").getValue().toString())
                                );
                                //OrderModel orderModel = orderSnapshot.getValue(OrderModel.class);
                                Log.v("每个order输出", orderModel.toString());
                                    return orderModel;

                            }
                        })
                        .build();
        orderAdapter = new FirebaseRecyclerAdapter<OrderModel, myViewHolder>(options) {

            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_item, parent, false);

                return new myViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull OrderModel model) {

                // add image
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(model.getOrder_iamge_name());
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(holder.order_image);
                    }
                });
                //add title and desciption
                holder.order_title.setText(model.getGood_type());
                holder.order_desc.setText(model.toString());

                //share button functionality
                String share_txt = model.toString();
                holder.share_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT,share_txt);//add share txt to intent
                        startActivity(Intent.createChooser(shareIntent,"share with"));
                        //Displays the Share dialog box, allowing users to select the application to share.
                    }
                });

                //click card, bring user to order detail fragment
                holder.order_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderDetailFragment fragment = new OrderDetailFragment(model);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.current_fragment, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        };
        
    }

    @Override
    public void onStart() {
        super.onStart();
        orderAdapter.startListening();
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to reload data: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        orderAdapter.stopListening();
    }
}
class myViewHolder extends RecyclerView.ViewHolder {
    ImageView order_image;
    TextView order_title, order_desc;
    ImageButton share_button;
    CardView order_item;


    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        order_image = itemView.findViewById(R.id.recycle_order_image);
        order_title = itemView.findViewById(R.id.recycle_order_title);
        order_desc = itemView.findViewById(R.id.recycle_order_description);
        share_button = itemView.findViewById(R.id.share_button);
        order_item = itemView.findViewById(R.id.recycle_order_item);

    }
}
