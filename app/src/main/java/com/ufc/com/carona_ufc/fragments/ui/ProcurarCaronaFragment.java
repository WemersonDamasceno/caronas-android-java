package com.ufc.com.carona_ufc.fragments.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProcurarCaronaFragment extends Fragment {


    public ProcurarCaronaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_procurar_carona, container, false);

        ViewPager viewPager = view.findViewById(R.id.pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);


        return view;
    }
}
