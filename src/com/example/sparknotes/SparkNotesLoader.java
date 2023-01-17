package com.example.sparknotes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

public class SparkNotesLoader extends AsyncTaskLoader<Cursor> {
	MainActivity context;
	SQLController dbController;
	public SparkNotesLoader(Context context) {
		super(context);
		this.context = (MainActivity) context;
	}
	
	public SparkNotesLoader(Context context, SQLController dbController) {
		super(context);
		this.context = (MainActivity) context;
		this.dbController = dbController;
	}

	@Override
	public Cursor loadInBackground() {
		if (context.searchResults == null) {
			return dbController.getSparkNotes();
		} else {
			return context.searchResults;
		}
		
		
	}

}
