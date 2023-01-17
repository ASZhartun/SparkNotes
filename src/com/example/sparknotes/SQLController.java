package com.example.sparknotes;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLController {
	private DBHelper db;
	private Context ctx;
	private SQLiteDatabase database;

	private SimpleDateFormat sdf = MainActivity.sdf;

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
		this.open();
		String[] allColumns = new String[] { DBHelper.TABLE_SPARK_NOTES_ID, DBHelper.TABLE_SPARK_NOTES_TITLE,
				DBHelper.TABLE_SPARK_NOTES_CONTENT, DBHelper.TABLE_SPARK_NOTES_INIT_DATE };
		Cursor c = database.query(DBHelper.TABLE_SPARK_NOTES, allColumns, null, null, null, null, "_id DESC");
		if (c != null) {
			c.moveToFirst();
		}
		this.close();
		return c;
	}

	public void addAll(ArrayList<SparkNote> notes) {

		ContentValues values = new ContentValues();
		for (int i = 0; i < notes.size(); i++) {
			SparkNote sparkNote = notes.get(i);
			values.put("title", sparkNote.getTitle());
			values.put("content", sparkNote.getContent());
			values.put("init_date", sdf.format(sparkNote.getInitDate()));
			long currentNoteID = database.insert(DBHelper.TABLE_SPARK_NOTES, null, values);
			ArrayList<AttachItem> enclosures = sparkNote.getEnclosures();
			if (enclosures.size() > 0) {
				ContentValues attachValues = new ContentValues();
				for (int j = 0; j < enclosures.size(); j++) {
					AttachItem attach = enclosures.get(j);
					attachValues.put("path", attach.getPath());
					attachValues.put("type", attach.getType());
					attachValues.put("spark_notes_id", currentNoteID);
					database.insert(DBHelper.TABLE_ATTACHES, null, attachValues);
				}
			}
		}
	}

	public void clear() {
		open();
		database.execSQL("DELETE FROM spark_notes");
		close();
	}

	public Cursor getNoteById(long position) {

		String pos = String.valueOf(position);
		String[] selectionArgs = new String[] { pos };
		String sql = "SELECT * FROM spark_notes WHERE _id=?";

		Cursor cursor = database.rawQuery(sql, selectionArgs);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	public ArrayList<SparkNote> getNotes() {
		open();

		String sql = "SELECT * FROM spark_notes";
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		close();
		return convertSparkCursorToList(cursor);
	}

	public Cursor getNotesByDatesAndText(String start, String end, String sample, String sampleCriteria) {
		Cursor cursor;
		this.open();
		if (start == null || start.equals("")) {
			start = sdf.format(new Date(0));
		}
		if (end == null || end.equals("")) {
			end = sdf.format(new Date());
		}
		String sql = "SELECT * FROM spark_notes ";
		String sqlWhereDates = "WHERE init_date BETWEEN  '" + start + "' AND '" + end + "'";
		String sqlFinal = sql + sqlWhereDates;
		if (sample == null || sample.equals("")) {
			cursor = database.rawQuery(sqlFinal, null);
		} else {
			String sqlWhereSampleIs = "WHERE " + sampleCriteria + " LIKE '%" + sample + "%'";
			String sqlSpecifiedFinal = "SELECT * FROM (" + sqlFinal + ") " + sqlWhereSampleIs;
			cursor = database.rawQuery(sqlSpecifiedFinal, null);
		}

		if (cursor != null) {
			cursor.moveToFirst();
		}
		this.close();
		return cursor;
	}

	private ArrayList<SparkNote> convertSparkCursorToList(Cursor notes) {
		ArrayList<SparkNote> results = new ArrayList<SparkNote>();
		do {
			try {
				results.add(new SparkNote(notes.getInt(0), notes.getString(1), notes.getString(2),
						sdf.parse(notes.getString(3))));
			} catch (ParseException e) {
				results.add(new SparkNote(notes.getInt(0), notes.getString(1), notes.getString(2), new Date()));
				e.printStackTrace();
			}
		} while (notes.moveToNext());
		return results;
	}

	public ArrayList<AttachItem> getAttachesByNoteId(long position) {
		open();

		String pos = String.valueOf(position);
		String[] selectionArgs = new String[] { pos };
		String sql = "SELECT * FROM attaches WHERE spark_notes_id=?";
		Cursor cursor = database.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();

		ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();
		int quantity = cursor.getCount();
		if (quantity > 0) {
			for (int i = 0; i < quantity; i++) {
				AttachItem attachItem = new AttachItem(cursor.getLong(0), cursor.getString(1),
						new File(cursor.getString(1)), cursor.getString(2), cursor.getLong(3));
				attaches.add(attachItem);
				if (!cursor.moveToNext())
					break;
			}
		}
		return attaches;
	}

	public ArrayList<AttachItem> getAttaches(ArrayList<Long> indices) {
		ArrayList<AttachItem> attaches = new ArrayList<AttachItem>();
		try {
			database.beginTransaction();
			for (int i = 0; i < indices.size(); i++) {
				ArrayList<AttachItem> attachesByNoteId = getAttachesByNoteId(indices.get(i));
				attaches.addAll(attachesByNoteId);
			}
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		return attaches;

	}

	public void updateNote(long position, String title, String content, String date, ArrayList<AttachItem> attaches) {
		this.open();

		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("content", content);

		try {
			database.beginTransaction();

			database.update(DBHelper.TABLE_SPARK_NOTES, values, "_id = ?", new String[] { String.valueOf(position) });
			updateNoteAttaches(position, attaches);

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}

		close();
	}

	public void saveNote(String title, String content, String date, ArrayList<AttachItem> attaches) {
		open();

		try {
			database.beginTransaction();
			ContentValues values = new ContentValues();
			values.put("title", title);
			values.put("content", content);
			values.put("init_date", sdf.format(new Date()));

			long insert = database.insert(DBHelper.TABLE_SPARK_NOTES, null, values);
			saveNoteAttaches(insert, attaches);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d("shit", "trouble with save no attaches");
		} 
		finally {
			database.endTransaction();
		}

		close();
	}

	private void saveNoteAttaches(long insert, ArrayList<AttachItem> attaches) {
		for (int i = 0; i < attaches.size(); i++) {
			attaches.get(i).setSparkNoteId(insert);

			ContentValues values = new ContentValues();
			values.put("path", attaches.get(i).getPath());
			values.put("type", attaches.get(i).getType());
			values.put("spark_notes_id", attaches.get(i).getSparkNoteId());

			database.insert(DBHelper.TABLE_ATTACHES, null, values);
		}
	}

	private void updateNoteAttaches(long position, ArrayList<AttachItem> attaches) {
		for (int i = 0; i < attaches.size(); i++) {
			AttachItem attachItem = attaches.get(i);
			if (attachItem.getId() == 0) {
				saveExtraAttach(attachItem);
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
	}

	private void saveExtraAttach(AttachItem attachItem) {

		ContentValues values = new ContentValues();
		values.put("path", attachItem.getPath());
		values.put("type", attachItem.getType());
		values.put("spark_notes_id", attachItem.getSparkNoteId());

		database.insert(DBHelper.TABLE_ATTACHES, null, values);

		return;
	}

	public void deleteNote(Long id) {
		try {
			database.beginTransaction();

			saveDeletedNote(getNoteById(id));
			database.delete(DBHelper.TABLE_SPARK_NOTES, "_id=?", new String[] { String.valueOf(id) });

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	public void deleteAttach(Long id) {
		open();
		database.delete(DBHelper.TABLE_ATTACHES, "_id=?", new String[] { String.valueOf(id) });
		close();
	}

	public Cursor getDeletedSparkNotes() {
		this.open();
		String[] allColumns = new String[] { DBHelper.TABLE_DELETED_SPARK_NOTES_ID,
				DBHelper.TABLE_DELETED_SPARK_NOTES_TITLE, DBHelper.TABLE_DELETED_SPARK_NOTES_CONTENT,
				DBHelper.TABLE_DELETED_SPARK_NOTES_INIT_DATE, DBHelper.TABLE_DELETED_SPARK_NOTES_PREV_ID };
		Cursor c = database.query(DBHelper.TABLE_DELETED_SPARK_NOTES, allColumns, null, null, null, null, "_id DESC");
		if (c != null) {
			c.moveToFirst();
		}
		this.close();
		return c;
	}

	public void saveDeletedNote(Cursor cursor) {
		String title = cursor.getString(1);
		String content = cursor.getString(2);
		String date = cursor.getString(3);
		Integer prevID = cursor.getInt(0);

		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("content", content);
		values.put("init_date", date);
		values.put("prev_id", prevID);

		database.insert(DBHelper.TABLE_DELETED_SPARK_NOTES, null, values);
	}

	public Cursor getDeletedNoteById(Long id) {
		String pos = String.valueOf(id);
		String[] selectionArgs = new String[] { pos };
		String sql = "SELECT * FROM deleted_spark_notes WHERE _id=?";

		Cursor cursor = database.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();

		return cursor;
	}

	public void restoreNote(Long id) {
		open();

		try {
			database.beginTransaction();

			Cursor deletedNoteById = getDeletedNoteById(id);
			String title = deletedNoteById.getString(1);
			String content = deletedNoteById.getString(2);
			String date = deletedNoteById.getString(3);
			Long prevID = deletedNoteById.getLong(4);

			ArrayList<AttachItem> attachesByNoteId = getAttachesByNoteId(prevID);
			saveNote(title, content, date, attachesByNoteId);

			String[] whereArgs = new String[] { String.valueOf(id) };
			database.delete(DBHelper.TABLE_DELETED_SPARK_NOTES, "_id=?", whereArgs);

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}

		close();
	}

	public void fullDeleteNote(Long id) {
		open();
		database.delete(DBHelper.TABLE_DELETED_SPARK_NOTES, "_id=?", new String[] { String.valueOf(id) });
		close();
	}
}
