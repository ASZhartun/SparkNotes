package com.example.sparknotes;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class UserNoteAdapter extends ArrayAdapter<SparkNote> {
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy");
	Context ctx;

	public UserNoteAdapter(Context context, List<SparkNote> objects) {
		super(context, 0, objects);
		this.ctx = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SparkNote note = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
		}

		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView content = (TextView) convertView.findViewById(R.id.content);
		TextView date = (TextView) convertView.findViewById(R.id.init_date);
		CheckBox checkNote = (CheckBox) convertView.findViewById(R.id.item_selector);

		title.setText(note.getTitle());
		content.setText(note.getContent());
		date.setText(sdf.format(note.getInitDate()));
		checkNote.setChecked(false);
		checkNote.setVisibility(View.GONE);

		checkNote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox current = (CheckBox) v;
				if (!current.isChecked()) {
					current.setChecked(false);
					current.setVisibility(View.GONE);
					Toast.makeText(getContext(), "Current state of checkbox is " + current.isChecked(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

//		convertView.setOnLongClickListener(new ListNoteItemListenerKeeper()); не дает работать листнеру выше по иерархии; лонгклик придется реализовать также выше

		return convertView;
	}

	public class ListNoteItemListenerKeeper implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			CheckBox checkNote = (CheckBox) v.findViewById(R.id.item_selector);
			checkNote.setVisibility(View.VISIBLE);
			checkNote.setChecked(true);
			return false;
		}


	}
}
