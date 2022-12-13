package com.example.sparknotes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



import android.content.Intent;
import android.content.res.Configuration;
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

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements ActionNoteItemListener, SupportFragmentHandlerListener {

	SQLController dbController;
	DummyNoteDB db = new DummyNoteDB();

	ArrayList<SparkNote> exportList = new ArrayList<SparkNote>();
	Boolean isSelected = false;
	
	public static ArrayList<SparkNote> searchResults = new ArrayList<SparkNote>();

	DrawerLayout drawer;
	ListView mainMenu;

	ActionBarDrawerToggle menuToggler;

	Fragment current;
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.yyyy", Locale.ROOT);
	public final static int GET_EXPORT_DIRECTORY = 101;
	public final static int GET_IMPORT_FILE = 102;

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

		// TODO Auto-generated method stub
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
			fragment = new RecycleBinFragment();
			break;
		case 4:
			fragment = new SearchFragment();
			break;
		case 5:
			fragment = new AppearanceFragment();
			break;
		case 6:
			fragment = new FAQFragment();
			break;
		case 7:
			intent = new Intent(this, CreateActivity.class);
			startActivity(intent);
			break;
		default:
			NoteListFragment noteListFragment = new NoteListFragment();
			dbController.open();
			noteListFragment.setAdapter(new SparkNoteCursorAdapter(this, dbController.getSparkNotes()));
			dbController.close();
			fragment = noteListFragment;
			break;
		}

		getSupportFragmentManager().beginTransaction().replace(R.id.work_frame, fragment).commit();
		current = fragment;
		mainMenu.setItemChecked(position, true);
		setTitle(getResources().getStringArray(R.array.main_menu_points)[position]);
		drawer.closeDrawer(mainMenu);
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
