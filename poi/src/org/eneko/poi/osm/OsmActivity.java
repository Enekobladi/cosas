package org.eneko.poi.osm;

/**
 * http://androcode.es/2012/06/osmdroid-introduccion-a-openstreetmap-en-android-osm-parte-i/
 */

import java.util.ArrayList;
import java.util.HashMap;

import org.eneko.poi.R;
import org.eneko.poi.json.Constants;
import org.eneko.poi.json.ServiceHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class OsmActivity extends Activity {
	private MapView myOpenMapView;
	private IMapController myMapController;
	private Context context;

	// URL to get contacts JSON
	public static String url = "http://t21services.herokuapp.com/points/";

	// JSON Node names
	public static final String TAG_ID = "id";
	public static final String TAG_TITLE = "title";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_TRANSPORT = "transport";
	public static final String TAG_EMAIL = "email";
	public static final String TAG_GEOCOORDINATES = "geocoordinates";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_PHONE = "phone";

	OnItemGestureListener<OverlayItem> myOnItemGestureListener = new OnItemGestureListener<OverlayItem>() {

		@Override
		public boolean onItemLongPress(int arg0, OverlayItem arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onItemSingleTapUp(int index, OverlayItem item) {
			Toast.makeText(
					OsmActivity.this,
					"descr" + "\n" + "title" + "\n"
							+ item.getPoint().getLatitudeE6() + " : "
							+ item.getPoint().getLongitudeE6(),
					Toast.LENGTH_LONG).show();

			return true;
		}

	};

	private HashMap<String, String> getPoi(String jsonStr, int index) {

		try {
			JSONObject jsonObj = new JSONObject(jsonStr);

			String id = jsonObj.getString(TAG_ID);
			String title = jsonObj.getString(TAG_TITLE);
			String address = jsonObj.getString(TAG_ADDRESS);
			String transport = jsonObj.getString(TAG_TRANSPORT);
			String email = jsonObj.getString(TAG_EMAIL);
			String geocordinates = jsonObj.getString(TAG_GEOCOORDINATES);
			String description = jsonObj.getString(TAG_DESCRIPTION);
			String phone = jsonObj.getString(TAG_PHONE);

			// tmp hashmap for single contact
			HashMap<String, String> poi = new HashMap<String, String>();

			// adding each child node to HashMap key => value
			poi.put(TAG_ID, id);
			poi.put(TAG_TITLE, title);
			poi.put(TAG_ADDRESS, address);
			poi.put(TAG_TRANSPORT, transport);
			poi.put(TAG_EMAIL, email);
			poi.put(TAG_GEOCOORDINATES, geocordinates);
			poi.put(TAG_DESCRIPTION, description);
			poi.put(TAG_PHONE, phone);

			return poi;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		this.context = this;

		myOpenMapView = (MapView) findViewById(R.id.openmapview);
		myOpenMapView.setBuiltInZoomControls(true);
		myMapController = myOpenMapView.getController();

		myOpenMapView.setMultiTouchControls(true);

		new GetPois(myOpenMapView).execute();

	}

	private class GetPois extends AsyncTask<Void, Integer, Void> {
		private MapView mapView;

		private HashMap<String, String> poi;

		public GetPois(MapView mapView) {
			this.mapView = mapView;

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			int index = 1;

			String jsonStr;

			do {
				// Making a request to url + index and getting response
				jsonStr = sh.makeServiceCall(Constants.url + index,
						ServiceHandler.GET);

				poi = getPoi(jsonStr, index);

				publishProgress(index);

				index++;

			} while (jsonStr != null && jsonStr.length() > 2);

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

			if (poi != null) {
				ArrayList<OverlayItem> overlayItems;
				overlayItems = new ArrayList<OverlayItem>();

				String[] coords = poi.get(TAG_GEOCOORDINATES).split(",");
				double x = Double.valueOf(coords[0]);
				double y = Double.valueOf(coords[1]);

				overlayItems.add(new OverlayItem(poi.get(TAG_ID), poi
						.get(TAG_TITLE), poi.get(TAG_ADDRESS) + "\n"
						+ poi.get(TAG_ADDRESS) + "\n" + poi.get(TAG_TRANSPORT)
						+ "\n" + poi.get(TAG_EMAIL) + "\n"
						+ poi.get(TAG_DESCRIPTION) + "\n" + poi.get(TAG_PHONE)
						+ "\n", new GeoPoint(x, y)));

				ItemizedOverlayWithFocus<OverlayItem> anotherItemizedIconOverlay = new ItemizedOverlayWithFocus<OverlayItem>(
						context, overlayItems, null);

				anotherItemizedIconOverlay.setFocusItemsOnTap(true);

				myOpenMapView.getOverlays().add(anotherItemizedIconOverlay);
				myOpenMapView.invalidate();

				if (values[0] == 1) {
					// zoom in first POI
					myOpenMapView.getController().setZoom(12);
					myOpenMapView.getController().setCenter(new GeoPoint(x, y));
				}

			}

		}

	}
}
