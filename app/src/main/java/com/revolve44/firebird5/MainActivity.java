package com.revolve44.firebird5;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.revolve44.firebird5.ui.HomeFragment;
import com.revolve44.firebird5.ui.CalcFragment;
import com.revolve44.firebird5.ui.SidekickFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.FrameMetrics.ANIMATION_DURATION;
import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private View root;

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new CalcFragment();
    final Fragment fragment3 = new SidekickFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    RelativeLayout SkyLayout;

    //Example
    //api.openweathermap.org/data/2.5/weather?q=moscow&appid=1b87fee17221ed7893aa488cff08bfa2
    // http://api.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=10&appid=ac79fea59e9d15377b787a610a29b784

    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String CITY;

    public static String AppId = "1b87fee17221ed7893aa488cff08bfa2";
    public static String MC = "&units=metric&appid=";

    public TextView temperatureText;
    public TextView windText;

    public static String lat = "80.75";
    public static String lon = "35.61";
    public static String metric = "metric";


    //Variables
    public float NominalPower = 100;//????????????????????????????????
    public float CurrentPower;
    public float cloud;
    public float wind;
    public float temp;

    public long UnixSunrise;
    public long UnixSunset;

    //public String

    public boolean isDataAvailable = false;
    public final LinkedHashMap<Long, Float> dataMap = new LinkedHashMap<>();

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final float TEXT2 = 0;

    public String sunset;
    public String sunrise;

    public TextView mainoutput;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = getIntent().getStringExtra("FROM_MAPS1");
        lon = getIntent().getStringExtra("FROM_MAPS2");
//        if (NominalPower>0){
//            getCurrentData();
//        }
        RelativeLayout SkyLayout = (RelativeLayout) findViewById(R.id.SkyLayout);

//        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();

        getCurrentData();
        mainoutput = findViewById(R.id.Forecast_number);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void getCurrentData() {


        //CITY = "Mexico";
        NominalPower = 1000;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        //Call<WeatherResponse> call = service.getCurrentWeatherData(CITY, metric, AppId);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, metric, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    isDataAvailable = true;

                    //main variables
                    cloud = weatherResponse.clouds.all;
                    temp = weatherResponse.main.temp;
                    wind = weatherResponse.wind.speed;

                    // time of sunrise and sunset
                    UnixSunrise = weatherResponse.sys.sunrise;
                    UnixSunset = weatherResponse.sys.sunset;


                    if (cloud >-1 ){
                        CurrentPower = NominalPower - NominalPower*(cloud/100);
                    }else{
                        CurrentPower = 404;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Fail in Response"+t.getMessage();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        // FOR GRAPHICS on 5 days
        Call<WeatherForecastResponse> forecastCall = service.getDailyData(CITY, MainActivity.AppId);
        forecastCall.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherForecastResponse> forecastCall, @NonNull Response<WeatherForecastResponse> response) {
                if (response.code() == 200) {
                    WeatherForecastResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    ArrayList<WeatherResponse> list = weatherResponse.list;
                    if (dataMap.size() == 0){
                        for(WeatherResponse wr: list){
                            dataMap.put((long)wr.dt * 1000, NominalPower * wr.clouds.all / 100);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherForecastResponse> forecastCall, @NonNull Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Fail in Response"+t.getMessage();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        Context context = getApplicationContext();
//        CharSequence text = "Hello toast! " + cloud + " and "+ temp;
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();

        //Converter UNIX-date to time
        long time = UnixSunrise * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat formaten = new SimpleDateFormat("hh:mm");
        formaten.setTimeZone(TimeZone.getTimeZone("GMT"));
        String sunrise = formaten.format(date);

        long time2 = UnixSunrise * (long) 1000;
        Date date2 = new Date(time);
        SimpleDateFormat formaten2 = new SimpleDateFormat("hh:mm");
        formaten.setTimeZone(TimeZone.getTimeZone("GMT"));
        String sunset = formaten.format(date);

        //mainoutput.setText("Curr pow is "+ CurrentPower);
        Toast.makeText(this,"Nominal is "+NominalPower+" Current power is "+ CurrentPower, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, " Lat is "+lat+" "+ lon, Toast.LENGTH_SHORT).show();
    }

    public Float getCurrentPowerData() {
        return CurrentPower;
    }
    public String getCityData() {
        return CITY;
    }
    public Float getTempData() {
        return temp;
    }
    public Float getWindData() { return wind; }
    public Boolean isDataAvailable(){ return isDataAvailable; }
    public LinkedHashMap<Long, Float> getDataPointsData() { return dataMap; }
    public Float getNominalPower() {return NominalPower;}
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getSunrise() {
        return sunrise;
    }
    public String getSunset() {
        return sunset;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void runforecast() {
        getCurrentData();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.nav_settings:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.nav_sidekick:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
            }
            return false;
        }
    };

    public void seven(View view) {
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    //retrofit






}

