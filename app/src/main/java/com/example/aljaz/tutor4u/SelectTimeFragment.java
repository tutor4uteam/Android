package com.example.aljaz.tutor4u;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class SelectTimeFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
