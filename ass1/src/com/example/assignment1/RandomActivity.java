/**
 * @author Lia
 * @version: 1.0
 */
package com.example.assignment1;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class RandomActivity extends Activity {
	TextView display;
	Random random = new Random();
	String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_random);

		display = (TextView) findViewById(R.id.textView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.random, menu);
		return true;
	}

	public void createRandom(View view) {
		// Creating Random number
		Integer t = (Integer) (random.nextInt(100) + 1);
		text = t.toString();
		display.setText(text);

	}
}
