package com.example.sparknotes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.EnvironmentCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateActivity extends FragmentActivity implements AttachActionListener {

	private static final int PICKFILE_RESULT_CODE = 0;

	Context ctx;
	long currentID;

	SQLController dbController;

	long position;
	TextView dateHolder;
	TextView idHolder;
	EditText titleInput;
	EditText contentInput;
	ListView lv;
	CreateNoteAttachAdapter attachAdapter;
	ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();

	SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_hh_mm ");

	@Override
	protected void onCreate(Bundle bundle) {
		setContentView(R.layout.fragment_create);
		ctx = this;

		dbController = new SQLController(this);
		dbController.open();
		super.onCreate(bundle);

		String title = "";
		String content = "";
		String date = "";
		long position = 0;
		try {
			currentID = getIntent().getExtras().getLong("noteID");
		} catch (Exception e) {
			currentID = 0;
		}
		if (currentID > 0) {
			Cursor currentNote = dbController.getNoteById(currentID);
			title = currentNote.getString(1);
			content = currentNote.getString(2);
			date = currentNote.getString(3);
			position = currentID;
			
			Cursor attachesCursor = dbController.getAttachesByNoteId(currentID);
			int quantity = attachesCursor.getCount();
			if (quantity > 0) {
				for (int i = 0; i < quantity; i++ ) {
					AttachItem attachItem = new AttachItem(
							attachesCursor.getLong(0), 
							attachesCursor.getString(1), 
							new File(attachesCursor.getString(1)), 
							attachesCursor.getString(2), 
							attachesCursor.getLong(3));
					attaches.add(attachItem);
				}
			}
		}
		dateHolder = (TextView) findViewById(R.id.create_date_holder);
		idHolder = (TextView) findViewById(R.id.spark_note_id);
		titleInput = (EditText) findViewById(R.id.create_title_note);
		contentInput = (EditText) findViewById(R.id.create_note_content);
		dateHolder.setText(date);
		idHolder.setText(String.valueOf(position));
		titleInput.setText(title);
		contentInput.setText(content);

		lv = (ListView) findViewById(R.id.create_note_attaches);
		attachAdapter = new CreateNoteAttachAdapter(this, attaches);
		lv.setAdapter(attachAdapter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case PICKFILE_RESULT_CODE:
//			String path = data.getData().getPath();
		try {
			String type = getTypeFrom(data);
			File newFile = copy(data);
			attaches.add(new AttachItem(0, newFile.getPath(), newFile, type, currentID));
			attachAdapter.notifyDataSetChanged();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//			break;
//		default:
//			Toast.makeText(this, "Weird result of CreateActivity", Toast.LENGTH_LONG).show();
//		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_confirm_item:
			if (currentID > 0) {
				update(currentID, titleInput.getText().toString(), contentInput.getText().toString(), dateHolder.getText().toString(),
						attaches);
			} else {
				save(titleInput.getText().toString(), contentInput.getText().toString(), sdf.format(new Date()),
						attaches);
			}

			finish();
			break;
		case R.id.action_bar_attach_item:
			chooseTypeOfAttachingFile();
			break;
		case R.id.action_bar_delete_item:
		default:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void save(String title, String content, String date, ArrayList<AttachItem> attaches) {
		Toast.makeText(this, "Doletelo v save activity", Toast.LENGTH_SHORT).show();
		dbController.saveNote(title, content, date, attaches);
		dbController.close();
	}
	
	public void update(long position, String title, String content, String date, ArrayList<AttachItem> attaches) {
		Toast.makeText(this, "Doletelo v save activity", Toast.LENGTH_SHORT).show();
		dbController.updateNote(currentID, title, content, date, attaches);
		dbController.close();
	}

	public void chooseTypeOfAttachingFile() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] points = getResources().getStringArray(R.array.attach_file_type_points);
		final String[] values = getResources().getStringArray(R.array.values_attach_file_type_points);
		builder.setTitle("Выберите тип файла, который хотите прикрепить к заметке:")
				.setItems(points, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "position is " + values[which], Toast.LENGTH_SHORT)
								.show();
						dialog.cancel();
						((CreateActivity) ctx).createAttachingFile(values[which]);
					}
				}).setNegativeButton("Decline", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getApplicationContext(), "Action was declined", Toast.LENGTH_SHORT).show();
						dialog.cancel();
					}
				}).setCancelable(false).show();
	}

	protected void createAttachingFile(String string) {
		Intent intent;
		if (string.equals("file")) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent = Intent.createChooser(intent, "CHOOSE_FILE");
			startActivityForResult(intent, PICKFILE_RESULT_CODE);
		} else if (string.equals("photo")) {

		} else if (string.equals("voice")) {

		} else {
			Toast.makeText(getApplicationContext(), "was choosen something unexpectable!", Toast.LENGTH_SHORT).show();
		}
	}

	private File copy(Intent data) throws FileNotFoundException, IOException {
		String extension = getExtensionFrom(data);
		File destination = createFile("." + extension);

		InputStream fis = getContentResolver().openInputStream(data.getData());
		FileOutputStream out = new FileOutputStream(destination, false);
		int temp = 0;
		while ((temp = fis.read()) != -1) {
			out.write(temp);
		}
		out.close();
		return destination;
	}

	private String getExtensionFrom(Intent data) {
		String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(getTypeFrom(data));
		return extensionFromMimeType;
	}
	
	private String getTypeFrom(Intent data) {
		String type = getContentResolver().getType(data.getData());
		return type;
	}

	private File createFile(String suffix) {

		File newAttach = new File(getFilesDir(), sdf.format(new Date()).trim() + suffix);
		if (!newAttach.exists()) {
			try {
				newAttach.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return newAttach;
	}

	@Override
	public void deleteAttach(int index) {
		attaches.remove(index);
		attachAdapter.notifyDataSetChanged();

	}

	@Override
	public void browseAttach(int index) {
		AttachItem attachItem = attaches.get(index);
		String path = attachItem.getPath();
		String type = attachItem.getType();
		Intent intent = new Intent(this, BrowseActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("path", path);
		startActivity(intent);
	}
}
