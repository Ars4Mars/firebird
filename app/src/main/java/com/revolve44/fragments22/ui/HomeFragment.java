package com.revolve44.fragments22.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.revolve44.fragments22.MainActivity;
import com.revolve44.fragments22.R;
import com.revolve44.fragments22.recyclerview.Model;
import com.revolve44.fragments22.recyclerview.MultiViewTypeAdapter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private ImageView ivSun;
    private RelativeLayout SkyLayout;
    private ImageView ivCloud1;
    private ImageView ivCloud2;
    private ImageView ivBird;
    private ImageView ivRoof;
    ImageView image;
    private int ANIMATION_DURATION = 3000;

    // a c = is start
    public float a = -400; // start X
    public float c = 60; // start Y

    public float b = 150;// to X
    public float d = 150;// to Y

    public int width;
    public int height;

    public int wind;

    public int xSun;
    public int ySun;

    //SupportMapFragment mapView;

    int random_num;

    ImageView cloud;
    ImageView cloud2;
    ImageView cloud3;
    ImageView cloud4;

    ImageView sun;

    ImageView imageX;



    Float nominalpower;
    String city;

    String sunrise;
    String sunset;
    String solarhoursString;
    int SunPeriod;

    TextView CityView;
    TextView NominalPower;
    TextView CurrentPower;

    TextView SunriseView;
    TextView SunsetView;
    TextView SolarHoursView;

    TextView WindView;
     ImageView HotView;

     ImageView mobile;
     ImageView lamp;
     ImageView tv;
     ImageView teapot;
     ImageView oven;

    public int currentPower;

    public int amortization = 0;


    RecyclerView mRecyclerView;
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);

//    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Lifecycle of Fragment->", " Fragment -> onCreate launch ");



    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home, container, false); //added View root

        Log.d("Lifecycle of Fragment->", " Fragment -> onCreateView launch ");

        //((MainActivity) Objects.requireNonNull(getActivity())).runforecast();
        ((MainActivity)getActivity()).runforecast();
//        MainActivity activity = (MainActivity) getActivity();
//        final Float currentPower2 = activity.getCurrentPowerData();
//        final String city = activity.getСityData();

        //((MainActivity) Objects.requireNonNull(getActivity())).runforecast();
        CurrentPower = root.findViewById(R.id.Forecast_number);
        NominalPower = root.findViewById(R.id.NominalView);
        CityView = root.findViewById(R.id.cityView);

        SunriseView = root.findViewById(R.id.timesunrise);
        SunsetView = root.findViewById(R.id.timesunset);
        SolarHoursView = root.findViewById(R.id.solardayhr);

        WindView = root.findViewById(R.id.WindSpeed);

        HotView = root.findViewById(R.id.HotView);

         mobile = root.findViewById(R.id.mobile);
        lamp = root.findViewById(R.id.lamp);
         tv = root.findViewById(R.id.tv);
        teapot = root.findViewById(R.id.teapot);
         oven = root.findViewById(R.id.oven);
        Button button = root.findViewById(R.id.refresh);


        //////////////////////////////////////////////////////////
        //              recyclerview                            //
        //////////////////////////////////////////////////////////

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        ArrayList<Model> list2= new ArrayList<>();
        //list2.add(new Model())
        list2.add(new Model(Model.TEXT_TYPE,"",0));
        try {
            list2.add(new Model(Model.GRAPH_TYPE,"",0));
        }catch (Exception e){
            Log.d("MyError -> ", " add graphtype");
        }

        //list.add(new Model(Model.GRAPH_TYPE,"",0 ));
        //list.add(new Model(Model.GRAPH_TYPE,"IRON MAN",0 ));
        final MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list2,getActivity());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        ////////////////////////////////////////////////////////////////////////////////


