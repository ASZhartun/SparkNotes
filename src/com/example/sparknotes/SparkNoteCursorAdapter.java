package com.example.sparknotes;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class SparkNoteCursorAdapter extends CursorAdapter{
	Context ctx;
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy");

	public SparkNoteCursorAdapter(Context context, Cursor c) {
		super(context, c);
		ctx = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView content = (TextView) view.findViewById(R.id.content);
		TextView date = (TextView) view.findViewById(R.id.init_date);
		CheckBox checkNote = (CheckBox) view.findViewById(R.id.item_selector);

		title.setText(cursor.getString(1));
		content.setText(cursor.getString(2));
		date.setText(cursor.getString(3));
		
//		title.setText(cursor.getColumnIndexOrThrow(DBHelper.TABLE_SPARK_NOTES_TITLE));
//		content.setText(cursor.getColumnIndexOrThrow(DBHelper.TABLE_SPARK_NOTES_CONTENT));
//		date.setText(sdf.format(cursor.getColumnIndexOrThrow(DBHelper.TABLE_SPARK_NOTES_INIT_DATE)));
		
		checkNote.setChecked(false);
		checkNote.setVisibility(View.GONE);
		
		checkNote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox current = (CheckBox) v;
				if (!current.isChecked()) {
					current.setChecked(false);
					current.setVisibility(View.GONE);
					Toast.makeText(ctx, "Current state of checkbox is " + current.isChecked(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);

	}

}