package com.whizzapps.stpsurniki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.whizzapps.stpsurniki.database.DatabaseHandler;
import com.whizzapps.stpsurniki.database.SQLiteHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Schedule extends ActionBarActivity {
	
	//Globalne spremenljivke
	String urlToLoad, razred;
	int predmet = 1;
	DatabaseHandler dbHandler;
	SharedPreferences prefs, settings;
	int week;
	RotateAnimation rotate;
	
	TextView dan, ucilnica;
	ArrayList<TextView> dnevi, ucilnice;
	
	//Ostali UI elementi
	ImageView progress;
	TableLayout tabela;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		//ActionBar back button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//SharedPreferences
		prefs = getSharedPreferences("urnikSp", 0);
		settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		//ArrayList
		dnevi = new ArrayList<TextView>();
		ucilnice = new ArrayList<TextView>();

        // Ostale spremenljivke
        dbHandler = new DatabaseHandler(getApplicationContext());
		
		//Vna�anje TextView-ov za predmete v ArrayList
		dan = (TextView) findViewById(R.id.pon1);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor1);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre1);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet1);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet1);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon2);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor2);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre2);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet2);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet2);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon3);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor3);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre3);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet3);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet3);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon4);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor4);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre4);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet4);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet4);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon5);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor5);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre5);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet5);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet5);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon6);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor6);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre6);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet6);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet6);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon7);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor7);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre7);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet7);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet7);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon8);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor8);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre8);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet8);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet8);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon9);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor9);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre9);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet9);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet9);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon10);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor10);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre10);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet10);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet10);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pon11);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.tor11);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.sre11);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.cet11);
		dnevi.add(dan);
		dan = (TextView) findViewById(R.id.pet11);
		dnevi.add(dan);
		
		// Vna�anje TextView-ov za u�ilnice v ArrayList
		ucilnica = (TextView) findViewById(R.id.pon1u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor1u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre1u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet1u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet1u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon2u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor2u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre2u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet2u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet2u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon3u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor3u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre3u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet3u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet3u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon4u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor4u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre4u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet4u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet4u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon5u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor5u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre5u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet5u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet5u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon6u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor6u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre6u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet6u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet6u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon7u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor7u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre7u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet7u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet7u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon8u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor8u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre8u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet8u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet8u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon9u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor9u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre9u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet9u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet9u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon10u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor10u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre10u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet10u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet10u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pon11u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.tor11u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.sre11u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.cet11u);
		ucilnice.add(ucilnica);
		ucilnica = (TextView) findViewById(R.id.pet11u);
		ucilnice.add(ucilnica);
		
		//Sprzanimo spremenljivke
		ucilnica = null;
		razred = null;
		
		//Animacija
		rotate = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setInterpolator(new LinearInterpolator());
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setDuration(1800);
		
		//Ostali UI elementi
		tabela = (TableLayout) findViewById(R.id.tabela);
		progress = (ImageView) findViewById(R.id.progress);
		
		//Pridobimo trenuten teden, da ga vnesemo v bazo
		Calendar cal = Calendar.getInstance();
		week = cal.get(Calendar.WEEK_OF_YEAR);
		
		//Get data
		getIntentData();
		getSupportActionBar().setTitle("Urnik - " + razred);

        //Ostale deklaracije spremenljivk
        boolean schudeleDownloaded = prefs.getBoolean(razred, false);
        boolean autoUpdate = settings.getBoolean("autoUpdate", true);

		//Prenos urnika, �e ga �e ni
		if (!schudeleDownloaded){
			/*
			 * Aplikacija preveri, če obstaja internetna povezava,
			 * če ne obstaja se urnik prenese in prikaže. Zaradi
			 * varnostnih razlogov pa prej pobrišem vse sledi
			 * kakršnegakoli urnika za ta razred iz podatkovne baze.
			 */
			if (Utility.isNetworkAvailable(getApplicationContext())){
				dbHandler.deleteFromDatabase(razred);
				new PopulateDatabase().execute();
			}
			// Če internetna povezava ne obstaja uporabniku prikaže
			// sporočilo, da ni internetne povezave.
			else{
				Utility.showNoNetworkMessage(this);
			}
		}
		/* 
		 * Aplikacija preveri, če se trenutni teden ujema s tednom
		 * v podatkovni bazi ter, če je uporabnik vključil avtomatske
		 * posodobitve. če se teden ne ujema in, če je uporabnik
		 * vključil avtomatske posodobitve se bo urnik posodobil
		 * ter prikazal.
		 */
		else if (!istiTeden() && autoUpdate)
		{
			if (Utility.isNetworkAvailable(getApplicationContext())){
				dbHandler.deleteFromDatabase(razred);
				new PopulateDatabase().execute();
			}
			else{
				Utility.showNoNetworkMessage(this);
			}
		}
		// �e zgornja pogoja ne obstajata se urnik prika�e.
		else{
		    napolni();
		    progress.setVisibility(View.GONE);
			tabela.setVisibility(View.VISIBLE);
		}

        // Reklame
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView ad = (AdView) findViewById(R.id.adView);
        ad.loadAd(adRequest);
	}
	
	//Funkcija preveri, �e je urnik za ta teden prene�en
	public boolean istiTeden(){
		Cursor c = dbHandler.getWeek(razred);
		boolean rezultat = false;
		if (c.moveToFirst()){
			int teden = c.getInt(c.getColumnIndex(SQLiteHelper.TEDEN));
			if (teden == week){
				rezultat = true;
			}
		}
		return rezultat;
	}
	
	/*
	 * Metoda, ki bere iz tabele v TextView-e.
	 * Kot parameter vnesemo kateri dan, ura in
	 * razred nas zanima ter tekstovna polja,
	 * ki jih �elimo napolniti.
	 */
	public void populateUrnik(String dan, int ura, String razred, TextView tw, TextView ucilnica){
		// Deklaracija spremenljivk v katere bomo kasneje brali predmete ter razrede.
		String predmet1 = null;
		String predmet2 = null;
		String prostor1 = null;
		String prostor2 = null;
		// Nov objekt razreda Cursor s katerim naredim poizvedbo podatkovne baze.
		Cursor c = dbHandler.getSubject(dan, ura, razred);
		// Cursor premaknem na prvo polje na�e poizvedbe, �e je rezultate prazen pustim tekstovno polje prazno.
		if (!c.moveToFirst())
		{
			tw.setText("");
			ucilnica.setText("");
		}
		// �e rezultat ni prazen tekstovno polje napolnim.
		else {
			predmet1 = c.getString(c.getColumnIndex(SQLiteHelper.PREDMET));
			prostor1 = c.getString(c.getColumnIndex(SQLiteHelper.UCILNICA));
			tw.setText(predmet1);
			ucilnica.setText(prostor1);
		}
		// Preverim, �e za isto uro obstaja �e kak�en predmet, �e obstaja napolnim �e drugo tekstovno polje.
		if (c.moveToNext())
		{
			predmet2 = c.getString(c.getColumnIndex(SQLiteHelper.PREDMET));
			prostor2 = c.getString(c.getColumnIndex(SQLiteHelper.UCILNICA));
			tw.setText(predmet1+"/"+predmet2);
			// Ker moram v isti element tabele stisniti dva predmeta zmanj�am velikost �rk.
			tw.setTextSize(10);
			ucilnica.setText(prostor1+"/"+prostor2);
		}
		// Pogoj, �e ima kratica drugega predmeta ve� kot 3 �rke.
		if (predmet2 != null && predmet2.length() > 3){
			// V tem primeru velikost �rk �e malo zmanj�am.
			tw.setTextSize(9);
		}
	}
	
	public void getIntentData(){
		razred = Utility.getClassNameFromChildAndHeader(
                getApplicationContext(),
                getIntent().getIntExtra("headerPosition", 0),
                getIntent().getIntExtra("childPosition", 0));
        Cursor c = dbHandler.getSingleClassUrl(razred);
        if (c.moveToFirst())
        {
            urlToLoad = c.getString(c.getColumnIndex(SQLiteHelper.URNIK_URL));
        }
	}
	
	/*
	 * AsyncTask razred, ki se pove�e na easistent.com ter
	 * pridobi podatke za �eljen urnik.
	 * Razlog, da za to uporabim AsyncTask razred je to,
	 * ker internetnih klicev ne smemo izvajati v UI niti.
	 */
	class PopulateDatabase extends AsyncTask<Void, Void, Void> {
		
		// Ta metoda se izvede preden se za�ne prena�ati urnik
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			/*
			 * Najprej izbri�em vse podatke o urniku za dolo�en razred
			 * zaradi varnostnih razlogov. �e bi v tabeli nastali
			 * dvojni podatki bi nastal velik problem, ker bi se vedno
			 * prikazovale dvojne ure ter aplikacija bi zasedala
			 * preve� pomnilnika na telefonu.
			 */
			dbHandler.deleteFromDatabase(razred);
			// Tabelo bomo prikazali �ele, ko bo urnik prene�en.
			tabela.setVisibility(View.GONE);
			// Prika�em sliko, ki nam pove, da se urnik prena�a.
			progress.setVisibility(View.VISIBLE);
			// Slika se ob prenosu rotira za 360 stopinj okoli svoje osi.
			progress.startAnimation(rotate);
		}

		// Ta metoda se za�ne izvajati takoj bo metodi onPreExecute.
		@Override
		protected Void doInBackground(Void... params) {
				try {
					// Z Jsoup-om se povežemo na URL s katerega želimo prenašati podatke
                    Document doc = Jsoup.connect(urlToLoad).get(); //TODO FIX 3.E neki narobe z linkam blage nimam
					// Ta for zanka "obdela" vsak dan v urniku posebej (ponedeljek - petek)
					for (int i = 1; i <= 5; i++)
					{
						// S to poizvedbo izberem glavo tabele (dnevi), začnem pri ponedeljku.
						Elements dan = doc.select("table tbody tr:eq(0) th:eq("+i+") div");
						// Ta for zanka "obdela" vsak predmet za določen dan posebej, začne pri prvi uri in se konča pri enajsti.
						for (int b = 1; b <= 11; b++)
						{
							// S to poizvedbo izberem vsak predmet za določen dan posebej.
							Elements predmeti = doc.select("table tbody  tr:eq("+b+") td:eq("+predmet+") td[class=text14 bold]");
							// S to poizvedbo izberem še učilnico, ki je pripisana tem predmetu.
							Elements ucilnice = doc.select("table tbody  tr:eq("+b+") td:eq("+predmet+") div[class=text11]");
							// S to for zanko se pomikam bo vsakem elementu znotraj spremenljivke "predmeti" ter ga vpisujem v bazo podatkov.
							for (int j = 0; j <= predmeti.size()-1; j++)
							{
								String uc;
								// Pogoj, �e spremenljivka "ucilnice" NI prazna.
								if (!ucilnice.isEmpty()){
									// Vzamem j-ti element iz spremenljivke "ucilnice".
									String ucilnica = ucilnice.get(j).text().toString();
									// Stringu izbrišem vse črke razen zadnje tri, saj samo zadnje tri predstavljajo učilnico.
									uc = ucilnica.substring(ucilnica.length()-3, ucilnica.length());
								}
								else{
									// �e u�ilnice ni, pustim prazno.
									uc = " ";
								}
								// Kon�no trenuten predmet vnesem �e v podatkovno bazo.
								dbHandler.vnosPredmeta(
										dan.get(0).text().toString(), //dan
										b, //ura
										predmeti.get(j).text().toString(), //predmet
										razred, //razred
										uc, //u�ilnica
										week); //teden	
							}
						}
						// Predmet pove�am sam, da sigurno dobim vse predmete za dan urnik.
						predmet++;
					}
					// Predmet vrnem na 1, da lahko znova zajamem vse predmete za vsak dan.
					predmet = 1;
				} catch (IOException e) {
					e.printStackTrace();
				}
			return null;
		}
		
		// Metoda, ki se izvede po kon�anem prenosu urnika.
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// Pokli�em metodo, ki tekstovne elemente napolni s prene�enimi podatki.
			napolni();
			// Ustavim animacijo ter skrijem "progress" sliko.
			progress.setVisibility(View.GONE);
			progress.setAnimation(null);
			// Prika�em tabelo.
			tabela.setVisibility(View.VISIBLE);
			// V SharedPreferences nastavim, da je urnik uspe�no prenesen in v podatkovni bazi.
			prefs.edit().putBoolean(razred, true).apply();
		}
	}
	
	//Funkcija za la�jo izvr�itev funkcije "populateUrnik"
	public void napolni(){
		
		int i = 0;
		
		for (int ura = 1; ura <= 11; ura++)
		{
			populateUrnik("Ponedeljek", ura, razred, dnevi.get(i), ucilnice.get(i));
			i++;
			populateUrnik("Torek", ura, razred, dnevi.get(i), ucilnice.get(i));
			i++;
			populateUrnik("Sreda", ura, razred, dnevi.get(i), ucilnice.get(i));
			i++;
			populateUrnik("Četrtek", ura, razred, dnevi.get(i), ucilnice.get(i));
			i++;
			populateUrnik("Petek", ura, razred, dnevi.get(i), ucilnice.get(i));
			i++;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.schedule, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
    	case R.id.action_settings:
    		Intent i = new Intent(this, NastavitveActivity.class);
    		startActivity(i);
    		return true;
        case R.id.action_refresh:
        	if (Utility.isNetworkAvailable(getApplicationContext())){
        		prefs.edit().putBoolean(razred, false).commit();
				dbHandler.deleteFromDatabase(razred);
				new PopulateDatabase().execute();
			}
			else{
				Utility.showNoNetworkMessage(this);
			}
        	return true;
        default:
        	finish();
            return super.onOptionsItemSelected(item);
		}
	}

}
