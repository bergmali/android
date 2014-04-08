package com.example.assignment3.incomingcalls;

/**
 * @author Lia
 * Helper Class for defining and creating Database
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "calls";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_NUMBER = "number";

	private static final String DATABASE_NAME = "number.db";
	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_CREATE = "create table " + TABLE_NAME + " (" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DATE + " text not null, "
			+ COLUMN_NUMBER + " text not null);";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DbHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
