package com.example.sparknotes;

import java.util.ArrayList;

import com.example.sparknotes.CreateFragment.EditNoteActionsListener;
import com.example.sparknotes.NoteListFragment.OpenNoteItemListener;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OpenNoteItemListener, EditNoteActionsListener{
	
	DummyNoteDB db = new DummyNoteDB();

	ArrayList<SparkNote> exportList = new ArrayList<SparkNote>();
	Boolean isSelected = false;

	DrawerLayout drawer;
	ListView mainMenu;
	ActionBarDrawerToggle menuToggler;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		// Showing main note list
		// BEGIN
//		FragmentManager fm = getSupportFragmentManager();
//		FragmentTransaction transaction = fm.beginTransaction();
//		transaction.add(R.id.work_frame, new NoteListFragment());
//		transaction.commit();
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
		menuToggler = new ActionBarDrawerToggle(this, // родительская Activity
				drawer, // объект DrawerLayout
				R.drawable.actionbar_burger_item, // иконка панели для замены 'Up'
				R.string.main_menu_open, // описание "open drawer"
				R.string.main_menu_close // описание "close drawer"
		) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu(); // вызов onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu(); // вызов onPrepareOptionsMenu()
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		menu.removeItem(R.id.action_bar_confirm_item);
		menu.removeItem(R.id.action_bar_attach_item);
		menu.removeItem(R.id.action_bar_back_item);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Кнопки home/up ActionBar’a открывают/закрывают панель.
		// ActionBarDrawerToggle заботится об этом.
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
			Toast.makeText(this, "Не назначено", Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during onPostCreate()
	 * and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Синхронизируем состояние переключателя после onRestoreInstanceState
		menuToggler.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Передаем конфигурацию в drawer toggls
		menuToggler.onConfigurationChanged(newConfig);
	}

	private class SupportDrawerEventKeeper implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			enterToDrawerMenuPointBy(position);
		}

	}

	private void enterToDrawerMenuPointBy(int position) {
		Fragment fragment;
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
			fragment = new CreateFragment();
			break;
		default:
//			Toast.makeText(this, "List of notes is opened", Toast.LENGTH_SHORT).show();
			NoteListFragment noteListFragment = new NoteListFragment();
			noteListFragment.setAdapter(new UserNoteAdapter(this, db.getNotes()));
			fragment = noteListFragment;
			break;
		}

		// set new fragment
		getSupportFragmentManager().beginTransaction().replace(R.id.work_frame, fragment).commit();
		// refresh and close drawer menu
		mainMenu.setItemChecked(position, true);
		setTitle(getResources().getStringArray(R.array.main_menu_points)[position]);
		drawer.closeDrawer(mainMenu);
	}

	@Override
	public void openNote(int position) {
		SparkNote note = db.getNoteById(position);
		Bundle bundle = new Bundle();
		bundle.putString("title", note.getTitle());
		bundle.putString("content", note.getContent());
		bundle.putInt("position", position);
		CreateFragment fragment = new CreateFragment();
		fragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.work_frame, fragment).commit();
	}

	@Override
	public void save(int position, String title, String content, ArrayList<AttachItem> attaches) {
		Toast.makeText(this, "Doletelo v save activity", Toast.LENGTH_SHORT).show();
		db.updateNote(position, title, content);	
		enterToDrawerMenuPointBy(0);
	}
}
