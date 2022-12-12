package com.example.sparknotes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ImportActivity extends FragmentActivity {
	SQLController dbController;

	Button locationButton;
	Button importButton;
	FragmentActivity ctx;

	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy", Locale.ROOT);

	@Override
	protected void onCreate(Bundle bundle) {
		dbController = new SQLController(this);
		ctx = this;
		setContentView(R.layout.fragment_import);
		locationButton = (Button) findViewById(R.id.button_locate_import);
		locationButton.setText(Environment.getExternalStorageDirectory().toString());
		locationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), FilePickerActivity.class);
				intent.putExtra("import", MainActivity.GET_IMPORT_FILE);
				startActivityForResult(intent, MainActivity.GET_EXPORT_DIRECTORY);
			}
		});

		importButton = (Button) findViewById(R.id.button_import);
		importButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String filepath = locationButton.getText().toString();
				importSparkNotesFromExternalFolder(filepath);
				finish();
			}
		});
		super.onCreate(bundle);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == MainActivity.GET_EXPORT_DIRECTORY) {
			String filepath = data.getExtras().getString(FilePickerActivity.EXTRA_CURRENT_ROOT_DIRECTORY);
			locationButton.setText(filepath);
		} else {
			Toast.makeText(this, "File was unhandled! Try once more", Toast.LENGTH_LONG).show();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void importSparkNotesFromExternalFolder(String filepath) {
		ArrayList<SparkNote> notes = new ArrayList<SparkNote>();
		File target = new File(filepath);
		if (target.isDirectory()) {
			File[] noteFolders = target.listFiles();
			for (int i = 0; i < noteFolders.length; i++) {
				notes.add(createSparkByFolder(noteFolders[i]));
			}
		} else {
			notes.add(createSparkByFolder(target));
		}

		dbController.open();
		dbController.addAll(notes);
		dbController.close();
	}

	private SparkNote createSparkByFolder(File target) {
		File[] innerFiles = target.listFiles();
		SparkNote currentNote = new SparkNote();
		for (int i = 0; i < innerFiles.length; i++) {
			if (innerFiles[i].isFile()) {
				fillNoteByTxtFile(currentNote, innerFiles[i]);
			} else {
				currentNote.setEnclosures(getAttachesFromFolder(innerFiles[i]));
			}
		}
		return currentNote;
	}

	private ArrayList<AttachItem> getAttachesFromFolder(File file) {
		ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();
		if (file.isDirectory()) {
			File[] innerFiles = file.listFiles();
			for (int i = 0; i < innerFiles.length; i++) {
				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				String type = fileNameMap.getContentTypeFor(innerFiles[i].getAbsolutePath());
				attaches.add(new AttachItem(0, innerFiles[i].getAbsolutePath(), null, type));
			}
		}
		return attaches;
	}

	private void fillNoteByTxtFile(SparkNote currentNote, File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			currentNote.setTitle(reader.readLine());
			currentNote.setInitDate(sdf.parse(reader.readLine()));
			StringBuilder sb = new StringBuilder();
			String temp;
			while ((temp = reader.readLine()) != null) {
				sb.append(temp);
			}
			currentNote.setContent(sb.toString());
			reader.close();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cancel_single, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
