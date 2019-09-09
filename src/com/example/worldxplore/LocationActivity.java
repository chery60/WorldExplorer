package com.example.worldxplore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class LocationActivity extends FragmentActivity {
	GoogleMap googleMap;
	MarkerOptions marker;
	Double latitude, longitude;
	Double lat, lon;
	LocationManager locationManager;
	Location location;
	TextView addressText, meansTransport;
	String transport;
	String place;

	HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment1);
		marker = new MarkerOptions();
		googleMap = fragment.getMap();
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		addressText = (TextView) findViewById(R.id.subaddress);
		meansTransport = (TextView) findViewById(R.id.transport);
		place = getIntent().getStringExtra("place");
		transport = getIntent().getStringExtra("transport");
		meansTransport.setText(transport);
		try {
			locationAddress(place);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(),
						PlaceDetailsActivity.class);
				String reference = mMarkerPlaceLink.get(arg0.getId());
				intent.putExtra("reference", reference);
				if (reference != null) {
					Log.i("reference", reference);
				}

				// Starting the Place Details Activity
				startActivity(intent);
			}
		});
		findViewById(R.id.get_route).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				track();
				findViewById(R.id.get_route).setEnabled(false);
			}
		});
		findViewById(R.id.get_hotels).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				findViewById(R.id.get_route).setEnabled(true);
				// int selectedPosition =
				// mSprPlaceType.getSelectedItemPosition();
				String type = "restaurant";
				searchNearby(type);
			}
		});
	}

	private void searchNearby(String type) {
		StringBuilder sb = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
		sb.append("location=" + latitude + "," + longitude);
		// Log.d(latitude, longitude)
		sb.append("&radius=1000");
		sb.append("&types=" + type);
		sb.append("&sensor=true");
		sb.append("&key=AIzaSyDbPXgoxEVVctp2hST_6yfFqEztB8JfhAI");
		Log.e("value ", sb.toString());
		// Creating a new non-ui thread task to download Google place json data
		PlacesTask placesTask = new PlacesTask();

		// Invokes the "doInBackground()" method of the class PlaceTask
		placesTask.execute(sb.toString());
	}

	private class PlacesTask extends AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {
			ParserMapTask parserTask = new ParserMapTask();

			// Start parsing the Google places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}

	}

	public void locationAddress(String place) throws IOException {
		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(this, Locale.getDefault());
		addresses = geocoder.getFromLocationName(place, 1);
		latitude = addresses.get(0).getLatitude();
		longitude = addresses.get(0).getLongitude();
		LatLng position = new LatLng(latitude, longitude);

		String address = addresses.get(0).getAddressLine(0);
		String city = addresses.get(0).getLocality();
		String state = addresses.get(0).getAdminArea();
		String country = addresses.get(0).getCountryName();
		String postalCode = addresses.get(0).getPostalCode();
		String knownName = addresses.get(0).getFeatureName();
		addressText.setText(address + "," + knownName + "\n" + city + ","
				+ state + "\n" + country + "," + postalCode);
		marker.position(position);
		marker.title(place);
		googleMap.addMarker(marker);
		marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		googleMap
				.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14));

	}

	public void track() {
		Toast.makeText(getApplicationContext(), "Tracking Location..",
				Toast.LENGTH_LONG).show();
		googleMap.clear();
		GPSservice gpsservice = new GPSservice();
		try {
			location = gpsservice.getGpsLocation();
			lat = location.getLatitude();
			lon = location.getLongitude();
			Toast.makeText(getApplicationContext(), "Used Service:GPS",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
			location = gpsservice.getAGpsLocation();
			lat = location.getLatitude();
			lon = location.getLongitude();
			Toast.makeText(getApplicationContext(), "Used Service:AGPS",
					Toast.LENGTH_SHORT).show();
		}
		marker.position(new LatLng(lat, lon));
		marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		marker.title("YOU");
		googleMap.addMarker(marker);
		gpsservice.marker(latitude, longitude);
		// // Drawline drawline=new Drawline(new LatLng(lat, lon), new
		// LatLng(13.0102,80.2157));
		LatLng origin = null, dest = null;
		origin = new LatLng(lat, lon);
		dest = new LatLng(latitude, longitude);
		if (!transport.equalsIgnoreCase("Flight")) {
			String url = getDirectionsUrl(origin, dest);
			DownloadTask downloadTask = new DownloadTask();
			downloadTask.execute(url);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(lat, lon), 5));

		} else {
			Polyline line = googleMap.addPolyline(new PolylineOptions()
					.add(origin, dest).width(5).color(Color.RED));
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(lat, lon), 1));
		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(5);
				lineOptions.color(Color.RED);
			}

			// Drawing polyline in the Google Map for the i-th route
			googleMap.addPolyline(lineOptions);
		}
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	public class GPSservice {
		private Location getGpsLocation() {
			// TODO Auto-generated method stub
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			return location;
		}

		private Location getAGpsLocation() {
			// TODO Auto-generated method stub
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			return location;
		}

		public void marker(Double lat, Double lon) {

			MarkerOptions marker = new MarkerOptions();
			marker.title("DESTINATION");
			marker.position(new LatLng(lat, lon));
			marker.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			googleMap.addMarker(marker);
		}

	}

	private class ParserMapTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;
			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a List construct */
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {

			// Clears all the existing markers
//			googleMap.clear();

			for (int i = 0; i < list.size(); i++) {

				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();

				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(i);

				// Getting latitude of the place
				double lat = Double.parseDouble(hmPlace.get("lat"));

				// Getting longitude of the place
				double lng = Double.parseDouble(hmPlace.get("lng"));

				// Getting name
				String name = hmPlace.get("place_name");

				// Getting vicinity
				String vicinity = hmPlace.get("vicinity");

				LatLng latLng = new LatLng(lat, lng);

				// Setting the position for the marker
				markerOptions.position(latLng);

				// Setting the title for the marker.
				// This will be displayed on taping the marker
				markerOptions.title(name + " : " + vicinity);
				markerOptions.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
				// Placing a marker on the touched position
				Marker m = googleMap.addMarker(markerOptions);
				// Linking Marker id and place reference
				mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));

			}

		}

	}

}
