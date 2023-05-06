package com.example.new61d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;
import java.util.UUID;

public class NewOrderActivity extends AppCompatActivity {

    private static final int CHOOSE_PHOTO = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    EditText receiver_name_input,pick_up_time_input,drop_off_location_input,
            good_type_other,vehicle_other,
            weight_input,width_input,length_input,height_input;
    RadioGroup good_type_group,vehicle_type_group;
    Button create_order_button;
    CalendarView calendarView;
    OrderModel newOrder;
    String receiver_name, sender_name;
    String pick_up_time;
    String drop_off_location;
    String weight,width,length,height;
    String pick_up_date;
    String good_type, vehicle_type;
    String image_name = "default.png";
    ImageView order_imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseRef;
    Uri imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        initial();

        //upload image by user
        order_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int permission = ActivityCompat.checkSelfPermission(NewOrderActivity.this,"android.permission.WRITE_EXTERNAL_STORAGE");
                    if(permission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(NewOrderActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    }else{
                        openGallery();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //get date from calendar view
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                pick_up_date = String.format(Locale.getDefault(), "%04d-%02d-%02d", i, i1 + 1, i2);
                Log.v("order_date",pick_up_date);
            }
        });
        create_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInfo();
                checkValidation();
                saveInFirebase();
            }
        });
    }

    private void checkValidation() {
        //check the null value
        if(TextUtils.isEmpty(receiver_name)){
            receiver_name_input.setError(" receivername is required");
            return;
        }

        if(TextUtils.isEmpty(pick_up_time)){
            pick_up_time_input.setError("pickup time is required");
            return;
        }

        if(TextUtils.isEmpty(drop_off_location)){
            drop_off_location_input.setError("drop_off location is required");
            return;
        }

        if(TextUtils.isEmpty(weight)){
            weight_input.setError("weight is required");
            return;
        }

        if(TextUtils.isEmpty(width)){
            width_input.setError("width is required");
            return;
        }

        if(TextUtils.isEmpty(length)){
            length_input.setError("length is required");
            return;
        }

        if(TextUtils.isEmpty(height)){
            height_input.setError("height is required");
            return;
        }
        if(TextUtils.isEmpty(good_type)){
            Toast.makeText(NewOrderActivity.this, "please choose a good type", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(vehicle_type)){
            Toast.makeText(NewOrderActivity.this, "please choose a vehicle type", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!imageUrl.equals(null)) {
            //upload to firebase storage
            image_name =UUID.randomUUID().toString();
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(image_name);

            Log.v("new order image:",image_name);
            UploadTask uploadTask = imageRef.putFile(imageUrl);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(NewOrderActivity.this, "success to upload image", Toast.LENGTH_SHORT).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewOrderActivity.this, "fail to upload image" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    private void saveInFirebase() {
        //put all data into orderModel:
        newOrder = new OrderModel(receiver_name,sender_name,pick_up_date,pick_up_time,drop_off_location,good_type,weight,width,length,height,vehicle_type,image_name,false);
        Log.v("new order",newOrder.toString(1));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef = firebaseDatabase.getReference("orderlist");
        String orderId = databaseRef.push().getKey();
        databaseRef.child(orderId).setValue(newOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(NewOrderActivity.this, "success to create your order", Toast.LENGTH_SHORT).show();
                        Log.d("TAG","Data has been written successfully");
                        Intent my_orderIntent = new Intent(NewOrderActivity.this, MainActivity.class);
                        my_orderIntent.putExtra("my_order_id",2);
                        startActivity(my_orderIntent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error writing data to Firebase database: " + e.getMessage());
                    }
                });


    }

    private void getInfo() {
        receiver_name = receiver_name_input.getText().toString();
        pick_up_time = pick_up_time_input.getText().toString();
        drop_off_location = drop_off_location_input.getText().toString();
        weight = weight_input.getText().toString();
        width = width_input.getText().toString();
        length = length_input.getText().toString();
        height = height_input.getText().toString();
        good_type = good_type_other.getText().toString();
        vehicle_type = vehicle_other.getText().toString();
        order_imageView = findViewById(R.id.order_image_input);

        SharedPreferences sharedPreferences = getSharedPreferences("my_pref",MODE_PRIVATE);
        sender_name = sharedPreferences.getString("login_name","none");



        //get good type;
        if(TextUtils.isEmpty(good_type)){
            int good_type_id = good_type_group.getCheckedRadioButtonId();
            if(good_type_id !=-1){
                RadioButton good_type_button = findViewById(good_type_id);
                good_type = good_type_button.getText().toString();
            }
        }

        //get vehicle type
        if(TextUtils.isEmpty(vehicle_type)){
            int vehicle_type_id = vehicle_type_group.getCheckedRadioButtonId();
            if(vehicle_type_id !=-1){
                RadioButton vehicle_type_button = findViewById(vehicle_type_id);
                vehicle_type = vehicle_type_button.getText().toString();
            }
        }


    }

    private void initial() {
// EditText
        receiver_name_input = findViewById(R.id.receiver_name_input);
        pick_up_time_input = findViewById(R.id.pick_up_time_input);
        drop_off_location_input = findViewById(R.id.drop_off_location_input);
        good_type_other = findViewById(R.id.good_other);
        vehicle_other = findViewById(R.id.vehicle_other);
        weight_input = findViewById(R.id.weight_input);
        width_input = findViewById(R.id.width_input);
        length_input = findViewById(R.id.length_input);
        height_input = findViewById(R.id.height_input);

// RadioGroup
        good_type_group = findViewById(R.id.good_type_group);
        vehicle_type_group = findViewById(R.id.vehicle_type_group);

// Button
        create_order_button = findViewById(R.id.create_order_button);
//calendar
        calendarView = findViewById(R.id.pick_up_calendar);
//imageview
        order_imageView = findViewById(R.id.order_image_input);


    }
    private void openGallery() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PHOTO:
                    imageUrl= data.getData();
                    order_imageView.setImageURI(imageUrl);
                    break;
                default:
                    break;
            }
        }

    }
}