//        Toast.makeText(getActivity(),"Starting fragment "+city,Toast.LENGTH_SHORT).show();

        button.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v)
            {
                LaunchForecast();

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LaunchForecast();
            }
        }, 1500);

        final ImageView sun= root.findViewById(R.id.ivSun);
        final ImageView ivRoof= root.findViewById(R.id.ivRoof);
        final ImageView cloud= root.findViewById(R.id.cloud1);
        final ImageView cloud2= root.findViewById(R.id.cloud2);
        final ImageView cloud3= root.findViewById(R.id.cloud3);
        final ImageView cloud4= root.findViewById(R.id.cloud4);

        final ImageView imageX= root.findViewById(R.id.fan);

        //Random
        int min = 1;
        int max = 50;

        Random r = new Random();
        random_num = r.nextInt(max - min + 1) + min;
        //Toast.makeText(getActivity(),"Text!"+width,Toast.LENGTH_SHORT).show();
        //Random end

        //sunny day



        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void LaunchForecast (){
        Log.d("Lifecycle -> method "," LaunchForecast ");
        //Toast.makeText(getActivity(),"Loading ...",Toast.LENGTH_SHORT).show();
        ((MainActivity) Objects.requireNonNull(getActivity())).runforecast();
        //CurrentPower.setText(""+currentPower2);
        //mainLoader.setVisibility(View.VISIBLE);
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading ... Please wait...", true);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                LoadDataFragment();
                //((MainActivity) Objects.requireNonNull(getActivity())).TimeManipulations();

                MainActivity activity = (MainActivity) getActivity();
                currentPower = activity.getCurrentPowerData();
                final Float nominalpower = activity.getNominalPower();
                city = activity.getСityData();
                sunrise = activity.getSunrisetime();
                sunset = activity.getSunsetime();
                solarhoursString = activity.getSolarHours();
                 int SunPeriod = activity.getSunPeriod();
                 wind = activity.getWindSpeed();
                 Boolean HotCheck = activity.HotCheck();

                //dirty hack
                if (currentPower == 0 & SunPeriod != 0){

                    if (amortization<2){
                        LaunchForecast();
                        amortization++;
                    }

                }
                if (currentPower >0 & SunPeriod ==0){

                    if (amortization<2){
                        LaunchForecast();
                        amortization++;
                    }
                }
                if (Integer.parseInt(solarhoursString)<=0 ){

                    if (amortization<2){
                        LaunchForecast();
                        amortization++;
                    }
                }

                if (Integer.parseInt(sunrise.substring(0, sunrise.length() - 3))>8 ){

                    if (amortization<2){
                        LaunchForecast();
                        amortization++;
                    }
                }
                CurrentPower.setText(""+currentPower);
                NominalPower.setText(nominalpower+" W");
                CityView.setText(""+city);
                CityView.setSelected(true);
                SunriseView.setText(""+sunrise);
                SunsetView.setText(""+sunset);
                SolarHoursView.setText(solarhoursString+ " hr");
                WindView.setText(wind+ "");

                Log.d("Fragment currentpower>", " "+ currentPower);

                if (HotCheck==true){
                    HotView.setVisibility(View.VISIBLE);
                }

                //mainLoader.setVisibility(View.INVISIBLE);

                if (currentPower >15){
                    mobile.setVisibility(View.VISIBLE);
                }
                if (currentPower >60){
                    lamp.setVisibility(View.VISIBLE);
                }
                if (currentPower >300){
                    tv.setVisibility(View.VISIBLE);
                }
                if (currentPower > 1500){
                    teapot.setVisibility(View.VISIBLE);
                }
                if (currentPower > 2000){
                    oven.setVisibility(View.VISIBLE);
                }


                //MasterCloud();
                //sunrise();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                height = displaymetrics.heightPixels;
                width = displaymetrics.widthPixels;
                //Toast.makeText(getActivity(),"Cur Pow "+currentPower,Toast.LENGTH_SHORT).show();


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms

                        rotateFan();
                    }
                }, 4500);
                //Restart recyclerview
                ArrayList<Model> list2= new ArrayList<>();
                //list2.add(new Model())
                list2.add(new Model(Model.TEXT_TYPE,"24 hour power generation forecast  ->",0));
                list2.add(new Model(Model.GRAPH_TYPE,"",0));
                //list.add(new Model(Model.GRAPH_TYPE,"",0 ));
                //list.add(new Model(Model.GRAPH_TYPE,"IRON MAN",0 ));
                final MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list2,getActivity());
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(adapter);

                dialog.dismiss();
                if (SunPeriod==1){
                    sunrise();
                }else if (SunPeriod == 2){
                    deg45();
                }else if (SunPeriod == 3){
                    deg90();
                }else if (SunPeriod == 4){
                    deg135();
                }else if (SunPeriod == 5){
                    sunset();
                }else{
                    night();
                }

            }
        }, 7000);



    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Lifecycle of Fragment->", " Fragment -> onViewCreated launch ");



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        Log.d("Lifecycle of Fragment->", " Fragment -> onResume launch ");
//        dialog = ProgressDialog.show(getActivity(), "",
//                "Collect data from satellites ... Please wait...", true);
        //((MainActivity) Objects.requireNonNull(getActivity())).runforecast();
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        LoadDataFragment();
        MainActivity activity = (MainActivity) getActivity();
        nominalpower = activity.getNominalPower();
        city = activity.getСityData();
//        NominalPower.setText(""+nominalpower+" W");
//        CityView.setText(""+city);


        sunrise = activity.getSunrisetime();
        sunset = activity.getSunsetime();
        solarhoursString = activity.getSolarHours();
        SunPeriod = activity.getSunPeriod();
