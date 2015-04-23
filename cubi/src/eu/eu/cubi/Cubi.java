package eu.eu.cubi;

import android.app.Application;
import android.content.Context;

public class Cubi extends Application {
	private static Context context;

	/** Returns context of this activity **/
	public static Context getContext() {
		return Cubi.context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Cubi.context = getApplicationContext();
	}
}
