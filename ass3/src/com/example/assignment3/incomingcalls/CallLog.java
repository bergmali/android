package com.example.assignment3.incomingcalls;

/**
 * @author Lia
 * @version 1.0
 * Class to show list of incoming calls
 */
import java.util.List;

import com.example.assignment3.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

public class CallLog extends Activity {

	private ListView lv;
	private static MyAdapter adapt;
	private static DataSource db;
	private static List<Call> calls;

	/**
	 * Initialising view, database and other required fields
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_call_log);

		db = new DataSource(this);
		db.open();
		calls = db.getAllCalls();

		lv = (ListView) findViewById(android.R.id.list);
		adapt = new MyAdapter(this, R.layout.list_row_calls, calls);
		TextView empty = (TextView) findViewById(R.id.empty);
		lv.setEmptyView(empty);
		lv.setAdapter(adapt);
		registerForContextMenu(lv);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call_log, menu);
		return true;
	}

	/**
	 * Create context menu to let user delete, call or send numbers via message
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == android.R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("" + calls.get(info.position).getNumber() + " "
					+ calls.get(info.position).getDate().toString());
			menu.add(0, 0, 0, getResources().getString(R.string.delete));
			menu.add(1, 1, 1, getResources().getString(R.string.send_message));
			menu.add(2, 2, 2, getResources().getString(R.string.call));
		}
	}

	/**
	 * Handling context-item selected calls
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case 0: // delete item
			Call c = adapt.getItem(info.position);
			db.open();
			db.deleteCall(c);
			db.close();
			adapt.remove(c);
			break;
		case 1: // send message
			String number = calls.get(info.position).getNumber();
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_TEXT, number);
			try {
				startActivity(Intent.createChooser(i, getResources().getString(R.string.send_via)));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(CallLog.this, getResources().getString(R.string.no_messaging_app),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case 2: // call number
			String n = "tel:" + calls.get(info.position).getNumber().trim();
			Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(n));
			startActivity(in);
			break;
		default:
			return super.onContextItemSelected(item);
		}

		return true;
	}

	/**
	 * Adapter Class for showing Call objects in ListView
	 * 
	 * @author Lia
	 * 
	 */
	public class MyAdapter extends ArrayAdapter<Call> {

		public MyAdapter(Context context, int resource, List<Call> objects) {
			super(context, resource, objects);
		}

		@Override
		// Called when updating the ListView
		public View getView(int position, View convertView, ViewGroup parent) {
			View row;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.list_row_calls, parent, false);
			} else {
				row = convertView;
			}

			TextView viewDate = (TextView) row.findViewById(R.id.show_date);
			TextView viewNumber = (TextView) row.findViewById(R.id.show_number);
			viewDate.setText(calls.get(position).getDate());
			viewNumber.setText(calls.get(position).getNumber());
			return row;
		}

	}

	/**
	 * method for updating ListView with specific Call
	 * 
	 * @param call
	 */
	public static void updateListView(Call call) {
		List<Call> callList = db.getAllCalls();
		adapt.clear();
		adapt.addAll(callList);
		adapt.notifyDataSetChanged();
	}
}
