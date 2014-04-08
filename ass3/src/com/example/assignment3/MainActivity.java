package com.example.assignment3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.assignment3.incomingcalls.CallLog;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {

	private List<String> activities = new ArrayList<String>();
	private Map<String, Class> name2class = new HashMap<String, Class>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Add Activities to list */
		setup_activities();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.main_list_item, activities));

		/* Attach list item listener */
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClick());
	}

	/* Private Help Entities */
	private class OnItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			/* Find selected activity */
			String activity_name = activities.get(position);
			Class<?> activity_class = name2class.get(activity_name);

			/* Start new Activity */
			Intent intent = new Intent(MainActivity.this, activity_class);
			MainActivity.this.startActivity(intent);
		}
	}

	private void setup_activities() {
		addActivity(getResources().getString(R.string.title_activity_call_log),
				com.example.assignment3.incomingcalls.CallLog.class);
	}

	private void addActivity(String name, Class<CallLog> activity) {
		activities.add(name);
		name2class.put(name, activity);
	}
}