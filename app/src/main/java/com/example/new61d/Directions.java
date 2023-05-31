package com.example.new61d;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Directions {
    private String MAP_KEY="AIzaSyCxDc0s5_Qor-_kzKXCfof5esTuwR7csxI";
    String url = "https://maps.googleapis.com/maps/api/directions/json?";
    String overview_polyline = null;

    public String getDirections(String orign, String destination){
        String req_url= url
                +"origin="+orign
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
                    Log.v("怎么 又报错了！！！！！",overviewPolylineObject.optString("points"));

                }catch (JSONException e){
                    Log.e("获取路线错误",e.getMessage());
                }
            }
        });

        Log.v("路线",overview_polyline);
        return overview_polyline;

    }

}
