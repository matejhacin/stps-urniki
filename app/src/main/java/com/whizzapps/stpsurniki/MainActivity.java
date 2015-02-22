package com.whizzapps.stpsurniki;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.whizzapps.stpsurniki.database.DatabaseHandler;
import com.whizzapps.stpsurniki.database.SQLiteHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	
	// Global variables
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int expandedGroup = -1;
    int hour, minutes, day;
    SharedPreferences settings;
    String izbranRazred;
    String trenutenDan;
    SharedPreferences prefs;
    
    // UI elementi
    TextView dan, ura;
    ImageView facebook, twitter, youtube;
    TextView twDan, twUrnik, twUra, twPredmet, trenutnaUra, twPodatki;

    // XML Parser
    static final String XML_URL = "http://www.stps-trbovlje.si/urniki.xml";
    // Vozlišča
    static final String KEY_ITEM = "item";
    static final String KEY_ID = "id";
    static final String KEY_URL = "url";
    // Dialog
    ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// SharedPreference
		prefs = getSharedPreferences("prefs", 0);

        // Dialog
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Prenašanje URL povezav do urnikov");
        dialog.setCancelable(false);

        //Nastavitev noge
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        View footerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        expListView.addFooterView(footerView);
        expListView.setIndicatorBounds(50, 50);

        //Povezava z elementi iz navigacije
        dan = (TextView) findViewById(R.id.dan);
        ura = (TextView) findViewById(R.id.ura);
        facebook = (ImageView) findViewById(R.id.facebook);
        twitter = (ImageView) findViewById(R.id.twitter);
        youtube = (ImageView) findViewById(R.id.youtube);
        trenutnaUra = (TextView) findViewById(R.id.textView4);
        
        //Listenerji
        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        youtube.setOnClickListener(this);
        
        //Klicanje pomembnih funkcij
        setCustomFontToAllTextViews();
        getDay();
        getTime();
        expListViewClickListeners();
        posodabljanjeUre();

        // Preveri, če mora aplikacija prenesti URL-je in razrede
        if (prefs.getBoolean("urlsDownloaded", false))
        {
            prepareListData();
        }
        else
        {
            downloadURLs();
        }

        // Reklame
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView ad = (AdView) findViewById(R.id.adView);
        ad.loadAd(adRequest);
	}

    @Override
    protected void onResume() {
        super.onResume();

        getDay();

        //Nastavljanje trenutnega predmeta
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        izbranRazred = settings.getString("oddelek", "null");
        String predmet1 = null;
        String predmet2 = null;
        String ucilnica1 = null;
        String ucilnica2 = null;

        if (izbranRazred.matches("null"))
        {
            trenutnaUra.setTextSize(12);
            trenutnaUra.setText("V nastavitvah aplikacije\n izberi svoj razred.");
        }
        else if (Utility.getCurrentHour() == -1)
        {
            trenutnaUra.setTextSize(12);
            trenutnaUra.setText("Ni pouka!");
        }
        else
        {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            Cursor c = db.getSubject(trenutenDan, Utility.getCurrentHour(), izbranRazred);

            if (c.moveToFirst())
            {
                predmet1 = c.getString(c.getColumnIndex(SQLiteHelper.PREDMET));
                ucilnica1 = c.getString(c.getColumnIndex(SQLiteHelper.UCILNICA));
                trenutnaUra.setTextSize(20);
                trenutnaUra.setText(predmet1 + " " + ucilnica1);
                if (c.moveToNext())
                {
                    predmet2 = c.getString(c.getColumnIndex(SQLiteHelper.PREDMET));
                    ucilnica2 = c.getString(c.getColumnIndex(SQLiteHelper.UCILNICA));
                    trenutnaUra.setTextSize(14);
                    trenutnaUra.setText(predmet1 + " " + ucilnica1 + "\n" + predmet2 + " " + ucilnica2);
                }
            }
            else
            {
                trenutnaUra.setTextSize(12);
                trenutnaUra.setText("Ni pouka!");
            }
        }
    }
	
	private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding header data
        listDataHeader.add("1. LETNIKI");
        listDataHeader.add("2. LETNIKI");
        listDataHeader.add("3. LETNIKI");
        listDataHeader.add("4. LETNIKI");
        
        // Adding child data
        List<String> prviLetniki = new ArrayList<String>();
        addChildrenToHeaders(1, prviLetniki);

        List<String> drugiLetniki = new ArrayList<String>();
        addChildrenToHeaders(2, drugiLetniki);
 
        List<String> tretjiLetniki = new ArrayList<String>();
        addChildrenToHeaders(3, tretjiLetniki);
        
        List<String> cetrtiLetniki = new ArrayList<String>();
        addChildrenToHeaders(4, cetrtiLetniki);
        
        // Connecting header and child data
        listDataChild.put(listDataHeader.get(0), prviLetniki);
        listDataChild.put(listDataHeader.get(1), drugiLetniki);
        listDataChild.put(listDataHeader.get(2), tretjiLetniki);
        listDataChild.put(listDataHeader.get(3), cetrtiLetniki);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    public void addChildrenToHeaders(int number, List<String> list)
    {
        Cursor c = new DatabaseHandler(getApplicationContext()).getUrls(number);
        if (c.moveToFirst())
        {
            do {
                list.add(c.getString(c.getColumnIndex(SQLiteHelper.RAZRED)));
            } while (c.moveToNext());
        }
    }
	
	public void getDay(){
    	day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    	
    	switch (day){
    	case 0:
    		trenutenDan = "Sobota";
    		dan.setText(trenutenDan);
    		break;
    	case 1:
    		trenutenDan = "Nedelja";
    		dan.setText(trenutenDan);
    		break;
    	case 2:
    		trenutenDan = "Ponedeljek";
    		dan.setText(trenutenDan);
    		break;
    	case 3:
    		trenutenDan = "Torek";
    		dan.setText(trenutenDan);
    		break;
    	case 4:
    		trenutenDan = "Sreda";
    		dan.setText(trenutenDan);
    		break;
    	case 5:
    		trenutenDan = "Četrtek";
    		dan.setText(trenutenDan);
    		break;
    	case 6:
    		trenutenDan = "Petek";
    		dan.setText(trenutenDan);
    		break;
    	}
    }
	
	public void expListViewClickListeners(){
		
		// Ta metoda se izvede kadar kliknemo na podelement (child) razširljivega seznama
		expListView.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int headerPosition, int childPosition, long arg4) {
				// Kreiramo nov objekt tipa Intent, ki mu določimo parametra Context ter aktivnost, ki jo želimo odpreti
				Intent scheudele = new Intent(MainActivity.this, Schedule.class);
				// Novi aktivnosti zraven pošljemo še spremenljivki, ki nam poveta kateri element in kateri podelement je uporabnik kliknil
				scheudele.putExtra("childPosition", childPosition);
				scheudele.putExtra("headerPosition", headerPosition);
				// Novo aktivnost zaženemo
				startActivity(scheudele);
				expandedGroup = headerPosition;
				return false;
			}
        	
        });
        
		// Ta metoda se izvede kadar kliknemo na glavni element (group) razširljivega seznama
        expListView.setOnGroupExpandListener(new OnGroupExpandListener(){

			@Override
			public void onGroupExpand(int groupPosition) {
				// Če je razširjen katerikoli drug element razširljivega seznama ga zapremo ter odpremo novega
				if (expandedGroup != -1 && groupPosition != expandedGroup)
				{
					expListView.collapseGroup(expandedGroup);
				}
				expandedGroup = groupPosition;
			}
        	
        });
	}
	
	public void getTime(){
    	String time = "hh:mm";
    	ura.setText(DateFormat.format(time, Calendar.getInstance().getTime()));
    }
	
	public void setCustomFontToAllTextViews(){
    	twDan = (TextView) findViewById (R.id.dan);
    	twUrnik = (TextView) findViewById (R.id.textView1);
    	twUra = (TextView) findViewById (R.id.ura);
    	twPredmet = (TextView) findViewById (R.id.textView4);
    	twPodatki = (TextView) findViewById(R.id.podatkiSole);
    	
    	Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/stps_font.OTF");
    	
    	//Setting typefaces
    	twDan.setTypeface(tf);
    	twUrnik.setTypeface(tf);
    	twUra.setTypeface(tf);
    	twPredmet.setTypeface(tf);
    	twPodatki.setTypeface(tf);
    }
	
	public void posodabljanjeUre(){
		Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						getTime();
					}
				});
			}
        }, 0, 1000);
	}

    public void downloadURLs()
    {
        if (Utility.isNetworkAvailable(getApplicationContext()))
        {
            new XMLdownloader().execute();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pozor");
            builder.setMessage("Za prenos URL povezav do urnikov potrebuješ internetno povezavo. Še enkrat zaženi aplikacijo z internetno povezavo!");
            builder.setPositiveButton("V redu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.action_settings:
        		Intent i = new Intent(this, NastavitveActivity.class);
        		startActivity(i);
        		return true;
            case R.id.action_updateURL:
                downloadURLs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onClick(View v) {
		
		switch (v.getId())
		{
		case R.id.facebook:
			String urlFb = "https://www.facebook.com/stpstrbovlje";
			Intent iFb = new Intent(Intent.ACTION_VIEW);
			iFb.setData(Uri.parse(urlFb));
			startActivity(iFb);
			break;
		case R.id.twitter:
			String urlTw = "https://twitter.com/STPSTrbovlje";
			Intent iTw = new Intent(Intent.ACTION_VIEW);
			iTw.setData(Uri.parse(urlTw));
			startActivity(iTw);
			break;
		case R.id.youtube:
			String urlYt = "https://www.youtube.com/channel/UC_ei6S4UNMFUKFME8-0NzwA";
			Intent iYt = new Intent(Intent.ACTION_VIEW);
			iYt.setData(Uri.parse(urlYt));
			startActivity(iYt);
			break;
		}
	}

    class XMLdownloader extends AsyncTask<Void, Void, Void> {
        XMLParser parser;
        String xml;
        Document doc;
        NodeList nl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            new DatabaseHandler(getApplicationContext()).clearURLtable();
            dialog.show();
            prefs.edit().putBoolean("urlsDownloaded", false).apply();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parser = new XMLParser();
            xml = parser.getXmlFromUrl(XML_URL);
            doc = parser.getDomElement(xml);
            nl = doc.getElementsByTagName(KEY_ITEM);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                new DatabaseHandler(getApplicationContext()).vnosPovezave(parser.getValue(e, KEY_ID), parser.getValue(e, KEY_URL));
            }
            prepareListData();
            prefs.edit().putBoolean("urlsDownloaded", true).apply();
            dialog.dismiss();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }
    }
}
