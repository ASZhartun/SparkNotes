package com.example.sparknotes;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CreateActivity extends FragmentActivity {
	
	SQLController dbController;
	
	long position;
	TextView dateHolder;
	TextView idHolder;
	EditText titleInput;
	EditText contentInput;
	ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.fragment_create);
		
		dbController = new SQLController(null);
		super.onCreate(arg0);

		String title = "";
		String content = "";
		String date = "";
		long position = 0;
		long currentID;
		try {
			currentID = arg0.getLong("noteID");			
		} catch (Exception e) {
			currentID = 0;
		}
		if (currentID > 0) {
			Cursor currentNote = dbController.getNoteById(currentID);
			title = currentNote.getString(1);
			content = currentNote.getString(2);
			date = currentNote.getString(3);
			position = currentID;
		}
		dateHolder = (TextView) findViewById(R.id.create_date_holder);
		idHolder = (TextView) findViewById(R.id.spark_note_id);
		titleInput = (EditText) findViewById(R.id.create_title_note);
		contentInput = (EditText) findViewById(R.id.create_note_content);
		dateHolder.setText(date);
		idHolder.setText(String.valueOf(position));
		titleInput.setText(title);
		contentInput.setText(content);

		ListView lv = (ListView) findViewById(R.id.create_note_attaches);
		lv.setAdapter(new CreateNoteAttachAdapter(this, attaches));

	}

	
}
