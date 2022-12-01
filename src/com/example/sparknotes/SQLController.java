package com.example.sparknotes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SQLController {
	private DBHelper db;
	private Context ctx;
	private SQLiteDatabase database;

	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy");

	public SQLController(Context c) {
		ctx = c;
	}

	public SQLController open() throws SQLException {
		db = DBHelper.getInstance(ctx);
		database = db.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public Cursor getSparkNotes() {
		String[] allColumns = new String[] { db.TABLE_SPARK_NOTES_ID, db.TABLE_SPARK_NOTES_TITLE,
				db.TABLE_SPARK_NOTES_CONTENT, db.TABLE_SPARK_NOTES_INIT_DATE };
		Cursor c = database.query(db.TABLE_SPARK_NOTES, allColumns, null, null, null, null, "_id DESC");
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public void addAll(ArrayList<SparkNote> notes) {
		database.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (int i = 0; i < notes.size(); i++) {
				SparkNote sparkNote = notes.get(i);
//				values.put("_id", sparkNote.getId());
				values.put("title", sparkNote.getTitle());
				values.put("content", sparkNote.getContent());
				values.put("init_date", sdf.format(sparkNote.getInitDate()));
				database.insert(DBHelper.TABLE_SPARK_NOTES, null, values);
			}
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	public void clear() {
		database.beginTransaction();
		try {
			database.execSQL("DELETE FROM spark_notes");
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}

	}

	public Cursor getNoteById(long position) {
		Cursor cursor;
		database.beginTransaction();

//		String[] allColumns = new String[] { DBHelper.TABLE_SPARK_NOTES_ID, DBHelper.TABLE_SPARK_NOTES_TITLE,
//				DBHelper.TABLE_SPARK_NOTES_CONTENT, DBHelper.TABLE_SPARK_NOTES_INIT_DATE };
		String pos = String.valueOf(position);
//		String selection = "_id = " + pos;
		String[] selectionArgs = new String[] { pos };
//		cursor = database.query(DBHelper.TABLE_SPARK_NOTES, allColumns, selection, selectionArgs, null, null, null, null);
		String sql = "SELECT * FROM spark_notes WHERE _id=?";
		cursor = database.rawQuery(sql, selectionArgs);
//			cursor = database.query(DBHelper.TABLE_SPARK_NOTES, null, null,
//					null, null, null, null);
//			int count = cursor.getCount();
		cursor.moveToFirst();
		database.setTransactionSuccessful();
		database.endTransaction();
		return cursor;

	}

	public ArrayList<AttachItem> getAttachesByNoteId(long position) {
		database = db.getReadableDatabase();
		database.beginTransaction();

		Cursor cursor;
		String pos = String.valueOf(position);
		String[] selectionArgs = new String[] { pos };
		String sql = "SELECT * FROM attaches WHERE spark_notes_id=?";
		cursor = database.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();

		database.setTransactionSuccessful();
		database.endTransaction();
		
		ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();
		int quantity = cursor.getCount();
		if (quantity > 0) {
			for (int i = 0; i < quantity; i++ ) {
				AttachItem attachItem = new AttachItem(
						cursor.getLong(0), 
						cursor.getString(1), 
						new File(cursor.getString(1)), 
						cursor.getString(2), 
						cursor.getLong(3));
				attaches.add(attachItem);
				if (!cursor.moveToNext()) break;
			}
		}
		return attaches;
	}

	public void updateNote(long position, String title, String content, String date, ArrayList<AttachItem> attaches) {
		this.open();
		database = db.getWritableDatabase();
		database.beginTransaction();
//		String[] selectionArgs = new String[] { title, content, date, String.valueOf(position) };
//		String sql = "UPDATE spark_notes SET title = ?, content = ?, init_date = ? WHERE _id = ?";
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("content", content);
		int update = database.update(DBHelper.TABLE_SPARK_NOTES, values, "_id = ?",
				new String[] { String.valueOf(position) });
		database.setTransactionSuccessful();
		database.endTransaction();

		updateNoteAttaches(position, attaches);
	}

	public void saveNote(String title, String content, String date, ArrayList<AttachItem> attaches) {
		this.open();
		database = db.getWritableDatabase();
		database.beginTransaction();
//		String sql = "INSERT INTO spark_notes (title,content , init_date) VALUES ( ? , ? , ? )";
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("content", content);
		values.put("init_date", sdf.format(new Date()));
		long insert = database.insert(DBHelper.TABLE_SPARK_NOTES, null, values);
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

		saveNoteAttaches(insert, attaches);
	}

	private void saveNoteAttaches(long insert, ArrayList<AttachItem> attaches) {
		database = db.getWritableDatabase();
		database.beginTransaction();
		for (int i = 0; i < attaches.size(); i++) {
			attaches.get(i).setSparkNoteId(insert);
			;

			ContentValues values = new ContentValues();
			values.put("path", attaches.get(i).getPath());
			values.put("type", attaches.get(i).getType());
			values.put("spark_notes_id", attaches.get(i).getSparkNoteId());

			database.insert(DBHelper.TABLE_ATTACHES, null, values);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		return;
	}

	private void updateNoteAttaches(long position, ArrayList<AttachItem> attaches) {
		this.open();
		database = db.getWritableDatabase();
		database.beginTransaction();

		for (int i = 0; i < attaches.size(); i++) {
			AttachItem attachItem = attaches.get(i);
			if (attachItem.getId() == 0) {
				saveExtraAttach(attachItem); // currentID of note already exists because it works for update note when
												// note already had id
			} else {
				ContentValues values = new ContentValues();
				values.put("_id", attachItem.getId());
				values.put("path", attachItem.getPath());
				values.put("type", attachItem.getType());
				values.put("spark_notes_id", attachItem.getSparkNoteId());

				database.update(DBHelper.TABLE_ATTACHES, values, "_id=?",
						new String[] { String.valueOf(attachItem.getId()) });
			}
		}

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		return;
	}

	private void saveExtraAttach(AttachItem attachItem) {
		database = db.getWritableDatabase();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("path", attachItem.getPath());
		values.put("type", attachItem.getType());
		values.put("spark_notes_id", attachItem.getSparkNoteId());

		database.insert(DBHelper.TABLE_ATTACHES, null, values);

		database.setTransactionSuccessful();
		database.endTransaction();
		return;
	}

	public void deleteNote(Long id) {
		database = db.getWritableDatabase();
		database.beginTransaction();
		database.delete(DBHelper.TABLE_SPARK_NOTES, "_id=?", new String[] { String.valueOf(id) });
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}
	
	public void deleteAttach(Long id) {
		database = db.getWritableDatabase();
		database.beginTransaction();
		database.delete(DBHelper.TABLE_ATTACHES, "_id=?", new String[] { String.valueOf(id) });
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}
}
