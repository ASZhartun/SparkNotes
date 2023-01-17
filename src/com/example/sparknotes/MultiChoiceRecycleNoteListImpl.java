package com.example.sparknotes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Toast;

public class MultiChoiceRecycleNoteListImpl implements AbsListView.MultiChoiceModeListener {
	private AbsListView listView;
	private SparkNoteCursorAdapter adapter;
	private Context ctx;
	private ActionNoteItemListener activity;

	private static HashSet<Long> selectingItemIDs = new HashSet<Long>();

	public MultiChoiceRecycleNoteListImpl(ActionNoteItemListener activity, AbsListView listView, Context ctx,
			SparkNoteCursorAdapter adapter) {
		this.activity = activity;
		this.listView = listView;
		this.ctx = ctx;

		this.adapter = adapter;
		Log.d("TAG CONSTRUCTOR", "Constructor is done");
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		Log.d("MULTI_CHOICE_NOTE_LIST_RECYCLE_TAG", "onCreateActionMode");
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.recycler_multi_choice_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if (item.getTitle() == ctx.getResources().getString(R.string.actionbar_restore_button)) {
			activity.restoreNotes(getSelections());
		} else if (item.getTitle() == ctx.getResources().getString(R.string.actionbar_delete_button)) {
			activity.fullDeleteNotes(getSelections());
		}
		selectingItemIDs.clear();
		Toast.makeText(ctx, item.getTitle(), Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		adapter.clearSelection();
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
		int selectedCount = listView.getCheckedItemCount();
		setSubtitle(mode, selectedCount);
		if (!selectingItemIDs.contains(id)) {
			selectingItemIDs.add(id);
			adapter.setNewSelection(position, checked);
		} else {
			selectingItemIDs.remove(id);
			adapter.removeSelection(position);
		}

	}

	private void setSubtitle(ActionMode mode, int selectedCount) {
		switch (selectedCount) {
		case 0:
			mode.setSubtitle(null);
			break;
		default:
			mode.setTitle(String.valueOf(selectedCount));
			break;
		}
	}

	private ArrayList<Long> getSelections() {
		ArrayList<Long> list = new ArrayList<Long>();
		Iterator<Long> iterator = selectingItemIDs.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

}
