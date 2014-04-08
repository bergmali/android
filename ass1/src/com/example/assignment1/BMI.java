/**
 * @author: Julia Bergmayr
 * @version: 1.0
 */
package com.example.assignment1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BMI extends Activity {

	TextView display;
	String height = "";
	String weight = "";
	EditText editHeight;
	EditText editWeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bmi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bmi, menu);
		return true;
	}

	/**
	 * Handler for Button "Compute" Reading Values, Validation of input
	 * calculation for BMI
	 * 
	 * @param view
	 */
	public void calculateBMI(View view) {
		display = (TextView) findViewById(R.id.bmi);
		try {
			editHeight = ((EditText) findViewById(R.id.edit_height));
			editWeight = ((EditText) findViewById(R.id.edit_weight));
			height = editHeight.getText().toString().trim();
			weight = editWeight.getText().toString().trim();
			float h = Float.parseFloat(height);
			if (h <= 0 || h >= 250) {
				String text = getResources().getString(R.string.correctHeight);
				int duration = 3;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
			}
			float w = Float.parseFloat(weight);
			if (w <= 0 || w >= 300) {
				String text = getResources().getString(R.string.correctWeight);
				int duration = 3;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
			} else {
				h = h / 100;
				Float bmi = w / (h * h);
				if (bmi < 18.5) {
					display.setText(getResources().getString(
							R.string.underweight)
							+ " \n" + bmi.toString());
				} else if (bmi >= 18.5 && bmi <= 25.0) {
					display.setText(getResources().getString(
							R.string.normal_weight)
							+ " \n" + bmi.toString());
				} else if (bmi > 25.0) {
					display.setText(getResources().getString(
							R.string.overweight)
							+ " \n" + bmi.toString());
				}
			}

		} catch (NumberFormatException nfe) {

			String text = "Please enter numbers only";
			int duration = 3;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
		editHeight.setText("");
		editWeight.setText("");
	}

	/**
	 * Clearing values on pushing Button "Reset"
	 * 
	 * @param view
	 */
	public void reset(View view) {
		editHeight.setText("");
		editWeight.setText("");
		display.setText("");
	}

}
