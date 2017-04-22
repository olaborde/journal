package com.osselaborde.journal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import java.util.Calendar;

/**
 * Date picker for choosing a date.
 */
public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    private DateSetListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (listener != null) {
            listener.onDateSet(year, month, day);
        }
    }

    public void setDateSetListener(DateSetListener listener) {
        this.listener = listener;
    }

    public interface DateSetListener {
        void onDateSet(int year, int month, int day);
    }
}
