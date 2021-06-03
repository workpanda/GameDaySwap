package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
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

public class UserChalListPage extends MenuBarActivity implements
		SimpleGestureListener {

	private ProgressDialog pDialog;

	// URL to get contacts JSON

	private SimpleGestureFilter detector;
	// JSON Node names

	JSONArray chalsin = new JSONArray();
	chalList chalListIn = new chalList(1);
	boolean isChallenger = true;
	Challenge currentChal;
	TextView TextTitle;
	TextView noChalTitle;
	int numchalsin = -1;
	ListView lv;

	int chalType;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> chalarraylist;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userchalpage);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		TextTitle = (TextView) findViewById(R.id.UserChalListTitle);
		noChalTitle = (TextView) findViewById(R.id.noChalText1);
		noChalTitle.setText("");

		Intent in = getIntent();

		// Get JSON values from previous intent
		chalType = in.getIntExtra("chalType", 0);
		String title = "";
		switch (chalType) {
		case (CHAL_STATUS_ALLOPEN):
			// pending
			title = "Pending Challenges";
			break;
		case (CHAL_STATUS_ALLACCEPTED):
			// active
			title = "Active Challenges";
			break;
		case (CHAL_STATUS_ALLCLOSED):
			// complete
			title = "Completed Challenges";
			break;
		}

		TextTitle.setText(title);

		chalarraylist = new ArrayList<HashMap<String, String>>();
		final Context context = this;
		// Log.d("pro hockey", "hockeygamelist made");

		lv = (ListView) findViewById(R.id.userChalList);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Challenge thischal = chalListIn.getChal(position);
				selectedChal = thischal;
				Intent intent1 = new Intent(context, SingleChalActivity.class);
				startActivity(intent1);
			}
		});

		new Getchals().execute();
		
		String ttl = "Your Challenges:";
		String txt = "Review your challenges";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(chalType);

		detector = new SimpleGestureFilter(this, this);
	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Your Challenges:";
		String txt = "Review your challenges";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(chalType);
		currentActivity = this;
	}

	public class Getchals extends AsyncTask<Void, Void, Void> {
		
		Handler handle;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(UserChalListPage.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			WebContentGet webOb = new WebContentGet();
			chalsin = webOb.ALPHA(chalType);
			if (chalsin != null) {
				numchalsin = 0;
				parseAlpha(chalsin);
				if(currentChalList.getChals()>0){
					numchalsin = 1;
					chalarraylist = chalList2Adapter(currentChalList);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Log.d("pro hockey", "inpostexecute");
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new ChalListAdapter(UserChalListPage.this,
					R.layout.list_item, chalarraylist);

			lv.setAdapter(adapter);
			if (numchalsin == -1) {
				noChalTitle.setText("Communication Error");
			} else if (numchalsin == 0) {
				noChalTitle
						.setText("You have no Active Challenges at the moment - Go challenge your friends!");
			} else {
				noChalTitle.setText("");
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
			super.finish();
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
}
