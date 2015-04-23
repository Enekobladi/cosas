package org.eneko.poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eneko.poi.json.Constants;

import org.eneko.poi.json.ServiceHandler;
import org.eneko.poi.osm.OsmActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.context = this;

		loadViews();
	}

	private void loadViews(){
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

}
