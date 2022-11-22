package com.example.sparknotes;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ImportFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_import, null);
		Button location = (Button) v.findViewById(R.id.button_locate_import);
		location.setText(Environment.getExternalStorageDirectory().toString());
		return v;
	}
}
