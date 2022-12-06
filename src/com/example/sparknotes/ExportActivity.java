package com.example.sparknotes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExportActivity extends FragmentActivity {
	EditText input;
	Button location;
	Button export;

	@Override
	protected void onCreate(Bundle bundle) {
		setContentView(R.layout.fragment_export);
		input = (EditText) findViewById(R.id.input_zip_name_export);
		location = (Button) findViewById(R.id.button_locate_export);
		location.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), FilePickerActivity.class);
				intent.putExtra(FilePickerActivity.EXTRA_SELECT_DIRECTORIES_ONLY, true);
				startActivityForResult(intent, MainActivity.GET_EXPORT_DIRECTORY);
			}
		});
		export = (Button) findViewById(R.id.button_export);
		export.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (verify(input.getText().toString())) {
					Toast.makeText(v.getContext(), "Create result archive!", Toast.LENGTH_LONG).show();
					File file = new File(location.getText().toString(),input.getText().toString());
				} else {
					Toast.makeText(v.getContext(), "Invalidate name! Please, choose another...", Toast.LENGTH_LONG).show();
				}

			}
		});
		location.setText(Environment.getExternalStorageDirectory().toString());
		super.onCreate(bundle);
	}

	protected boolean verify(String name) {
		File file = new File(getFilesDir(), name);
		boolean created = false;
		try {
			try {
				created = file.createNewFile();
			} catch (IOException e) {
				Toast.makeText(this, "Cant create some reason", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			return created;
		}  finally {
			if (created) {
				file.delete();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MainActivity.GET_EXPORT_DIRECTORY) {
			location.setText(data.getExtras().getString(FilePickerActivity.EXTRA_CURRENT_ROOT_DIRECTORY, "/"));
		} else {
			location.setText(data.getExtras().getString("/"));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cancel_single, menu);
		return true;
	}

}
