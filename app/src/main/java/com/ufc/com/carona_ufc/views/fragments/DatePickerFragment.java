package com.ufc.com.carona_ufc.views.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) - 8; //BUG no Calender
        int dia = calendar.get(Calendar.DAY_OF_YEAR);

        return new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) getActivity(), ano, mes, dia);
    }

}
