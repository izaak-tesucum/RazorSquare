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
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
//import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends Activity {
	final public static String DELETE_POSTING = "edu.uark.csce.iitesucu.razorsquare.editactivity.DELETE_POSTING";
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		// Get the description from the intent
				Intent intent = getIntent();
			    String description = intent.getStringExtra(Overview.show);
			    String ptime = intent.getStringExtra(Overview.posttime);

			    // Create the text view
			    TextView showpost = (TextView)findViewById(R.id.EditText);
			    showpost.setText(description);
			    TextView showtime = (TextView)findViewById(R.id.EditText2);
			    showtime.setText(ptime);
			    
			    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
				
				if (status == ConnectionResult.SUCCESS) 
				{
					double lat = intent.getDoubleExtra(Overview.lat,0.00);
				    double lng = intent.getDoubleExtra(Overview.lng,0.00);
				    
					MapFragment MapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map2);
					map = MapFragment.getMap();
			
					//LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			
					/*Criteria criteria = new Criteria();
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
							locationListener);*/
					//double lat = loc.getLatitude();
					//double lng = loc.getLongitude();

					LatLng location = new LatLng(lat, lng);
					map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location,
							16);
					map.animateCamera(update);
					map.addMarker(new MarkerOptions().position(location).title(
							"I was here!"));
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
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}
	
	public void Delete(View view)
	{
		Intent intent = getIntent();
		intent.putExtra(DELETE_POSTING, true);
		setResult(RESULT_OK,intent);
		finish();
	}
	
	/*private void updateWithNewLocation(Location loc) {
		if (loc != null) {
			//Intent intent = getIntent();
			double lat = loc.getLatitude();
			double lng = loc.getLongitude();

			LatLng location = new LatLng(lat, lng);
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location,
					16);
			map.animateCamera(update);
			map.addMarker(new MarkerOptions().position(location).title(
					"I was here!"));
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
	};*/
	

}

