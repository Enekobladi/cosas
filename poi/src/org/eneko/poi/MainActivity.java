package org.eneko.poi;

import org.eneko.poi.osm.OsmActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * Main. Menu...
 * 
 * @author Eneko
 * 
 */
public class MainActivity extends Activity {

	private Context context;

	private void loadViews() {
		setContentView(R.layout.activity_main);

		Button btnMap = (Button) findViewById(R.id.activity_main_map);
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, OsmActivity.class);
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.context = this;

		loadViews();
	}

}
