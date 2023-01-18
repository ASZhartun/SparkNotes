package com.example.sparknotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class AppearanceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String MY_PREF = "MY_PREF";
	public static final String ACTION_PATH_KEY = "ACTION_PATH";
	Preference pref;
	String defaultPath;
	SharedPreferences sharedPreferences;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		pref = findPreference("pref_test");
		sharedPreferences = getSharedPreferences(MY_PREF, 0);
		defaultPath = sharedPreferences.getString(ACTION_PATH_KEY, Environment.getExternalStorageDirectory().getPath());
		pref.setSummary(defaultPath);
		pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(getApplicationContext(), FilePickerActivity.class);
				startActivityForResult(intent, 0);
				return false;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		defaultPath = data.getExtras().getString(FilePickerActivity.EXTRA_CURRENT_ROOT_DIRECTORY);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(ACTION_PATH_KEY, defaultPath);
		editor.commit();
		pref.setSummary(data.getExtras().getString(FilePickerActivity.EXTRA_CURRENT_ROOT_DIRECTORY,
				Environment.getExternalStorageDirectory().getPath()));
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub

	}

}
