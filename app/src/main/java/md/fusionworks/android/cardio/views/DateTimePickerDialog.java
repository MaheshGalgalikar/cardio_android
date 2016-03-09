package md.fusionworks.android.cardio.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import md.fusionworks.android.cardio.R;

/**
 * Created by admin on 24.08.2015.
 */
public class DateTimePickerDialog extends DialogFragment implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {


    public static interface OnDatePickedListener {

        public abstract void onDatePicked(Calendar calendar);
    }

    private static final Long MAX_DATE = Long.valueOf(0x1f40060ec80L);
    private static final Long MIN_DATE = Long.valueOf(0x112a880L);
    public static final String TAG = DateTimePickerDialog.class.getName();
    private Calendar calendar;
    private DatePicker datePicker;
    private OnDatePickedListener listener;
    private TimePicker timePicker;

    public DateTimePickerDialog() {
    }

    public static void show(FragmentManager fragmentmanager, OnDatePickedListener ondatepickedlistener) {

        DateTimePickerDialog datetimepickerdialog = new DateTimePickerDialog();
        datetimepickerdialog.setOnDatePickedListener(ondatepickedlistener);
        datetimepickerdialog.show(fragmentmanager, TAG);
    }

    public Dialog onCreateDialog(Bundle bundle) {

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_date_time_picker, null));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (listener != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, datePicker.getYear());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.DATE, datePicker.getDayOfMonth());
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour().intValue());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute().intValue());
                    listener.onDatePicked(calendar);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getDialog().cancel();
            }
        });

        return builder.create();
    }


    private void updateUI() {

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        datePicker.init(year, month, date, this);
        datePicker.setMaxDate(MAX_DATE.longValue());
        datePicker.setMinDate(MIN_DATE.longValue());
        timePicker.setCurrentHour(Integer.valueOf(hourOfDay));
        timePicker.setCurrentMinute(Integer.valueOf(minute));
        timePicker.setOnTimeChangedListener(this);
    }

    public void setOnDatePickedListener(OnDatePickedListener ondatepickedlistener) {
        listener = ondatepickedlistener;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(Boolean.valueOf(false));
        updateUI();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

    }
}
