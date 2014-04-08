package com.example.assignment1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class ColorDisplay extends Activity {
	EditText editRed;
	EditText editGreen;
	EditText editBlue;
	View myLayout;
	String hex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_display);
		myLayout = (View) findViewById(R.id.RelLay);
		if (savedInstanceState != null) {
			// Convert hex-String to RGB-values
			String color = savedInstanceState.getString("color");
			Integer r = Integer.valueOf(color.substring(1, 3), 16);
			Integer g = Integer.valueOf(color.substring(3, 5), 16);
			Integer b = Integer.valueOf(color.substring(5, 7), 16);
			myLayout.setBackgroundColor(Color.argb(255, r, g, b));

		} else {
			myLayout.setBackgroundColor(R.color.background);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstance) {
		// saving current color in hex-String
		super.onSaveInstanceState(savedInstance);
		savedInstance.putString("color", hex);
		System.out.println(hex);
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.color_display, menu);
		return true;
	}

	/**
	 * Handler for Button "Display Color" Error handling in case of wrong input
	 * 
	 * @param view
	 */
	public void displayColor(View view) {
		try {
			// reading Values
			editRed = (EditText) findViewById(R.id.edit_red);
			editGreen = (EditText) findViewById(R.id.edit_green);
			editBlue = (EditText) findViewById(R.id.edit_blue);
			String red = editRed.getText().toString().trim();
			String green = editGreen.getText().toString().trim();
			String blue = editBlue.getText().toString().trim();
			Integer r = Integer.parseInt(red);
			Integer g = Integer.parseInt(green);
			Integer b = Integer.parseInt(blue);
			// setting hex-String (stackoverflow.com)
			hex = String.format("#%02x%02x%02x", r, g, b);
			if (r <= 255 && b <= 255 && g <= 255) {
				myLayout.setBackgroundColor(Color.argb(255, r, g, b));
				editRed.setText("");
				editGreen.setText("");
				editBlue.setText("");
			} else {
				String text = getResources().getString(R.string.enterNumbers);
				int duration = 3;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
			}
		} catch (NumberFormatException nfe) {
			String text = getResources().getString(R.string.correctHeight);
			;
			int duration = 3;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
	}

}
