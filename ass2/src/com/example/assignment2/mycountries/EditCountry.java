package com.example.assignment2.mycountries;

import java.util.List;

import com.example.assignment2.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

/**
 * Activity for editing country year and/or name
 * 
 * @author: Julia Bergmayr
 * @version: 1.1
 */

public class EditCountry extends Activity {

	private EditText editCountry;
	private EditText editYear;
	private DataSource ds;
	private int position;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_edit_country);

		Intent myIntent = getIntent();
		editYear = (EditText) findViewById(R.id.edit_year);
		editCountry = (EditText) findViewById(R.id.edit_country);
		int y = myIntent.getIntExtra("year", 0);
		editYear.setText(String.valueOf(y), TextView.BufferType.EDITABLE);
		editCountry.setText(myIntent.getStringExtra("country"), TextView.BufferType.EDITABLE);
		editYear.setTextSize(loadSizePref());
		editCountry.setTextSize(loadSizePref());

		Button btn = (Button) findViewById(R.id.button_done);
		btn.setTextColor(loadColorPref());
		btn.setTextSize(loadSizePref());

		position = myIntent.getIntExtra("pos", 0);

		View linLay = (LinearLayout) findViewById(R.id.edit_layout);
		loadPreferences(linLay);

	}

	/**
	 * load user preferences for text color
	 * 
	 * @return
	 */
	private int loadColorPref() {
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		String colorTextString = sp.getString(
				getResources().getString(R.string.prefs_text_color_key), "0");
		int colorText = Integer.parseInt(colorTextString);

		if (colorText == 1) {
			return getResources().getColor(R.color.white);
		} else if (colorText == 2) {
			return getResources().getColor(R.color.pink);
		} else if (colorText == 3) {
			return getResources().getColor(R.color.red);
		}
		return getResources().getColor(R.color.black);
	}

	/**
	 * load user preferences for text size
	 * 
	 * @return
	 */
	private float loadSizePref() {
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		String textSizeString = sp.getString(getResources().getString(R.string.pres_text_size_key),
				"16");
		return Integer.parseInt(textSizeString);
	}

	/**
	 * load user preferences for background color
	 * 
	 * @param view
	 */
	private void loadPreferences(View view) {
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		String backgroundColor = sp.getString(
				getResources().getString(R.string.prefs_back_color_key), "0");
		int colorBack = Integer.parseInt(backgroundColor);

		if (colorBack == 0) {
			view.setBackgroundResource(R.color.white);
		} else if (colorBack == 1) {
			view.setBackgroundResource(R.color.blue);
		} else if (colorBack == 2) {
			view.setBackgroundResource(R.color.black);
		} else if (colorBack == 3) {
			view.setBackgroundResource(R.color.green);
		} else if (colorBack == 4) {
			view.setBackgroundResource(R.color.red);
		} else if (colorBack == 5) {
			view.setBackgroundResource(R.color.yellow);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_country, menu);
		return true;
	}

	/**
	 * Handler for Button "Done" Validating Input-Values, Error Handling
	 */
	public void onDone(View view) {
		ds = new DataSource(this);
		ds.open();
		List<Visited> list = ds.getAllVisited();
		ds.deleteEntry(list.get(position));
		try {
			String year = editYear.getText().toString().trim();
			String country = editCountry.getText().toString().trim();
			if (Integer.parseInt(year) > 1920 && Integer.parseInt(year) < 2014
					&& !country.equalsIgnoreCase("")) {
				Intent reply = new Intent();
				reply.putExtra("year", year);
				reply.putExtra("country", country);
				reply.putExtra("pos", position);
				setResult(RESULT_OK, reply);
				finish();
			} else {
				Context context = getApplicationContext();
				CharSequence text = getResources().getString(R.string.correctInputsCountry);
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		} catch (NumberFormatException nfe) {
			Context context = getApplicationContext();
			CharSequence text = getResources().getString(R.string.bothValues);
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

}
