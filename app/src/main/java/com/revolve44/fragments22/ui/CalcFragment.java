package com.revolve44.fragments22.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.revolve44.fragments22.MainActivity;
import com.revolve44.fragments22.R;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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

        //((MainActivity) Objects.requireNonNull(getActivity())).TimeManipulations();
        MainActivity activity = (MainActivity) getActivity();
        NominalPower = activity.getNominalPower();
         Float nominalpower = activity.getNominalPower();

        final TextView NominalView2= root.findViewById(R.id.NominalView2);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                //((MainActivity) Objects.requireNonNull(getActivity())).TimeManipulations();
                MainActivity activity = (MainActivity) getActivity();
                NominalPower = activity.getNominalPower();
                Float nominalpower = activity.getNominalPower();

                NominalView2.setText("" + nominalpower+" W - is nominal power of your solar panels");
                NominalView2.setSelected(true);
            }
        }, 2000);

        final EditText PriceEnergyINPUT = root.findViewById(R.id.price_per_kWh);
        final EditText PriceofStationINPUT = root.findViewById(R.id.price_of_station);
        final EditText GridINPUT = root.findViewById(R.id.times_grid_off);
        final EditText CostFoodINPUT = root.findViewById(R.id.cost_food);

        final TextView PaybackView = root.findViewById(R.id.paybackView);

        final Switch Checkgrid = root.findViewById(R.id.checkgrid);

        // Spinner element
        String [] values =
                {"$","€","₽",};
        Spinner spinner = (Spinner) root.findViewById(R.id.Currency);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


        TimesOffGrid = 365; // default - user do not have connection to the power grid
        Button button = root.findViewById(R.id.tocalc);
//        Toast.makeText(getActivity(),"Starting fragment "+city,Toast.LENGTH_SHORT).show();
        Checkgrid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    // user have connection to power grid
                    try {
                        TimesOffGrid = Float.parseFloat(GridINPUT.getText().toString());
                    }catch (Exception e){
                        TimesOffGrid = 0;
                        GridINPUT.setText("0");
                    }

                } else {
                    // user don`t have connection to power grid
                    TimesOffGrid = 365;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v)
            {
                try {
                    PricekWh = Float.parseFloat(PriceEnergyINPUT.getText().toString());
                    CostStation = Float.parseFloat(PriceofStationINPUT.getText().toString());

                    try {
                        CostFood = Float.parseFloat(CostFoodINPUT.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(getActivity(),"Food empty",Toast.LENGTH_SHORT).show();
                    }

                    if (NominalPower>0) {

                        if (Currency=="$"){
                            PaybackPeriod = CostStation/((NominalPower/1000) * (PricekWh/100) * 5 * 365 + TimesOffGrid * CostFood);
                            DecimalFormat df = new DecimalFormat("##.##");
                            PaybackView.setText(df.format(PaybackPeriod)+" years");
                            //Toast.makeText(getActivity(),"$year ->  "+PaybackPeriod,Toast.LENGTH_SHORT).show();

                        }else if (Currency=="€"){

                            PaybackPeriod = CostStation/ ((NominalPower/1000) * (PricekWh/100) * 5 * 365 + TimesOffGrid * CostFood);
                            DecimalFormat df = new DecimalFormat("##.##");
                            PaybackView.setText(df.format(PaybackPeriod)+" years");

                        }else if (Currency=="₽"){

                            PaybackPeriod = CostStation/((NominalPower/1000) * PricekWh * 5 * 365 + TimesOffGrid * CostFood);
                            DecimalFormat df = new DecimalFormat("##.##");
                            PaybackView.setText(df.format(PaybackPeriod)+" years");
                            //Toast.makeText(getActivity(),"Food "+CostFood + "Times grid "+ TimesOffGrid,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(),"РУБЛЬCurrency "+PaybackPeriod+" NomPow "+ NominalPower,Toast.LENGTH_SHORT).show();

                        }
//                        else {
//                            PaybackPeriod = CostStation /((NominalPower/1000) * (PricekWh/100) * 5 * 365 + TimesOffGrid * CostFood);
//                            DecimalFormat df = new DecimalFormat("##.##");
//                            PaybackView.setText(df.format(PaybackPeriod)+" years");
//                        }
                    }else{
                        Toast.makeText(getActivity(),"Make sure you input a NOMINAL POWER ",Toast.LENGTH_SHORT).show();
                    }




                }catch (Exception e){
                    Toast.makeText(getActivity(),"Please correct fill above forms ",Toast.LENGTH_SHORT).show();
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

        NominalView2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"This is the rated (nominal) power of your solar panels ",Toast.LENGTH_SHORT).show();
            }

        });

        return root;
    }
}
