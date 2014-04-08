package com.example.assignment3.incomingcalls;

/**
 * @author Lia
 * Class for database-actions
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

	// Database fields
	private SQLiteDatabase database;
	private DbHelper dbHelper;
	private String[] allColumns = { DbHelper.COLUMN_ID, DbHelper.COLUMN_DATE,
			DbHelper.COLUMN_NUMBER };

	public DataSource(Context context) {
		System.out.println("Datasource constr.");
		dbHelper = new DbHelper(context);
	}

	/**
	 * opens database connection
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Close database connection
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Add a new Call to database
	 * 
	 * @param date
	 * @param number
	 * @return new Call object
	 */
	public Call addCall(String date, String number) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_DATE, date); // Date of call
		values.put(DbHelper.COLUMN_NUMBER, number); // Phone Number
		long insertId = database.insert(DbHelper.TABLE_NAME, null, values);
		Cursor cursor = database.query(DbHelper.TABLE_NAME, allColumns, DbHelper.COLUMN_ID + " = "
				+ insertId, null, null, null, null);
		cursor.moveToFirst();
		Call c = cursorToCall(cursor);
		cursor.close();
		return c;
	}

	/**
	 * Delete Call entry
	 */
	public void deleteCall(Call c) {
		long id = c.getId();
		System.out.println("DS - Call deleted with id: " + id);
		database.delete(DbHelper.TABLE_NAME, DbHelper.COLUMN_ID + " = " + id, null);
	}

	/**
	 * Update call
	 * 
	 * @param id
	 * : Id of call to be updated
	 * @param date
	 * : new date
	 * @param number
	 * : number of call to be updated
	 * @return
	 */
	public boolean updateCall(long id, String date, String number) {
		ContentValues args = new ContentValues();
		args.put(DbHelper.COLUMN_DATE, date);
		args.put(DbHelper.COLUMN_NUMBER, number);

		String restrict = DbHelper.COLUMN_ID + "=" + id;
		return database.update(DbHelper.TABLE_NAME, args, restrict, null) > 0;
	}

	/**
	 * Cursor to Call Here the new Call-object is created
	 * 
	 * @param cursor
	 * @return
	 */
	private Call cursorToCall(Cursor cursor) {
		Call c = new Call();
		c.setId(cursor.getInt(0));
		c.setDate(cursor.getString(1));
		c.setNumber(cursor.getString(2));
		return c;
	}

	/**
	 * Returns call with specific id
	 * 
	 * @param id
	 * @return
	 */
	public Call getCall(int id) {
		System.out.println("DS - getCall");
		String restrict = DbHelper.COLUMN_ID + "=" + id;
		Cursor cursor = database.query(true, DbHelper.TABLE_NAME, allColumns, restrict, null, null,
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Call c = cursorToCall(cursor);
			return c;
		}
		// Make sure to close the cursor
		cursor.close();
		return null;
	}

	/**
	 * Returns all Calls currently saved in the database
	 * 
	 * @return
	 */
	public List<Call> getAllCalls() {
		System.out.println("DS - getAllCalls");
		List<Call> callList = new ArrayList<Call>();
		Cursor cursor = database.query(DbHelper.TABLE_NAME, allColumns, null, null, null, null,
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Call c = cursorToCall(cursor);
			callList.add(c);
			cursor.moveToNext();
		}
		cursor.close();
		return callList;
	}

}
