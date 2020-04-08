package com.revolve44.firebird5.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.revolve44.firebird5.MainActivity;
import com.revolve44.firebird5.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.revolve44.firebird5.MainActivity.SHARED_PREFS;

//import static com.revolve44.firebird5.R.id.map;

public class CalcFragment extends Fragment  {
    EditText PriceEnergy;
    EditText PriceofStation;
    EditText Grid;
    EditText CurrentPower;

    float PricekWh;
    float CostStation;
    float TimesOffGrid;
    float CostFood;
    float PaybackPeriod;

    float NominalPower;
    String Language;
    public String Currency;

    public static final String SHARED_PREFS = "sharedPrefs";





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View root =  inflater.inflate(R.layout.fragment_calc, container, false);
        Language = Locale.getDefault().getDisplayLanguage();


        MainActivity activity = (MainActivity) getActivity();
        NominalPower = activity.getNominalPower();

        final EditText PriceEnergyINPUT = root.findViewById(R.id.price_per_kWh);
        final EditText PriceofStationINPUT = root.findViewById(R.id.price_of_station);
        final EditText GridINPUT = root.findViewById(R.id.times_grid_off);
        final EditText CostFoodINPUT = root.findViewById(R.id.cost_food);

        final TextView PaybackView = root.findViewById(R.id.paybackView);
        //Spinner spinner = (Spinner) root.findViewById(R.id.Currency);

        // Spinner element
        String [] values =
                {"$","€","₽",};
        Spinner spinner = (Spinner) root.findViewById(R.id.Currency);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


//        List<String> spinnerArray =  new ArrayList<String>();
//        spinnerArray.add("item1");
//        spinnerArray.add("item2");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.Currency, spinnerArray);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinnerArray.setAdapter(adapter);
        Button button = root.findViewById(R.id.tocalc);
//        Toast.makeText(getActivity(),"Starting fragment "+city,Toast.LENGTH_SHORT).show();

        button.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v)
            {


                PricekWh = Float.parseFloat(PriceEnergyINPUT.getText().toString());
                CostStation = Float.parseFloat(PriceofStationINPUT.getText().toString());
                TimesOffGrid = Float.parseFloat(GridINPUT.getText().toString());
                CostFood = Float.parseFloat(CostFoodINPUT.getText().toString());


                if (Currency=="$"){
                    PaybackPeriod = CostStation/(NominalPower * (PricekWh/100) * 5 * 365 + TimesOffGrid * CostFood);
                    DecimalFormat df = new DecimalFormat("##.##");
                    PaybackView.setText(df.format(PaybackPeriod)+" years");
                    //Toast.makeText(getActivity(),"$year ->  "+PaybackPeriod,Toast.LENGTH_SHORT).show();

                }else if (Currency=="€"){
                    PaybackPeriod = CostStation/ (NominalPower * (PricekWh/100) * 5 * 365 + TimesOffGrid * CostFood);
                    DecimalFormat df = new DecimalFormat("##.##");
                    PaybackView.setText(df.format(PaybackPeriod)+" years");
                }else if (Currency=="₽"){
                    PaybackPeriod = CostStation/(NominalPower * PricekWh * 5 * 365 + TimesOffGrid * CostFood);
                    DecimalFormat df = new DecimalFormat("##.##");
                    PaybackView.setText(df.format(PaybackPeriod)+" years");
                    //Toast.makeText(getActivity(),"РУБЛЬCurrency "+PaybackPeriod+" NomPow "+ NominalPower,Toast.LENGTH_SHORT).show();
                }else {
                    PaybackPeriod = CostStation - (NominalPower * (PricekWh/100) * 5 * 365 + TimesOffGrid * CostFood);
                    DecimalFormat df = new DecimalFormat("##.##");
                    PaybackView.setText(df.format(PaybackPeriod)+" years");
                }


                //CalculatePayback();
            }


        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos,
                                       long id) {
                Currency = adapterView.getItemAtPosition(pos).toString();
                Toast.makeText(adapter.getContext(),
                        Currency + " is chosen",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                 Currency = "$";
            }
        });

        return root;
    }
}
