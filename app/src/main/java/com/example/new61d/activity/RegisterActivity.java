package com.example.new61d.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.new61d.R;
import com.example.new61d.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterActivity extends AppCompatActivity {

    private static final int CHOOSE_PHOTO = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static final int STORAGE_PERMISSION = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    public static final String TAG = "TAG";


    FirebaseStorage storage;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ImageView imageView_input;
    EditText fullName_input, userName_input, password_input1, password_input2, phone_input;
    Button createAccount_button;

    private Uri imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageView_input = findViewById(R.id.image_input);
        fullName_input = findViewById(R.id.full_name_input);
        userName_input = findViewById(R.id.user_name_input);
        password_input1 = findViewById(R.id.Password_input_one);
        password_input2 = findViewById(R.id.Password_input_two);
        phone_input = findViewById(R.id.phone_input);
        createAccount_button = findViewById(R.id.create_account_button);

        //firebase
        storage = FirebaseStorage.getInstance();


        //add image by user
        imageView_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int permission = ActivityCompat.checkSelfPermission(RegisterActivity.this,"android.permission.WRITE_EXTERNAL_STORAGE");
                    if(permission != PackageManager.PERMISSION_GRANTED){
                        //Toast.makeText(this,"You denied the permission", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(RegisterActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    }else{
                        openGallery();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                //openGallery();
            }
        });

        //create account
        createAccount_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(createAccount()){
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }

            }
        });
    }

    private boolean createAccount() {
        String full_name = fullName_input.getText().toString();
        String user_name = userName_input.getText().toString();
        String password1 = password_input1.getText().toString();
        String password2 = password_input2.getText().toString();
        String phone_num =phone_input.getText().toString();
        if(TextUtils.isEmpty(full_name)){
            fullName_input.setError("fullname is requied");
            return false;
        }
        if(TextUtils.isEmpty(user_name)){
            userName_input.setError("username is requied");
            return false;
        }
        if(TextUtils.isEmpty(password1)){
            password_input1.setError("password is requied");
            return false;
        }
        if(TextUtils.isEmpty(password2)){
            password_input2.setError("confirmed password is requied");
            return false;
        }
        if(TextUtils.isEmpty(phone_num)){
            phone_input.setError("phone number is requied");
            return false;
        }
        if(!password1.equals(password2)){
            password_input1.setError("please enter two same password");
            password_input2.setError("please enter two same password");
        }else{
            UserModel new_user = new UserModel(full_name,user_name,password1,phone_num,imageUrl);
            firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(user_name+"@test.com",password1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("users");
                                databaseReference.child(uid).setValue(new_user);
                                Toast.makeText(RegisterActivity.this,"successful to create an account", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, "fail to create an account" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }
        return true;
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
                    imageView_input.setImageURI(imageUrl);
                    break;
                default:
                    break;
            }
        }

    }
}