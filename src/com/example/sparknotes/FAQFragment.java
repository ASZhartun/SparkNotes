package com.example.sparknotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FAQFragment extends ListFragment {
	DummyNoteDB db = new DummyNoteDB();
	UserRefPositionAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		adapter = new UserRefPositionAdapter(getActivity(), db.getRefPositions());
		setListAdapter(adapter);
		return (ListView) inflater.inflate(R.layout.fragment_main_list, null);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.cancel_single, menu);
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}
}
