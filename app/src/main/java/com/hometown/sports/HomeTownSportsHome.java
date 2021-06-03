package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HomeTownSportsHome extends MenuBarActivity {
	Button button1;
	TextView WelcomeLine, pendingTitle, activeTitle;
	//SessionManager session;
	ListView activeList, pendingList;
	private ProgressDialog pDialog, pDialog2;
	JSONArray chalsin = new JSONArray();
	chalList chalListPending = new chalList(1);
	chalList chalListPending2 = new chalList(1);
	chalList chalListActive = new chalList(1);
	boolean isChallenger = true;
	boolean YouAcceptClickable = true;
	boolean OppAcceptClickable = true;
	int numchalsin1 = -1;
	int numchalsin2 = -1;
	int chalID;
	int challengerID;
	int acpID;
	String chalName;
	String acpName;
	double wager;
	int winner;
	double line;
	int betType;
	int bStatus;
	Challenge useChal;
	// Hashmap for ListView
	ArrayList<HashMap<String, String>> chalarraylist1;
	ArrayList<HashMap<String, String>> chalarraylist2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (!isTaskRoot()) {
		    final Intent intent = getIntent();
		    if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
		        Log.d("Main Activity is not the root.","Finishing Main Activity instead of launching.");
		        finish();
		        return;       
		    }
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hometownsports_home);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		long unixTime = System.currentTimeMillis() / 1000L;
		session = new SessionManager(getApplicationContext());
		assignSession(session);
		// for server add unixTime to checkLoginarg
		Log.d("HOMETOWN HOME","checking Login");
		session.checkLogin(unixTime);

		WelcomeLine = (TextView) findViewById(R.id.UserChalListTitle);
		activeTitle = (TextView) findViewById(R.id.homeactivetitle);
		pendingTitle = (TextView) findViewById(R.id.homependingtitle);

		new getUserAccountInfo().execute();

		chalarraylist1 = new ArrayList<HashMap<String, String>>();
		chalarraylist2 = new ArrayList<HashMap<String, String>>();
		pendingList = (ListView) findViewById(R.id.homePending);
		activeList = (ListView) findViewById(R.id.homeActive);

		pendingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (YouAcceptClickable) {
					// getting values from selected ListItem
					setSelectedChal(chalListPending.getChal(position));
					// Starting single contact activity
					useChal = chalListPending.getChal(position);
					
					Intent in = new Intent(getApplicationContext(),
							ConfirmationPage.class);
					
					in.putExtra(TAG_ACCEPTER, useChal.getAccepter());
					in.putExtra(TAG_CHALLENGER, useChal.getChallenger());
					in.putExtra(TAG_HOME1, useChal.getHomeRoster());
					in.putExtra(TAG_HOME2, useChal.getHomeRoster2());
					in.putExtra(TAG_AWAY1, useChal.getAwayRoster());
					in.putExtra(TAG_AWAY2, useChal.getAwayRoster2());
					in.putExtra(TAG_HOMEP1, useChal.getPlayerID(1));
					in.putExtra(TAG_HOMEP2, useChal.getPlayerID(2));
					in.putExtra(TAG_AWAYP1, useChal.getPlayerID(3));
					in.putExtra(TAG_AWAYP2, useChal.getPlayerID(4));
					in.putExtra(TAG_WINNER, useChal.getWinner());
					in.putExtra(TAG_LINE, useChal.getLine());
					in.putExtra(TAG_VALUE, useChal.getWager());
					in.putExtra(TAG_STATUS, useChal.getStatus());
					in.putExtra(TAG_ID, useChal.getChalID());

					startActivity(in);

				}
			}
		});
		final Context context = this;

		activeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (OppAcceptClickable) {
					// getting values from selected ListItem
					useChal = chalListActive.getChal(position);
					Intent intent1 = new Intent(context,
							SingleChalActivity.class);
					
					intent1.putExtra(TAG_ACCEPTER, useChal.getAccepter());
					intent1.putExtra(TAG_CHALLENGER, useChal.getChallenger());
					intent1.putExtra(TAG_HOME1, useChal.getHomeRoster());
					intent1.putExtra(TAG_HOME2, useChal.getHomeRoster2());
					intent1.putExtra(TAG_AWAY1, useChal.getAwayRoster());
					intent1.putExtra(TAG_AWAY2, useChal.getAwayRoster2());
					intent1.putExtra(TAG_HOMEP1, useChal.getPlayerID(1));
					intent1.putExtra(TAG_HOMEP2, useChal.getPlayerID(2));
					intent1.putExtra(TAG_AWAYP1, useChal.getPlayerID(3));
					intent1.putExtra(TAG_AWAYP2, useChal.getPlayerID(4));
					intent1.putExtra(TAG_WINNER, useChal.getWinner());
					intent1.putExtra(TAG_LINE, useChal.getLine());
					intent1.putExtra(TAG_VALUE, useChal.getWager());
					intent1.putExtra(TAG_STATUS, useChal.getStatus());
					intent1.putExtra(TAG_ID, useChal.getChalID());
					startActivity(intent1);


				}
			}
		});

		// ServerVersion
		new Getchals2().execute();

	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Welcome to Game Day Swap";
		String txt = "Navigate between different screens using the bottom bar, the full menu in the top corner, or any of the clickable lists";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
		Log.d("HOMETOWN HOME onResume","bottom bar values set");
	}
	
	public void onPause(){
		super.onPause();
		Log.d("HOMETOWN HOME onPause","start gc");
		System.gc();
		Log.d("HOMETOWN HOME onPause","end gc");
	}
