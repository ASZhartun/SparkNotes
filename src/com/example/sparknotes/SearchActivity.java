package com.example.sparknotes;

import android.app.Activity;
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

public class SearchActivity extends FragmentActivity {
	SQLController SQLController;
	MainActivity parent;
	
	EditText userInput;
	RadioGroup radioGroup;
	RadioButton radioTitle;
	RadioButton radioContent;
	CheckBox dateNoticer;
	ImageButton beginPointer;
	ImageButton endPointer;
	EditText beginDate;
	EditText endDate;
	Button search;
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.fragment_search);
		SQLController = new SQLController(this);
		parent = (MainActivity) this.getParent();

		userInput = (EditText) findViewById(R.id.input_search);
		radioGroup = (RadioGroup) findViewById(R.id.radio_group_category);
		radioTitle = (RadioButton) findViewById(R.id.radio_by_title);
		radioContent = (RadioButton) findViewById(R.id.radio_by_content);
		dateNoticer = (CheckBox) findViewById(R.id.checkbox_notice_dates);
		beginDate = (EditText) findViewById(R.id.begin_date_search);
		endDate = (EditText) findViewById(R.id.finish_date_search);
		beginPointer = (ImageButton) findViewById(R.id.pointer_begin_date_search);
		endPointer = (ImageButton) findViewById(R.id.pointer_finish_date_search);
		search = (Button) findViewById(R.id.button_search);
		
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
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
