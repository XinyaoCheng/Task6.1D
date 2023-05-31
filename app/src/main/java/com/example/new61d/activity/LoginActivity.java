package com.example.new61d.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.new61d.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

public class LoginActivity extends AppCompatActivity {

    FirebaseStorage storage;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    TextView username_input, password_input;
    Button login_button, signup_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_input = findViewById(R.id.user_name_input_login);
        password_input = findViewById(R.id.password_input_login);
        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.sign_button);

        //sign in button is clicked
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signinIntent);
            }
        });
        //log in button is clicked
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //addData();
                String username = username_input.getText().toString();
                String password = password_input.getText().toString();

                if(TextUtils.isEmpty(username)){
                    username_input.setError("username is requied");
                }
                if(TextUtils.isEmpty(password)){
                    password_input.setError("password is requied");
                }

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(username+"@test.com", password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, "success to log in", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    SharedPreferences sharedPreferences = getSharedPreferences("my_pref",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("login_name",username);
                                    editor.apply();
                                    startActivity(homeIntent);

                                }else{
                                    Toast.makeText(LoginActivity.this, "fail to log in", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });
    }

}