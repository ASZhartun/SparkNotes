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

public class CreateFragment extends Fragment {

	EditNoteActionsListener ctx;

	int position;
	EditText titleInput;
	EditText contentInput;
	ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = (EditNoteActionsListener) getActivity();

		setHasOptionsMenu(true);

		String title = "";
		String content = "";
		if (getArguments() != null) {
			title = getArguments().getString("title");
			content = getArguments().getString("content");
			position = getArguments().getInt("position");
		}
		View v = inflater.inflate(R.layout.fragment_create, null);
		titleInput = (EditText) v.findViewById(R.id.create_title_note);
		contentInput = (EditText) v.findViewById(R.id.create_note_content);
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
			ctx.save(position, titleInput.getText().toString(), contentInput.getText().toString(), attaches);
		}
		return super.onOptionsItemSelected(item);
	}

	interface EditNoteActionsListener {
		void save(int position, String title, String content, ArrayList<AttachItem> attaches);
	}

}
