package com.revolve44.fragments22;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener{

    private GoogleMap mMap;
    public LatLng lol;
    Marker marker;
    double latitude;
    double longitude;
    public Float NominalPower = 0.0f;

    public String Latitude;
    public String Longitude;

    String currentTimezone = "GMT+0";

    Boolean check = false;

    LatLng MYLOCATION =  new LatLng (latitude, longitude);

    LinearLayout Loader;

    TextView textView;

    EditText inputnominalpower;


    boolean tempFahrenheit;
    CheckBox checkImperial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        checkImperial = findViewById(R.id.checkImperial);
        //Loader = (LinearLayout) findViewById(R.id.loader);
        inputnominalpower = findViewById(R.id.nominalpower);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //textView = findViewById(R.id.LOLIK);
        final SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        tempFahrenheit = sharedPreferences.getBoolean("tempFahrenheit",tempFahrenheit);

        try {
            NominalPower = sharedPreferences.getFloat("Nominal_Power", (float) NominalPower);
        }catch(Exception e){
            Toast.makeText(MapsActivity.this, "Maybe  error. NM is "+ NominalPower, Toast.LENGTH_SHORT).show();
        }

        if (!sharedPreferences.getBoolean("firstTime", false)) {
            editor.putBoolean("firstTime", true);
            editor.apply();
            // <---- run your one time code here
            AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
            alertDialog.setTitle("Before starting");
            alertDialog.setMessage("Please set location and nominal power (theoretical max power) of your solar station");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            // mark first time has ran.



        }
        inputnominalpower.setText(""+Math.round(NominalPower));



        checkImperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("tempFahrenheit", ((CheckBox) view).isChecked());
                editor.commit();
                if (checkImperial.isChecked()) {

                } else {

                }
            }
        });

        checkImperial.setChecked(tempFahrenheit);

        Log.d("after Sh pref CHECKBOX ", tempFahrenheit+ " ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle ->","Maps onResume launch ");
        LoadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lifecycle ->","Maps onPause launch ");
        SaveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Lifecycle ->","Maps onStop launch ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Lifecycle ->","Maps onDestroy launch ");
    }


    /** ШАБЛОННЫЙ ГУГОЛОВСКИЙ КОМЕНТАРИЙ ПО ПОВОДУ ИХ КАРТ, РЕЛАКС. донт ворри
     * просто оставил почитать
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
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);

        latitude = sharedPreferences.getFloat("latitudeF",(float)latitude);
        longitude = sharedPreferences.getFloat("longitudeF",(float)longitude);
        check = sharedPreferences.getBoolean("CHECK_SAVINGS",check);
        mMap = googleMap;


        if(check = true){
            LatLng resumedPosition = new LatLng(latitude,longitude);

            marker = googleMap.addMarker(new MarkerOptions()
                    .position(resumedPosition)
                    .draggable(true)
            );
            mMap.setOnMarkerDragListener(this); // bridge for connect marker with methods located below
            mMap.animateCamera(CameraUpdateFactory.newLatLng(resumedPosition)); // move camera to current position
        }else{
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(MYLOCATION)
                    .draggable(true)
            );
            mMap.setOnMarkerDragListener(this); // bridge for connect marker with methods located below
            mMap.animateCamera(CameraUpdateFactory.newLatLng(MYLOCATION)); // move camera to current position

        }

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //setUpTracking();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                // create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps");

                // adding marker
                mMap.addMarker(marker);

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                 //Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                // get coord
                latitude = latLng.latitude;
                longitude = latLng.longitude;

                Toast.makeText(MapsActivity.this, ""+latitude+" "+longitude, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(MapsActivity.this, "onMarkerDragStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Toast.makeText(MapsActivity.this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        MYLOCATION = marker.getPosition();
        textView.setText(lol+"");
        Toast.makeText(MapsActivity.this, ""+lol, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        if (NominalPower<=0){
            Toast.makeText(MapsActivity.this, "Please input NOMINAL POWER of your solar panels ", Toast.LENGTH_SHORT).show();
        }
        if (NominalPower>0){
            super.onBackPressed(); // default method to back
        }
    }

    public void ClickButton(View view) {

        // Float Coordination ==> String Coordination
        // its need for Retrofit request

        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //put to sharedpref:
        //editor.putBoolean("tempFahrenheit",tempFahrenheit);

        Log.d("Listener CHECKBOX ", tempFahrenheit+ " ");

        //////////////////Cut variable

        Latitude = String.valueOf(latitude);
        Longitude = String.valueOf(longitude);
        try{
            Latitude = Latitude.substring(0,6); // cutting symbols on veeeeeryyy loooong simbols of coordination
            Longitude = Longitude.substring(0,6); // cutting symbols on veeeeeryyy loooong simbols of coordination
        }catch (Exception q){

        }
        editor.putString("lati",Latitude);
        editor.putString("long",Longitude);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                       Start savings
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        SaveData();
        try {
            NominalPower = Float.parseFloat(inputnominalpower.getText().toString());


            if (NominalPower > 0) {
                Toast toast = Toast.makeText(MapsActivity.this,"In maps "+Latitude+" "+Longitude,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                SaveData();

                // send coordination to MainActivity, in future i replce this
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("FROM_MAPS1", Latitude);
                intent.putExtra("FROM_MAPS2", Longitude);
                intent.putExtra("CHECK_SAVINGS",check);
                startActivity(intent);
            }
            if (NominalPower==0){
                Toast.makeText(MapsActivity.this, "Please input NOMINAL POWER of your solar panels ", Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e1){
            if (NominalPower > 0){
                Toast toast = Toast.makeText(MapsActivity.this,"In maps "+Latitude+" "+Longitude,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                SaveData();

                // send coordination to MainActivity, in future i replce this
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("FROM_MAPS1", Latitude);
                intent.putExtra("FROM_MAPS2", Longitude);
                intent.putExtra("CHECK_SAVINGS",check);
                startActivity(intent);
            }else {
                try {
//                inputnominalpower.setText(""+NominalPower);
                    NominalPower = Float.parseFloat(inputnominalpower.getText().toString());

                    if (NominalPower > 0) {
                        Toast toast = Toast.makeText(MapsActivity.this, "In maps " + Latitude + " " + Longitude, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();
                        SaveData();

                        // send coordination to MainActivity, in future i replce this
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.putExtra("FROM_MAPS1", Latitude);
                        intent.putExtra("FROM_MAPS2", Longitude);
                        intent.putExtra("CHECK_SAVINGS", check);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MapsActivity.this, "Please input NOMINAL POWER of your solar panels ", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e2) {
                    Toast.makeText(MapsActivity.this, "Please input NOMINAL POWER of your solar panels ", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public void SaveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();



        editor.putFloat("latitudeF",(float)latitude);
        editor.putFloat("longitudeF",(float)longitude);
        editor.putFloat("Nominal_Power",(float)NominalPower);

        editor.putBoolean("CHECK_SAVINGS",check);
        editor.putString("lati",Latitude);
        editor.putString("long",Longitude);

//        editor.putString(TEXT, textView.getText().toString());
//        editor.putBoolean(SWITCH1, switch1.isChecked());

        editor.apply();// change from .commit()

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        lat = sharedPreferences.getString("lati",lat);
//        lon = sharedPreferences.getString("long",lon);
//        NominalPower = sharedPreferences.getFloat("Nominal_Power", (float) NominalPower);
//
//        Log.d("************", "LAT LOT:  "+lat+ " and " + lon);
//        Log.d("************", "GMT:  "+GMT);
//        Log.d("************", "temp:  "+temp);
    }





    public void testMEM(View view) {
        Toast.makeText(this, "load "+latitude+" long"+longitude, Toast.LENGTH_SHORT).show();
    }

    public void writetodev(View view) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@revolna.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "i have a question or suggestion");
        i.putExtra(Intent.EXTRA_TEXT   , "So, ...");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MapsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void gosite(View view) {
        goToUrl ( "http://revolna.com/");
    }

    public void goinstagram(View view) {
        goToUrl ( "https://www.instagram.com/revolna_workshop/");
    }

    public void gotelegram(View view) {
        goToUrl ( "https://t.me/solarpan");

    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    public void twitter(View view) {
        goToUrl ( "https://twitter.com/arstagaev");
    }
}




