package com.example.assignment2.alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database Helper Class
 * 
 * @author Lia
 * 
 */
public class DbHelper extends SQLiteOpenHelper {

	// necessary columns
	public static final String TABLE_NAME = "alarms";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_HOUR = "hour";
	public static final String COLUMN_MINUTE = "minute";
	public static final String COLUMN_ACTIVE = "active";
	public SQLiteDatabase sqldb;

	private static final String DATABASE_NAME = "alarms.db";
	private static final int DATABASE_VERSION = 9;

	// create statement
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME + " (" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_HOUR + " integer, " + COLUMN_MINUTE
			+ " integer, " + COLUMN_ACTIVE + " integer);";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		sqldb = db;
		System.out.println("calling DATABASE_CREATE");
		db.execSQL(DATABASE_CREATE);
	}

	/**
	 * Upgrade database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DbHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	/**
	 * check if open
	 * 
	 * @return
	 */
	public boolean isOpen() {
		if (sqldb != null)
			return sqldb.isOpen();
		else
			return false;
	}

}
