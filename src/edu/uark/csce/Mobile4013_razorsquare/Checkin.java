package edu.uark.csce.Mobile4013_razorsquare;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Dialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
//import android.support.v4.app.FragmentActivity;

public class Checkin extends Activity {
	private GoogleMap map;
	private String username;
	public static final String P_DATA_KEY = "razorsquare.NEW_POST";
	public static final String Latitude="Latitude";
	public static final String Longitude="Longitude";
	//public static Location loc;
	double lat,lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin);
		
		username = getIntent().getStringExtra(Login.username);
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		
		if (status == ConnectionResult.SUCCESS) 
		{
			MapFragment MapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			map = MapFragment.getMap();
	
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setSpeedRequired(false);
			criteria.setCostAllowed(true);
	
			// String provider = LocationManager.GPS_PROVIDER;
			String provider = locationManager.getBestProvider(criteria, true);
			Location loc = locationManager.getLastKnownLocation(provider);
	
			// updateWithNewLocation(loc);
			locationManager.requestLocationUpdates(provider, 2000, 10,
					locationListener);
		}
		
		else 
		{
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.checkin, menu);
		return true;
	}
	
	public void fOverview(View view)
	{
		Intent intent = new Intent();
	    intent.putExtra(Login.username, username);
	    intent.putExtra(Checkin.P_DATA_KEY,((TextView)findViewById(R.id.checkintext)).getText().toString());
	    intent.putExtra(Checkin.Latitude,String.valueOf(lat));
	    intent.putExtra(Checkin.Longitude,String.valueOf(lng));
		setResult(RESULT_OK, intent);  //Set the result data that is returned to the parent activity when this activity dies.
		finish();  //Tells the framework that we are done with this activity.  Kill it.
	}
	
	private void updateWithNewLocation(Location loc) {
		if (loc != null) {
			  lat = loc.getLatitude();
			 lng = loc.getLongitude();

			LatLng location = new LatLng(lat, lng);
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location,16);
			map.animateCamera(update);
			map.addMarker(new MarkerOptions().position(location).title("I am here!"));
		}
		
		
		
	}

	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}
	};
}
	



