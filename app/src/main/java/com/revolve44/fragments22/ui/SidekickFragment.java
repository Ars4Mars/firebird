package com.revolve44.fragments22.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.revolve44.fragments22.R;

/*
@revolna.com
Constructor mk1
 */

public class SidekickFragment extends Fragment {

    public float SumPower;
    public float SumBattery;
    public float SumAdditional;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_sidekick, container, false);

        final CheckBox lamp = root.findViewById(R.id.lamp);
        final CheckBox tv = root.findViewById(R.id.tv);
        final CheckBox kettle = root.findViewById(R.id.kettle);
        final CheckBox oven = root.findViewById(R.id.oven);

        final CheckBox checkGrid = root.findViewById(R.id.checkGrid);
        final CheckBox checkBattery = root.findViewById(R.id.checkbattery);

        final EditText inputAdditional = root.findViewById(R.id.inputAdditional);
        final TextView consrtuctView = root.findViewById(R.id.constructView);

        final Button tocontruct = root.findViewById(R.id.toconstruct);

        inputAdditional.setText("0");
        tocontruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lamp.isChecked()){
                    SumPower = SumPower+ 60;
                }
                if (tv.isChecked()){
                    SumPower = SumPower+ 300;
                }
                if (kettle.isChecked()){
                    SumPower = SumPower+ 1500;
                }
                if (oven.isChecked()){
                    SumPower = SumPower+ 2000;
                }

                if (checkGrid.isChecked() & checkBattery.isChecked()){
                    SumPower = SumPower*0.8f;
                    SumBattery = SumPower *0.1f+SumPower;
                }else if (checkBattery.isChecked()){
                    SumPower = SumPower* 1.5f;
                    SumBattery = SumPower *0.5f+SumPower;
                }else{
                    SumPower = SumPower*1.5f;
                }

                try {

                    SumAdditional = Float.parseFloat(inputAdditional.getText().toString());
                    SumPower= SumPower + SumAdditional;
                }catch(Exception e) {
                    Toast.makeText(getActivity(),"Error but add is  "+SumAdditional,Toast.LENGTH_SHORT).show();
                }

                consrtuctView.setText("you need: ~" + SumPower+" W from solar panels"+ " \n and ~" + SumBattery +" Ah batteries ");
                SumPower = 0;
                SumBattery = 0;

            }
        });
        return root;
    }
}
