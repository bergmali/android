package com.example.assignment2.mycountries;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database actions
 * 
 * @author Lia
 * 
 */
public class DataSource {

	// Database fields
	private SQLiteDatabase database;
	private DbHelper dbHelper;
	private String[] allColumns = { DbHelper.COLUMN_ID, DbHelper.COLUMN_YEAR,
			DbHelper.COLUMN_COUNTRY };

	public DataSource(Context context) {
		dbHelper = new DbHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Visited createEntry(int year, String country) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_YEAR, year);
		values.put(DbHelper.COLUMN_COUNTRY, country);
		long insertId = database.insert(DbHelper.TABLE_NAME, null, values);
		Cursor cursor = database.query(DbHelper.TABLE_NAME, allColumns, DbHelper.COLUMN_ID + " = "
				+ insertId, null, null, null, null);
		cursor.moveToFirst();
		Visited newVis = cursorToVis(cursor);
		cursor.close();
		return newVis;
	}

	public void deleteEntry(Visited vis) {
		long id = vis.getId();
		System.out.println("Task deleted with id: " + id);
		database.delete(DbHelper.TABLE_NAME, DbHelper.COLUMN_ID + " = " + id, null);
	}

	public Visited getVis(long id) {
		String restrict = DbHelper.COLUMN_ID + "=" + id;
		Cursor cursor = database.query(true, DbHelper.TABLE_NAME, allColumns, restrict, null, null,
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Visited v = cursorToVis(cursor);
			return v;
		}
		// Make sure to close the cursor
		cursor.close();
		return null;
	}

	public boolean updateTask(long id, int year, String country) {
		ContentValues args = new ContentValues();
		args.put(DbHelper.COLUMN_YEAR, year);
		args.put(DbHelper.COLUMN_COUNTRY, country);

		String restrict = DbHelper.COLUMN_ID + "=" + id;
		return database.update(DbHelper.TABLE_NAME, args, restrict, null) > 0;
	}

	public List<Visited> getAllVisited() {
		List<Visited> visited = new ArrayList<Visited>();

		Cursor cursor = database.query(DbHelper.TABLE_NAME, allColumns, null, null, null, null,
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Visited v = cursorToVis(cursor);
			visited.add(v);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return visited;
	}

	private Visited cursorToVis(Cursor cursor) {
		Visited v = new Visited(cursor.getInt(1), cursor.getString(2));
		v.setId(cursor.getLong(0));
		return v;
	}
}
