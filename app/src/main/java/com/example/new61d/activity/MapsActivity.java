package com.example.new61d.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.new61d.Directions;
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
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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
    TextView fee_TextView, time_TextView;
    String driverNumber = "0416931778";
    private String MAP_KEY="AIzaSyCxDc0s5_Qor-_kzKXCfof5esTuwR7csxI";
    LatLng startLatLng, endLatLng;
    String origin, destination;
    String overview_polyline = null;
    String url = "https://maps.googleapis.com/maps/api/directions/json?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // initial
        call_button = findViewById(R.id.call_button);
        pay_button = findViewById(R.id.pay_button);
        time_TextView = findViewById(R.id.time_TextView);
        fee_TextView = findViewById(R.id.fee_TextView);
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
                getSupportFragmentManager().findFragmentById(R.id.start_fragment);
        startAutocompleteFragment.setHint("enter start point address");
        AutocompleteSupportFragment endAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.end_fragment);
        startAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        endAutocompleteFragment.setHint("enter end point address");
        endAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG));




        startAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                startLatLng = place.getLatLng();
                Log.v("deakin address",startLatLng.toString());
                origin = place.getAddress();
                startAutocompleteFragment.setHint(place.getAddress());
                mMap.addMarker(new MarkerOptions().position(startLatLng).title("Start Point"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng,13));
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        endAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                endLatLng = place.getLatLng();
                destination = place.getAddress();
                endAutocompleteFragment.setHint(place.getAddress());
                mMap.addMarker(new MarkerOptions().position(endLatLng).title("End Point"));

                //get directions:
                setDirections();


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
                Log.v("网页响应",res);
                try{
                    JSONObject resonse_json = new JSONObject(res);
                    JSONArray routesArray = resonse_json.getJSONArray("routes");
                    JSONObject routeObject = routesArray.getJSONObject(0);
                    JSONObject overviewPolylineObject = routeObject.getJSONObject("overview_polyline");
                    overview_polyline = overviewPolylineObject.optString("points");
                    Log.v("怎么 又报错了！！！！！",overview_polyline);
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
    }


}