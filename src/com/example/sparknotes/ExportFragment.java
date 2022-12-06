package com.example.sparknotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("InflateParams")
public class ExportFragment extends Fragment {
	EditText archiveName;
	Button location;
	Button export;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.fragment_export, null);
		archiveName = (EditText) v.findViewById(R.id.input_zip_name_export);
		location = (Button) v.findViewById(R.id.button_locate_export);
		location.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FilePickerActivity.class);
				intent.putExtra(FilePickerActivity.EXTRA_SELECT_DIRECTORIES_ONLY, true);
				startActivityForResult(intent, MainActivity.GET_EXPORT_DIRECTORY);
			}
		});
		export = (Button) v.findViewById(R.id.button_export);
		location.setText(Environment.getExternalStorageDirectory().toString());
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.cancel_single, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void setArchiveName(String name) {
		archiveName.setText(name);
	}

	public void setLocation(String dir) {
		location.setText(dir);
	}

}
