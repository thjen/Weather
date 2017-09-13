package com.example.qthjen.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class TheNext7Days extends AppCompatActivity{

    String tempCity = "ha noi";
    private TextView tvCityA2;
    private ImageView ivBack;
    private ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7days);
        findComponent();

        Intent intent = getIntent();
        String city = intent.getStringExtra("mykey");

        if ( city.trim().equals("")) {
            readWeather7Days(tempCity);
        } else {
            tempCity = city;
            readWeather7Days(tempCity);
        }

    }

    public void readWeather7Days(String data) {

        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + data + "&units=metric&cnt=7&appid=4e32d6027c86aaa2574c9c329ee801d9";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TheNext7Days.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(TheNext7Days.this);
        requestQueue.add(stringRequest);

    }

    public void findComponent() {

        tvCityA2 = (TextView) findViewById(R.id.tvCityA2);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        lv = (ListView) findViewById(R.id.lv);

    }

}
