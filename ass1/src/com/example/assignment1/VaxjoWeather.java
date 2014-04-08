/**
 * VaxjoWeather.java
 * Created: May 9, 2010
 * Jonas Lundberg, LnU
 */

package com.example.assignment1;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is a first prototype for a weather app. It is currently only downloading
 * weather data for Växjö.
 * 
 * This activity downloads weather data and constructs a WeatherReport, a data
 * structure containing weather data for a number of periods ahead.
 * 
 * The WeatherHandler is a SAX parser for the weather reports (forecast.xml)
 * produced by www.yr.no. The handler constructs a WeatherReport containing meta
 * data for a given location (e.g. city, country, last updated, next update) and
 * a sequence of WeatherForecasts. Each WeatherForecast represents a forecast
 * (weather, rain, wind, etc) for a given time period.
 * 
 * The next task is to construct a list based GUI where each row displays the
 * weather data for a single period.
 * 
 * 
 * @author jlnmsi
 * 
 */

public class VaxjoWeather extends ListActivity {
	private WeatherReport report = null;
	ListView lv = null;
	ArrayList<WeatherForecast> fc = new ArrayList<WeatherForecast>();
	Context context = this;
	ListAdapter adapt = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * check if airplane mode is off - internet connection needed
		 */
		if (!isAirplaneModeOn(getApplicationContext())) {
			try {
				URL url = new URL(
						"http://www.yr.no/sted/Sverige/Kronoberg/V%E4xj%F6/forecast.xml");
				new WeatherRetriever().execute(url);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			setContentView(R.layout.weather_main);
			lv = (ListView) findViewById(android.R.id.list);
			adapt = new MyAdapter(this);
		} else {
			String text = getResources().getString(R.string.noInternet);
			int duration = 3;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			System.out.println(text);
		}
	}

	// checking if airplanemode is on
	@SuppressWarnings("deprecation")
	private static boolean isAirplaneModeOn(Context context) {

		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;

	}

	private void PrintReportToConsole() {
		if (this.report != null) {
			/* Print location meta data */
			System.out.println(report);

			/* Print forecasts */
			int count = 0;
			for (WeatherForecast forecast : report) {
				count++;
				System.out.println("Forecast " + count);
				System.out.println(forecast.toString());

			}
		} else {
			System.out.println("Weather report has not been loaded.");
		}
	}

	private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport> {
		protected WeatherReport doInBackground(URL... urls) {
			try {
				return WeatherHandler.getWeatherReport(urls[0]);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		protected void onProgressUpdate(Void... progress) {

		}

		// adding forecasts to list, setting listadapter
		protected void onPostExecute(WeatherReport result) {
			report = result;
			if (report != null) {
				for (WeatherForecast forecast : report) {
					fc.add(forecast);
					lv.setAdapter(adapt);
				}
			} else {
				String text = "No weather report found";
				int duration = 1;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			PrintReportToConsole();
			System.out.println("FC: " + fc.toString());
		}
	}

	/**
	 * New Adapter Class for row-layout in WeatherApp
	 * 
	 * @author Lia
	 * 
	 */
	class MyAdapter extends BaseAdapter implements ListAdapter {
		Context context;
		ArrayList<WeatherForecast> forecasts;

		public MyAdapter(Context context) {
			this.context = context;
			this.forecasts = fc;
		}

		@Override
		public int getCount() {
			return forecasts.size();
		}

		@Override
		public Object getItem(int position) {
			return forecasts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		// Called when updating the ListView
		public View getView(int position, View convertView, ViewGroup parent) {
			View row;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.row_layout, parent, false);
			} else {
				row = convertView;
			}

			// setting image - dependent on weather-forecast
			ImageView image = (ImageView) row.findViewById(R.id.symbol);
			int imageResource = 0;

			switch (forecasts.get(position).getWeatherCode()) {
			case (1):
				imageResource = R.drawable.sunny;
			case (2):
				imageResource = R.drawable.fair;
				break;
			case (3):
				imageResource = R.drawable.partly_cloudy;
				break;
			case (4):
				imageResource = R.drawable.cloudy;
				break;
			case (5):
				imageResource = R.drawable.rain_showers;
				break;
			case (6):
				imageResource = R.drawable.rain_showers_thunder;
				break;
			case (7):
				imageResource = R.drawable.sleet_showers;
				break;
			case (8):
				imageResource = R.drawable.snow_showers;
				break;
			case (9):
				imageResource = R.drawable.rain;
				break;
			case (10):
				imageResource = R.drawable.heavy_rain;
				break;
			case (11):
				imageResource = R.drawable.rain_and_thunder;
				break;
			case (12):
				imageResource = R.drawable.sleet;
				break;
			case (13):
				imageResource = R.drawable.snow;
				break;
			case (14):
				imageResource = R.drawable.snow_thunder;
				break;
			case (15):
				imageResource = R.drawable.fog;
				break;
			case (20):
				imageResource = R.drawable.sleet_thunger;
				break;
			case (21):
				imageResource = R.drawable.snow_shower_thunder;
				break;
			case (22):
				imageResource = R.drawable.rain_thunder;
				break;
			case (23):
				imageResource = R.drawable.sleet_and_thunder;
				break;
			default:
				imageResource = R.drawable.fair;
			}

			Drawable res = getResources().getDrawable(imageResource);
			image.setImageDrawable(res);

			TextView start = (TextView) row.findViewById(R.id.view_start);
			TextView endTime = (TextView) row.findViewById(R.id.end_time);
			TextView temp = (TextView) row.findViewById(R.id.temp);
			TextView wind = (TextView) row.findViewById(R.id.wind);
			TextView rain = (TextView) row.findViewById(R.id.rain);
			// Retrieving necessary information from forecast-Object
			start.setText(forecasts.get(position).getStartYYMMDD().toString()
					+ " to "
					+ forecasts.get(position).getEndYYMMDD().toString());
			endTime.setText(forecasts.get(position).getStartHHMM() + " to "
					+ forecasts.get(position).getEndHHMM());
			temp.setText(getResources().getString(R.string.temp)
					+ forecasts.get(position).getTemp() + "�C");
			wind.setText(getResources().getString(R.string.wind)
					+ forecasts.get(position).getWindSpeed() + "km/h");
			rain.setText(getResources().getString(R.string.rain)
					+ forecasts.get(position).getRain() + "mm/h");
			return row;
		}

	}
}
