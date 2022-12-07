package com.example.sparknotes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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
	SQLController dbController;

	EditText input;
	Button location;
	Button export;

	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy");

	@Override
	protected void onCreate(Bundle bundle) {
		dbController = new SQLController(this);
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

					ArrayList<SparkNote> notes = getNotes();
					ArrayList<AttachItem> attaches = getAttaches();
					merge(notes, attaches);
					File file;
					try {
						file = createResultFolderFrom(notes);
						Toast.makeText(v.getContext(), "Directory was created at\n" + file.mkdir(), Toast.LENGTH_LONG)
								.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					Toast.makeText(v.getContext(), "Invalidate name! Please, choose another...", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
		location.setText(Environment.getExternalStorageDirectory().toString());
		super.onCreate(bundle);
	}

	protected File createResultFolderFrom(ArrayList<SparkNote> notes) throws IOException {
		File resultFolder = new File(location.getText().toString(), input.getText().toString());
		resultFolder.mkdir();
		boolean rootExists = resultFolder.exists();
		for (int i = 0; i < notes.size(); i++) {
			SparkNote sparkNote = notes.get(i);
			String uniqName = sparkNote.getId() + "_" + sparkNote.getInitDate().getTime();
			File noteFolder = new File(resultFolder, uniqName);
			noteFolder.mkdir();
			boolean folderOfNoteExists = noteFolder.exists();
			File noteContent = new File(noteFolder, sparkNote.getId() + ".txt");
			if (!noteContent.exists()) {
				noteContent.createNewFile(); // error idk y
			}
			File attaches = new File(noteFolder, "attaches");
			attaches.mkdir();
			for (int j = 0; j < sparkNote.getEnclosures().size(); j++) {
				AttachItem attachItem = sparkNote.getEnclosures().get(j);
				String extension = attachItem.getType().split("\\/")[1];
				File attachFile = new File(attaches, attachItem.getId() + "_" + uniqName + extension);
				if (!attachFile.exists()) {
					attachFile.createNewFile();
				}
			}
		}

		return resultFolder;
	}

	protected void merge(ArrayList<SparkNote> notes, ArrayList<AttachItem> attaches) {
		for (int i = 0; i < notes.size(); i++) {
			SparkNote sparkNote = notes.get(i);
			ArrayList<AttachItem> enclosures = sparkNote.getEnclosures();
			for (int j = 0; j < attaches.size(); j++) {
				AttachItem attachItem = attaches.get(j);
				if (sparkNote.getId() == attachItem.getSparkNoteId()) {
					enclosures.add(attachItem);
				}
			}
		}

	}

	protected ArrayList<AttachItem> getAttaches() {
		dbController.open();
		ArrayList<AttachItem> attaches = dbController.getAttaches();
		dbController.close();
		return attaches;
	}

	protected ArrayList<SparkNote> getNotes() {
		dbController.open();
		ArrayList<SparkNote> notes = dbController.getNotes();
		dbController.close();
		return notes;
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
		} finally {
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
