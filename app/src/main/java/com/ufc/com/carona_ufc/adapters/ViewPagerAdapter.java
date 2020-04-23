package com.ufc.com.carona_ufc.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ufc.com.carona_ufc.fragments.ui.CaronasGratisFragment;
import com.ufc.com.carona_ufc.fragments.ui.CaronasPagasFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new CaronasGratisFragment();
                break;

            case 1:
                fragment = new CaronasPagasFragment();
                break;
        }
        return fragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String str = null;
        switch (position) {
            case 0:
                str = "Caronas Gratis";
                break;
            case 1:
                str = "Caronas Pagas";
                break;
        }
        return str;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
