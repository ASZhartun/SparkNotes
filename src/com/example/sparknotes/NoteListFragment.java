package com.example.sparknotes;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class NoteListFragment extends ListFragment {

	ActionNoteItemListener ctx;
	ListView lv;

	public static ArrayList<Long> selectingItemIDs = new ArrayList<Long>();

	public static Boolean isSelecting = false;
	SparkNoteCursorAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = (ActionNoteItemListener) getActivity();
		setHasOptionsMenu(true);
		setListAdapter(adapter);
		lv = (ListView) inflater.inflate(R.layout.fragment_main_list, null);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				isSelecting = true;
				CheckBox checkNote = (CheckBox) view.findViewById(R.id.item_selector);

				checkNote.setVisibility(View.VISIBLE);
				checkNote.setChecked(true);

//				selectingItemIDs.add(id);
				return false;
			}
		});
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
		if (item.getItemId() == R.id.action_bar_delete_item) {
			ctx.deleteNotes(selectingItemIDs);
		} else if (item.getItemId() == R.id.action_bar_decline_item) {
			Toast.makeText(getActivity(), "nazhal na action bar fragmenta", Toast.LENGTH_LONG).show();
			ctx.clearSearchResult();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		CheckBox checkNote = (CheckBox) v.findViewById(R.id.item_selector);

		if (isSelecting) {
			checkNote = (CheckBox) v.findViewById(R.id.item_selector);
			checkNote.setVisibility(View.VISIBLE);
			checkNote.setChecked(true);

			selectingItemIDs.add(id);

		} else {
			ctx.openNote(id);
		}

	}

	public SparkNoteCursorAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(SparkNoteCursorAdapter adapter) {
		this.adapter = adapter;
	}
}
