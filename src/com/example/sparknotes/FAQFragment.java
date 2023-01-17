package com.example.sparknotes;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FAQFragment extends ListFragment {
	UserRefPositionAdapter adapter;
	ActionNoteItemListener ctx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		ctx = (ActionNoteItemListener) getActivity();
		adapter = new UserRefPositionAdapter(getActivity(), getRefPositions());
		setListAdapter(adapter);
		return (ListView) inflater.inflate(R.layout.fragment_main_list, null);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.cancel_single, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public ArrayList<ReferencePosition> getRefPositions() {
		ArrayList<ReferencePosition> positions = new ArrayList<ReferencePosition>();
		String[] titles = getResources().getStringArray(R.array.faq_title_points);
		String[] contents = getResources().getStringArray(R.array.faq_description_points);
		for (int i = 0; i < titles.length; i++) {
			positions.add(new ReferencePosition(titles[i], contents[i]));
		}
		return positions;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ctx.clearSearchResult();
		return super.onOptionsItemSelected(item);
	}

}
