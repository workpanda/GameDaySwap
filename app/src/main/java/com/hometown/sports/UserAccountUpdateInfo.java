package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserAccountUpdateInfo extends MenuBarActivity implements
		View.OnClickListener {
	Button buttonUpdate, buttonBack;
	EditText emailField, FirstName, LastName;
	TextView feedback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changeuserinfo);
		ActionBar actionBar = getActionBar();
		actionBar.show();

		buttonUpdate = (Button) findViewById(R.id.buttonUpdateUserAccount);
		buttonBack = (Button) findViewById(R.id.buttonUpdateBack);
		emailField = (EditText) findViewById(R.id.editTextEmailAddress);

		feedback = (TextView) findViewById(R.id.textUserAcctUpdateFeedback);
		FirstName = (EditText) findViewById(R.id.editTextFirstName);
		LastName = (EditText) findViewById(R.id.editTextLastName);
		feedback.setText("");

		buttonUpdate.setOnClickListener(this);
		buttonBack.setOnClickListener(this);

		FirstName.setText(userFName);
		LastName.setText(userLName);
		emailField.setText(userEmail);

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
					return true;
				}
				return false;
			}

		});
		
		

	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Update Account";
		String txt = "Correct or change any personal information";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.buttonUpdateUserAccount:

			String emailIn;
			String fName;
			String lName;

			emailIn = emailField.getText().toString();
			emailIn = emailIn.trim();
			emailIn = emailIn.toUpperCase();
			fName = FirstName.getText().toString();
			fName = fName.trim();
			lName = LastName.getText().toString();
			lName = lName.trim();

			int proceed = 1;

			if (emailIn.equals("")) {
				proceed = 0;
			}
			if (fName.equals("")) {
				proceed = 0;
			}
			if (lName.equals("")) {
				proceed = 0;
			}

			if (proceed == 0) {
				feedback.setText("Please fill out all fields");
				break;
			}

			if (!emailIn.equals(userEmail)) {
				
				UpdateUserCall u = new UpdateUserCall(emailIn, fName,lName);
				u.execute();

			}

			
			break;

		case R.id.buttonUpdateBack:
			super.finish();
			break;

		}
	}
	
	ProgressDialog pDialog;
	private class UpdateUserCall extends AsyncTask<Void, Void, Void> {

		String email;
		String fName;
		String lName;
		int updateStatus;
		
		UpdateUserCall(String e, String f, String l){
			email = e;
			fName = f;
			lName = l;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(UserAccountUpdateInfo.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			JSONObject jOb = new JSONObject();

			try {
				jOb.put("userID", thisUser);
				jOb.put("firstName", fName);
				jOb.put("lastName", lName);
				jOb.put("emailAddress", email);
				jOb.put("address1", "");
				jOb.put("address2", "");
				jOb.put("city", "");
				jOb.put("state", "");
				jOb.put("zipCode", 0);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			WebContentGet webOb = new WebContentGet();

			updateStatus = webOb.updateUserPersonalInfo(jOb);

			return null;

			
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			
			switch(updateStatus){
			case USER_STATUS_PREEXISTINGEMAIL:
				feedback.setText("Email Address already registered");
				break;
			case USER_STATUS_INVALIDEMAIL:
				feedback.setText("Invalid Email Address");
				break;
			case USER_STATUS_INVALIDPW:
				feedback.setText("Invalid password");
				break;
			case USER_STATUS_CREATED:
				Intent in2 = new Intent(UserAccountUpdateInfo.this, AccountInfo.class);
				startActivity(in2);
				break;
			}
		}
	}
}
