package com.example.assignment2.alarmclock;

/**
 * Receiver for Alarms
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	/**
	 * create new intent for showing dialog when receiving alarm
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Alarm al = (Alarm) intent.getSerializableExtra("alarm");
		Intent in = new Intent(context, AlarmDialog.class);
		in.putExtra("alarm", al);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(in);

	}
}
