package com.whizzapps.stpsurniki;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.whizzapps.stpsurniki.database.DatabaseHandler;
import com.whizzapps.stpsurniki.database.SQLiteHelper;

import java.util.Calendar;

public class Utility {
	
	//Metoda s katero preverim, �e ima telefon povezavo do interneta
	public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
	
	//Metoda, ki prika�e sporo�ilo, �e telefon nima dostopa do interneta
	public static void showNoNetworkMessage(Context context){
    	AlertDialog.Builder noNetworkError = new AlertDialog.Builder(context);
    	noNetworkError.setTitle("Pozor");
    	noNetworkError.setMessage("Za nadaljevanje potrebuješ internetno povezavo.");
    	noNetworkError.setPositiveButton("V redu", null);
    	noNetworkError.show();
    }

    //Metoda, ki prikaže sporočilo, če uporabnik želi posodobiti URL-je brez internetne povezave
    public static void downloadingURLSwithoutNetwork(Context context)
    {
        AlertDialog.Builder noNetworkError = new AlertDialog.Builder(context);
        noNetworkError.setTitle("Pozor");
        noNetworkError.setMessage("Za pravilno delovanje aplikacije potrebuješ internetno povezavo vsaj ob prvotnem zagonu. Prosim, če aplikacijo zaženeš še enkrat, ko boš priključen/a na internet.");
        noNetworkError.setPositiveButton("V redu", null);
        noNetworkError.show();
    }
	
	//Metoda s katero pridobimo trenutno uro
	public static int getCurrentHour(){
		Calendar cal = Calendar.getInstance();
		int trenutnaUra = -1;
		int min = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		
		if (hour == 7 && min < 45)
		{
			trenutnaUra = 1;
		}
		else if ((hour == 7 && min >= 45) || (hour == 8 && min < 35))
		{
			trenutnaUra = 2;
		}
		else if ((hour == 8 && min >= 35) || (hour == 9 && min < 25))
		{
			trenutnaUra = 3;
		}
		else if ((hour == 9 && min >= 25) || (hour == 10 && min < 40))
		{
			trenutnaUra = 4;
		}
		else if ((hour == 10 && min >= 40) || (hour == 11 && min < 30))
		{
			trenutnaUra = 5;
		}
		else if ((hour == 11 && min >= 30) || (hour == 12 && min < 20))
		{
			trenutnaUra = 6;
		}
		else if ((hour == 12 && min >= 20) || (hour == 13 && min < 10))
		{
			trenutnaUra = 7;
		}
		else if (hour == 13 && min >= 10)
		{
			trenutnaUra = 8;	
		}
		else if (hour == 14 && min < 50)
		{
			trenutnaUra = 9;
		}
		else if ((hour == 14 && min >= 50) || (hour == 15 && min < 40))
		{
			trenutnaUra = 10;
		}
		else if ((hour == 15 && min >= 40) || (hour == 16 && min < 30))
		{
			trenutnaUra = 11;
		}
		
		return trenutnaUra;
	}

    public static String getClassNameFromChildAndHeader(Context context, int headerPosition, int childPosition)
    {
        DatabaseHandler db = new DatabaseHandler(context);
        int i = 0;
        headerPosition++;
        Cursor c = db.getUrls(headerPosition);
        c.moveToFirst();
        while (i < childPosition)
        {
            c.moveToNext();
            i++;
        }
        return c.getString(c.getColumnIndex(SQLiteHelper.RAZRED));
    }
}
