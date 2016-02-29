package edu.uark.csce.Mobile4013_razorsquare;

//import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
//import android.net.Uri;
//import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.app.Activity;
//import android.content.Intent;
//import android.view.Menu;
//import android.view.View;
//import android.widget.Button;

public class Login extends Activity {
	public final static String username = "razorsquare.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void Overview(View view) {
		Context context= getApplicationContext();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		Intent intent = new Intent(Login.this, Overview.class);
		EditText uname = (EditText) findViewById(R.id.LoginText);
		String message = uname.getText().toString();
		// intent.putExtra(username, message);
		//EditText password = (EditText) findViewById(R.id.PasswordText);
		//String pass = password.getText().toString();
		editor.putString(username, message);
		//editor.putString("Password", pass);
		editor.commit();
		startActivity(intent);
	}

}
