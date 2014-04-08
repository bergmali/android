package com.example.assignment3.weather;

import com.example.assignment3.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * Widget Activity, only loads layout
 * 
 * @author Julia Bergmayr
 * @version 1.0
 */
public class WidgetActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.row_layout);
	}
}
