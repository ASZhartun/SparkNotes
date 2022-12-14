package com.example.sparknotes;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserRefPositionAdapter extends ArrayAdapter<ReferencePosition> {
	public UserRefPositionAdapter(Context context, List<ReferencePosition> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReferencePosition ref = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_faq, parent, false);
		}

		TextView title = (TextView) convertView.findViewById(R.id.search_begin_title);
		TextView content = (TextView) convertView.findViewById(R.id.item_description_faq);
		ImageButton image = (ImageButton) convertView.findViewById(R.id.item_title_image_faq);

		title.setText(ref.getTitle());
		content.setText(ref.getContent());

		if (ref.getIcon() != null) {
			image.setImageBitmap(ref.getIcon());
		} else {
			image.setVisibility(View.GONE);
		}

		return convertView;
	}
}
