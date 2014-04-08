package com.example.assignment2.mycountries;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.assignment2.R;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Class for showing country-list Main Activity of application
 * 
 * @author: Julia Bergmayr
 * @version: 1.1
 */
public class MyCountries extends ListActivity {
	private static DataSource datasource;
	private List<Visited> values;
	private ListView lv;
	private MyAdapter adapt;
	private boolean sortYear = false;
	private boolean sortCountry = false;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// creating action bar
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		// getting preferences
		PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
		sp = PreferenceManager.getDefaultSharedPreferences(this);

		// connection to database
		datasource = new DataSource(this);
		datasource.open();
		values = datasource.getAllVisited();
		sortCountry = sp.getBoolean("sort_country", false);
		if (sortCountry) {
			Collections.sort(values, new MyCountryComparator());
		} else
			Collections.sort(values, new MyYearComparator());

		setContentView(R.layout.mycountries);

		// setting background color - preferences
		View view = findViewById(R.id.country_layout);
		loadBackgroundPreferences(view);

		// initializing list view and adapter
		lv = (ListView) findViewById(android.R.id.list);
		adapt = new MyAdapter(this, R.layout.list_row_countries, values);
		lv.setAdapter(adapt);
		registerForContextMenu(lv);
	}

	/**
	 * load user preferences for background color
	 * 
	 * @param view
	 */
	private void loadBackgroundPreferences(View view) {
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_countries, menu);
		return true;
	}

	/*
	 * Specifying actions for actionbar menue (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_country:
			Intent intent = new Intent(this, AddCountry.class);
			this.startActivityForResult(intent, 0);
			return true;
		case R.id.sort_year:
			Collections.sort(values, new MyYearComparator());
			if (adapt == null) {
				adapt = new MyAdapter(this, R.layout.list_row_countries, values);
			}
			adapt.notifyDataSetChanged();
			sortYear = true;

			sortCountry = false;
			Editor ed = sp.edit();
			ed.putBoolean("sort_year", true); // save sorting order
			ed.putBoolean("sort_country", false); // save sorting order
			ed.apply();

			return true;
		case R.id.sort_country:
			Collections.sort(values, new MyCountryComparator());
			if (adapt == null) {
				adapt = new MyAdapter(this, R.layout.list_row_countries, values);
			}
			adapt.notifyDataSetChanged();
			sortCountry = true;
			sortYear = false;
			Editor eddi = sp.edit();
			eddi.putBoolean("sort_year", false); // save sorting order
			eddi.putBoolean("sort_country", true); // save sorting order
			eddi.apply();
			return true;
		case R.id.prefs:
			Intent myIntent = new Intent(this,
					com.example.assignment2.mycountries.Preferences.class);
			this.startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// getting sorting preferences
		if (sp.getBoolean("sortYear", true)) {
			Collections.sort(values, new MyYearComparator());
			if (adapt == null) {
				adapt = new MyAdapter(this, R.layout.list_row_countries, values);
			}
			adapt.notifyDataSetChanged();
		} else if (sp.getBoolean("sortCountry", true)) {
			Collections.sort(values, new MyCountryComparator());
			if (adapt == null) {
				adapt = new MyAdapter(this, R.layout.list_row_countries, values);
			}
			adapt.notifyDataSetChanged();
		}
		adapt.notifyDataSetChanged();
		datasource.open();

	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	protected void onStop() {
		// saving preferences before quitting app
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("sortYear", sortYear);
		edit.putBoolean("sortCountry", sortCountry);
		edit.commit();
		edit.apply();
		super.onStop();
	}

	/**
	 * Creating context menue for listview items (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
	 * android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == android.R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("" + values.get(info.position).getYear() + " "
					+ values.get(info.position).getCountry().toString());
			menu.add(0, 0, 0, getResources().getString(R.string.delete));
			menu.add(1, 1, 1, getResources().getString(R.string.update));
		}
	}

	/**
	 * Specifying actions for context menue (non-Javadoc)
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getItemId() == 0) { // delete task
			Visited vis = values.get(info.position);
			datasource.deleteEntry(vis);
			adapt.remove(vis);
		} else if (item.getItemId() == 1) { // update task
			Visited vis = values.get(info.position);
			Intent intent = new Intent(this, com.example.assignment2.mycountries.EditCountry.class);
			intent.putExtra("year", vis.getYear());
			intent.putExtra("country", vis.getCountry());
			intent.putExtra("pos", info.position);
			this.startActivityForResult(intent, 1);
		}
		return true;
	}

	/**
	 * Specifying actions for activity results
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent result) {
		switch (requestCode) {
		case 0: // for adding countries
			if (resultCode == RESULT_OK) {
				String country = result.getStringExtra("country");
				Integer year = Integer.parseInt(result.getStringExtra("year"));
				if (!country.isEmpty()) {
					datasource.open();
					Visited vis = datasource.createEntry(year, country);
					adapt.add(vis);
					adapt.notifyDataSetChanged();
				}
			}
			break;
		case 1: // for editing countries
			if (resultCode == RESULT_OK) {
				String country = result.getStringExtra("country");
				Integer year = Integer.parseInt(result.getStringExtra("year"));
				if (!country.isEmpty()) {
					datasource.open();
					Visited vis = datasource.createEntry(year, country);
					int pos = result.getIntExtra("pos", 0);
					adapt.remove(values.get(pos)); // removing "old" entry
					adapt.add(vis); // adding new entry
					adapt.notifyDataSetChanged();
				}
			}
		}
	}

	/**
	 * Adapter for showing Country-List using the list_row layout
	 * 
	 * @author Lia
	 */

	public class MyAdapter extends ArrayAdapter<Visited> {

		public MyAdapter(Context context, int resource, List<Visited> objects) {
			super(context, resource, objects);
		}

		@Override
		// Called when updating the ListView
		public View getView(int position, View convertView, ViewGroup parent) {
			View row;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.list_row_countries, parent, false);
			} else {
				row = convertView;
			}
			TextView viewYear = (TextView) row.findViewById(R.id.show_year);
			TextView viewCountry = (TextView) row.findViewById(R.id.show_country);
			viewYear.setText("" + values.get(position).getYear());
			viewYear.setTextSize(getSizePref());
			viewYear.setTextColor(getFontColorPref());
			viewCountry.setText(values.get(position).getCountry());
			viewCountry.setTextSize(getSizePref());
			viewCountry.setTextColor(getFontColorPref());
			return row;
		}

	}

	/**
	 * get user preferences for text color
	 * 
	 * @return
	 */
	private int getFontColorPref() {
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
	 * get user preferences for text size
	 * 
	 * @return
	 */
	private float getSizePref() {
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		String textSizeString = sp.getString(getResources().getString(R.string.pres_text_size_key),
				"16");
		return Integer.parseInt(textSizeString);
	}

	/**
	 * Comparator Class for sorting countries alphabetically
	 * 
	 * @author Lia
	 * 
	 */
	public class MyCountryComparator implements Comparator<Visited> {

		@Override
		public int compare(Visited lhs, Visited rhs) {
			return lhs.compareTo(rhs);
		}

	}

	/**
	 * Comparator Class for sorting countries in time
	 * 
	 * @author Lia
	 * 
	 */
	public class MyYearComparator implements Comparator<Visited> {

		@Override
		public int compare(Visited lhs, Visited rhs) {
			return lhs.compareYears(rhs);
		}

	}

}
