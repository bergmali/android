package com.example.assignment2.alarmclock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.assignment2.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Class for creating a new alarm
 * 
 * @author Lia
 * 
 */
public class AddAlarm extends Activity {

	private TimePicker tp;
	private TextView currTime;
	private DataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_alarm);

		datasource = new DataSource(this);

		currTime = (TextView) findViewById(R.id.currentTime);
		currTime.setText(getCurrentTime());
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

		tp = (TimePicker) findViewById(R.id.timepicker);
		tp.setIs24HourView(true);
		tp.setCurrentHour(0);
		tp.setCurrentMinute(0);

		t.start();
		Button button = (Button) findViewById(R.id.setAlarm);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int h = (tp.getCurrentHour());
				int m = (tp.getCurrentMinute());
				long diff = calculateTimeDifference(h, m);
				datasource.open();
				Alarm al = datasource.createEntry(h, m);
				datasource.close();
				makeToasts(diff);
				makeReplyIntent(al);

			}
		});
	}

	/**
	 * creates reply-Intent
	 * 
	 * @param h
	 * @param m
	 */
	protected void makeReplyIntent(Alarm a) {
		Intent reply = new Intent();
		reply.putExtra("alarm", a);
		setResult(RESULT_OK, reply);
		finish();

	}

	/**
	 * make toast to when alarm will go off
	 * 
	 * @param diff
	 */
	protected void makeToasts(long diff) {
		long d;
		long h;
		long m;
		long s;
		d = diff / (24 * 60 * 60 * 1000);
		h = (diff % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
		m = (diff % (60 * 60 * 1000)) / (60 * 1000);
		s = (diff % (60 * 1000)) / (1000);
		if (d > 0) {
			Toast.makeText(
					AddAlarm.this,
					"Alarm goes off in " + d + "days, " + h + " hours, " + m + "minutes and " + s
							+ "seconds.", Toast.LENGTH_LONG).show();
		} else if (h > 0) {
			Toast.makeText(AddAlarm.this,
					"Alarm goes off in " + h + " hours, " + m + "minutes and " + s + "seconds.",
					Toast.LENGTH_LONG).show();
		} else if (m > 0) {
			Toast.makeText(AddAlarm.this,
					"Alarm goes off in " + m + " minutes and " + s + "seconds.", Toast.LENGTH_LONG)
					.show();
		} else
			Toast.makeText(AddAlarm.this, "Alarm goes off in " + s + " seconds", Toast.LENGTH_LONG)
					.show();

	}

	/**
	 * Calculating time difference between set alarm and actual time
	 * 
	 * @param h
	 * @param m
	 * @return
	 */
	protected long calculateTimeDifference(int h, int m) {
		long diff;
		Calendar cal = GregorianCalendar.getInstance(); // Date for Alarm
		Calendar c = GregorianCalendar.getInstance(); // Date now
		cal.set(Calendar.HOUR_OF_DAY, h); // setting hour of Alarm
		cal.set(Calendar.MINUTE, m);// setting minute of alarm
		if (cal.before(c)) { // if alarm in future set day to next
			cal.set(Calendar.DAY_OF_YEAR, (cal.get(Calendar.DAY_OF_YEAR) + 1));

		}
		diff = cal.getTimeInMillis() - c.getTimeInMillis(); // calculate
															// difference

		return diff;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_alarm, menu);
		return true;
	}

	/**
	 * returns current time in string format
	 * 
	 * @return
	 */
	public String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String d = sdf.format(date);
		return d;
	}

}
