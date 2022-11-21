package com.example.sparknotes;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	DrawerLayout drawer;
	ActionBarDrawerToggle menuToggler;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		// Showing main note list
		// BEGIN

		// END

		// Showing main note list
		// BEGIN
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.add(R.id.work_frame, new NoteListFragment());
		transaction.commit();
		// END

		// Showing main note list
		// BEGIN
		String[] menuPoints = getResources().getStringArray(R.array.main_menu_points);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ListView mainMenu = (ListView) findViewById(R.id.main_menu);
		mainMenu.setAdapter(new ArrayAdapter<String>(this, R.layout.item_main_menu, menuPoints));

		// Set back button to back on the one step in the stack
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Create object with Listener for button of actionBar which open or close
		// drawer menu
		menuToggler = new ActionBarDrawerToggle(this, // родительска€ Activity
				drawer, // объект DrawerLayout
				R.drawable.actionbar_burger_item, // иконка панели дл€ замены 'Up'
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
		// END

		// Showing main note list
		// BEGIN

		// END
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//  нопки home/up ActionBarТa открывают/закрывают панель.
		// ActionBarDrawerToggle заботитс€ об этом.
		if (menuToggler.onOptionsItemSelected(item)) {
			return true;
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
		// —инхронизируем состо€ние переключател€ после onRestoreInstanceState
		menuToggler.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// ѕередаем конфигурацию в drawer toggls
		menuToggler.onConfigurationChanged(newConfig);
	}

}
