package com.example.sparknotes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.sparknotes.ExportOptionsDialog.ExportOptionsDialogListener;
import com.example.sparknotes.RecycleItemDialog.RecycleItemDialogListener;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements ActionNoteItemListener, ExportOptionsDialogListener, RecycleItemDialogListener {

	SQLController dbController;

	ArrayList<SparkNote> exportList = new ArrayList<SparkNote>();
	Boolean isSelected = false;

	public Cursor searchResults = null;
	public SparkNoteCursorAdapter noteListAdapter = null;

	DrawerLayout drawer;
	ListView mainMenu;

	ActionBarDrawerToggle menuToggler;

	Fragment current;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ROOT);
	public static boolean SEARCH_FLAG = false;
	public final static int GET_EXPORT_DIRECTORY = 101;
	public final static int GET_IMPORT_FILE = 102;
	public final static int GET_SEARCH_RESULT = 103;

	
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		dbController = new SQLController(getApplicationContext());
		dbController.open();

		String[] menuPoints = getResources().getStringArray(R.array.main_menu_points);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mainMenu = (ListView) findViewById(R.id.main_menu);
		mainMenu.setAdapter(new ArrayAdapter<String>(this, R.layout.item_main_menu, menuPoints));

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		menuToggler = new ActionBarDrawerToggle(this, drawer, R.drawable.actionbar_burger_item, R.string.main_menu_open,
				R.string.main_menu_close) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		drawer.setDrawerListener(menuToggler);
		getActionBar().setIcon(null);
		mainMenu.setOnItemClickListener(new SupportDrawerEventKeeper());

		enterToDrawerMenuPointBy(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == GET_SEARCH_RESULT) {
			String sample = data.getStringExtra(SearchActivity.SAMPLE_KEY);
			String criteria = data.getStringExtra(SearchActivity.SAMPLE_CRITERIA_KEY);
			String start = data.getStringExtra(SearchActivity.START_DATE_KEY);
			String end = data.getStringExtra(SearchActivity.END_DATE_KEY);
			searchResults = dbController.getNotesByDatesAndText(start, end, sample, criteria);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		dbController.open();
		enterToDrawerMenuPointBy(0);
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (menuToggler.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_bar_search_item:
			enterToDrawerMenuPointBy(4);
			break;
		case R.id.action_bar_export_item:
			enterToDrawerMenuPointBy(1);
			break;
		case R.id.action_bar_create_item:
			enterToDrawerMenuPointBy(7);
			break;
		default:
			// do nothing...
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (current instanceof NoteListFragment) {
			if (SEARCH_FLAG) {
				SEARCH_FLAG = false;
				searchResults = null;
				enterToDrawerMenuPointBy(0);
			} else {
				super.onBackPressed();
			}
		} else {
			enterToDrawerMenuPointBy(0);
		}

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		menuToggler.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		menuToggler.onConfigurationChanged(newConfig);
	}

	private class SupportDrawerEventKeeper implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			enterToDrawerMenuPointBy(position);
		}

	}

	private void enterToDrawerMenuPointBy(int position) {
		Fragment fragment = new NoteListFragment();
		Intent intent;
		switch (position) {
		case 1:
			intent = new Intent(this, ExportActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(this, ImportActivity.class);
			startActivity(intent);
			break;
		case 3:
			RecycleBinFragment recycleBinFragment = new RecycleBinFragment();
			dbController.open();
			recycleBinFragment.setAdapter(new SparkNoteCursorAdapter(this, dbController.getDeletedSparkNotes()));
			fragment = recycleBinFragment;
			dbController.close();
			break;
		case 4:
			intent = new Intent(this, SearchActivity.class);
			startActivityForResult(intent, GET_SEARCH_RESULT);
			break;
		case 5:
			Toast.makeText(this, "Settings will delivery later", Toast.LENGTH_SHORT).show();
			NoteListFragment refreshNoteList = refreshNoteListAdapter();
			refreshNoteList.adapter.notifyDataSetChanged();
			fragment = refreshNoteList;
			position = 0;
//			intent = new Intent(this, SettingsActivity.class);
//			startActivity(intent);
			break;
		case 6:
			fragment = new FAQFragment();
			break;
		case 7:
			intent = new Intent(this, CreateActivity.class);
			startActivity(intent);
			break;
		default:
//			clearSearchResult();
			NoteListFragment noteListFragment = refreshNoteListAdapter();
			noteListFragment.adapter.notifyDataSetChanged();
			fragment = noteListFragment;
			break;
		}

		getSupportFragmentManager().beginTransaction().replace(R.id.work_frame, fragment).commit();
		current = fragment;
		mainMenu.setItemChecked(position, true);
		if (SEARCH_FLAG) {
			setTitle(getResources().getString(R.string.search_results));
		} else {
			setTitle(getResources().getStringArray(R.array.main_menu_points)[position]);
		}

		drawer.closeDrawer(mainMenu);
	}

	private NoteListFragment refreshNoteListAdapter() {
		NoteListFragment noteListFragment = new NoteListFragment();
		dbController.open();
		if (searchResults != null) {
			noteListAdapter = new SparkNoteCursorAdapter(this, searchResults);

		} else {
			noteListAdapter = new SparkNoteCursorAdapter(this, dbController.getSparkNotes());
		}
		noteListFragment.setAdapter(noteListAdapter);
		dbController.close();
		return noteListFragment;
	}

	@Override
	public void openNote(long position) {

		Bundle bundle = new Bundle();
		bundle.putLong("noteID", position);

		Intent intent = new Intent(this, CreateActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);

		enterToDrawerMenuPointBy(0);
		return;
	}

	@Override
	public void save(long position, String title, String content, String date, ArrayList<AttachItem> attaches) {
		Toast.makeText(this, "Doletelo v save activity", Toast.LENGTH_SHORT).show();
		if (position > 0) {
			dbController.updateNote(position, title, content, date, attaches);
		} else {
			dbController.saveNote(title, content, date, attaches);
		}
		enterToDrawerMenuPointBy(0);
	}

	@Override
	public void deleteNotes(ArrayList<Long> positions) {
		dbController.open();
		for (int i = 0; i < positions.size(); i++) {
			dbController.deleteNote(positions.get(i));
		}
		dbController.close();
		enterToDrawerMenuPointBy(0);
	}

	@Override
	public void deleteNote(Long id) {
		dbController.deleteNote(id);
		enterToDrawerMenuPointBy(0);
	}

	@Override
	protected void onStop() {
		dbController.close();
		super.onStop();
	}

	@Override
	public void clearSearchResult() {
		searchResults = null;
//		if (current instanceof NoteListFragment) {
//			NoteListFragment frag = (NoteListFragment) current;
//			frag.adapter = new SparkNoteCursorAdapter(this, dbController.getSparkNotes());
//		}
		enterToDrawerMenuPointBy(0);
	}

	@Override
	public void openDeletedNote(long id) {
		Toast.makeText(this, "Alert box with actions", Toast.LENGTH_LONG).show();
		return;

	}

	@Override
	public void restoreNotes(ArrayList<Long> positions) {
		dbController.open();
		for (int i = 0; i < positions.size(); i++) {
			dbController.restoreNote(positions.get(i));
		}
		dbController.close();
		RecycleBinFragment.selectingItemIDs.clear();
		enterToDrawerMenuPointBy(0);

	}

	@Override
	public void fullDeleteNotes(ArrayList<Long> positions) {
		dbController.open();
		for (int i = 0; i < positions.size(); i++) {
			dbController.fullDeleteNote(positions.get(i));
		}
		dbController.close();
		RecycleBinFragment.selectingItemIDs.clear();
		enterToDrawerMenuPointBy(3);
	}

	@Override
	public void shareSelectedNotesAsZip(ArrayList<Long> positions) {
		ArrayList<SparkNote> list = getSparkNotesByPostions(positions);
		ExportActivity.shareNotes = list;
		enterToDrawerMenuPointBy(1);
	}

	private ArrayList<SparkNote> getSparkNotesByPostions(ArrayList<Long> positions) {
		ArrayList<SparkNote> list = new ArrayList<SparkNote>();
		for (int i = 0; i < positions.size(); i++) {
			SparkNote spark = convert(positions.get(i), dbController.getNoteById(positions.get(i)));
			list.add(spark);
		}
		return list;
	}

	private SparkNote convert(Long id, Cursor noteById) {
		String title = noteById.getString(1);
		String content = noteById.getString(2);
		String date = noteById.getString(3);
		Date parseDate;
		try {
			parseDate = sdf.parse(date);
		} catch (ParseException e) {
			parseDate = new Date();
		}

		return new SparkNote(id, title, content, parseDate, dbController.getAttachesByNoteId(id));
	}

	@Override
	public void shareSelectedNotesByApps(ArrayList<Long> positions) {
		ArrayList<SparkNote> list = getSparkNotesByPostions(positions);
		String result = packContent(list);
		
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
		intent.putExtra(android.content.Intent.EXTRA_TEXT, result);
		startActivity(Intent.createChooser(intent, "Choose sharing method"));
	}

	private String packContent(ArrayList<SparkNote> list) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			SparkNote sparkNote = list.get(i);
			sb.append(sparkNote.getTitle()).append("\r\n").append(sdf.format(sparkNote.getInitDate())).append("\r\n")
					.append(sparkNote.getContent()).append("\r\n------\r\n");
		}
		return sb.toString();
	}
	
	public void showExportDialog() {
		DialogFragment dialog = new ExportOptionsDialog();
        dialog.show(getSupportFragmentManager(), "ExportOptionsDialogFragment");
	}

	@Override
	public void onExportDialogPositiveClick(DialogFragment dialog) {
		shareSelectedNotesByApps(MultiChoiceMainNoteListImpl.selections);
		Log.d("Dialog EXPORT", "Pressed YES - ZIP");
	}

	@Override
	public void onExportDialogNegativeClick(DialogFragment dialog) {
		shareSelectedNotesAsZip(MultiChoiceMainNoteListImpl.selections);
		Log.d("Dialog EXPORT", "Pressed NO - MESSANGER");
	}

	@Override
	public void showRecycleDialog(long id) {
		DialogFragment dialog = new RecycleItemDialog(id);
        dialog.show(getSupportFragmentManager(), "RecycleItemDialogFragment");
	}
	
	@Override
	public void onRecycleDialogPositiveClick(DialogFragment dialog, long id) {
		dbController.restoreNote(id);
		Log.d("Dialog RECYCLE", "Resotre note");
		enterToDrawerMenuPointBy(3);
	}

	@Override
	public void onRecycleDialogNegativeClick(DialogFragment dialog, long id) {
		dbController.fullDeleteNote(id);
		Log.d("Dialog RECYCLE", "Destroy note");
		enterToDrawerMenuPointBy(3);
	}

	@Override
	public void onRecycleDialogNeutralClick(DialogFragment dialog) {
		Toast.makeText(this, "Operation was canceled", Toast.LENGTH_SHORT).show();
		Log.d("Dialog RECYCLE", "Destroy note");
		
	}

}
