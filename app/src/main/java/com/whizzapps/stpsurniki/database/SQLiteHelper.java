package com.whizzapps.stpsurniki.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	public static String DATABASE_NAME = "urnik.db";
	public static int DATABASE_VERSION = 3;

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//Tabela za predmete
	public static String IME_TABELE = "predmeti";
	public static String ID = "id";
	public static String ODDELEK = "oddelek";
	public static String DAN = "dan";
	public static String URA = "ura";
	public static String PREDMET = "predmet";
	public static String UCILNICA = "ucilnica";
	public static String TEDEN = "teden";

    // Tabela za razrede in njihove povezave
    public static String IME_TABELE2 = "povezave";
    public static String RAZRED = "razred";
    public static String URNIK_URL = "url";
	
	String predmeti_query =
			"CREATE TABLE " + IME_TABELE + " ("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DAN + " TEXT, "
			+ URA + " INTEGER, "
			+ PREDMET + " TEXT, "
			+ ODDELEK + " TEXT, "
		    + UCILNICA + " TEXT, "
			+ TEDEN + " TEXT"+ ");";

    String povezave_query =
            "CREATE TABLE " + IME_TABELE2 + " ("
            + RAZRED + " TEXT, "
            + URNIK_URL + " TEXT" + ");";


	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("TAG", "Database created");
		db.execSQL(predmeti_query);
        db.execSQL(povezave_query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("TAG", "Databse upgraded from " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + IME_TABELE);
		onCreate(db);
	}
}
