package com.example.assignment2.alarmclock;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class for Database-Actions
 * 
 * @author Lia
 * 
 */
public class DataSource {

	// Database fields
	private SQLiteDatabase database;
	private DbHelper dbHelper;
	private String[] allColumns = { DbHelper.COLUMN_ID, DbHelper.COLUMN_HOUR,
			DbHelper.COLUMN_MINUTE, DbHelper.COLUMN_ACTIVE };

	/**
	 * Creating DatabaseHelper
	 * 
	 * @param context
	 */
	public DataSource(Context context) {
		dbHelper = new DbHelper(context);
	}

	/**
	 * Open database
	 */
	public void open() throws SQLException {
		if (dbHelper != null && !dbHelper.isOpen())
			database = dbHelper.getWritableDatabase();
	}

	/**
	 * close Database
	 */
	public void close() {
		if (dbHelper != null && dbHelper.isOpen())
			dbHelper.close();
	}

	/**
	 * Create a new database entry
	 * 
	 * @param h
	 * @param min
	 * @return alarm-object
	 */
	public Alarm createEntry(int h, int min) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_HOUR, h);
		values.put(DbHelper.COLUMN_MINUTE, min);
		values.put(DbHelper.COLUMN_ACTIVE, 1);
		open();
		long insertId = database.insert(DbHelper.TABLE_NAME, null, values);
		Cursor cursor = database.query(DbHelper.TABLE_NAME, allColumns, DbHelper.COLUMN_ID + " = "
				+ insertId, null, null, null, null);
		cursor.moveToFirst();
		Alarm newAl = cursorToAl(cursor);
		cursor.close();
		close();
		return newAl;
	}

	/**
	 * Delete specified entry from database
	 * 
	 * @param al
	 * = alarm to delete
	 */
	public void deleteEntry(Alarm al) {
		long id = al.getId();
		System.out.println("Task deleted with id: " + id);
		open();
		database.delete(DbHelper.TABLE_NAME, DbHelper.COLUMN_ID + " = " + id, null);
		close();
	}

	/**
	 * get Alarm
	 * 
	 * @param id
	 * @return alarm with id
	 */
	public Alarm getAl(long id) {
		String restrict = DbHelper.COLUMN_ID + "=" + id;
		open();
		Cursor cursor = database.query(true, DbHelper.TABLE_NAME, allColumns, restrict, null, null,
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Alarm v = cursorToAl(cursor);
			return v;
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return null;
	}

	/**
	 * update task
	 * 
	 * @param id
	 * @param hour
	 * @param minute
	 * @param active
	 * @return
	 */
	public boolean updateTask(long id, String hour, String minute, int active) {
		ContentValues args = new ContentValues();
		args.put(DbHelper.COLUMN_HOUR, Integer.valueOf(hour));
		args.put(DbHelper.COLUMN_MINUTE, Integer.valueOf(minute));
		args.put(DbHelper.COLUMN_ACTIVE, active);

		open();
		String restrict = DbHelper.COLUMN_ID + "=" + id;
		boolean b = database.update(DbHelper.TABLE_NAME, args, restrict, null) > 0;
		close();
		return b;
	}

	/**
	 * @return all alarms of database
	 */
	public List<Alarm> getAllAlarm() {
		List<Alarm> alarms = new ArrayList<Alarm>();
		open();
		Cursor cursor = database.query(DbHelper.TABLE_NAME, allColumns, null, null, null, null,
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Alarm v = cursorToAl(cursor);
			alarms.add(v);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return alarms;
	}

	/**
	 * @param cursor
	 * @return
	 */
	private Alarm cursorToAl(Cursor cursor) {
		Alarm a = new Alarm();
		a.setId(cursor.getInt(0));
		a.setHour(String.valueOf(cursor.getInt(1)));
		a.setMinute(String.valueOf(cursor.getInt(2)));
		if (cursor.getInt(3) == 0) {
			a.setActive(false);
		} else if (cursor.getInt(3) == 1) {
			a.setActive(true);
		}
		return a;
	}
}
