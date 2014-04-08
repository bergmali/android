/**
 * @author: Julia Bergmayr
 * @version: 1.0
 */
package com.example.assignment1;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyCountries extends ListActivity {

	ArrayAdapter<Object> adapter;
	ListView visited;
	ArrayList<Visited> vis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// creating ActionBar
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.mycountries);
		visited = (ListView) findViewById(android.R.id.list);
		vis = new ArrayList<Visited>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_countries, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_country:
			Intent intent = new Intent(this, AddCountry.class);
			this.startActivityForResult(intent, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent result) {
		if (resultCode == RESULT_OK) {
			String country = result.getStringExtra("country");
			Integer year = Integer.parseInt(result.getStringExtra("year"));
			vis.add(new Visited(year, country));
			MyAdapter adapt = new MyAdapter(this);
			visited.setAdapter(adapt);

		}
	}

	/**
	 * Adapter for showing Country-List
	 * 
	 * @author Lia
	 */
	class MyAdapter extends BaseAdapter implements ListAdapter {
		Context context;
		ArrayList<Visited> countriesVisited = vis;

		public MyAdapter(Context context) {
			this.context = context;

		}

		@Override
		public int getCount() {
			return vis.size();
		}

		@Override
		public Object getItem(int position) {
			return vis.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		// Called when updating the ListView
		public View getView(int position, View convertView, ViewGroup parent) {
			View row;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.list_row, parent, false);
			} else {
				row = convertView;
			}

			TextView viewYear = (TextView) row.findViewById(R.id.show_year);
			TextView viewCountry = (TextView) row
					.findViewById(R.id.show_country);
			viewYear.setText("" + countriesVisited.get(position).getYear());
			viewCountry.setText(countriesVisited.get(position).getCountry());
			return row;
		}
	}

}
