package com.example.qthjen.weather;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class Weather extends AppCompatActivity implements View.OnClickListener {
    /** https://jsonformatter.curiousconcept.com/# **/
    private Button btOk;
    private EditText etInput;
    private TextView tvDate;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvCondision;
    private TextView tvCity;
    private TextView tvStatus;
    private ImageView ivStatus;
    private TextView tvWind;
    private Button btNextDays;
    private String temp = "ha noi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        findComponent();
        btOk.setOnClickListener(this);
        btNextDays.setOnClickListener(this);
        readDataWeather(temp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /** Intent giống như 1 jFrame trong java **/
            case R.id.about:
                Intent about = new Intent("com.example.qthjen.weather.ABOUT"); // lấy ở android:name trong manifest
                startActivity(about);
                break;

            case R.id.setting:
                Intent setting = new Intent("com.example.qthjen.weather.SETTING");
                startActivity(setting);
                break;

        }

        return false;
    }

    private void findComponent() {

        btOk = (Button) findViewById(R.id.btOk);
        etInput = (EditText) findViewById(R.id.etInput);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvCondision = (TextView) findViewById(R.id.tvCondision);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        ivStatus = (ImageView) findViewById(R.id.ivStatus);
        tvWind = (TextView) findViewById(R.id.tvWind);
        btNextDays = (Button) findViewById(R.id.btNextDays);

    }

    @Override
    public void onClick(View v) {

        String city = etInput.getText().toString();
        switch ( v.getId()) {

            case R.id.btOk:

                if ( city.trim().equals("")) {
                    readDataWeather(temp);
                } else {
                    temp = city;
                    readDataWeather(temp);
                }
                break;

            case R.id.btNextDays:

                Intent intent = new Intent(Weather.this, TheNext7Days.class);

                intent.putExtra("mykey",city); // mykey  là key cho để truyền dữ liệu ( putExtra hàm truyền dữ liệu)

                startActivity(intent);
                break;
        }

    }

    public void readDataWeather(String data) {

        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=4e32d6027c86aaa2574c9c329ee801d9";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String name = jsonObject.getString("name"); // key date trong json

                    /** lấy thời gian hiện tại của hệ thống **/
//                    Calendar cal = Calendar.getInstance();
//                    Formatter fmt = new Formatter();
//                    fmt.format("%tc", cal);

                    /** lấy thời gian từ json **/
                    String day = jsonObject.getString("dt");
                    long dayLong = Long.valueOf(day);
                    Date date = new Date(dayLong*1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy"); // EEEE là tháng
                    String Day = simpleDateFormat.format(date);

                    tvCity.setText("Tên thành phố: " + name);
                    tvDate.setText("Current date: " + Day);

                    JSONArray jsonArrayRequest = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayRequest.getJSONObject(0);
                    String status = jsonObjectWeather.getString("main");
                    String icon = jsonObjectWeather.getString("icon");
                    /** hàm lấy ảnh picasso **/
                    Picasso.with(Weather.this).load("http://openweathermap.org/img/w/" + icon + ".png").into(ivStatus);
                    tvStatus.setText("Status: " + status);

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String temperature = jsonObjectMain.getString("temp");
                    String humidity = jsonObjectMain.getString("humidity");

                    Double temp = Double.valueOf(temperature);
                    String Temperature = String.valueOf(temp.intValue());

                    tvTemperature.setText("Temperature: " + Temperature + "°C"); // dùng character map của windows để viết °(alt+0176)
                    tvHumidity.setText("Humidity: " + humidity + "%");

                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                    String speed = jsonObjectWind.getString("speed");
                    tvWind.setText("Wind: " + speed + "m/s");

                    JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                    String cloud = jsonObjectCloud.getString("all");
                    tvCondision.setText("Cloud: " + cloud + "%");

                } catch (JSONException e) {
                    Toast.makeText(Weather.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Weather.this);
        requestQueue.add(stringRequest);

    }

}
