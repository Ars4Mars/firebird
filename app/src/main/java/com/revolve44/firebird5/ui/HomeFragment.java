package com.revolve44.firebird5.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.revolve44.firebird5.MainActivity;
import com.revolve44.firebird5.R;
import java.util.Random;

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

    //SupportMapFragment mapView;

    int random_num;

    ImageView cloud;
    ImageView cloud2;
    ImageView cloud3;
    ImageView cloud4;

    ImageView sun;

    ImageView imageX;



//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);

//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        RelativeLayout SkyLayout = getView().findViewById(R.id.SkyLayout);
//        // or  (ImageView) view.findViewById(R.id.foo);
//        ValueAnimator skyAnim =
//                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
//                        Color.parseColor(getString(R.string.night)),
//                        Color.parseColor(getString(R.string.day)));
//        skyAnim.setDuration(ANIMATION_DURATION);
//        //skyAnim.setRepeatCount(ValueAnimator.INFINITE);
//        //skyAnim.setRepeatMode(ValueAnimator.REVERSE);
//        skyAnim.setEvaluator(new ArgbEvaluator());
//        skyAnim.start();

        Toast.makeText(getActivity(),"Starting fragment"+width,Toast.LENGTH_SHORT).show();
        //MasterCloud();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home, container, false); //added View root

//        ivSun = (ImageView) findViewById(R.id.ivSun);
//        SkyLayout = (RelativeLayout) findViewById(R.id.SkyLayout);
//        ivRoof = (ImageView) findViewById(R.id.ivRoof);

        final ImageView sun= root.findViewById(R.id.ivSun);
        //final RelativeLayout SkyLayout= root.findViewById(R.id.SkyLayout);
        final ImageView ivRoof= root.findViewById(R.id.ivRoof);
        //final ImageView oven= root.findViewById(R.id.oven);

        final ImageView cloud= root.findViewById(R.id.cloud1);
        final ImageView cloud2= root.findViewById(R.id.cloud2);
        final ImageView cloud3= root.findViewById(R.id.cloud3);
        final ImageView cloud4= root.findViewById(R.id.cloud4);

        final ImageView imageX= root.findViewById(R.id.fan);

        final int width = getResources().getConfiguration().screenWidthDp;
        final int height = getResources().getConfiguration().screenHeightDp;

        //Random
        int min = 1;
        int max = 50;

        Random r = new Random();
        random_num = r.nextInt(max - min + 1) + min;
        Toast.makeText(getActivity(),"Text!"+width,Toast.LENGTH_SHORT).show();
        //Random end

        //sunny day



        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);























