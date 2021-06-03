package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewUserPage1 extends MenuBarActivity implements
		View.OnClickListener {
	Button buttonContinue;
	EditText emailField, passwordField, passwordField2, FirstName, LastName;
	TextView feedback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newuser1);
		ActionBar actionBar = getActionBar();
		actionBar.show();

		buttonContinue = (Button) findViewById(R.id.buttonNext);
		emailField = (EditText) findViewById(R.id.emailField2);

		passwordField = (EditText) findViewById(R.id.passwordField2);
		passwordField2 = (EditText) findViewById(R.id.password2Field2);
		feedback = (TextView) findViewById(R.id.newUser1feedback);
		FirstName = (EditText) findViewById(R.id.firstNameField);
		LastName = (EditText) findViewById(R.id.lastNameField);
		feedback.setText("");

		buttonContinue.setOnClickListener(this);

		FirstName.requestFocus();

		FirstName.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent e) {
				if ((e.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					LastName.requestFocus();
					return true;
				}
				return false;
			}

		});

		LastName.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent e) {
				if ((e.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					emailField.requestFocus();
					return true;
				}
				return false;
			}

		});

		emailField.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent e) {
				if ((e.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					passwordField.requestFocus();
					return true;
				}
				return false;
			}

		});

		passwordField.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent e) {
				if ((e.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					passwordField2.requestFocus();
					return true;
				}
				return false;
			}

		});

		passwordField2.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent e) {
				if ((e.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {

					return true;
				}
				return false;
			}

		});

	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.buttonNext:

			String emailIn;
			String pwIn;
			String fName;
			String lName;
			String pwIn2;

			emailIn = emailField.getText().toString();
			emailIn = emailIn.trim();
			emailIn = emailIn.toUpperCase();
			pwIn = passwordField.getText().toString();
			pwIn = pwIn.trim();
			pwIn2 = passwordField2.getText().toString();
			pwIn2 = pwIn2.trim();
			fName = FirstName.getText().toString();
			fName = fName.trim();
			lName = LastName.getText().toString();
			lName = lName.trim();

			int proceed = 1;

			if (emailIn.equals("")) {
				proceed = 0;
			}
			if (pwIn.equals("")) {
				proceed = 0;
			}
			if (fName.equals("")) {
				proceed = 0;
			}
			if (lName.equals("")) {
				proceed = 0;
			}
			if (pwIn2.equals("")) {
				proceed = 0;
			}

			if (proceed == 0) {
				showDialog(this, "User Create Error",
						"Please fill in all fields");
				break;
			}

			if (pwIn.length() < 8) {
				showDialog(this, "User Create Error",
						"Password must be 8 characters long");
				break;
			}

			if (!pwIn.equals(pwIn2)) {
				showDialog(this, "User Create Error", "Passwords do not match");
				break;
			}

			
			MakeUser make = new MakeUser(emailIn,pwIn,fName,lName);
			make.execute();
			

			break;

		}
	}

	public void showDialog(Activity activity, String title, CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// close box
			}
		});
		builder.show();
	}

	ProgressDialog pDialog;
	private class MakeUser extends AsyncTask<Void, Void, Void> {

		String email;
		String password;
		String fName;
		String lName;
		int makeUserStatus;

		MakeUser(String e, String pw, String fn, String ln) {
			email = e;
			password = pw;
			fName = fn;
			lName = ln;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(NewUserPage1.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			JSONObject j = new JSONObject();
			
			User u = new User(email,password, fName,lName);
			u.makeCreateJSON(j);

			WebContentGet webOb = new WebContentGet();

			makeUserStatus = webOb.makeUser(j);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			
			switch(makeUserStatus){
			case USER_STATUS_PREEXISTINGEMAIL:
				showDialog(NewUserPage1.this, "User Create Error", "Email already in system");
				break;
			case USER_STATUS_INVALIDEMAIL:
				showDialog(NewUserPage1.this, "User Create Error", "Invalid Email Address");
				break;
			case USER_STATUS_INVALIDPW:
				showDialog(NewUserPage1.this, "User Create Error", "Invalid password");
				break;
			case USER_STATUS_CREATED:
				Intent in = new Intent(NewUserPage1.this, CreditCardPage.class);
				in.putExtra("FirstName", fName);
				in.putExtra("LastName", lName);
				in.putExtra("emailAddress", email);
				in.putExtra("password", password);
				session.createLoginSession(email, password);

				startActivity(in);
				break;
			}
		}
	}
}
