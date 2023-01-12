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
		String[] allColumns = new String[] { db.TABLE_SPARK_NOTES_ID, db.TABLE_SPARK_NOTES_TITLE,
				db.TABLE_SPARK_NOTES_CONTENT, db.TABLE_SPARK_NOTES_INIT_DATE };
		Cursor c = database.query(db.TABLE_SPARK_NOTES, allColumns, null, null, null, null, "_id DESC");
		if (c != null) {
			c.moveToFirst();
		}
		this.close();
		return c;
	}

	public void addAll(ArrayList<SparkNote> notes) {
		database.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (int i = 0; i < notes.size(); i++) {
				SparkNote sparkNote = notes.get(i);
				values.put("title", sparkNote.getTitle());
				values.put("content", sparkNote.getContent());
				values.put("init_date", sdf.format(sparkNote.getInitDate()));
				long currentNoteID = database.insert(DBHelper.TABLE_SPARK_NOTES, null, values);
				ArrayList<AttachItem> enclosures = sparkNote.getEnclosures();
				if (enclosures.size() > 0) {
					for (int j = 0; j < enclosures.size(); j++) {
						AttachItem attach = enclosures.get(j);
						ContentValues attachValues = new ContentValues();
						attachValues.put("path", attach.getPath());
						attachValues.put("type", attach.getType());
						attachValues.put("spark_notes_id", currentNoteID);
						database.insert(DBHelper.TABLE_ATTACHES, null, attachValues);
					}

				}
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
		open();
		Cursor cursor;
		database.beginTransaction();
		String pos = String.valueOf(position);

		String[] selectionArgs = new String[] { pos };
		String sql = "SELECT * FROM spark_notes WHERE _id=?";

		cursor = database.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		database.setTransactionSuccessful();
		database.endTransaction();
		return cursor;
	}

	public ArrayList<SparkNote> getNotes() {
		open();
		Cursor cursor;
		database.beginTransaction();

		String sql = "SELECT * FROM spark_notes";
		cursor = database.rawQuery(sql, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		database.setTransactionSuccessful();
		database.endTransaction();
		close();
		return convertSparkCursorToList(cursor);
	}

	public Cursor getNotesByDatesAndText(String start, String end, String sample, String sampleCriteria) {
		Cursor cursor;
		this.open();
		database.beginTransaction();
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

		database.setTransactionSuccessful();
		database.endTransaction();
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
		int size = indices.size();
		Log.d("SIZE_ATTACHES", "size of indices array is " + size);
		for (int i = 0; i < size; i++) {
			ArrayList<AttachItem> attachesByNoteId = getAttachesByNoteId(indices.get(i));
			attaches.addAll(attachesByNoteId);
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
		close();
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
		close();
	}

	private void saveNoteAttaches(long insert, ArrayList<AttachItem> attaches) {
		open();
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
		close();
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
		open();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("path", attachItem.getPath());
		values.put("type", attachItem.getType());
		values.put("spark_notes_id", attachItem.getSparkNoteId());

		database.insert(DBHelper.TABLE_ATTACHES, null, values);

		database.setTransactionSuccessful();
		database.endTransaction();
		close();
		return;
	}

	public void deleteNote(Long id) {
		database.beginTransaction();
		saveDeletedNote(getNoteById(id));
		database.delete(DBHelper.TABLE_SPARK_NOTES, "_id=?", new String[] { String.valueOf(id) });
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public void deleteAttach(Long id) {
		open();
		database.beginTransaction();
		database.delete(DBHelper.TABLE_ATTACHES, "_id=?", new String[] { String.valueOf(id) });
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		close();
	}

	public Cursor getDeletedSparkNotes() {
		this.open();
		String[] allColumns = new String[] { db.TABLE_DELETED_SPARK_NOTES_ID, db.TABLE_DELETED_SPARK_NOTES_TITLE,
				db.TABLE_DELETED_SPARK_NOTES_CONTENT, db.TABLE_DELETED_SPARK_NOTES_INIT_DATE,
				db.TABLE_DELETED_SPARK_NOTES_PREV_ID };
		Cursor c = database.query(db.TABLE_DELETED_SPARK_NOTES, allColumns, null, null, null, null, "_id DESC");
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
		database.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("content", content);
		values.put("init_date", date);
		values.put("prev_id", prevID);
		long insert = database.insert(DBHelper.TABLE_DELETED_SPARK_NOTES, null, values);
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public Cursor getDeletedNoteById(Long id) {
		Cursor cursor;
		database.beginTransaction();
		String pos = String.valueOf(id);

		String[] selectionArgs = new String[] { pos };
		String sql = "SELECT * FROM deleted_spark_notes WHERE _id=?";

		cursor = database.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();
		database.setTransactionSuccessful();
		database.endTransaction();
		return cursor;
	}

	public void restoreNote(Long id) {
		open();
		database.beginTransaction();
		Cursor deletedNoteById = getDeletedNoteById(id);
		database.setTransactionSuccessful();
		database.endTransaction();

		String title = deletedNoteById.getString(1);
		String content = deletedNoteById.getString(2);
		String date = deletedNoteById.getString(3);
		Long prevID = deletedNoteById.getLong(4);

		open();
		database.beginTransaction();
		ArrayList<AttachItem> attachesByNoteId = getAttachesByNoteId(prevID);
		database.setTransactionSuccessful();
		database.endTransaction();

		saveNote(title, content, date, attachesByNoteId);

		String[] whereArgs = new String[] { String.valueOf(id) };

		open();
		database.beginTransaction();
		int count = database.delete(DBHelper.TABLE_DELETED_SPARK_NOTES, "_id=?", whereArgs);
//		database.rawQuery("DELETE FROM deleted_spark_notes WHERE _id=?", whereArgs);
		Log.d(RecycleBinFragment.LOG_TAG, String.valueOf(count));
		database.setTransactionSuccessful();
		database.endTransaction();

	}

	public void fullDeleteNote(Long id) {
		open();
		database.beginTransaction();
		database.delete(DBHelper.TABLE_DELETED_SPARK_NOTES, "_id=?", new String[] { String.valueOf(id) });
		database.setTransactionSuccessful();
		database.endTransaction();
		close();
	}
}
