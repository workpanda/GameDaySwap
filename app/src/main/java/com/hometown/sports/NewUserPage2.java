package com.hometown.sports;

import java.util.ArrayList;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewUserPage2 extends MenuBarActivity implements View.OnClickListener {
	Button buttonMakeUser, buttonBack;
	EditText CCNumber;
	TextView feedback;
	Spinner spinMonth, spinYear;
	ArrayList<String> monthList;
	ArrayList<String> yearList;
	int[] monthInts;
	int[] yearInts;
	String fName;
	String lName;
	String email;
	String pw;
	boolean isNewUser = true;
	SessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.newuser2);
		Intent in = getIntent();
		ActionBar actionBar = getActionBar();
        actionBar.show();
        session = new SessionManager(getApplicationContext());

		// Get JSON values from previous intent
		fName = in.getStringExtra("FirstName");
		lName = in.getStringExtra("LastName");
		email = in.getStringExtra("emailAddress");
		pw = in.getStringExtra("password");

		if (email.equals("")) {
			isNewUser = false;
		}

		buttonMakeUser = (Button) findViewById(R.id.buttonFinishUser);
		if (isNewUser) {
			buttonMakeUser.setText("Create Account!");
		} else {
			buttonMakeUser.setText("Update Credit Card");
		}
		buttonBack = (Button) findViewById(R.id.buttonCCBack);

		CCNumber = (EditText) findViewById(R.id.CCNumField);
		spinMonth = (Spinner) findViewById(R.id.spinnerMonth);
		spinYear = (Spinner) findViewById(R.id.spinnerYear);
		feedback = (TextView) findViewById(R.id.newUser2feedback);

		feedback.setText("");

		monthList = new ArrayList<String>();
		monthInts = new int[12];

		for (int i = 1; i < 13; i++) {

			String monthStr;
			monthStr = String.valueOf(i);

			monthList.add(monthStr);
			monthInts[i - 1] = i;

		}

		final ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, monthList);
		spinMonth.setAdapter(adapterMonth);

		yearList = new ArrayList<String>();
		yearInts = new int[10];

		for (int i = 2015; i < 2025; i++) {
			String yearStr;
			yearStr = String.valueOf(i);

			yearList.add(yearStr);
			yearInts[i - 2015] = i;

		}

		final ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, yearList);
		spinYear.setAdapter(adapterYear);

		buttonMakeUser.setOnClickListener(this);
		buttonBack.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.buttonFinishUser:
			long CCn;
			int mon;
			int yr;
			String CCnStr = CCNumber.getText().toString();

			if (!CCnStr.equals("")) {

				CCn = Long.parseLong(CCNumber.getText().toString());
				mon = monthInts[spinMonth.getSelectedItemPosition()];
				yr = monthInts[spinYear.getSelectedItemPosition()];
				
				UpdateUser update = new UpdateUser(CCn,mon,yr,isNewUser);
				update.execute();
			}

			break;
		case R.id.buttonCCBack:
			super.finish();
			break;

		}
	}
	
	public void showDialog(Activity activity, String title, CharSequence message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
    	builder.setMessage(message);
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int which) {
    			//close box
    		}
    		});
    	builder.show();
    }
	
	ProgressDialog pDialog;
	private class UpdateUser extends AsyncTask<Void, Void, Void> {

		long CCN;
		int mon;
		int yr;
		boolean isNewUser;
		int makeUserStatus;

		UpdateUser(Long c, int m, int y, boolean n) {
			CCN = c;
			mon = m;
			yr = y;
			isNewUser = n;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(NewUserPage2.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			JSONObject jOb = new JSONObject();

			try {
				jOb.put("userID", thisUser);
				jOb.put("CCN", CCN);
				jOb.put("year", yr);
				jOb.put("month", mon);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			WebContentGet webOb = new WebContentGet();

			makeUserStatus = webOb.updateUserCreditCard(jOb);

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
				showDialog(NewUserPage2.this, "User Update Error", "Email already in system");
				break;
			case USER_STATUS_INVALIDEMAIL:
				showDialog(NewUserPage2.this, "User Update Error", "Invalid Email Address");
				break;
			case USER_STATUS_INVALIDPW:
				showDialog(NewUserPage2.this, "User Update Error", "Invalid password");
				break;
			case USER_STATUS_CREATED:
				if(isNewUser){
					Toast.makeText(NewUserPage2.this, "Welcome to HomeTown Sports", Toast.LENGTH_LONG)
					.show();
					Intent in = new Intent(NewUserPage2.this, HomeTownSportsHome.class);
					startActivity(in);
				}
				else{
					Intent in2 = new Intent(NewUserPage2.this, AccountInfo.class);
					startActivity(in2);
				}
				break;
			}
		}
	}

}
