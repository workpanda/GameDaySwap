package com.hometown.sports;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CancelPending extends MenuBarActivity implements
		SimpleGestureListener, View.OnClickListener {
	private SimpleGestureFilter detector;
	TextView lineText, opponentText, wagerText;
	Button cancelBet;
	String comboString;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		ActionBar actionBar = getActionBar();
		actionBar.show();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cancelpendinglayout);
		detector = new SimpleGestureFilter(this, this);
		lineText = (TextView) findViewById(R.id.confirmTextLine);
		opponentText = (TextView) findViewById(R.id.confirmTextOpponent);
		wagerText = (TextView) findViewById(R.id.confirmTextWager);
		cancelBet = (Button) findViewById(R.id.buttonCancelPending);

		cancelBet.setOnClickListener(this);

		convertToChalDisplay();
		comboString = "You win if: \n" + getDetailChalDisplay();
		lineText.setText(getChalDisplayString());
		opponentText.setText(getOpponentString());
		wagerText.setText(getWagerDisplayString());

		Team h;
		Team a;
		int spid = 0;
		long time = 0;
		int date = 0;
		
		h = currentTeamList.getTeamByID(currentRosterList.getRosterByID(selectedChal.getHomeRoster()).getTeamID());
		a = currentTeamList.getTeamByID(currentRosterList.getRosterByID(selectedChal.getAwayRoster()).getTeamID());
		
		TextView awayName = (TextView) findViewById(R.id.awayTeamCounter);
		TextView homeName = (TextView) findViewById(R.id.homeTeamCounter);

		awayName.setText(a.getDisplayName());
		homeName.setText(h.getDisplayName());

		ImageView awayLogo = (ImageView) findViewById(R.id.counterAwayLogo);
		ImageView homeLogo = (ImageView) findViewById(R.id.counterHomeLogo);

		String homeIcon = h.getLogo();
		String awayIcon = a.getLogo();

		homeLogo.setImageDrawable(getResources().getDrawable(
				getResources().getIdentifier("drawable/" + homeIcon,
						"drawable", getPackageName())));
		awayLogo.setImageDrawable(getResources().getDrawable(
				getResources().getIdentifier("drawable/" + awayIcon,
						"drawable", getPackageName())));

	}
	
	public void onResume(){
		super.onResume();
		String ttl = "This Challenge Explained:";
		String txt = comboString;
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buttonCancelPending:
			showDialog2(this, "Confirm:", "Are you sure you want to cancel?");

			break;

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

	public void showDialog2(Activity activity, String title,
			CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("Cancel Challenge",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new CallCancel().execute();
					}
				});
		builder.setNegativeButton("Back",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// close box
					}
				});
		builder.show();
	}
	
	ProgressDialog pDialog;
	public class CallCancel extends AsyncTask<Void, Void, Void> {

		int r;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(CancelPending.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			WebContentGet webOb = new WebContentGet();
			r = webOb.cancelChal(selectedChal.getChalID());
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			
			switch (r) {
			case (CHAL_STATUS_FAILED):
				Toast.makeText(CancelPending.this, "Could not Communicate", Toast.LENGTH_LONG)
						.show();
				break;
			case (0):
				Toast.makeText(CancelPending.this, "Game has started, not accepting challenges",
						Toast.LENGTH_LONG).show();
				break;
			case (CHAL_STATUS_SUCCESSFUL):
				Toast.makeText(CancelPending.this, "Cancellation Made", Toast.LENGTH_LONG).show();
				break;
			}
			Intent intent1 = new Intent(CancelPending.this, HomeTownSportsHome.class);
			startActivity(intent1);
		}
	}
}