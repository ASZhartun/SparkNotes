package com.example.sparknotes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class BrowseActivity extends Activity {
	ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_browse_attach);
		iv = (ImageView) findViewById(R.id.attach_image_browser_area);
		
		Bundle extras = getIntent().getExtras();
		String path = (String) extras.get("path");
		Bitmap bm = BitmapFactory.decodeFile(path);
		iv.setImageBitmap(bm);
		super.onCreate(savedInstanceState);
	}

}
