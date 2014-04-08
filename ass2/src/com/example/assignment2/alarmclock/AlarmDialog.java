package com.example.assignment2.alarmclock;

import java.util.Calendar;

import com.example.assignment2.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Class for creating dialog when alarm goes off
 * 
 * @author Lia
 * 
 */
public class AlarmDialog extends Activity {

	private DataSource ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ds = new DataSource(getApplicationContext());

		final MediaPlayer mp = MediaPlayer.create(this, R.raw.railroad_crossing);
		mp.start();

		// Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		// final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
		// r.play();

		Intent intent = getIntent();
		final Alarm al = (Alarm) intent.getSerializableExtra("alarm");
		final int id = al.getId();
		final Alarm alarm = ds.getAl(id);
		final String hour = alarm.getHour();
		final String min = "" + (Integer.parseInt(alarm.getMinute()) + 5);

		if (alarm.isActive()) {
			/**
			 * Create alert dialog to cancel/snooze the alarm
			 */
			new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.alarm))
					.setMessage(getResources().getString(R.string.good_morning))
					.setPositiveButton(R.string.snooze, new DialogInterface.OnClickListener() {
						/**
						 * OnClickListener for snooze button -> set new alarm 5 mins from now
						 */
						public void onClick(DialogInterface dialog, int which) {
							alarm.setMinute(min);
							Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
							in.putExtra("alarm", alarm);
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
							cal.set(Calendar.MINUTE, Integer.parseInt(min));
							long time = cal.getTimeInMillis();
							PendingIntent pending = PendingIntent.getBroadcast(
									getApplicationContext(), alarm.getId(), in, 0);
							AlarmManager alman = (AlarmManager) getSystemService(ALARM_SERVICE);
							alman.set(AlarmManager.RTC_WAKEUP, time, pending);
							mp.stop();
							finish();
						}
					}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						/**
						 * OnClickListener for cancel button -> stop alarmsound
						 */
						public void onClick(DialogInterface dialog, int which) {
							mp.stop();
							finish();
						}
					}).setIcon(android.R.drawable.ic_lock_idle_alarm).show();
			super.onCreate(savedInstanceState);
		} else {
			finish();
		}
	}

}
