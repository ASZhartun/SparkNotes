package com.example.sparknotes;

import java.util.ArrayList;

import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
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

public class NoteListFragment extends ListFragment {

	OpenNoteItemListener ctx;
	ListView lv;
	
	public static ArrayList<Long> selectingItemIDs = new ArrayList<Long>();

	public static Boolean isSelecting = false;
//	DummyNoteDB db = new DummyNoteDB();
	SparkNoteCursorAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = (OpenNoteItemListener) getActivity();
		setHasOptionsMenu(true);
//		adapter = new UserNoteAdapter(getActivity(), db.getNotes());
		setListAdapter(adapter);
		lv = (ListView) inflater.inflate(R.layout.fragment_main_list, null);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), "Добавил позиции в статик эрэй" + position, Toast.LENGTH_SHORT).show();
				isSelecting = true;
				CheckBox checkNote = (CheckBox) view.findViewById(R.id.item_selector);
				checkNote.setVisibility(View.VISIBLE);
				checkNote.setChecked(true);
				
				selectingItemIDs.add(id);
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
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (isSelecting) {
			Toast.makeText(getActivity(), "Добавил позиции в статик эрэй" + id, Toast.LENGTH_SHORT).show();
			CheckBox checkNote = (CheckBox) v.findViewById(R.id.item_selector);
			checkNote.setVisibility(View.VISIBLE);
			checkNote.setChecked(true);
			
			selectingItemIDs.add(id);
		} else {
			Toast.makeText(getActivity(), "Короткий клик на позиции" + id, Toast.LENGTH_SHORT).show();
			ctx.openNote(id);
		}

	}

	public SparkNoteCursorAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(SparkNoteCursorAdapter adapter) {
		this.adapter = adapter;
	}

	interface OpenNoteItemListener {
		public void openNote(long position);
	}

}
