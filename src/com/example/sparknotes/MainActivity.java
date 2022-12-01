package com.example.sparknotes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionNoteItemListener, SupportFragmentHandlerListener {

	SQLController dbController;
	DummyNoteDB db = new DummyNoteDB();

	ArrayList<SparkNote> exportList = new ArrayList<SparkNote>();
	Boolean isSelected = false;

	DrawerLayout drawer;
	ListView mainMenu;
	ActionBarDrawerToggle menuToggler;

	Fragment current;
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy");

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		// Initiate DB
		// BEGIN
		dbController = new SQLController(getApplicationContext());
		dbController.open();
//		dbController.clear();
//		dbController.addAll(db.getNotes());

		// END

		// Showing main note list
		// BEGIN
		String[] menuPoints = getResources().getStringArray(R.array.main_menu_points);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mainMenu = (ListView) findViewById(R.id.main_menu);
		mainMenu.setAdapter(new ArrayAdapter<String>(this, R.layout.item_main_menu, menuPoints));

		// Set `back` button to get back on the one step in the stack
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Create object with Listener for button of actionBar which open or close
		// drawer menu
		menuToggler = new ActionBarDrawerToggle(this, drawer, R.drawable.actionbar_burger_item, R.string.main_menu_open,
				R.string.main_menu_close) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		// set listeners for drawer node by single object
		drawer.setDrawerListener(menuToggler);
		getActionBar().setIcon(null);
		// set listeners for points of drawer menu (ListView)
		mainMenu.setOnItemClickListener(new SupportDrawerEventKeeper());
		// END

		// Section with something
		// BEGIN
		enterToDrawerMenuPointBy(0);
		// END
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
			Toast.makeText(this, "пїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ", Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		boolean isNoteListFragment = current instanceof NoteListFragment;
		if (NoteListFragment.isSelecting || !(isNoteListFragment)) {
			enterToDrawerMenuPointBy(0);
			NoteListFragment.isSelecting = false;
			NoteListFragment.selectingItemIDs.clear();
			Toast.makeText(this, "Clean static array", Toast.LENGTH_SHORT).show();
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during onPostCreate()
	 * and onConfigurationChanged()...
	 */
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
		switch (position) {
		case 1:
//			Toast.makeText(this, "position is 1", Toast.LENGTH_SHORT).show();
			fragment = new ExportFragment();
			break;
		case 2:
//			Toast.makeText(this, "position is 2", Toast.LENGTH_SHORT).show();
			fragment = new ImportFragment();
			break;
		case 3:
//			Toast.makeText(this, "position is 3", Toast.LENGTH_SHORT).show();
			fragment = new RecycleBinFragment();
			break;
		case 4:
//			Toast.makeText(this, "position is 4", Toast.LENGTH_SHORT).show();
			fragment = new SearchFragment();
			break;
		case 5:
//			Toast.makeText(this, "position is 5", Toast.LENGTH_SHORT).show();
			fragment = new AppearanceFragment();
			break;
		case 6:
//			Toast.makeText(this, "position is 6", Toast.LENGTH_SHORT).show();
			fragment = new FAQFragment();
			break;
		case 7:
//			Toast.makeText(this, "position is 7", Toast.LENGTH_SHORT).show();
//			fragment = new CreateFragment();
//			Bundle bundle = new Bundle();
//			bundle.putBoolean("isOpen", false);
//			fragment.setArguments(bundle);

			Intent intent = new Intent(this, CreateActivity.class);
			startActivity(intent);
			break;
		default:
//			Toast.makeText(this, "List of notes is opened", Toast.LENGTH_SHORT).show();
			NoteListFragment noteListFragment = new NoteListFragment();
//			noteListFragment.setAdapter(new UserNoteAdapter(this, db.getNotes()));

			noteListFragment.setAdapter(new SparkNoteCursorAdapter(this, dbController.getSparkNotes()));

			fragment = noteListFragment;
			current = fragment;
			break;
		}

		// set new fragment
		getSupportFragmentManager().beginTransaction().replace(R.id.work_frame, fragment).commit();
		current = fragment;
		// refresh and close drawer menu
		mainMenu.setItemChecked(position, true);
		setTitle(getResources().getStringArray(R.array.main_menu_points)[position]);
		drawer.closeDrawer(mainMenu);
	}

	@Override
	public void openNote(long position) {
		Toast.makeText(this, "position is " + position, Toast.LENGTH_SHORT).show();

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
//		db.updateNote(position, title, content);
		if (position > 0) {
			dbController.updateNote(position, title, content, date, attaches);
		} else {
			dbController.saveNote(title, content, date, attaches);
		}
		enterToDrawerMenuPointBy(0);
	}

	@Override
	public void deleteNotes(ArrayList<Long> positions) {
		for (int i = 0; i < positions.size(); i++) {
			dbController.deleteNote(positions.get(i));
		}
		NoteListFragment.selectingItemIDs.clear();
		NoteListFragment.isSelecting = false;
		enterToDrawerMenuPointBy(0);
	}

	@Override
	public void deleteNote(Long id) {
		dbController.deleteNote(id);
		enterToDrawerMenuPointBy(0);
	}

	@Override
	public void refreshZeroSelectedListViewItems() {
		if (NoteListFragment.selectingItemIDs.size() == 0) {
			NoteListFragment.isSelecting = false;
			enterToDrawerMenuPointBy(0);
		}
		;
	}

	@Override
	protected void onStop() {
		dbController.close();
		super.onStop();
	}

}
