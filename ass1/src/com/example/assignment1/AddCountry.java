/**
 * @author: Julia Bergmayr
 * @version: 1.0
 */
package com.example.assignment1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddCountry extends Activity {

	EditText editYear;
	EditText editCountry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_country);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_country, menu);
		return true;
	}

	/*
	 * Handler for Button "Done" Validating Input-Values, Error Handling
	 */
	public void onDone(View view) {
		try {
			editYear = (EditText) findViewById(R.id.edit_year);
			editCountry = (EditText) findViewById(R.id.edit_country);
			String year = editYear.getText().toString().trim();
			String country = editCountry.getText().toString().trim();
			if (Integer.parseInt(year) > 1920 && Integer.parseInt(year) < 2014
					&& !country.equalsIgnoreCase("")) {
				Intent reply = new Intent();
				reply.putExtra("year", year);
				reply.putExtra("country", country);
				setResult(RESULT_OK, reply);
				finish();
			} else {
				Context context = getApplicationContext();
				CharSequence text = getResources().getString(
						R.string.correctInputsCountry);
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		} catch (IllegalStateException ise) {
			Context context = getApplicationContext();
			CharSequence text = getResources().getString(R.string.bothValues);
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} catch (NumberFormatException nfe) {
			Context context = getApplicationContext();
			CharSequence text = getResources().getString(R.string.bothValues);
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

}
