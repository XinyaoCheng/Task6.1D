package com.example.new61d.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.new61d.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.new61d.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.PolyUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationClient;
    Button pay_button, call_button;
    TextView destination_view, fee_TextView, time_TextView;
    String driverNumber = "0416931778";
    private String MAP_KEY="AIzaSyCxDc0s5_Qor-_kzKXCfof5esTuwR7csxI";
    LatLng startLatLng, endLatLng;
    String origin, destination;
    String overview_polyline = null;
    String url = "https://maps.googleapis.com/maps/api/directions/json?";
    String duration = null, bill = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // initial
        call_button = findViewById(R.id.call_now_button);
        pay_button = findViewById(R.id.pay_button);
        time_TextView = findViewById(R.id.time_TextView);
        fee_TextView = findViewById(R.id.fee_TextView);
        destination_view = findViewById(R.id.destination_view);
        //initiate the SDK
        if (!Places.isInitialized()) {
            Places.initialize(this, MAP_KEY);
        }
        placesClient = Places.createClient(this);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AutocompleteSupportFragment startAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.origin_fragment);
        startAutocompleteFragment.setHint("enter start point address");
        startAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG));





        startAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                startLatLng = place.getLatLng();
                Log.v("deakin address",startLatLng.toString());
                origin = place.getAddress();
                startAutocompleteFragment.setHint(place.getAddress());
                mMap.addMarker(new MarkerOptions().position(startLatLng).title("pick up"));
                //get directions:
                setDirections();

                //move the camera, make it can include two address
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(startLatLng);
                builder.include(endLatLng);
                LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        //call drivers
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+driverNumber));
                startActivity(callIntent);
            }
        });


        //pay now:
        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void setDirections() {

        String req_url= url
                +"origin="+origin
                +"&destination="+destination
                +"&key="+MAP_KEY;
        Log.v("请求路线的url",req_url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(req_url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("获取路线错误",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();

                try{
                    JSONObject resonse_json = new JSONObject(res);
                    JSONArray routesArray = resonse_json.getJSONArray("routes");
                    JSONObject routeObject = routesArray.getJSONObject(0);
                    //get the approx duration:
                    JSONArray legsArray = routeObject.getJSONArray("legs");
                    if (legsArray.length() > 0) {
                        JSONObject leg = legsArray.getJSONObject(0);
                        JSONObject durationObject = leg.getJSONObject("duration");
                        String durationText = durationObject.getString("text");
                        Log.v("预估的时间",durationText);
                        duration = "Approx. Travel Time:"+durationText;

                        JSONObject distance = leg.getJSONObject("distance");
                        int distance_value = distance.getInt("value");
                        int money = 10+distance_value/1927;
                        Log.v("预估的钱",String.valueOf(money));
                        bill = "Approx. Fare:$"+money;
                    }

                    //get the diraction
                    JSONObject overviewPolylineObject = routeObject.getJSONObject("overview_polyline");
                    overview_polyline = overviewPolylineObject.optString("points");

                    List<LatLng> decodedPath = PolyUtil.decode(overview_polyline);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PolylineOptions lineOptions = new PolylineOptions();
                            lineOptions.addAll(decodedPath);
                            lineOptions.color(Color.RED);
                            lineOptions.jointType(JointType.ROUND);
                            lineOptions.width(15f);
                            mMap.addPolyline(lineOptions);
                            time_TextView.setText(duration);
                            fee_TextView.setText(bill);
                        }
                    });

                }catch (JSONException e){
                    Log.e("获取路线错误",e.getMessage());
                }
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng deakin =new LatLng(-37.8445153, 145.1122556);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deakin,15));
        destination = getIntent().getStringExtra("destination");
        destination_view.setText("Drop off in "+destination);
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(destination, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                endLatLng = new LatLng(address.getLatitude(),address.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(endLatLng).title("drop off"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLng, 15));
            }
        } catch (IOException e) {
            Toast.makeText(MapsActivity.this, "fail to get your destination in map, error:" + e.getMessage(), Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }


    }


}