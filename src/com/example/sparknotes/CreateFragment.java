package com.example.sparknotes;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CreateFragment extends Fragment {

	ActionNoteItemListener ctx;

	long position;
	TextView dateHolder;
	TextView idHolder;
	EditText titleInput;
	EditText contentInput;
	ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = (ActionNoteItemListener) getActivity();

		setHasOptionsMenu(true);

		String title = "";
		String content = "";
		String date = "";
		long position = 0;
		if (getArguments() != null || getArguments().getBoolean("isOpen")) {
			title = getArguments().getString("title");
			content = getArguments().getString("content");
			date = getArguments().getString("date");
			position = getArguments().getLong("position");
		}
		View v = inflater.inflate(R.layout.fragment_create, null);
		dateHolder = (TextView) v.findViewById(R.id.create_date_holder);
		idHolder = (TextView) v.findViewById(R.id.spark_note_id);
		titleInput = (EditText) v.findViewById(R.id.create_title_note);
		contentInput = (EditText) v.findViewById(R.id.create_note_content);
		dateHolder.setText(date);
		idHolder.setText(String.valueOf(position));
		titleInput.setText(title);
		contentInput.setText(content);

		ListView lv = (ListView) v.findViewById(R.id.create_note_attaches);
		lv.setAdapter(new CreateNoteAttachAdapter(getActivity(), attaches));
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.create, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_bar_confirm_item) {
			ctx.save(Long.parseLong(idHolder.getText().toString()), titleInput.getText().toString(),
					contentInput.getText().toString(), dateHolder.getText().toString(), attaches);
		} else if (item.getItemId() == R.id.action_bar_delete_item) {
			ctx.deleteNote(Long.parseLong(idHolder.getText().toString()));
		}
		return super.onOptionsItemSelected(item);
	}

}
