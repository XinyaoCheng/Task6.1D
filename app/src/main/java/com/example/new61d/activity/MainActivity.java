package com.example.new61d.activity;

/**
  * Xinyao Cheng
   * SIT305
  * 223122637
  */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.new61d.fragment.HomeFragment;
import com.example.new61d.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView manu_button;
    NavigationView home_navigation;
    ListView manu_listView;
    String[] manu_value_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        manu_listView.setAdapter((new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,manu_value_list)));

        HomeFragment homeFragment = new HomeFragment();
        setFragment(homeFragment);


        //choose manu item
        manu_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        getIntent().putExtra("my_order_id",0);
                        setFragment(homeFragment);
                        break;
                    case 1:
                        //chose
                        break;
                    case 2:
                        getIntent().putExtra("my_order_id",2);
                        HomeFragment my_order_fragment = new HomeFragment();
                        setFragment(my_order_fragment);
                        break;
                    default:
                        setFragment(homeFragment);
                }
            }
        });
        manu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

    }
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.current_fragment,fragment)
                .commit();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void initial() {
        drawerLayout = findViewById(R.id.manu_drawable_layout);
        manu_button = findViewById(R.id.manu_button);
        home_navigation = findViewById(R.id.manu_NavigationView);
        manu_listView = findViewById(R.id.manu_listView);
        manu_value_list = getResources().getStringArray(R.array.manu_value);
    }
}



