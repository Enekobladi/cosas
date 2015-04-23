package eu.eu.cubi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ServiceThread serviceThread;

	Context context;

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			serviceThread = ((ServiceThread.MyBinder) binder).getService();

		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			serviceThread = null;
		}

	};

	void doBindService() {
		bindService(new Intent(this, ServiceThread.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = getApplicationContext();

		final Preferences prefs = new Preferences(context);

		if (serviceThread == null) {
			doBindService();

		}

		Button button = (Button) findViewById(R.id.main_exit);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				serviceThread.stopSelf();
				serviceThread = null;
			}
		});

		final TextView intervalText = (TextView) findViewById(R.id.main_interval_current);

		SeekBar seekBar = (SeekBar) findViewById(R.id.main_interval_seekbar);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progress++;

				int hours = progress / 60;
				int minutes = progress - (hours * 60);

				intervalText
						.setText(hours + " horas y " + minutes + " minutos");

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (serviceThread != null) {
					serviceThread.setUpdateIntervalInMinutes(seekBar
							.getProgress() + 1);
					serviceThread.restart();

					prefs.setInterval(seekBar.getProgress() + 1);
				}

			}
		});

		int interval = prefs.getInterval();
		int hours = interval / 60;
		int minutes = interval - (hours * 60);

		intervalText.setText(hours + " horas y " + minutes + " minutos");
		seekBar.setProgress(interval);

	}

}
