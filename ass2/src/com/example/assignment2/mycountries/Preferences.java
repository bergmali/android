package com.example.assignment2.mycountries;

import java.util.List;

import com.example.assignment2.R;

import android.preference.PreferenceActivity;

/**
 * Load Preference Header
 * 
 * @author Lia
 * 
 */
public class Preferences extends PreferenceActivity { // implements OnSharedPreferenceChangeListener

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
}