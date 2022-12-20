package com.example.sparknotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SearchActivity extends FragmentActivity {
	SQLController dbController;

	EditText userInput;
	RadioGroup radioGroup;
	RadioButton radioTitle;
	RadioButton radioContent;
	CheckBox dateNoticer;
	TextView beginTitle;
	TextView endTitle;
	ImageButton beginPointer;
	ImageButton endPointer;
	EditText beginDate;
	EditText endDate;
	Button search;

	public static String SAMPLE_KEY = "sample";
	public static String SAMPLE_CRITERIA_KEY = "sampleCriteria";
	public static String START_DATE_KEY = "startDate";
	public static String END_DATE_KEY = "endDate";

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.fragment_search);
		dbController = new SQLController(this);

		userInput = (EditText) findViewById(R.id.input_search);
		radioGroup = (RadioGroup) findViewById(R.id.radio_group_category);
		radioTitle = (RadioButton) findViewById(R.id.radio_by_title);
		radioContent = (RadioButton) findViewById(R.id.radio_by_content);
		dateNoticer = (CheckBox) findViewById(R.id.checkbox_notice_dates);
		beginTitle = (TextView) findViewById(R.id.search_begin_title);
		endTitle = (TextView) findViewById(R.id.search_end_title);
		beginDate = (EditText) findViewById(R.id.begin_date_search);
		endDate = (EditText) findViewById(R.id.finish_date_search);
		beginPointer = (ImageButton) findViewById(R.id.pointer_begin_date_search);
		endPointer = (ImageButton) findViewById(R.id.pointer_finish_date_search);
		search = (Button) findViewById(R.id.button_search);

		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String start = null;
				String end = null;

				if (dateNoticer.isChecked()) {
					start = beginDate.getText().toString();
					end = endDate.getText().toString();
				}

				String sample = userInput.getText().toString();
				String criteria = "content";
				if (radioTitle.isChecked())
					criteria = "title";

				Intent data = new Intent();
				data.putExtra(SAMPLE_KEY, sample);
				data.putExtra(SAMPLE_CRITERIA_KEY, criteria);
				data.putExtra(START_DATE_KEY, start);
				data.putExtra(END_DATE_KEY, end);
				setResult(MainActivity.GET_SEARCH_RESULT, data);
				MainActivity.SEARCH_FLAG = true;
				finish();
			}
		});

		dateNoticer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox noticer = (CheckBox) v;
				if (!noticer.isChecked()) {
					beginTitle.setVisibility(View.GONE);
					endTitle.setVisibility(View.GONE);
					beginDate.setVisibility(View.GONE);
					endDate.setVisibility(View.GONE);
					beginPointer.setVisibility(View.GONE);
					endPointer.setVisibility(View.GONE);
					beginDate.setText("");
					endDate.setText("");
				} else {
					beginTitle.setVisibility(View.VISIBLE);
					endTitle.setVisibility(View.VISIBLE);
					beginDate.setVisibility(View.VISIBLE);
					endDate.setVisibility(View.VISIBLE);
					beginPointer.setVisibility(View.VISIBLE);
					endPointer.setVisibility(View.VISIBLE);
				}
			}
		});

		beginPointer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DateTimeFragment newFragment = new DateTimeFragment(beginDate);
				newFragment.show(getFragmentManager(), "dtPicker");
			}
		});

		endPointer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DateTimeFragment newFragment = new DateTimeFragment(endDate);
				newFragment.show(getFragmentManager(), "dtPicker");
			}
		});
		super.onCreate(arg0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cancel_single, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		finish();
		return super.onMenuItemSelected(featureId, item);
	}

}
