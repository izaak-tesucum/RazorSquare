package edu.uark.csce.Mobile4013_razorsquare;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


public class Overview extends Activity  {
	final private int NEW_POST = 1;
	final private int DELETE_POST = 2;
	final private String POST_INDEX = "edu.uark.csce.iitesucu.razorsquare.overview.postindex";
	private String message;
	final ArrayList<PostData> itemList=new ArrayList<PostData>();
	private PostDataAdapter listAdapter;
	public  static final String show = "razorsquare";
	public static  String posttime = "This_time";
	public static final String lat="latitude";
	public static final String lng="longitude";
	public RazorContentProvider rc;
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	//ArrayList<HashMap<String, String>> postsList;

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	

	// posts JSONArray
	JSONArray posts = null;
	String cid = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);

		// Loading products in Background Thread
		ListView itemView = (ListView)findViewById(R.id.list);
		listAdapter = new PostDataAdapter(Overview.this, R.layout.list_layout, itemList);
		itemView.setAdapter(listAdapter);
		new LoadAllPosts().execute();
		
		// Get the message from the intent
		Context context= getApplicationContext();
		SharedPreferences Pref= PreferenceManager.getDefaultSharedPreferences(context);
		message=Pref.getString(Login.username, "hi");

	    // Create the text view
	    TextView userPassedBy = (TextView) findViewById(R.id.OverviewTitle);
	    userPassedBy.setText(message);
	    itemView.setOnItemClickListener(new OnItemClickListener()
	    {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
			{
				PostData post =(PostData)parent.getItemAtPosition(position);
				Intent intent = new Intent(getApplicationContext(), EditActivity.class);
				intent.putExtra(Overview.show, post.getPost());
				intent.putExtra(Overview.posttime, post.getCreatedDate());
				intent.putExtra(POST_INDEX, post.getRow());
				intent.putExtra(Overview.lat, post.getLat());
				intent.putExtra(Overview.lng, post.getLong());
				startActivityForResult(intent, DELETE_POST);
			}
	    	
	    }
	    );  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.overview, menu);
		return true;
	}
	
	public void Checkin(View view)
	{
		Intent intent = new Intent(Overview.this, Checkin.class);
	    intent.putExtra(Login.username, message);
		startActivityForResult(intent, NEW_POST);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != RESULT_CANCELED){
			if(requestCode == NEW_POST)
			{
				Date d = Calendar.getInstance().getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("MMM d, HH:mm:ss");
				String time=sdf.format(d).toString();
				String p= data.getStringExtra(Checkin.P_DATA_KEY);
				String lat= data.getStringExtra(Checkin.Latitude);
				String lng= data.getStringExtra(Checkin.Longitude);
				new CreateNewPost().execute(p,time,lat,lng);
			}
				if(data.getBooleanExtra(EditActivity.DELETE_POSTING, false))
				{
					String position = data.getStringExtra(POST_INDEX);
					if(position != "-2")
					{
						new DeletePost().execute(position);
					}
			
				}
		}
	}
	
	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllPosts extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Overview.this);
			pDialog.setMessage("Loading posts. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", message));
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(
					PostUtil.URL_ALL_POSTS, "GET", params);

			// Check your log cat for JSON response
			Log.d("All Products: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) 
				{
					// posts found
					// Getting Array of Posts
					posts = json.getJSONArray(show);
					
					itemList.clear();
					// looping through All Posts
					for (int i = 0; i < posts.length(); i++) 
					 {
						JSONObject c = posts.getJSONObject(i);
						if(message.equals(c.getString("username")))
						{
							// Storing each json item in variable
							String id = c.getString("cid");
							String name = c.getString("username");
							String postDes = c.getString("post");
							String post_date = c.getString("post_date");
							double lat = Double.parseDouble(c.getString("latitude"));
							double lng = Double.parseDouble(c.getString("longitude"));
							// creating new HashMap
							PostData map = new PostData();
							map.setPostDatadb(name, postDes, post_date);
							map.setRow(id);
							map.setLat(lat);
							map.setLong(lng);
							itemList.add(map);
						}
					}
				} 
				
				else 
				{
					itemList.clear();
				}
			} 
				catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					// updating listview
					listAdapter.notifyDataSetChanged();
				}
			});

		}

	}
	
	 class CreateNewPost extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Overview.this);
			pDialog.setMessage("Creating Post..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", message));
			params.add(new BasicNameValuePair("post", args[0]));
			params.add(new BasicNameValuePair("post_date", args[1]));
			params.add(new BasicNameValuePair("latitude", args[2]));
			params.add(new BasicNameValuePair("longitude", args[3]));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jParser.makeHttpRequest(
					PostUtil.URL_CREATE_POST, "POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());
			// check for success tag
			String errMessage = null;
			try 
			{
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) 
				{
				} 
				else 
				{
					// failed to create product
					errMessage = json.getString("message");
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			return errMessage;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String errMessage) 
		{
			// dismiss the dialog once done
			pDialog.dismiss();
			if(errMessage == null)
			{
				runOnUiThread(new Runnable()
				{		
					@Override
					public void run()
					{
						new LoadAllPosts().execute();
					}
				});
			} 
			else 
			{
				Log.d("Overview:", errMessage);
			}
		}

	}
	
	class DeletePost extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Overview.this);
			pDialog.setMessage("Deleting Product...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Deleting product
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;
			
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("cid", args[0]));

				// getting product details by making HTTP request
				JSONObject json = jParser.makeHttpRequest(
						PostUtil.URL_DELETE_POST, "POST", params);

				// check your log for json response
				Log.d("Delete Product", json.toString());

				String errMessage = null;
				try 
				{
					success = json.getInt(TAG_SUCCESS);

					if (success == 1) 
					{
					} 
					else 
					{
						// failed to create product
						errMessage = json.getString("message");
					}
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
				return errMessage;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String errMessage) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if(errMessage == null)
			{
				runOnUiThread(new Runnable()
				{		
					@Override
					public void run()
					{
						new LoadAllPosts().execute();
					}
				});
			} 
			else 
			{
				Log.d("Overview:", errMessage);
			}

		}

	}
	
}
