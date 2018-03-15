package ru.samara.mapapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.GregorianCalendar;

import ru.samara.mapapp.R;
import ru.samara.mapapp.utils.ActivityUtils;


public class DateTimePickerDialog extends Dialog {

    private DateTimePickerDialogListener listener;

    public DateTimePickerDialog(@NonNull Context context, DateTimePickerDialogListener listener) {
        super(context);
        this.listener = listener;
        onCreate();
    }

    private void onCreate() {
        setContentView(R.layout.date_picker);
        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);
        final int[] hour = {-1}, minute = {-1};
        findViewById(R.id.date_picker_accept).setOnClickListener(view -> {
            if (datePicker.getVisibility() == View.VISIBLE) {
                ActivityUtils.changeVisible(datePicker);
                ActivityUtils.changeVisible(timePicker);
                timePicker.setOnTimeChangedListener((v, hourOfDay, minutes) -> {
                    hour[0] = hourOfDay;
                    minute[0] = minutes;
                });
            } else {
                if (hour[0] != -1 && minute[0] != -1) {
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), hour[0], minute[0]);
                    listener.onComplete(calendar.getTimeInMillis() / 1000);
                    dismiss();
                }
            }
        });

    }


}
