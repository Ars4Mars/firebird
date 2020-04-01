package com.revolve44.firebird5.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.revolve44.firebird5.R;

//import static com.revolve44.firebird5.R.id.map;

public class CalcFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_calc, container, false);

        CalcFragment frag1 = (CalcFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

//        Fragment fragment = new SettingsFragment();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(map, fragment).commit();

        return root;
    }
}
