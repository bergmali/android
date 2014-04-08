package com.example.assignment2.mycountries;

import com.example.assignment2.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * PreferenceFragment for setting user specified colors and textSize
 * 
 * @author Lia
 * 
 */
public class MyPreferenceFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
}