//        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("MasterSave",MODE_PRIVATE);
//
//        nominalpower = sharedPreferences.getFloat("Nominal_Power", 1f);
//        city = sharedPreferences.getString("MyCity",city);
        Log.d(" Fragment var", sunrise+ " ");
        NominalPower.setText(nominalpower+" W");
        CityView.setText(""+city);
        CityView.setSelected(true);

        //SunriseView.setText(""+sunrise);
        SunriseView.setText(sunrise);
        SunsetView.setText(""+sunset);
        SolarHoursView.setText(solarhoursString+ " hr");

        //CurrentPower.setText(""+currentPower2);
        //mainLoader.setVisibility(View.VISIBLE);


        Log.d("###########","in Resume - City is " +city + nominalpower);
        //dialog.dismiss();
    }

    public void rotateFan() {
        ImageView imageX= (ImageView) Objects.requireNonNull(getView()).findViewById(R.id.fan);
        RotateAnimation rotate = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // Bofort scale
        if (wind == 0){
            rotate.setDuration(9000);
        }else if (wind>=1 && wind <= 3) {
            rotate.setDuration(5000);
        }else if (wind>=2 && wind <=5){
            rotate.setDuration(3000);
        }else if (wind>=6 && wind <= 10){
            rotate.setDuration(1500);
        }else if (wind>=11){
            rotate.setDuration(900);
        }
//        else if (){
//
//        }
//        rotate.setDuration(1500);
        rotate.setRepeatCount(Animation.INFINITE);

        rotate.setInterpolator(new LinearInterpolator());
        imageX.startAnimation(rotate);

    }


    public void night(){
        ImageView sun= getView().findViewById(R.id.ivSun);


        //darken sky
        SkyLayout = (RelativeLayout) getView().findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.day)),
                        Color.parseColor(getString(R.string.night)));

        skyAnim.setDuration(ANIMATION_DURATION);
        //skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        //skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();

        TranslateAnimation animation1 = new TranslateAnimation(xSun, width,ySun, 200 );
        animation1.setDuration(3000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);

    }

    // motion desing

    public void sunrise() {
        ImageView sun= getView().findViewById(R.id.ivSun);


        //darken sky
        SkyLayout = (RelativeLayout) getView().findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.night)),
                        Color.parseColor(getString(R.string.day)));
        skyAnim.setDuration(ANIMATION_DURATION);
        //skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        //skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();

       // MasterCloud();

        //Motion Sun
        //ImageView sun = (ImageView) findViewById(R.id.ivSun);
        sun.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation animation1 = new TranslateAnimation(-220, width/100,250, 200 );
        animation1.setDuration(3000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);

        xSun = sun.getLeft(); // you may remove this
        ySun = sun.getTop();   // you may remove this

    }

    public void deg45() {
        ImageView sun= getView().findViewById(R.id.ivSun);

        //darken sky
        SkyLayout = (RelativeLayout) getView().findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.night)),
                        Color.parseColor(getString(R.string.day)));
        skyAnim.setDuration(ANIMATION_DURATION);

        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();
        //Motion Sun
        sun.setVisibility(View.VISIBLE);
        TranslateAnimation animation1 = new TranslateAnimation(-220, width/6,200, -20 );
        animation1.setDuration(3000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);
        xSun = sun.getLeft();
        ySun = sun.getTop();


    }

    public void deg90() {
        ImageView sun= getView().findViewById(R.id.ivSun);

        //darken sky
        SkyLayout = (RelativeLayout) getView().findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim2 =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.night)),
                        Color.parseColor(getString(R.string.day)));
        skyAnim2.setDuration(ANIMATION_DURATION);
        skyAnim2.setEvaluator(new ArgbEvaluator());
        skyAnim2.start();

        //Motion Sun
        sun.setVisibility(View.VISIBLE);
        TranslateAnimation animation2 = new TranslateAnimation(-220, width/2-100,200, -20 );
        animation2.setDuration(3000);
        animation2.setFillAfter(true);
        sun.startAnimation(animation2);
        xSun = sun.getLeft();
        ySun = sun.getTop();
    }

    public void deg135() {

        ImageView sun= getView().findViewById(R.id.ivSun);

        //darken sky
        SkyLayout = (RelativeLayout) getView().findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.day)),
                        Color.parseColor(getString(R.string.evening)));
        skyAnim.setDuration(ANIMATION_DURATION);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();
        //Motion Sun
        sun.setVisibility(View.VISIBLE);
        TranslateAnimation animation1 = new TranslateAnimation(0, (width/6)*5,-320, -20 );
        animation1.setDuration(4000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);
        xSun = sun.getLeft();
        ySun = sun.getTop();
    }

    public void sunset() {


        ImageView sun= getView().findViewById(R.id.ivSun);

        //darken sky
        SkyLayout = (RelativeLayout) getView().findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.day)),
                        Color.parseColor(getString(R.string.evening)));
        skyAnim.setDuration(ANIMATION_DURATION);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();
        //Motion Sun
        sun.setVisibility(View.VISIBLE);
        TranslateAnimation animation1 = new TranslateAnimation(0, width-200,-320, 200 );
        animation1.setDuration(4000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);
        xSun = sun.getLeft();
        ySun = sun.getTop();
        //Toast.makeText(this, ""+width2, Toast.LENGTH_SHORT).show();
    }

    public void LoadDataFragment(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MasterSave",MODE_PRIVATE);
        try {
            currentPower = sharedPreferences.getInt("CurPow",currentPower);
            nominalpower = sharedPreferences.getFloat("Nominal_Power",(float)nominalpower);
            city = sharedPreferences.getString("MyCity"," ");
            sunrise = sharedPreferences.getString("sunrise",sunrise);
            sunset = sharedPreferences.getString("sunset",sunset);
        }catch(Exception e){
            Log.d("myError", "from Load nominal Frag");
        }



    }
}
