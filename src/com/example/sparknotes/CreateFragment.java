package com.example.sparknotes;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CreateFragment extends Fragment {
	ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.fragment_create, null);
		ListView lv = (ListView) v.findViewById(R.id.create_note_attaches);
		lv.setAdapter(new CreateNoteAttachAdapter(getActivity(), attaches));
		return v;
	}
	
	

}
