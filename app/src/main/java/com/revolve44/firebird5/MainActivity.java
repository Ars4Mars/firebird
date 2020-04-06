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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import java.util.Calendar;
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

    public static String lat;
    public static String lon;
    public static String metric = "metric";


    //Variables
    public float NominalPower = 100;//????????????????????????????????
    public float CurrentPower;
    public float cloud;
    public float wind;
    public float temp;

    public long unixSunrise;
    public long unixSunset;

    public String city;
    public String country;

    //public String

    public boolean isDataAvailable = false;
    public final LinkedHashMap<Long, Float> dataMap = new LinkedHashMap<>();

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final float TEXT2 = 0;

    public String sunset;
    public String sunrise;

    public TextView mainoutput;

    Boolean check = false;

//    Boolean PeriodSunrise = false;
//    Boolean Period45deg = false;
//    Boolean Period90deg = false;
//    Boolean Period135deg = false;
//    Boolean PeriodSunset = false;

    int SunPeriod = 0;

    public double solarhours;
    public String solarhoursString;

    //Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = getIntent().getStringExtra("FROM_MAPS1");
        lon = getIntent().getStringExtra("FROM_MAPS2");
        //check = getIntent().getBooleanExtra("CHECK_SAVINGS",check);
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


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lat = sharedPreferences.getString("lati",lat);
        lon = sharedPreferences.getString("long",lon);



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
                    country = weatherResponse.sys.country;
                    unixSunrise = weatherResponse.sys.sunrise;// May delete****
                    unixSunset = weatherResponse.sys.sunset; // time of sunrise and sunset
                    city = weatherResponse.name; // i added two variable and this work, before i dont see this in Toast.


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

         TimeManipulations();
    }

    public void TimeManipulations(){

        long unixUTC = System.currentTimeMillis() / 1000L;

        // Get current UNIX time
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone =  cal.getTimeZone();
        Date cals =    Calendar.getInstance(TimeZone.getDefault()).getTime();
        long milliseconds =   cals.getTime();
        milliseconds = milliseconds + timeZone.getOffset(milliseconds);
        long UnixCurrentTime = milliseconds / 1000L;
        long GMT = UnixCurrentTime-unixUTC;
        Toast.makeText(this, "UTC"+GMT, Toast.LENGTH_SHORT).show();
//        long unixTimestamp = 1427607706;
//        long javaTimestamp = unixTimestamp * 1000L;
//        Date date = new Date(javaTimestamp);



        //Get sunshine duration per day
        unixSunrise=unixSunrise+GMT;
        unixSunset=unixSunset+GMT;
        long UnixSolarTime = unixSunset- unixSunrise;

        long UnixVar = UnixSolarTime/5;

        if (UnixCurrentTime>unixSunrise & (UnixVar+unixSunrise)>UnixCurrentTime){
            Toast.makeText(this, "sunrise", Toast.LENGTH_SHORT).show();
            SunPeriod=1;
        }else if ((unixSunrise+UnixVar) < UnixCurrentTime & UnixCurrentTime < ((UnixVar*2)+unixSunrise)){
            Toast.makeText(this, "45", Toast.LENGTH_SHORT).show();
            SunPeriod=2;
        }else if ((unixSunrise+UnixVar*2) < UnixCurrentTime & UnixCurrentTime < ((UnixVar*3)+unixSunrise)){
            Toast.makeText(this, "90", Toast.LENGTH_SHORT).show();
            SunPeriod=3;
        }else if ((unixSunrise+UnixVar*3) < UnixCurrentTime & UnixCurrentTime < ((UnixVar*4)+unixSunrise)){
            Toast.makeText(this, "135", Toast.LENGTH_SHORT).show();
            SunPeriod=4;
        }else if ((unixSunrise+UnixVar*4) < UnixCurrentTime & UnixCurrentTime < ((UnixVar*5)+unixSunrise)){
            Toast.makeText(this, "sunsetT", Toast.LENGTH_SHORT).show();
            SunPeriod=5;
        }else{
            SunPeriod=0;
            Toast.makeText(this, "NIGHT", Toast.LENGTH_SHORT).show();
        };
        Toast.makeText(this, unixSunrise+" < "+UnixCurrentTime+" < "+ unixSunset, Toast.LENGTH_SHORT).show();
        //(UnixCurrentTime<unixSunrise || unixSunset < UnixCurrentTime)

        //Converter UNIX-date to time
        Toast.makeText(this, unixSunset+"", Toast.LENGTH_SHORT).show();
        long time = unixSunrise * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat formaten = new SimpleDateFormat("HH:mm");
        formaten.setTimeZone(TimeZone.getDefault());
        sunrise = formaten.format(date);

        long time2 = unixSunrise * (long) 1000;
        Date date2 = new Date(time2);
        SimpleDateFormat formaten2 = new SimpleDateFormat("HH:mm");
        formaten2.setTimeZone(TimeZone.getDefault());
        sunset = formaten.format(date2);

        solarhours = UnixSolarTime/(60.0*60.0);
        //solarhoursString = String.valueOf(solarhours);
        solarhoursString = String.format("%.2f", solarhours);;

        //почему лонг тип иногда не показывается в тосте?
    }

    public int getSunPeriod() {
        return SunPeriod;
    }

    public String getSolarHours() {
        return solarhoursString;
    }
    public String getSunrisetime() {
        return sunrise;
    }

    public Float getCurrentPowerData() {
        return CurrentPower;
    }
//    public String getCityData() {
//        return CITY;
//    }
    public String getСityData() {
        return city;
    }


    public Float getTempData() {
        return temp;
    }
    public Float getWindData() { return wind; }
    public Boolean isDataAvailable(){ return isDataAvailable; }
    public LinkedHashMap<Long, Float> getDataPointsData() { return dataMap; }
    public Float getNominalPower() {return NominalPower;}
   // @RequiresApi(api = Build.VERSION_CODES.N)
//    public String getSunrise() {
//        return sunrise;
//    }
//    public String getSunset() {
//        return sunset;
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void runforecast() {
        getCurrentData();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//
//                TimeManipulations();
//            }
//        }, 2000);

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

