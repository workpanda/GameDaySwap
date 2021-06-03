package com.hometown.sports;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.example.*;

public class AccountInfo extends MenuBarActivity implements
		SimpleGestureListener, View.OnClickListener {
	final String TAG = getClass().getName();
	TextView FNameText, LNameText;
	TextView CCNText;
	TextView FreeMoneyText;
	TextView FrozenMoneyText;
	TextView emailText;
	Button btnWithdrawl;
	Button btnUpdateCC;
	Button btnUpdateUserAct;
	Button btnChangePW;
	Button btnDeposit;
	double depositValue;

	private SimpleGestureFilter detector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountinformation);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		detector = new SimpleGestureFilter(this, this);


		btnWithdrawl = (Button) findViewById(R.id.buttonWithdrawl);
		btnDeposit = (Button) findViewById(R.id.buttonDeposit);
		btnUpdateCC = (Button) findViewById(R.id.buttonChangeCCN);
		btnUpdateUserAct = (Button) findViewById(R.id.buttonEditInfo);
		btnChangePW = (Button) findViewById(R.id.buttonChangePW);

		btnDeposit.setOnClickListener(this);
		btnWithdrawl.setOnClickListener(this);
		btnUpdateCC.setOnClickListener(this);
		btnUpdateUserAct.setOnClickListener(this);
		btnChangePW.setOnClickListener(this);
		
		FNameText = (TextView) findViewById(R.id.textViewFirstName);
		LNameText = (TextView) findViewById(R.id.textViewLastName);
		FrozenMoneyText = (TextView) findViewById(R.id.textViewAccountFrozen);
		CCNText = (TextView) findViewById(R.id.textViewCreditCardSet);
		emailText = (TextView) findViewById(R.id.textViewEmailSet);
		FreeMoneyText = (TextView) findViewById(R.id.textViewAccountFree);

		// SERVER CALL
		new getUserAccountInfo().execute();
		

	}
	public void onResume(){
		super.onResume();
		String ttl = "Account";
		String txt = "Review information, Make a deposit or withdrawal";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.buttonWithdrawl:
			showDialog2(this, "Withdrawal:",
					"Would you like to withdrawal all funds to stored bank account?");
			break;
		case R.id.buttonChangeCCN:
			Intent in = new Intent(context, CreditCardPage.class);
			in.putExtra("fromAccount", true);
			in.putExtra("LastName", "");
			in.putExtra("emailAddress", "");
			in.putExtra("password", "");
			startActivity(in);
			break;
		case R.id.buttonEditInfo:
			Intent in1 = new Intent(context, UserAccountUpdateInfo.class);
			startActivity(in1);
			break;
		case R.id.buttonChangePW:
			Intent in2 = new Intent(context, NewPassword.class);
			startActivity(in2);
			break;
		case R.id.buttonDeposit:
			
			getDepositAmount();
			
			break;
		}

	}

	private void getDepositAmount() {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
		builder.setTitle("Deposit");
		LayoutInflater inflater = this.getLayoutInflater();
		View displayView = inflater.inflate(R.layout.deposit_form, null);
		builder.setView(displayView);
		final Spinner depositSpin = (Spinner) displayView.findViewById(R.id.depositSpinner);
		final double[] values = new double[5];
		values[0] = 6.5;
		values[1] = 11.5;
		values[2] = 28.75;
		values[3] = 57.5;
		values[4] = 115;
		ArrayList<String> setList = new ArrayList<String>();
		setList.add("$5");
		setList.add("$10");
		setList.add("$25");
		setList.add("$50");
		setList.add("$100");
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, R.layout.spinnerlayout, setList);
		
		depositSpin.setAdapter(adapter);
		builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						depositValue = values[depositSpin.getSelectedItemPosition()];
						WebContentGet webOb = new WebContentGet();
						int stat = webOb.makeCharge(depositValue);
						
						if(stat == 71){
							Toast.makeText(getBaseContext(),
						            "Deposit successful",
						            Toast.LENGTH_LONG
						          ).show();
						}
						else{
							Toast.makeText(getBaseContext(),
						            "Could not complete deposit",
						            Toast.LENGTH_LONG
						          ).show();
						}
					}
				});

		builder.create();
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		final Context context = this;
		String str = "";
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			Intent in2 = new Intent(context, HomeTownSportsHome.class);
			startActivity(in2);
			break;
		case SimpleGestureFilter.SWIPE_LEFT:

			break;
		case SimpleGestureFilter.SWIPE_DOWN:
			break;
		case SimpleGestureFilter.SWIPE_UP:
			break;
		}
	}

	@Override
	public void onDoubleTap() {
	}

	public void onBackPressed() {
		final Context context = this;
		Intent in2 = new Intent(context, HomeTownSportsHome.class);
		startActivity(in2);
	}

	public void showDialog2(Activity activity, String title,
			CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("Withdrawal",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						FreeMoneyText.setText("$0");
						setUserAcctMoney(0,
								userFrozenAccount);
						Toast.makeText(AccountInfo.this, "Withdrawal Made",
								Toast.LENGTH_LONG).show();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// close box
					}
				});
		builder.show();
	}

	private ProgressDialog pDialog7;

	private class getUserAccountInfo extends AsyncTask<Void, Void, Void> {
		String freeM, frozeM, CCNStr, uemail;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog7 = new ProgressDialog(AccountInfo.this);
			pDialog7.setMessage("Please wait...");
			pDialog7.setCancelable(false);
			pDialog7.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			WebContentGet webOb = new WebContentGet();
			JSONObject jOB = webOb.getUserAccountInfo(thisUser);
			String userFName = "";
			String userLName = "";

			double userFreeAct = 0;
			double userFrozAct = 0;
			String userCCND = "";
			String userEml = "";

			try {
				userFName = jOB.getString("firstName");
				userLName = jOB.getString("lastName");
				userFreeAct = Double.parseDouble(jOB.getString("availableMoney"));
				userFrozAct = Double
						.parseDouble(jOB.getString("frozenMoney"));
				
				userEml = jOB.getString("emailAddress");
				userCCND = jOB.getString("CCNDisp");
			} catch (JSONException e) {
				userCCND = session.getCard();
			}
			setUserName(userFName, userLName);
			setUserAcctInfo(userFreeAct, userFrozAct, userCCND,
					userEml);

			
			

			
			String freeMoney = "$"
					+ String.valueOf(userFreeAccount);
			freeM = freeMoney;
			

			
			String frozeMoney = "$"
					+ String.valueOf(userFrozenAccount);
			frozeM = frozeMoney;
			

			
			String CCNdisp = userCCNDisp;
			if(CCNdisp.length()>2) CCNStr= "*******" + CCNdisp;
			else CCNStr = "No card on file";
			

			
			uemail = userEmail;
			

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			FNameText.setText(userFName);
			LNameText.setText(userLName);
			FreeMoneyText.setText(freeM);
			FrozenMoneyText.setText(frozeM);
			CCNText.setText(CCNStr);
			emailText.setText(uemail);
			// Dismiss the progress dialog
			if (pDialog7.isShowing())
				pDialog7.dismiss();

		}
	}

}
