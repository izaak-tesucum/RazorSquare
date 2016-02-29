package edu.uark.csce.Mobile4013_razorsquare;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class RazorContentProvider extends ContentProvider {
	
	public static final String uriString = "content://edu.uark.csce.mobile.razorprovider/postdata";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	
	public static final String CID = "_id";
	public static final String KEY_POST = "post";
	public static final String KEY_USERNAME= "username";
	public static final String KEY_DATE = "date";
	public static final String KEY_LAT = "latitude";
	public static final String KEY_LONG = "longitude";
	
	private DatabaseHelper dbHelper;
	private static final int ALLROWS = 1;
	private static final int SINGLEROW = 2;
	
	private static final UriMatcher myUriMatcher;
	static {
		myUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		myUriMatcher.addURI("edu.uark.csce.mobile.razorprovider", "postdata", ALLROWS);
		myUriMatcher.addURI("edu.uark.csce.mobile.razorprovider", "postdata/#", SINGLEROW);
	}
	
	public RazorContentProvider() {
	}

	@SuppressWarnings("static-access")
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (myUriMatcher.match(uri)) {
			case SINGLEROW:
				String rowID = uri.getPathSegments().get(1);
				selection = CID + "=" + rowID;
				String appendString= " ";
				if (!TextUtils.isEmpty(selection)) {
					 appendString = " and (" + selection + ")";
				}
				selection += appendString;
			default:
				break;
		}
		
		if (selection == null) {
			selection = "1";
		}
		int deleteCount = db.delete(dbHelper.DATABASE_TABLE, selection, selectionArgs);
		Uri deleteID= ContentUris.withAppendedId(CONTENT_URI, deleteCount);
		getContext().getContentResolver().notifyChange(deleteID, null);
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		switch(myUriMatcher.match(uri)) {
			case ALLROWS:
				// vnd stands for "vendor-specific MIME types". vnd.<company name>.<provider name>
				return "vnd.android.cursor.dir/vnd.uark.mycontent";
			case SINGLEROW:
				return "vnd.android.cursor.post/vnd.uark.mycontent";
			default:
				throw new IllegalArgumentException("Unsupported URI: "+ uri);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String nullColHack = null;
		long id = db.insert(dbHelper.DATABASE_TABLE, nullColHack, values);
		
		if (id > -1) {
			Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
			getContext().getContentResolver().notifyChange(insertedId, null);
			return insertedId;
		}
		return null;
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext(), 
				dbHelper.DATABASE_NAME, 
				null, 
				dbHelper.DATABASE_VERSION);
		return true;
	}

	@SuppressWarnings("static-access")
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String groupby = null;
		String having = null;
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(dbHelper.DATABASE_TABLE);
		
		switch (myUriMatcher.match(uri)) {
			case SINGLEROW:
				String rowID = uri.getPathSegments().get(1);
				queryBuilder.appendWhere(CID + "=" + rowID);
			default:
				break;
		}
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupby, having, sortOrder);
		return cursor;
	}

	@SuppressWarnings("static-access")
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (myUriMatcher.match(uri)) {
			case SINGLEROW:
				String rowID = uri.getPathSegments().get(1);
				selection = CID + "=" + rowID;
				if (!TextUtils.isEmpty(selection)) {
					String appendString = " and (" + selection + ")";
					selection += appendString;
				}
			default:
				break;
		}
		
		int updateCount = db.update(dbHelper.DATABASE_TABLE, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		
		return updateCount;
	}
	
	
	// SQLite
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME = "razorsquareDatabase.db";
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_TABLE = "userTable";
		
		private static final String DATABASE_CREATE_CMD = 
				" create table " + DATABASE_TABLE + " ( " + CID +
				" integer primary key autoincrement, " + 
				KEY_USERNAME + " text not null, " + KEY_POST + " text, " + KEY_DATE + " text, " + KEY_LAT + " real, " +
				KEY_LONG + " real); "
		;
		private static final String DATABASE_DROP_CMD = 
				" drop table if it exists " + DATABASE_TABLE;
		
		public DatabaseHelper(Context context, String name, 
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_CMD);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("MYCONTENTPROVIDER", "Upgrading from version " + oldVersion +
				" to " + newVersion + ". All data will be deleted."
				);
			db.execSQL(DATABASE_DROP_CMD);
			db.execSQL(DATABASE_CREATE_CMD);
		}
		
		
		
		
		
		
	}
}
