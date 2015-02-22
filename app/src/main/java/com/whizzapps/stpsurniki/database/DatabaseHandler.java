package com.whizzapps.stpsurniki.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHandler {
	private SQLiteHelper dbHelper;
	private SQLiteDatabase db;
	
	public DatabaseHandler(Context context){
		dbHelper = new SQLiteHelper(context);
	}
	
	public void open(){
		db = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		if (db != null) db.close();
	}

    /*
    * UPRAVLJANJE S PODATKI TABELE ZA URNIK
    */
	public int vnosPredmeta(String dan, int ura, String predmet, String oddelek, String ucilnica, int teden)
	{
		//Vrednosti shranimo
		ContentValues cv = new ContentValues();
		cv.put(SQLiteHelper.DAN, dan);
		cv.put(SQLiteHelper.URA, ura);
		cv.put(SQLiteHelper.PREDMET, predmet);
		cv.put(SQLiteHelper.ODDELEK, oddelek);
		cv.put(SQLiteHelper.UCILNICA, ucilnica);
		cv.put(SQLiteHelper.TEDEN, teden);
		
		//Odpremo podatkovno bazo in podatke zapi�emo
		open();
		
		//�e je vnos uspe�en, metoda vrne ID vnosa, v primeru napake dobimo -1
		return (int)db.insert(SQLiteHelper.IME_TABELE, null, cv);
	}
	
	public Cursor getSubject(String dan, int ura, String oddelek){
		String[] columns = new String[]{SQLiteHelper.PREDMET, SQLiteHelper.UCILNICA};
		String selection = SQLiteHelper.DAN+"=? and "+SQLiteHelper.URA+"=? and "+SQLiteHelper.ODDELEK+"=?";
		String[] selectionArgs = new String[]{dan, Integer.toString(ura), oddelek};
		open();
		return db.query(
				SQLiteHelper.IME_TABELE, 
				columns, 
				selection, 
				selectionArgs,
				null, null, null);
	}
	
	public Cursor getWeek(String oddelek){
		String[] columns = new String[]{SQLiteHelper.TEDEN};
		String selection = SQLiteHelper.ODDELEK+"=?";
		String[] selectionArgs = new String[]{oddelek};
		open();
		return db.query(
				SQLiteHelper.IME_TABELE, 
				columns, 
				selection, 
				selectionArgs, 
				null, null, null);
	}
	
	public void deleteFromDatabase(String razred){
		open();
		db.delete(
				SQLiteHelper.IME_TABELE, 
				SQLiteHelper.ODDELEK+"=?", 
				new String[]{razred}
				);
	}

    /*
    * UPRAVLJANJE S PODATKI TABELE ZA POVEZAVE
    */
    public int vnosPovezave(String razred, String povezava)
    {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteHelper.RAZRED, razred);
        cv.put(SQLiteHelper.URNIK_URL, povezava);

        open();

        return (int)db.insert(SQLiteHelper.IME_TABELE2, null, cv);
    }

    public void clearURLtable()
    {
        open();
        db.execSQL("DELETE FROM " + SQLiteHelper.IME_TABELE2);
    }

    public Cursor getUrls(int oddelki){
        String[] columns = new String[]{SQLiteHelper.RAZRED, SQLiteHelper.URNIK_URL};
        String selection = SQLiteHelper.RAZRED+" LIKE ?";
        String[] selectionArgs = new String[]{oddelki+"%"};
        open();
        return db.query(
                SQLiteHelper.IME_TABELE2,
                columns,
                selection,
                selectionArgs,
                null, null, null);
    }

    public Cursor getSingleClassUrl(String className)
    {
        String[] columns = new String[]{SQLiteHelper.URNIK_URL};
        String selection = SQLiteHelper.RAZRED+" LIKE ?";
        String[] selectionArgs = new String[]{className};
        open();
        return db.query(
                SQLiteHelper.IME_TABELE2,
                columns,
                selection,
                selectionArgs,
                null, null, null);
    }
}
