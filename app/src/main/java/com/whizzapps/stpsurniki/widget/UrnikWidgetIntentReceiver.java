package com.whizzapps.stpsurniki.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.RotateAnimation;
import android.widget.RemoteViews;

import com.whizzapps.stpsurniki.R;
import com.whizzapps.stpsurniki.Utility;
import com.whizzapps.stpsurniki.database.DatabaseHandler;
import com.whizzapps.stpsurniki.database.SQLiteHelper;

import java.util.Calendar;

public class UrnikWidgetIntentReceiver extends BroadcastReceiver {
	
	RotateAnimation rotate;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(UrnikWidget.WIDGET_BUTTON)){
			Log.i("TAG", "Clicked");
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
			remoteViews.setOnClickPendingIntent(R.id.refresh, UrnikWidget.buildButtonPendingIntent(context));
			remoteViews.setTextViewText(R.id.trenutniPredmetWidget, getFromDb(context));
			UrnikWidget.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
		}
	}
	
	private String getFromDb(Context context){
		//Nastavljanje trenutnega predmeta
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String izbranRazred = settings.getString("oddelek", "null");
        String predmet1 = null;
        String predmet2 = null;
        String ucilnica1 = null;
        String ucilnica2 = null;
        String trenutno = "";
        
        if (izbranRazred.matches("null"))
        {
        	trenutno = "Razred ni izbran!";
        }
        else if (Utility.getCurrentHour() == -1)
        {
        	trenutno = "Ni pouka!";
        }
        else
        {
        	DatabaseHandler db = new DatabaseHandler(context);
        	Cursor c = db.getSubject(getDay(), Utility.getCurrentHour(), izbranRazred);
        	
        	if (c.moveToFirst())
        	{
        		predmet1 = c.getString(c.getColumnIndex(SQLiteHelper.PREDMET));
        		ucilnica1 = c.getString(c.getColumnIndex(SQLiteHelper.UCILNICA));
        		trenutno = trenutno + predmet1 + " " + ucilnica1;
        		if (c.moveToNext())
            	{
            		predmet2 = c.getString(c.getColumnIndex(SQLiteHelper.PREDMET));
            		ucilnica2 = c.getString(c.getColumnIndex(SQLiteHelper.UCILNICA));
            		trenutno = trenutno + " / " + predmet2 + " " + ucilnica2;
            	}
        	}
        	else
        	{
        		trenutno = "Ni pouka!";
        	}
        }
        
        return trenutno;
	}
	
	public String getDay(){
    	int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    	String trenutenDan = null;
    	
    	switch (day){
    	case 0:
    		trenutenDan = "Sobota";
    		break;
    	case 1:
    		trenutenDan = "Nedelja";
    		break;
    	case 2:
    		trenutenDan = "Ponedeljek";
    		break;
    	case 3:
    		trenutenDan = "Torek";
    		break;
    	case 4:
    		trenutenDan = "Sreda";
    		break;
    	case 5:
    		trenutenDan = "Četrtek";
    		break;
    	case 6:
    		trenutenDan = "Petek";
    		break;
    	}
		return trenutenDan;
    }


}
