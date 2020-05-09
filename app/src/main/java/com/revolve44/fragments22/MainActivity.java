package com.revolve44.fragments22;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.gson.Gson;
import com.revolve44.fragments22.recyclerview.Model;
import com.revolve44.fragments22.recyclerview.MultiViewTypeAdapter;
import com.revolve44.fragments22.ui.HomeFragment;
import com.revolve44.fragments22.ui.CalcFragment;
import com.revolve44.fragments22.ui.SidekickFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
Внимание! при каждом запуске эмулятора ставь правильную дату на эмуляторе
ото бывает время восхода и заката не схадятся из за разницы дат и времени
 */

/*
TODO: The main functions of this class: communication with Weather API,
  get main variables (cloud, temp coefficient, sunrise sunset time)

 */

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
    public static String CITY = "Moscow";

    public static String AppId = "4e78fa71de97f97aa376e42f2f1c99cf";
    public static String MC = "&units=metric&appid=";

    public TextView temperatureText;
    public TextView windText;

    public static String lat;
    public static String lon;
    public static String metric = "metric";


    //Variables
    public float NominalPower;//????????????????????????????????
    public float CurrentPower;
    public int CurrentPowerInt;
    public float cloud;
    public float windF;
    public int windI;
    public float temp;

    public String desription;
    public float pressure;
    public float humidity;
    public boolean tempScale;

    public long unixSunrise;
    public long unixSunset;

    public String city;
    public String country;

    //public String

    public boolean isDataAvailable = false;
    public LinkedHashMap<Long, Float> dataMap = new LinkedHashMap<>();
    public LinkedHashMap<String, Float> corvette = new LinkedHashMap<>();

    public String sunset;
    public String sunrise;
    public String hournowStr;

    public TextView mainoutput;
    public TextView saymayname;

    public long TimeHashMap;
    public float CurrentPowerHashMap;

    Boolean HotCheck= false;

    int SunPeriod = 0;

    public String solarhoursString;

    public long GMT = 0;
    public long UTCtime;
    public long UnixCurrentTime;

    public String jsonString;


    //Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Lifecycle ->"," onCreate launch ");

        SharedPreferences sharedPreferences = this.getSharedPreferences("MasterSave", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            Intent intent2 = new Intent(this, MapsActivity.class);
            startActivity(intent2);

        }

        //setContentView(R.layout.activity_main);
        mainoutput = findViewById(R.id.Forecast_number);

        lat = getIntent().getStringExtra("FROM_MAPS1");
        lon = getIntent().getStringExtra("FROM_MAPS2");
        ///////////////////////////////////////////////////////////////////////////////////////////
        //                      Launch Pad                                                       //
        ///////////////////////////////////////////////////////////////////////////////////////////
//        Log.d("       Launch method ", " loaddata() ");
//        LoadData();
//        Log.d("       Launch method ", " getcurrentdata() ");
//        getCurrentData();
//        Log.d("       Launch method ", " loaddata() ");
//        TimeManipulations();
//        Log.d("         Launch method ", " othermainupulations() ");
//        //OtherManipulations();
//        Log.d("       Launch method ", " savedata() ");
//        SaveData();

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

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle ->"," onResume launch ");
        LoadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lifecycle ->"," onPause launch ");
        SaveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Lifecycle ->"," onStop launch ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Lifecycle ->"," onDestroy launch ");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void runforecast() {
        //////////////////////////////////////////////
        //         Click from Fragment              //
        //////////////////////////////////////////////
        Log.d("Lifecycle -> method "," runforecast ");
        LoadData();
        getCurrentData();
        TimeManipulations();
        SaveData();
       // OtherManipulations();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void getCurrentData() {
        Log.d("Lifecycle -> method "," getCurrentdata ");

//        // Before all, we load coordinations and nominal power of station
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        lat = sharedPreferences.getString("lati",lat);
        lon = sharedPreferences.getString("long",lon);
