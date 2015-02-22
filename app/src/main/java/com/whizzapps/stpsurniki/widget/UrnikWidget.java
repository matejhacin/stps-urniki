package com.whizzapps.stpsurniki.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.whizzapps.stpsurniki.R;

public class UrnikWidget extends AppWidgetProvider {
	
	public static String WIDGET_BUTTON = "com.whizzapps.stpsurniki.widget_refresh_button";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
		remoteViews.setOnClickPendingIntent(R.id.refresh, buildButtonPendingIntent(context));
		
		pushWidgetUpdate(context, remoteViews);
	}
	
	public static PendingIntent buildButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction(WIDGET_BUTTON);
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, UrnikWidget.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}
}
