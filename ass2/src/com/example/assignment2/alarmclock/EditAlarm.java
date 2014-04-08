package com.example.assignment2.alarmclock;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.assignment2.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditAlarm extends Activity {

	private TimePicker tp;
	private TextView currTime;
	private Alarm a;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_alarm);
		tp = (TimePicker) findViewById(R.id.timepicker);
		Intent intent = getIntent();
		a = (Alarm) intent.getSerializableExtra("alarm");
		String hour = a.getHour();
		String min = a.getMinute();
		currTime = (TextView) findViewById(R.id.currentTime);
		currTime.setText(getCurrentTime());
		tp.setCurrentHour(Integer.parseInt(hour));
		tp.setCurrentMinute(Integer.parseInt(min));
		tp.setIs24HourView(true);

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
		Button button = (Button) findViewById(R.id.setAlarm);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int h = (tp.getCurrentHour());
				int m = (tp.getCurrentMinute());
				a.setHour(String.valueOf(h));
				a.setMinute(String.valueOf(m));
				setAlarm(a);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_alarm, menu);
		return true;
	}

	/**
	 * registers broadcastreceiver for new alarm
	 * 
	 * @param a
	 */
	public void setAlarm(Alarm alarm) {
		Intent reply = new Intent();
		reply.putExtra("alarm", alarm);
		setResult(RESULT_OK, reply);
		finish();
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
