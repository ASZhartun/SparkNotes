package com.example.sparknotes;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

public class CreateFragment extends Fragment {
	
	ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		String title = getArguments().getString("title");
		String content = getArguments().getString("content");
		View v = inflater.inflate(R.layout.fragment_create, null);
		EditText titleInput = (EditText)v.findViewById(R.id.create_title_note);
		EditText contentInput = (EditText)v.findViewById(R.id.create_note_content);
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
	
	

}
