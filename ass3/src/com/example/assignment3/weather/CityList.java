package com.example.assignment3.weather;

import com.example.assignment3.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activity that let's user select city to get weather information for Will be started when widget
 * is placed on screen
 * 
 * @author Julia Bergmayr
 * @version 1.0
 */
public class CityList extends Activity {

	// List for available cities, can be extended
	private final String[] cities = { "Växjö", "Stockholm", "Copenhagen" };
	private ListView lv;
	private ArrayAdapter<String> adapt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_list);

		lv = (ListView) findViewById(R.id.citylist);
		adapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
		lv.setAdapter(adapt);

		lv.setOnItemClickListener(new OnItemClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city_list, menu);
		return true;
	}

	// starts intent with selected city
	public class OnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			String city = (String) lv.getItemAtPosition(arg2);
			Intent intent = new Intent(CityList.this, VaxjoWeather.class);
			intent.putExtra("city", city);

			startActivity(intent);
		}

	}
}
