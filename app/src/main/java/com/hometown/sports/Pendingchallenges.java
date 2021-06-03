package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Pendingchallenges extends MenuBarActivity implements
		SimpleGestureListener {
	private SimpleGestureFilter detector;
	private ProgressDialog pDialog;

	JSONArray chalsin = new JSONArray();
	chalList chalListYouAccept = new chalList(1);
	chalList chalListOppAccept = new chalList(1);
	boolean isChallenger = true;
	boolean YouAcceptClickable = true;
	boolean OppAcceptClickable = true;
	Game currentGame;
	Challenge currentChal;
	ListView YouAcceptList;
	ListView OppAcceptList;
	TextView noBetTitle1;
	int numChalsIn1 = -1;
	TextView noChalTitle2;
	int numChalsIn2 = -1;

	int chalType;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> chalarraylist1;
	ArrayList<HashMap<String, String>> chalarraylist2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendingchallenges);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		Intent in = getIntent();

		noBetTitle1 = (TextView) findViewById(R.id.noChalsPend1);
		noBetTitle1.setText("");
		noChalTitle2 = (TextView) findViewById(R.id.noChalsPend2);
		noChalTitle2.setText("");

		// Get JSON values from previous intent
		chalType = in.getIntExtra("chalType", 0);
		String title = "";

		chalarraylist1 = new ArrayList<HashMap<String, String>>();
		chalarraylist2 = new ArrayList<HashMap<String, String>>();

		YouAcceptList = (ListView) findViewById(R.id.listYouAccept);
		OppAcceptList = (ListView) findViewById(R.id.listOppAccept);

		YouAcceptList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (YouAcceptClickable) {
					// getting values from selected ListItem
					selectedChal = chalListYouAccept.getChal(position);
					// Starting single contact activity
					Intent in = new Intent(getApplicationContext(),
							ConfirmationPage.class);

					startActivity(in);
				}

			}
		});

		OppAcceptList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("pending challenges", "In opponent accept click listener");

				if (OppAcceptClickable) {
					// getting values from selected ListItem
					selectedChal = chalListOppAccept.getChal(position);

					// Starting single contact activity
					Intent in = new Intent(getApplicationContext(),
							CancelPending.class);

					startActivity(in);

				}
			}
		});

		new Getchals().execute();
		detector = new SimpleGestureFilter(this, this);
		
		
	}
	
	public void onResume(){
		super.onResume();
		String ttl = "PendingChallenges";
		String txt = "Manage your pending challenges";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;

	}

	public class Getchals extends AsyncTask<Void, Void, Void> {

		Handler handle;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(Pendingchallenges.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			WebContentGet webOb = new WebContentGet();

			chalsin = webOb.ALPHA(CHAL_STATUS_ALLOPEN);

			if (chalsin != null) {
				numChalsIn1 = 0;
				numChalsIn2 = 0;
				parseAlpha(chalsin);

				for (int i = 0; i < currentChalList.getChals(); i++) {
					Challenge checkChal = currentChalList.getChal(i);
					int status = checkChal.getStatus();

					switch (status) {
					case CHAL_STATUS_OPENPUBLIC:
						chalListOppAccept.push(checkChal);
						numChalsIn2 = 1;
						break;
					case CHAL_STATUS_OPENFRIEND:
						int checkID = checkChal.getChallenger();
						if (checkID == thisUser) {
							chalListOppAccept.push(checkChal);
							numChalsIn2 = 1;
						} else {
							chalListYouAccept.push(checkChal);
							numChalsIn1 = 1;
						}
						break;
					}
				}
				// run the hash array converter thingy

				chalarraylist1 = chalList2Adapter(chalListYouAccept);
				chalarraylist2 = chalList2Adapter(chalListOppAccept);

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
			ListAdapter adapter1 = new ChalListAdapter(Pendingchallenges.this,
					R.layout.list_item, chalarraylist1);

			ListAdapter adapter2 = new ChalListAdapter(Pendingchallenges.this,
					R.layout.list_item, chalarraylist2);

			YouAcceptList.setAdapter(adapter1);
			OppAcceptList.setAdapter(adapter2);

			if (numChalsIn1 == -1) {
				noBetTitle1.setText("Communication Error");
			} else if (numChalsIn1 == 0) {
				noBetTitle1.setText("No posted challenges for this game");
			} else {
				noBetTitle1.setText("");
			}
			if (numChalsIn2 == -1) {
				noChalTitle2.setText("Communication Error");
			} else if (numChalsIn2 == 0) {
				noChalTitle2.setText("No posted challenges for this game");
			} else {
				noChalTitle2.setText("");
			}
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		String str = "";
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			Log.d("pending challenges", "swipe right Called");
			Intent in = new Intent(getApplicationContext(),
					HomeTownSportsHome.class);

			startActivity(in);
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

	@Override
	public void onBackPressed() {
		Log.d("pending challenges", "onBackPressed Called");
		Intent in = new Intent(getApplicationContext(),
				HomeTownSportsHome.class);

		startActivity(in);
	}
}