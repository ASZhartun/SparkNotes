package com.example.sparknotes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateActivity extends FragmentActivity {

	SQLController dbController;

	long position;
	TextView dateHolder;
	TextView idHolder;
	EditText titleInput;
	EditText contentInput;
	ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();

	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy");

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.fragment_create);

		dbController = new SQLController(this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_confirm_item:
			save(0, titleInput.getText().toString(), contentInput.getText().toString(), sdf.format(new Date()),
					attaches);
			finish();
			break;
		case R.id.action_bar_attach_item:
			chooseTypeOfAttachingFile();
			break;
		case R.id.action_bar_delete_item:
		default:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void save(long position, String title, String content, String date, ArrayList<AttachItem> attaches) {
		Toast.makeText(this, "Doletelo v save activity", Toast.LENGTH_SHORT).show();
		dbController.saveNote(title, content, date, attaches);
		dbController.close();
	}

	public void chooseTypeOfAttachingFile() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] points = getResources().getStringArray(R.array.attach_file_type_points);
		final String[] values = getResources().getStringArray(R.array.values_attach_file_type_points);
		builder.setTitle("Выберите тип файла, который хотите прикрепить к заметке:").setItems(
				points, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "position is " + values[which], Toast.LENGTH_SHORT).show();
						dialog.cancel();
					}
				}).setNegativeButton("Decline", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getApplicationContext(), "Action was declined", Toast.LENGTH_SHORT).show();
						dialog.cancel();
					}
				}).setCancelable(false).show();
	}
}
