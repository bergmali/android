package com.example.assignment3.weather;

import com.example.assignment3.R;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * Service for Updating the weather-data
 * 
 * @author Julia Bergmayr
 * @version 1.0
 */
public class UpdateService extends Service {

	private int appWidgetId;
	private String city;
	private RemoteViews views;
	private AppWidgetManager appWidgetManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// called when starting the service
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleCommand(intent);
		return START_STICKY; // service ends only when we tell it to
	}

	// responsible for updating weahter data for specific widget-instance
	private void handleCommand(Intent intent) {
		int[] widgetIds = intent.getIntArrayExtra("appWidgetIds");
		Context context = getBaseContext();

		final int N = widgetIds.length;
		for (int i = 0; i < N; i++) {
			this.appWidgetId = widgetIds[i];
			if (WeatherWidgetConfigure.loadBoolPref(context, appWidgetId) == true) {
				city = WeatherWidgetConfigure.loadCityPref(context, appWidgetId);
				views = new RemoteViews(context.getPackageName(), R.layout.widget);
				appWidgetManager = AppWidgetManager.getInstance(context);
				WeatherWidget ww = new WeatherWidget();
				ww.updateAppWidget(context, appWidgetManager, appWidgetId, city);
				ww.updateCity(city, context);
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		}
		stopSelf(); // service no longer needed
	}
}
