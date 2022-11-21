package com.example.sparknotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NoteListFragment extends ListFragment {

	DummyNoteDB db = new DummyNoteDB();
	UserNoteAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		adapter = new UserNoteAdapter(getActivity(), db.getNotes());
		setListAdapter(adapter);
		return (ListView) inflater.inflate(R.layout.fragment_main_list, null);
	}
}