//        NominalPower = sharedPreferences.getFloat("Nominal_Power", (float) NominalPower);
        //city = sharedPreferences.getString("MyCity",city);
        //NominalPower = 1000;
        //CITY = "Mexico";
        //NominalPower = 29000;
        OkHttpClient.Builder  okhttpClientBuilder = new OkHttpClient.Builder();//for create a LOGs
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); //for create a LOGs
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); //for create a LOGs
        okhttpClientBuilder.addInterceptor(logging); //for create a LOGs

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build()) //for create a LOGs
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
//                    Log.d("RETROFITTTTTTTT    ", weatherResponse+" ");

                    //main variables
                    cloud = weatherResponse.clouds.all;
                    temp = weatherResponse.main.temp;
                    windF = weatherResponse.wind.speed;
                    country = weatherResponse.sys.country;
                    unixSunrise = weatherResponse.  sys.sunrise;
                    unixSunset = weatherResponse.sys.sunset; // time of sunrise and sunset
                    city = weatherResponse.name; // i added two variable and this work, before i dont see this in Toast.
                    pressure = weatherResponse.main.pressure;
                    humidity = weatherResponse.main.humidity;
                    GMT = weatherResponse.timezone;
                    UnixCurrentTime = (long) weatherResponse.dt;
                    Log.d("From retrofit         ",  "Temp and press " + temp+ " "+ pressure);