/*
	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.challenge:
			Intent intent1 = new Intent(context, Challenge.class);
			startActivity(intent1);
			break;
		}
	}
*/
	public class Getchals2 extends AsyncTask<Void, Void, Void> {

		Handler handle;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(HomeTownSportsHome.this);
			pDialog.setMessage("Please wait, Trying to connect to web...");
			pDialog.setCancelable(false);
			pDialog.show();
			Log.d("HOMETOWN HOME","getchals2 started");

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			
			
			WebContentGet webOb = new WebContentGet();
			Log.d("HOMETOWN HOME","getchals calling alpha");
			chalsin = webOb.ALPHA(CHAL_STATUS_ALLOPENACCEPTED);

			if (chalsin != null) {
				numchalsin1 = 0;
				numchalsin2 = 0;
				parseAlpha(chalsin);

				for (int i = 0; i < currentChalList.getChals(); i++) {
					Challenge checkChal = currentChalList.getChal(i);
					int status = checkChal.getStatus();

					switch (status) {
					case CHAL_STATUS_OPENPUBLIC:
						chalListPending.push(checkChal);
						numchalsin1 = 1;
						break;
					case CHAL_STATUS_ACCEPTEDPUBLIC:
						chalListActive.push(checkChal);
						numchalsin2 = 1;
						break;
					case CHAL_STATUS_OPENFRIEND:
						int checkID = checkChal.getChallenger();
						if (checkID == thisUser)
							chalListPending2.push(checkChal);
						else {
							chalListPending.push(checkChal);
							numchalsin1 = 1;
						}
						break;
					case CHAL_STATUS_ACCEPTEDFRIEND:
						chalListActive.push(checkChal);
						numchalsin2 = 1;
						break;
					}
				}

				// run the hash array converter thingy

				chalarraylist1 = chalList2Adapter(chalListPending);
				chalarraylist2 = chalList2Adapter(chalListActive);
			}
			
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter1 = new ChalListAdapter(HomeTownSportsHome.this,
					R.layout.list_item, chalarraylist1);

			ListAdapter adapter2 = new ChalListAdapter(HomeTownSportsHome.this,
					R.layout.list_item, chalarraylist2);

			pendingList.setAdapter(adapter1);
			activeList.setAdapter(adapter2);

			if (numchalsin1 == -1) {
				pendingTitle.setText("Pending Challenges: Communication Error");
			} else if (numchalsin1 == 0) {
				pendingTitle.setText("Pending Challenges: You have no Pending Bets");
			} else {
				pendingTitle.setText("Pending Challenges:");
			}
			if (numchalsin2 == -1) {
				activeTitle.setText("Active Challenges: Communication Error");
			} else if (numchalsin2 == 0) {
				activeTitle.setText("Active Challenges: you have no Active Bets");
			} else {
				activeTitle.setText("Active Challenges:");
			}
			Log.d("HOMETOWN HOME","get chals leaving");
		}

	}

	private class getUserAccountInfo extends AsyncTask<Void, Void, Void> {

		Handler handle;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog2 = new ProgressDialog(HomeTownSportsHome.this);
			pDialog2.setMessage("Please wait...");
			pDialog2.setCancelable(false);
			pDialog2.show();
			Log.d("HOMETOWN HOME","getUserAccountInfo starting");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			WebContentGet webOb = new WebContentGet(HomeTownSportsHome.this);
			Log.d("HOMETOWN HOME","calling getUserAccountInfo");
			JSONObject jOB = webOb.getUserAccountInfo(thisUser);
			Log.d("HOMEPAGE String output of userinfo JSON",jOB.toString());
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
				//userCCND = jOB.getString("CCNDisp");
				userEml = jOB.getString("emailAddress");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setUserName(userFName, userLName);
			setUserAcctInfo(userFreeAct, userFrozAct, userCCND, userEml);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog2.isShowing())
				pDialog2.dismiss();

			String userName = userFName;
			String sendStr = "Welcome " + userName;
			WelcomeLine.setText(sendStr);
			Log.d("HOMETOWN HOME","leaving get user accountinfo");

		}
	}

	@Override
	public void onBackPressed() {

	}
}
