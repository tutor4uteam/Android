package com.example.aljaz.tutor4u;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
        Button btn_date_pick = getActivity().findViewById(R.id.btn_date_pick);
        btn_date_pick.setText(day + "/" + month + "/" + year);
        //dob.setText(month+"/"+day+"/"+year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