//                    unixSunrise = weatherResponse.sunrise;
//                    unixSunset = weatherResponse.sunset; // time of sunrise and sunset
//                    try {
//                        desription = weatherResponse.weather.description;
//                    }catch (Exception e){
//                        Log.d("################DEPLOY", "descript dont work");
//                    }

                    if (cloud >-1 ){
                        CurrentPower = NominalPower - NominalPower*(cloud/100)*0.8f;
                    }else{
                        CurrentPower = 404;
                    }
                }
            }


            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Check Internet connection. Fail in Response"+t.getMessage();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Call<WeatherForecastResponse> forecastCall = service.getDailyData(lat, lon, metric, AppId);
        forecastCall.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherForecastResponse> forecastCall, @NonNull Response<WeatherForecastResponse> response) {
                if (response.code() == 200) {
                    WeatherForecastResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    ArrayList<WeatherResponse> list = weatherResponse.list;

                    if (dataMap.size() == 0){
                        for(WeatherResponse wr: list){
                            // .put (Key, Clouds.all)

                            CurrentPowerHashMap = NominalPower - NominalPower * (wr.clouds.all / 100) * 0.8f;

                            TimeHashMap = (long) wr.dt * 1000;
                                //if (unixSunrise )

                            dataMap.put(TimeHashMap, CurrentPowerHashMap);
                            Log.d("Datamap ->", TimeHashMap+" "+ CurrentPowerHashMap);


//                            try {
//                            }catch (Exception e){
//                                Log.d("my Error", "in retrofit array");
//                            }
                            //list.dt -- is Time of data forecasted, unix, UTC
                            //Toast.makeText(getApplicationContext(), (CharSequence) dataMap, Toast.LENGTH_LONG).show();

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

        windI = Math.round(windF);
        if (temp>30){
            HotCheck = true;
        }


        //SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();

//        editor.putString("MyCity",city);
//        editor.apply();
        Log.d("From retrofit         ",  "Sunrise and sunset " + unixSunrise+ " "+ unixSunset);
        Log.d("From retrofit          ", GMT +"GMT now ");
        Log.d("From retrofit          ", UnixCurrentTime +"current time ");
        Log.d("$$$$$$$$$$", GMT +"Responce: " +city+country);
        Log.d("$$$$$$$$$$", GMT +"Responce: " +city+country+ "Current power "+ CurrentPower + " cloud " + cloud);
        Log.d("%%%%%%%%%%%%%%", " "+ dataMap);
        //TimeManipulations();
    }



    public void TimeManipulations(){
        Log.d("Lifecycle -> method "," timemanipulations ");
        ////////////////////////////////////////////////////
        //     Time zone & unix sunrise/sunset            //
        //                                                //
        ////////////////////////////////////////////////////
        UTCtime = System.currentTimeMillis(); // Here i have been problem coz i multipled on 1000L UTC time:(
        UnixCurrentTime = UTCtime;

        unixSunrise = unixSunrise* 1000L;
        unixSunset = unixSunset* 1000L;
        String hours = String.valueOf((GMT/3600));
        String blankGMT = "GMT";
        String finalblank;
        if (GMT>0){
            finalblank= blankGMT+"+"+hours;
        }else{
            finalblank= blankGMT+hours;
        }

        TimeZone tz = TimeZone.getTimeZone(finalblank);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(tz);
        Date netDate = (new Date(unixSunrise));
        sunrise = sdf.format(netDate);

        Date netDate2 = (new Date(unixSunset));
        sunset = sdf.format(netDate2);


        SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
        sd.setTimeZone(tz);
        Date netDate3 = (new Date(UnixCurrentTime));
        hournowStr = (sd.format(netDate3));

        int hournow = Integer.parseInt(hournowStr.substring(0, hournowStr.length() - 3));
        int hourRise = Integer.parseInt(sunrise.substring(0, sunrise.length() - 3));
        int hourSet = Integer.parseInt(sunset.substring(0, sunset.length() - 3));

        Log.d("?????????????????", "UTC -> "+UTCtime +" \\// "+UnixCurrentTime+ " UnixVar ->" + " hour now -> " + hournow+" and hournow str -> "+ hournowStr+ " ... ... "+hourRise+" "+ hourSet +" gmt -> "+ finalblank);

        int sector = (hourSet - hourRise)/5;

        //set Sun Position
        if (hournow >hourSet){
            SunPeriod=0;
            //Toast.makeText(this, "NIGHT", Toast.LENGTH_SHORT).show();
        }else if (hournow >hourSet - sector){
            //Toast.makeText(this, "sunset", Toast.LENGTH_SHORT).show();
            SunPeriod=5;

        }else if (hournow >hourSet - 2*sector){
            //Toast.makeText(this, "135", Toast.LENGTH_SHORT).show();
            SunPeriod=4;

        }else if (hournow >hourSet - 3*sector){
            //Toast.makeText(this, "90", Toast.LENGTH_SHORT).show();
            SunPeriod=3;

        }else if (hournow >hourSet - 4*sector){
            //Toast.makeText(this, "45", Toast.LENGTH_SHORT).show();
            SunPeriod=2;

        }else if (hournow >hourRise){
            //Toast.makeText(this, "sunrise", Toast.LENGTH_SHORT).show();
            SunPeriod=1;

        }else {
            SunPeriod=0;
//            Toast.makeText(this, "NIGHT", Toast.LENGTH_SHORT).show();
        }
        CurrentPowerInt = Math.round(CurrentPower);
        if (SunPeriod==0){
            CurrentPowerInt = 0;
        }


        solarhoursString = String.valueOf(hourSet-hourRise);
        Log.d("##########", " "+sunrise+" "+sunset + " unix -> " +unixSunrise + " GMT is ->" + GMT +" TZ is -> "+ finalblank);

        int a = 0;
        for(Map.Entry<Long, Float> entry : dataMap.entrySet()) {
            long key = (entry.getKey());
            float value = (entry.getValue());


            SimpleDateFormat sd4 = new SimpleDateFormat("HH:mm yyyy-MM-dd");
            sd4.setTimeZone(tz);
            Date netDate4 = (new Date(key));
            String ModernTime = (sd4.format(netDate4));

            //Log.d("Hashmap test loop -> ", "key : "+ key + " value : "+ value);
            int transitTime = Integer.parseInt(ModernTime.substring(0, ModernTime.length() - 14));
            Log.d("Hashmap test loop -> ", "transittime : "+ transitTime + " hoursunset : "+ hourSet);

            if (transitTime>hourSet){
                //0
                corvette.put(ModernTime, value*0f);
            }else if (transitTime>hourSet-sector){
                //0.6
                corvette.put(ModernTime, value*0.6f);
            }else if (transitTime>hourSet-2*sector){
                //0.8
                corvette.put(ModernTime, value*0.8f);
            }else if (transitTime>hourSet-3*sector){
                //1
                corvette.put(ModernTime, value);
            }else if (transitTime>hourSet-4*sector){
                //0.8
                corvette.put(ModernTime, value*0.8f);
            }else if (transitTime>hourSet-5*sector){
                //0.6
                corvette.put(ModernTime, value*0.6f);
            }else{
                //0
                corvette.put(ModernTime, value*0f);
            }
            a++;
            Log.d(" loop ", a+" times ");
        }
        //////////////////////////////////////////////////////////////////////////
        //                    convert to gson and save                          //
        //////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (corvette.size()>1){
            jsonString = new Gson().toJson(corvette);
            editor.putString("map", jsonString); // this hashmap
            editor.apply(); // added apply and this works!
            Log.d("Hash map corvette -> ", " "+ corvette + "json -> "+ jsonString);
        }
    }
    public void SaveData(){
        Log.d("Lifecycle -> method "," savedata ");
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CurPow",CurrentPowerInt);
        editor.putLong("GMT",GMT);
        editor.putBoolean("tempScale",tempScale);
        editor.putFloat("temp",temp);
        editor.putFloat("pressure",pressure);
        editor.putFloat("humidity",humidity);
        if (Integer.parseInt(sunrise.substring(0, sunrise.length() - 3))!=0){
            editor.putString("sunrise",sunrise);
        }
        if (Integer.parseInt(sunset.substring(0, sunset.length() - 3))!=0){
            editor.putString("sunset",sunset);
        }

        //editor.putString("map", jsonString); // this hashmap
        editor.putString("MyCity",city);
//        if (!city.equals("null")){
//
//        }

        editor.apply();
        Log.d("@@@@@@@@@@@@", "GMT:  "+GMT);
        Log.d("@@@@@@@@@@@@", "temp:  "+temp);
        Log.d("@@@@@@@@@@@@", "JSON:  "+jsonString);
        Log.d("@@@@@@@@@@@@", "CITY:  "+city);

    }
    public void LoadData(){
        Log.d("Lifecycle -> method "," Loaddata ");

        // Before all, we load coordinations and nominal power of station
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave",MODE_PRIVATE);

        //Check Savings
        Map<String, ?> allPrefs = sharedPreferences.getAll(); //your sharedPreference
        Set<String> set = allPrefs.keySet();
        for(String s : set){
            Log.d("Shared Pref", s + "<" + allPrefs.get(s).getClass().getSimpleName() +"> =  "
                    + allPrefs.get(s).toString());
        }

        lat = sharedPreferences.getString("lati",lat);
        lon = sharedPreferences.getString("long",lon);
        city = sharedPreferences.getString("MyCity",city);
        NominalPower = sharedPreferences.getFloat("Nominal_Power",(float)NominalPower);

        sunrise = sharedPreferences.getString("sunrise",sunrise);
        sunset = sharedPreferences.getString("sunset",sunset);

        Log.d("************", "LAT LOT:  "+lat+ " and " + lon);
        Log.d("************", "GMT:  "+GMT);
        Log.d("************", "temp:  "+temp);
        Log.d("************", "JSON:  "+jsonString);
        Log.d("************", "CITY:  "+city);



    }


    public int getWindSpeed() {
        return windI;
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
    public String getSunsetime() {
        return sunset;
    }

    public int getCurrentPowerData() {
        return CurrentPowerInt;
    }

    public String getСityData() {
        return city;
    }

    public Boolean HotCheck(){ return HotCheck; }
    public Boolean isDataAvailable(){ return isDataAvailable; }
    //public LinkedHashMap<String, Float> getDataPointsData() { return dataMap; }
    public Float getNominalPower() {return NominalPower;}
    @RequiresApi(api = Build.VERSION_CODES.N)

    //switcher of fragmnets, he help for switching without loss filled form in fragments
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
                //beginTransaction().hide(fragment1).hide(fragment3).show(fragment2).commit()

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

    public void toast0(View view) {
        Toast.makeText(this, " It`s CURRENT POWER output of your solar panels, with accountant weather data ", Toast.LENGTH_SHORT).show();
    }

    public void toast1(View view) {
        Toast.makeText(this, " Its time of sunrise ", Toast.LENGTH_SHORT).show();
    }

    public void toast2(View view) {
        Toast.makeText(this, " Its sunshine duration ", Toast.LENGTH_SHORT).show();
    }

    public void toast3(View view) {
        Toast.makeText(this, " Its time of sunset ", Toast.LENGTH_SHORT).show();
    }



}

