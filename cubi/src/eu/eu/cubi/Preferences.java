package eu.eu.cubi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

	private SharedPreferences prefs;

	public Preferences(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);

	}

	public int getInterval() {
		return prefs.getInt("interval", 60);

	}

	public void setInterval(int interval) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("interval", interval);
		editor.commit();
	}
}
