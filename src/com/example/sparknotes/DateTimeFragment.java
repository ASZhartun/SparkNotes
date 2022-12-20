package com.example.sparknotes;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

// Using DialogFragment has a disadvantage. It doesn't handle screen orientation. And crashes the app.
// So, you need to use setRetainInstance() inside onCreate() of DialogFragment class.

public class DateTimeFragment extends DialogFragment {

	Button btnDate, btnTime;
	TextView tvDate;
	DatePicker datePicker;
	TimePicker timePicker;

	private int year, month, day, hour, minute;

	public DateTimeFragment(TextView tv) {
		tvDate = tv;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public Dialog onCreateDialog(Bundle saved) {
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

	    LayoutInflater inflater = getActivity().getLayoutInflater();
	  	View view = inflater.inflate(R.layout.dialog_datetime, null);

		btnDate = (Button)view.findViewById(R.id.button1);
		btnTime = (Button)view.findViewById(R.id.button2);
		btnDate.setEnabled(false);
		btnTime.setEnabled(true);

		btnDate.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				btnTime.setEnabled(true);
				v.setEnabled(false);
				datePicker.setVisibility(View.VISIBLE);
				timePicker.setVisibility(View.INVISIBLE);
			}
		});		
		btnTime.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				btnDate.setEnabled(true);
				v.setEnabled(false);
				datePicker.setVisibility(View.INVISIBLE);
				timePicker.setVisibility(View.VISIBLE);
			}
		});
		
		datePicker = (DatePicker)view.findViewById(R.id.datePicker1);
		timePicker = (TimePicker)view.findViewById(R.id.timePicker1);

		// set current date into datepicker
		datePicker.init(year, month, day,
		    new OnDateChangedListener(){
		    	@Override
		    	public void onDateChanged(DatePicker view, int y, int m,int d) {
		    		year=y; month=m; day=d;
		    		updateDisplay();
		     }});

		// set current time into timepicker
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener(){
		   @Override
		   public void onTimeChanged(TimePicker view, int h, int m) {
			   hour=h; minute=m;
			   updateDisplay();
		   }});

		datePicker.setVisibility(View.VISIBLE);
		timePicker.setVisibility(View.INVISIBLE);

		updateDisplay();

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setView(view);
	    builder.setTitle("Выбор Даты и Времени");
	    builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				tvDate.setText(new StringBuilder()
						.append(year).append("-")
						.append(pad(month+1)).append("-")
						.append(pad(day)).append(" ")
						.append(pad(hour)).append(":")
						.append(pad(minute))
					);
			}
		});
	    builder.setNegativeButton("Отказаться", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	    builder.setCancelable(false);

	    return builder.create();
	}

	// updates the date we display in the TextView
    private void updateDisplay() {
    	btnDate.setText(new StringBuilder()
             .append(pad(day)).append("-")
             .append(pad(month+1)).append("-") // Month is 0 based so add 1
             .append(year).append(" "));
    	btnTime.setText(new StringBuilder()
             .append(pad(hour)).append(":")
             .append(pad(minute)));
    }

    private static String pad(int c) {
        return c >= 10 ? String.valueOf(c) : "0" + String.valueOf(c);
    }

}
