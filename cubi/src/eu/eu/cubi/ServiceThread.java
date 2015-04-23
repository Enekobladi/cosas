package eu.eu.cubi;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ServiceThread extends Service {
	private Timer timer = new Timer();

	/** 1000 * 60 -> 1 minuto */
	private static final long BUCLE_INTERVAL = 60000;// 60000;

	private long updateInterval;

	private final IBinder mBinder = new MyBinder();

	private Notifications notifications;

	private Context context;

	private Calendar updated;

	private String getNextText() {
		updated = Calendar.getInstance();
		updated.add(Calendar.MILLISECOND, (int) updateInterval);
		String sUpdated = updated.get(Calendar.HOUR_OF_DAY) + ":"
				+ updated.get(Calendar.MINUTE);

		return sUpdated;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		context = getApplicationContext();

		notifications = new Notifications(context);
		notifications.mostrarComprobando(true, true, true, getNextText());

		final Preferences prefs = new Preferences(context);
		updateInterval = prefs.getInterval() * 60000;

		pollForUpdates();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
		Log.i(getClass().getSimpleName(), "Timer stopped.");
		notifications.ocultarComprobando();

	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (timer != null) {
			timer.cancel();
		}
		Log.i(getClass().getSimpleName(), "Timer stopped.");
		notifications.ocultarComprobando();
		return super.onUnbind(intent);
	}

	private void pollForUpdates() {
		scheduleTimer();

	}

	public void restart() {
		this.timer.cancel();
		this.timer = new Timer();
		scheduleTimer();

	}

	private void scheduleTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// Codigo que se repetirá cada X segundos

				String sUpdated = getNextText();

				try {
					boolean internetAvailable = Tests
							.isInternetAvailable(context);
					while (!internetAvailable) {
						notifications.mostrarComprobando(internetAvailable,
								false, false, sUpdated);
						Log.v("test", "NO INTERNET " + updateInterval);

						Thread.sleep(BUCLE_INTERVAL);
						internetAvailable = Tests.isInternetAvailable(context);

					}
					;
					Log.v("test", "SI INTERNET " + updateInterval);

					notifications.mostrarComprobando(internetAvailable,
							Tests.isIpAvailable(), Tests.isHttpAvailable(),
							sUpdated);

					// Tests.isHttpAvailable();
					// notifications.ocultarComprobando();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, 0, updateInterval);
		Log.i(getClass().getSimpleName(), "Timer started.");
	}

	public void setUpdateIntervalInMinutes(int minutes) {
		this.updateInterval = minutes * 60000;
		Log.v("test", "setU " + updateInterval);

	}

	public class MyBinder extends Binder {
		ServiceThread getService() {
			return ServiceThread.this;
		}
	}

}
