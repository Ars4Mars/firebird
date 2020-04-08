package com.revolve44.firebird5.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.revolve44.firebird5.R;

public class SidekickFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_calc, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        final EditText num1 = root.findViewById(R.id.editText4);
        final EditText num2 = root.findViewById(R.id.editText5);
        final Button add = root.findViewById(R.id.superbutton4);
        final TextView lol = root.findViewById(R.id.output);

//        CalcFragment frag1 = (CalcFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);

//        Fragment fragment = new SettingsFragment();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(map, fragment).commit();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Now we can have sum of two input numbers!!!
                int number1 = Integer.parseInt(num1.getText().toString());
                int number2 = Integer.parseInt(num2.getText().toString());
                int sum = number1 + number2;

                lol.setText("Answer is " + sum);

                // do something
            }
        });
        return root;
    }
}
