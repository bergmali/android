package com.example.assignment3.incomingcalls;

/**
 * @author Lia 
 * BroadcastReceiver for receiving incoming calls
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class IncomingCallsReceiver extends BroadcastReceiver {

	private DataSource ds;
	private String d;

	/**
	 * When an incoming call is detected, it gets saved into the database and Listview gets updated
	 * manually
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("onReceive - IncomingCalls"); // just for debugging

		Call call = null;
		Bundle bundle = intent.getExtras();
		String state = bundle.getString(TelephonyManager.EXTRA_STATE);

		if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
			String number = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			ds = new DataSource(context);
			ds.open();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = sdf.format(date);
			List<Call> allCalls = ds.getAllCalls();
			// if number already exists only update date of call
			// no duplicate entries in case of one number calling twice
			for (Call c : allCalls) {
				if (c.getNumber().equalsIgnoreCase(number)) {
					ds.deleteCall(c);
				}
			}
			call = ds.addCall(d, number);
			// update listview with new call
			CallLog.updateListView(call);
		}
	}
}
