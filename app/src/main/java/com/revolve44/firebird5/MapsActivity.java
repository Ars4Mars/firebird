package com.revolve44.firebird5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener{

    private GoogleMap mMap;
    public LatLng lol;
    Marker marker;
    double latitude;
    double longitude;
    int NominalPower;

    String Latitude;
    String Longitude;

    String currentTimezone = "GMT+0";

    Boolean check = false;

    LatLng MYLOCATION =  new LatLng (latitude, longitude);

    public static final String SHARED_PREFS = "sharedPrefs";


    LinearLayout Loader;

    TextView textView;

    EditText inputnominalpower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //textView = findViewById(R.id.LOLIK);

        Loader = (LinearLayout) findViewById(R.id.loader);
        inputnominalpower = findViewById(R.id.nominalpower);
    }

    /** ШАБЛОННЫЙ ГУГОЛОВСКИЙ КОМЕНТАРИЙ ПО ПОВОДУ ИХ КАРТ, РЕЛАКС. донт ворри
     *
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
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

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

    public void ClickButton(View view) {

        // Float Coordination ==> String Coordination
        // its need for Retrofit request

        Latitude = String.valueOf(latitude);
        Latitude = Latitude.substring(0,6); // cutting symbols on veeeeeryyy loooong simbols of coordination

        Longitude = String.valueOf(longitude);
        Longitude = Longitude.substring(0,6); // cutting symbols on veeeeeryyy loooong simbols of coordination

        Toast toast = Toast.makeText(MapsActivity.this,"In maps "+Latitude+" "+Longitude,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,250);
        toast.show();
        saveData();

        // send coordination to MainActivity
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("FROM_MAPS1", Latitude);
        intent.putExtra("FROM_MAPS2", Longitude);
        intent.putExtra("CHECK_SAVINGS",check);
        startActivity(intent);
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat("latitudeF",(float)latitude);
        editor.putFloat("longitudeF",(float)longitude);
        editor.putBoolean("CHECK_SAVINGS",check);
        editor.putString("lati",Latitude);
        editor.putString("long",Longitude);

//        editor.putString(TEXT, textView.getText().toString());
//        editor.putBoolean(SWITCH1, switch1.isChecked());

        editor.apply();// change from .commit()

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }



    public void testMEM(View view) {
        Toast.makeText(this, "load "+latitude+" long"+longitude, Toast.LENGTH_SHORT).show();
    }
}




