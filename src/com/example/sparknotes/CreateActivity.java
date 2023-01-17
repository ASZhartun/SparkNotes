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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

	SimpleDateFormat sdf = MainActivity.sdf;
	String takePhotoURI;

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

			attaches = dbController.getAttachesByNoteId(currentID);
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
		setTitle(getResources().getString(R.string.actionbar_create_button));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			String type = getTypeFrom(data);
			File newFile = copy(data);
			attaches.add(new AttachItem(0, newFile.getPath(), newFile, type, currentID));
			attachAdapter.notifyDataSetChanged();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
				update(currentID, titleInput.getText().toString(), contentInput.getText().toString(),
						dateHolder.getText().toString(), attaches);
			} else {
				save(titleInput.getText().toString(), contentInput.getText().toString(), sdf.format(new Date()),
						attaches);
			}
			finish();
			break;
		case R.id.action_bar_attach_item:
			chooseTypeOfAttachingFile();
			break;
		case R.id.action_bar_decline_item:
		default:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void save(String title, String content, String date, ArrayList<AttachItem> attaches) {
		dbController.saveNote(title, content, date, attaches);
	}

	public void update(long position, String title, String content, String date, ArrayList<AttachItem> attaches) {
		dbController.updateNote(currentID, title, content, date, attaches);
	}

	public void chooseTypeOfAttachingFile() {
		((CreateActivity) ctx).createAttachingFile("file");
	}

	protected void createAttachingFile(String string) {
		Intent intent = null;
		intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent = Intent.createChooser(intent, "CHOOSE_FILE");
		startActivityForResult(intent, PICKFILE_RESULT_CODE);
	}

	private File copy(Intent data) throws FileNotFoundException, IOException {
		String extension = getExtensionFrom(data);
		Uri tempUri = data.getData();
		File destination = createFile("." + extension);
		return copyFileByUri(destination, tempUri);
	}

	private File copyFileByUri(File destination, Uri tempUri) throws FileNotFoundException, IOException {
		InputStream fis = getContentResolver().openInputStream(tempUri);
		FileOutputStream out = new FileOutputStream(destination, false);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fis, 8192);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out, 8192);
		byte[] bys = new byte[8192];
		while ((bufferedInputStream.read(bys)) != -1) {
			bufferedOutputStream.write(bys);
			bufferedOutputStream.flush();
		}
		fis.close();
		bufferedInputStream.close();
		out.close();
		bufferedOutputStream.close();
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

	private File createFile(String suffix) throws IOException {
		File storage = getFilesDir();
		String filename = sdf.format(new Date()).trim();
		File image = File.createTempFile(filename, suffix, storage);
		takePhotoURI = "file:" + image.getAbsolutePath();
		return image;
	}

	@Override
	public void deleteAttach(int index) {
		dbController.open();
		long attachID = attaches.get(index).getId();
		dbController.deleteAttach(attachID);
		attaches.remove(index);
		attachAdapter.notifyDataSetChanged();

	}

	@Override
	public void browseAttach(int index) {
		AttachItem attachItem = attaches.get(index);
		String path = attachItem.getPath();
		String type = attachItem.getType();
		Intent intent = new Intent(this, BrowseActivity.class);
		intent.putExtra("type", type.split("\\/")[0]);
		intent.putExtra("path", path);
		startActivity(intent);
	}
}
