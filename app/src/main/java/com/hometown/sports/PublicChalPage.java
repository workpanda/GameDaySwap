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

public class PublicChalPage extends MenuBarActivity implements
		SimpleGestureListener {

	private ProgressDialog pDialog;

	// URL to get contacts JSON

	private SimpleGestureFilter detector;
	// JSON Node names

	JSONArray chalsin = new JSONArray();
	chalList chalListIn = new chalList(1);
	gameList gameListIn = new gameList(1);
	Game currentGame;
	Challenge currentChal;
	TextView TextTitle;
	TextView noChalTitle;
	int numchalsin = -1;
	int gameID;
	ListView lv;

	int chalType;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> chalarraylist;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publicchalpage);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		TextTitle = (TextView) findViewById(R.id.PublicChalListTitle);
		noChalTitle = (TextView) findViewById(R.id.noChalText);
		noChalTitle.setText("");
		Intent in = getIntent();

		// Get JSON values from previous intent
		chalType = in.getIntExtra("chalType", 0);
		gameID = in.getIntExtra("gameID", 0);
		Game tempGame = selectedGame;
		String title = "";

		title = currentTeamList.getTeamByID(currentRosterList.getRosterByID(tempGame.getAwayID()).getTeamID()).getDisplayName() + " at " + currentTeamList.getTeamByID(currentRosterList.getRosterByID(tempGame.getHomeID()).getTeamID()).getDisplayName() ;

		TextTitle.setText(title);

		chalarraylist = new ArrayList<HashMap<String, String>>();

		lv = (ListView) findViewById(R.id.publicChalList);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				selectedChal = chalListIn.getChal(position);
				int chalID = selectedChal.getChalID();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						MakeChalConfirmation.class);
				in.putExtra("previousPublicBetID", chalID);

				startActivity(in);

			}
		});

		new Getchals().execute();
		
		

		detector = new SimpleGestureFilter(this, this);
	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Public Challenges";
		String txt = "Accept challenges posted by HTS users you may not know";
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
			pDialog = new ProgressDialog(PublicChalPage.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			WebContentGet webOb = new WebContentGet();
			//chalsin = webOb.getPublicChalList(gameID);
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
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new ChalListAdapter(PublicChalPage.this,
					R.layout.list_item, chalarraylist);

			lv.setAdapter(adapter);
			if (numchalsin == -1) {
				noChalTitle.setText("Communication Error");
			} else if (numchalsin == 0) {
				noChalTitle.setText("No posted bets for this game");
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
