package com.ufc.com.carona_ufc.fragments.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ufc.com.carona_ufc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OferecerCaronaFragment extends Fragment {


    public OferecerCaronaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_oferecer_carona, container, false);
    }

}
