package com.example.sparknotes;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CreateNoteAttachAdapter extends ArrayAdapter<AttachItem> {

	public CreateNoteAttachAdapter(Context context, List<AttachItem> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		AttachItem attach = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_create_attach, parent, false);
		}

		TextView tv = (TextView) convertView.findViewById(R.id.attach_path);
		tv.setText(attach.getPath());

		return convertView;
	}

}