//        ImageView sun= getView().findViewById(R.id.ivSun);
//
//        //darken sky
//        SkyLayout = (RelativeLayout) getView().findViewById(R.id.SkyLayout);
//        ValueAnimator skyAnim =
//                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
//                        Color.parseColor(getString(R.string.night)),
//                        Color.parseColor(getString(R.string.day)));
//        skyAnim.setDuration(ANIMATION_DURATION);
//        //skyAnim.setRepeatCount(ValueAnimator.INFINITE);
//        //skyAnim.setRepeatMode(ValueAnimator.REVERSE);
//        skyAnim.setEvaluator(new ArgbEvaluator());
//        skyAnim.start();
//
//        MasterCloud();
//
//        //Motion Sun
//        //ImageView sun = (ImageView) findViewById(R.id.ivSun);
//        sun.setVisibility(View.VISIBLE);
//        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
//        TranslateAnimation animation1 = new TranslateAnimation(-220, width/100,250, 200 );
//        animation1.setDuration(3000);
//        animation1.setFillAfter(true);
//        sun.startAnimation(animation1);



    }

    public void firstsection() {

    }

    public void secondsection(View view) {

    }

    public void thirdsection(View view) {
        //startActivity(new Intent(MainActivity.this, MapsActivity.class));

    }

    public void MasterCloud(){

        final int width = getResources().getConfiguration().screenWidthDp;
        final int height = getResources().getConfiguration().screenHeightDp;

        final ImageView cloud= getView().findViewById(R.id.cloud1);
        final ImageView cloud2= getView().findViewById(R.id.cloud2);
        final ImageView cloud3= getView().findViewById(R.id.cloud3);
        final ImageView cloud4= getView().findViewById(R.id.cloud4);
        //Motion CloudL
        //ImageView cloud = (ImageView) findViewById(R.id.ivCloudLARGE);
        cloud.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation cloudanim = new TranslateAnimation(-720, (width/100)+random_num,-20, -20 );
        cloudanim.setDuration(3000);
        cloudanim.setFillAfter(true);
        cloud.startAnimation(cloudanim);
        //
        //Motion CloudL
        //ImageView cloud2 = (ImageView) findViewById(R.id.ivCloudLARGE2);
        cloud2.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation cloudanim2 = new TranslateAnimation(-720, (width/3)+random_num,-20, -20 );
        cloudanim2.setDuration(3500);
        cloudanim2.setFillAfter(true);
        cloud2.startAnimation(cloudanim2);
        //
        //Motion CloudL
        //ImageView cloud3 = (ImageView) findViewById(R.id.ivCloudLARGE3);
        cloud3.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation cloudanim3 = new TranslateAnimation(-720, (width/2)+random_num,-20, -20 );
        cloudanim3.setDuration(4000);
        cloudanim3.setFillAfter(true);
        cloud3.startAnimation(cloudanim3);
        //
        //Motion CloudL
        //ImageView cloud4 = (ImageView) findViewById(R.id.ivCloudLARGE4);
        cloud4.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation cloudanim4 = new TranslateAnimation(-720, (width-100)+random_num,-20, -20 );
        cloudanim4.setDuration(4500);
        cloudanim4.setFillAfter(true);
        cloud4.startAnimation(cloudanim4);
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

        MasterCloud();

        //Motion Sun
        //ImageView sun = (ImageView) findViewById(R.id.ivSun);
        sun.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation animation1 = new TranslateAnimation(-220, width/100,250, 200 );
        animation1.setDuration(3000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);

    }

    public void deg45(View view) {
        //darken sky
        //SkyLayout = (RelativeLayout) findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.night)),
                        Color.parseColor(getString(R.string.day)));
        skyAnim.setDuration(ANIMATION_DURATION);
        //skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        //skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();
        //Motion Sun
        //ImageView image4 = (ImageView) findViewById(R.id.ivSun);
        sun.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation animation1 = new TranslateAnimation(-220, width/6,200, -20 );
        animation1.setDuration(3000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);


    }

    public void deg90(View view) {
        //darken sky
        //SkyLayout = (RelativeLayout) findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim2 =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.night)),
                        Color.parseColor(getString(R.string.day)));
        skyAnim2.setDuration(ANIMATION_DURATION);
        //skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        //skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim2.setEvaluator(new ArgbEvaluator());
        skyAnim2.start();

        //Motion Sun
        //ImageView image4 = (ImageView) findViewById(R.id.ivSun);
        sun.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation animation2 = new TranslateAnimation(-220, width/2-100,200, -20 );
        animation2.setDuration(3000);
        animation2.setFillAfter(true);
        sun.startAnimation(animation2);
    }

    public void deg135(View view) {
        //darken sky
        //SkyLayout = (RelativeLayout) findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.night)),
                        Color.parseColor(getString(R.string.day)));
        skyAnim.setDuration(ANIMATION_DURATION);
        //skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        //skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();
        //Motion Sun
        //ImageView image4 = (ImageView) findViewById(R.id.ivSun);
        sun.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation animation1 = new TranslateAnimation(0, (width/6)*5,-320, -20 );
        animation1.setDuration(4000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);
    }

    public void sunset(View view) {
        //darken sky
        //SkyLayout = (RelativeLayout) findViewById(R.id.SkyLayout);
        ValueAnimator skyAnim =
                ObjectAnimator.ofInt(SkyLayout, "backgroundColor",
                        Color.parseColor(getString(R.string.night)),
                        Color.parseColor(getString(R.string.day)));
        skyAnim.setDuration(ANIMATION_DURATION);
        //skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        //skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();
        //Motion Sun
        //ImageView image4 = (ImageView) findViewById(R.id.ivSun);
        sun.setVisibility(View.VISIBLE);
        //TranslateAnimation animation2 = new TranslateAnimation(a, 200, c, 280);
        TranslateAnimation animation1 = new TranslateAnimation(0, width-200,-320, 200 );
        animation1.setDuration(4000);
        animation1.setFillAfter(true);
        sun.startAnimation(animation1);

        int width2 = sun.getDrawable().getIntrinsicWidth();
        //Toast.makeText(this, ""+width2, Toast.LENGTH_SHORT).show();
    }

    public void firsttest(View view) {
        //ImageView imageX= (ImageView) findViewById(R.id.birds);
        RotateAnimation rotate = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setDuration(Animation.INFINITE);
//        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(500);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        imageX.startAnimation(rotate);

    }

}
