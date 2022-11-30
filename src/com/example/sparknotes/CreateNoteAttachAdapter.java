package com.example.sparknotes;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreateNoteAttachAdapter extends ArrayAdapter<AttachItem> {

	AttachActionListener ctx;

	TextView tv;
	ImageButton delete;
	ImageButton browse;

	int index;

	public CreateNoteAttachAdapter(Context context, List<AttachItem> objects) {
		super(context, 0, objects);
		ctx = (AttachActionListener) context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		AttachItem attach = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_create_attach, parent, false);
		}

		tv = (TextView) convertView.findViewById(R.id.attach_path);
		tv.setText(attach.getPath());

		delete = (ImageButton) convertView.findViewById(R.id.attach_action_delete);
		browse = (ImageButton) convertView.findViewById(R.id.attach_action_browse);

		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ctx.deleteAttach(position);
			}
		});
		
		browse.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ctx.browseAttach(position);
			}
		});
		return convertView;
	}

}
