package com.example.sparknotes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("InflateParams")
public class NoteListFragment extends ListFragment {

	ActionNoteItemListener ctx;
	ListView lv;

	SparkNoteCursorAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = (ActionNoteItemListener) getActivity();
		setHasOptionsMenu(true);
		setListAdapter(adapter);
		
		lv = (ListView) inflater.inflate(R.layout.fragment_main_list, null);
		lv.setMultiChoiceModeListener(new MultiChoiceMainNoteListImpl(ctx, lv, getActivity(), adapter));
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		return lv;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ctx.openNote(id);
	}

	public SparkNoteCursorAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(SparkNoteCursorAdapter adapter) {
		this.adapter = adapter;
	}
}
