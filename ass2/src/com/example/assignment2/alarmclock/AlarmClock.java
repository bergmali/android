package com.example.assignment2.alarmclock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.assignment2.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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

/**
 * Main class of application, shows list of all alarms, option for adding new alarms
 * 
 * @author Lia
 */
public class AlarmClock extends Activity {

	private DataSource datasource;
	private List<Alarm> values;
	private TextView currTime;
	private ListView lv;
	private MyAdapter adapt;
	private AlarmManager manager;

	/**
	 * Creating application, initializing view and other objects
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		datasource = new DataSource(this);
		datasource.open();

		setContentView(R.layout.activity_alarm);

		currTime = (TextView) findViewById(R.id.currentTime);
		currTime.setText(getCurrentTime());

		values = datasource.getAllAlarm();
		adapt = new MyAdapter(this, R.layout.list_row_alarm, values);
		lv = (ListView) findViewById(android.R.id.list);
		TextView empty = (TextView) findViewById(R.id.empty);
		lv.setEmptyView(empty);
		lv.setAdapter(adapt);
		registerForContextMenu(lv);

	}

	/**
	 * overriting onResume to update time shown every 5 seconds
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// this part is taken from:
		// http://stackoverflow.com/questions/14814714/update-textview-every-second
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(5000); // 5 seconds
						// Thread.sleep(1000); // 1 second
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								currTime.setText(getCurrentTime());
							}
						});
					}
				} catch (InterruptedException e) {
				}
			}
		};
		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}

	/**
	 * Create context menu for entries that lets user delete, update and de-/activate the alarm
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == android.R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("" + values.get(info.position).getHour() + " "
					+ values.get(info.position).getMinute().toString());
			menu.add(0, 0, 0, getResources().getString(R.string.delete));
			menu.add(1, 1, 1, getResources().getString(R.string.update));
			if (values.get(info.position).isActive()) {
				menu.add(2, 2, 2, getResources().getString(R.string.deactivate));
			} else {
				menu.add(3, 3, 3, getResources().getString(R.string.activate));
			}
		}
	}

	/**
	 * actions for contextmenu
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case 0:
			Alarm al = values.get(info.position);
			datasource.deleteEntry(al);
			adapt.remove(al);
			unregisterAlarm(al);
			adapt.remove(al);
			break;
		case 1:
			Alarm al1 = values.get(info.position);
			adapt.remove(al1);
			Intent intent = new Intent(this, EditAlarm.class);
			intent.putExtra("alarm", al1);
			unregisterAlarm(al1);
			this.startActivityForResult(intent, 0);
			break;
		case 2:
			Alarm al2 = values.get(info.position);
			if (!al2.isActive()) {
				break;
			} else {
				al2.setActive(false);
				datasource.updateTask(al2.getId(), al2.getHour(), al2.getMinute(), 0);
				unregisterAlarm(al2);
				adapt.notifyDataSetChanged();
			}
			break;
		case 3:
			Alarm al3 = values.get(info.position);
			if (al3.isActive()) {
				break;
			} else {
				al3.setActive(true);
				datasource.updateTask(al3.getId(), al3.getHour(), al3.getMinute(), 1);
				registerAlarm(al3);
				adapt.notifyDataSetChanged();
			}
			break;
		}
		return true;
	}

	/**
	 * called after editing a task
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent result) {
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Alarm alarm = (Alarm) result.getSerializableExtra("alarm");

				boolean b = alarm.isActive();
				int active;
				if (b) {
					active = 1;
					registerAlarm(alarm);
				} else {
					active = 0;
				}

				datasource.open();
				datasource.updateTask(alarm.getId(), alarm.getHour(), alarm.getMinute(), active);
				datasource.close();

				adapt.add(alarm);
				adapt.notifyDataSetChanged();

			}
			break;
		case 1:
			if (resultCode == RESULT_OK) {
				Alarm al = (Alarm) result.getSerializableExtra("alarm");
				adapt.add(al);
				adapt.notifyDataSetChanged();
				registerAlarm(al);
			}
			break;

		}
	}

	/**
	 * calling activity to create new alarm
	 * 
	 * @param view
	 */
	public void makeAlarm(View view) {
		Intent intent = new Intent(this, AddAlarm.class);
		startActivityForResult(intent, 1);
	}

	/**
	 * registers broadcastreceiver for new alarm
	 * 
	 * @param a
	 */
	protected void registerAlarm(Alarm a) {
		if (a.isActive()) {
			int hour = Integer.parseInt(a.getHour());
			int min = Integer.parseInt(a.getMinute());
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, min);
			cal.set(Calendar.SECOND, 0);
			long time = cal.getTimeInMillis();
			Intent intent = new Intent(this, AlarmReceiver.class);
			intent.putExtra("alarm", a);
			PendingIntent pendInt = PendingIntent.getBroadcast(this, a.getId(), intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			manager = (AlarmManager) getSystemService(ALARM_SERVICE);
			manager.set(AlarmManager.RTC_WAKEUP, time, pendInt);
		}
	}

	/**
	 * unregisters broadcastreceiver (if inactive or deleted)
	 */
	protected void unregisterAlarm(Alarm a) {
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("alarm", a);
		PendingIntent pending = PendingIntent.getBroadcast(this, a.getId(), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		if (manager == null) {
			manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		}
		manager.cancel(pending);
	}

	/**
	 * @return current time as string
	 */
	public String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String d = sdf.format(date);
		return d;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public MyAdapter getAdapt() {
		return adapt;
	}

	public void setAdapt(MyAdapter adapt) {
		this.adapt = adapt;
	}

	/**
	 * Adapter for showing Alarm-List
	 * 
	 * @author Lia
	 */

	public class MyAdapter extends ArrayAdapter<Alarm> {

		public MyAdapter(Context context, int resource, List<Alarm> objects) {
			super(context, resource, objects);
		}

		@Override
		// Called when updating the ListView
		public View getView(int position, View convertView, ViewGroup parent) {
			Alarm alarm = getItem(position);
			View row;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.list_row_alarm, parent, false);
			} else {
				row = convertView;
			}

			TextView viewHour = (TextView) row.findViewById(R.id.show_hour);
			TextView viewMinute = (TextView) row.findViewById(R.id.show_minute);
			TextView divider = (TextView) row.findViewById(R.id.time_divider);
			viewHour.setText("" + values.get(position).getHour());
			viewMinute.setText(values.get(position).getMinute());
			if (alarm.isActive()) {
				viewHour.setTextColor(getResources().getColor(R.color.green));
				divider.setTextColor(getResources().getColor(R.color.green));
				viewMinute.setTextColor(getResources().getColor(R.color.green));
			} else {
				viewHour.setTextColor(getResources().getColor(R.color.red));
				divider.setTextColor(getResources().getColor(R.color.red));
				viewMinute.setTextColor(getResources().getColor(R.color.red));
			}

			return row;
		}

	}

}
