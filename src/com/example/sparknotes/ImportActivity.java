package com.example.sparknotes;

import java.io.File;
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
	FragmentActivity ctx;

	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy", Locale.ROOT);
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		ctx = this;
		setContentView(R.layout.fragment_import);
		locationButton = (Button) findViewById(R.id.button_locate_import);
		locationButton.setText(Environment.getExternalStorageDirectory().toString());
		locationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), FilePickerActivity.class);
				intent.putExtra(FilePickerActivity.EXTRA_SELECT_FILES_ONLY, true);
				startActivityForResult(intent, MainActivity.GET_EXPORT_DIRECTORY);
			}
		});
		super.onCreate(bundle);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MainActivity.GET_IMPORT_FILE) {
			String filepath = data.getStringExtra("filepath");
			importSparkNotesFromExternalFolder(filepath);
			finish();
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
		// TODO Auto-generated method stub
		return null;
	}

	private void fillNoteByTxtFile(SparkNote currentNote, File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cancel_single, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
