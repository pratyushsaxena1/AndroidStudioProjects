package com.saxenapratyush.lab11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    RequestQueue queue;
    String URL = "https://mw-demo.sites.tjhsst.edu/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.response);
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //format response to JSON object called "data"
                    JSONObject object = new JSONObject("{\"data\":"+response+"}");
                    //convert object to an array of objects
                    JSONArray array = object.getJSONArray("data");
                    //get first object and display its id
                    Handler handler = new Handler(Looper.getMainLooper());
                    for (int i = 0; i < 6; i++) {
                        final int index = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    textView.setText("ID: " + array.getJSONObject(index).getString("id") + ", Category: " + array.getJSONObject(index).getString("category"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, i * 1500);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volley error"+ error.toString());
            }
        });
        queue.add(request);
    }
}