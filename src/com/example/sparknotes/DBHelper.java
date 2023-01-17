package com.example.sparknotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	// DATABASE INFORMATION
	static final String DB_NAME = "SPARK_NOTE.DB";
	static final int DB_VERSION = 1;

	// TABLE INFO FOR MAIN SPARK NOTE LIST
	static final String TABLE_SPARK_NOTES = "spark_notes";
	static final String TABLE_SPARK_NOTES_ID = "_id";
	static final String TABLE_SPARK_NOTES_TITLE = "title";
	static final String TABLE_SPARK_NOTES_CONTENT = "content";
	static final String TABLE_SPARK_NOTES_INIT_DATE = "init_date";

	// TABLE INFO FOR MAIN SPARK NOTE ATTACHES
	static final String TABLE_ATTACHES = "attaches";
	static final String TABLE_ATTACHES_ID = "_id";
	static final String TABLE_ATTACHES_PATH = "path";
	static final String TABLE_ATTACHES_TYPE = "type";
	static final String TABLE_ATTACHES_SPARK_NOTES_ID = "spark_notes_id";

	// TABLE INFO FOR MAIN SPARK NOTE LIST
	static final String TABLE_DELETED_SPARK_NOTES = "deleted_spark_notes";
	static final String TABLE_DELETED_SPARK_NOTES_ID = "_id";
	static final String TABLE_DELETED_SPARK_NOTES_TITLE = "title";
	static final String TABLE_DELETED_SPARK_NOTES_CONTENT = "content";
	static final String TABLE_DELETED_SPARK_NOTES_INIT_DATE = "init_date";
	static final String TABLE_DELETED_SPARK_NOTES_PREV_ID = "prev_id";

	// SPARK NOTE TABLE CREATION STATEMENT
	private static final String CREATE_SPARK_NOTES = "create table " + TABLE_SPARK_NOTES + "(" + TABLE_SPARK_NOTES_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_SPARK_NOTES_TITLE + " TEXT, " + TABLE_SPARK_NOTES_CONTENT
			+ " TEXT, " + TABLE_SPARK_NOTES_INIT_DATE + " DATE NOT NULL);";

	// SPARK NOTE ATTACHES TABLE CREATION STATEMENT
	private static final String CREATE_ATTACHES = "create table " + TABLE_ATTACHES + "(" + TABLE_ATTACHES_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_ATTACHES_PATH + " TEXT NOT NULL, " + TABLE_ATTACHES_TYPE
			+ " TEXT NOT NULL, " + TABLE_ATTACHES_SPARK_NOTES_ID
			+ " INTEGER REFERENCES spark_notes (_id) ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE NOT NULL);";

	// SPARK NOTE TABLE CREATION STATEMENT
	private static final String CREATE_DELETED_SPARK_NOTES = "create table " + TABLE_DELETED_SPARK_NOTES + "("
			+ TABLE_DELETED_SPARK_NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  + TABLE_DELETED_SPARK_NOTES_TITLE + " TEXT, "
			+ TABLE_DELETED_SPARK_NOTES_CONTENT + " TEXT, " + TABLE_DELETED_SPARK_NOTES_INIT_DATE + " DATE NOT NULL, " + TABLE_DELETED_SPARK_NOTES_PREV_ID
			+ " INTEGER);";

	private static DBHelper instance;

	public static DBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DBHelper(context);
		}
		return instance;
	}

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_SPARK_NOTES);
		db.execSQL(CREATE_ATTACHES);
		db.execSQL(CREATE_DELETED_SPARK_NOTES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPARK_NOTES);
		onCreate(db);
	}

}
