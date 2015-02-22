package com.whizzapps.stpsurniki;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class NastavitveActivity extends PreferenceActivity {

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

}
