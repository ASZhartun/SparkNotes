package com.example.sparknotes;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BrowseActivity extends Activity {
	TextView tv;
	ImageView iv;
	MediaPlayer mediaPlayer;
	
	final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_browse_attach);
		tv = (TextView) findViewById(R.id.error_browser_field);
		iv = (ImageView) findViewById(R.id.attach_image_browser_area);

		Bundle extras = getIntent().getExtras();
		String path = (String) extras.get("path");
		String type = (String) extras.get("type");
		if (type.equals("image")) {
			Bitmap bm = BitmapFactory.decodeFile(path);
			iv.setImageBitmap(bm);
			
		} else if (type.equals("audio")) {
			iv.setVisibility(View.GONE);
			createMediaPlayer(path);

		} else {
			iv.setVisibility(View.GONE);
			tv.setVisibility(View.VISIBLE);
		}

		super.onCreate(savedInstanceState);
	}

	private void createMediaPlayer(String path) {
		mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(path)));
		mediaPlayer.start();
	}
}
