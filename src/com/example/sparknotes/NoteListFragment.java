package com.example.sparknotes;

import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class NoteListFragment extends ListFragment {

	OpenNoteItemListener ctx;

	public static Boolean isSelecting = false;
//	DummyNoteDB db = new DummyNoteDB();
	SparkNoteCursorAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = (OpenNoteItemListener) getActivity();
		setHasOptionsMenu(true);
//		adapter = new UserNoteAdapter(getActivity(), db.getNotes());
		setListAdapter(adapter);
		ListView lv = (ListView) inflater.inflate(R.layout.fragment_main_list, null);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), "Длинный клик на позиции" + position, Toast.LENGTH_SHORT).show();
				isSelecting = true;
				CheckBox checkNote = (CheckBox) view.findViewById(R.id.item_selector);
				checkNote.setVisibility(View.VISIBLE);
				checkNote.setChecked(true);
				return false;
			}
		});
		return lv;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (isSelecting) {
			Toast.makeText(getActivity(), "Длинный клик на позиции" + position, Toast.LENGTH_SHORT).show();
			CheckBox checkNote = (CheckBox) v.findViewById(R.id.item_selector);
			checkNote.setVisibility(View.VISIBLE);
			checkNote.setChecked(true);
		} else {
			Toast.makeText(getActivity(), "Короткий клик на позиции" + position, Toast.LENGTH_SHORT).show();
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
