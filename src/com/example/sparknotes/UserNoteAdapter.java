package com.example.sparknotes;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class UserNoteAdapter extends ArrayAdapter<SparkNote> {
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy");

	public UserNoteAdapter(Context context, List<SparkNote> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SparkNote note = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
		}

		TextView title = (TextView)convertView.findViewById(R.id.title);
		TextView content = (TextView)convertView.findViewById(R.id.content);
		TextView date = (TextView) convertView.findViewById(R.id.init_date);
		CheckBox checkNote = (CheckBox) convertView.findViewById(R.id.item_selector);
		
		title.setText(note.getTitle());
		content.setText(note.getContent());
		date.setText(sdf.format(note.getInitDate()));
		checkNote.setChecked(false);
		checkNote.setVisibility(View.GONE);

		return convertView;
	}

}
