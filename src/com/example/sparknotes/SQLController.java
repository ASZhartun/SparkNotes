package com.example.sparknotes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
		db = new DBHelper(ctx);
		database = db.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public Cursor getSparkNotes() {
		String[] allColumns = new String[] { db.TABLE_SPARK_NOTES_ID, db.TABLE_SPARK_NOTES_TITLE,
				db.TABLE_SPARK_NOTES_CONTENT, db.TABLE_SPARK_NOTES_INIT_DATE };
		Cursor c = database.query(db.TABLE_SPARK_NOTES, allColumns, null, null, null, null, null);
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
		database.endTransaction();
		return cursor;



	}
}
