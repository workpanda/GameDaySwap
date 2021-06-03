package com.hometown.sports;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.ConfirmationPage.makePayActivity;
import com.hometown.sports.MenuBarActivity.DownloadImageTask;
import com.hometown.sports.MenuBarActivity.RosterDisplay;
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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MakeChalConfirmation extends MenuBarActivity implements
		SimpleGestureListener, View.OnClickListener {
	private SimpleGestureFilter detector;

	TextView gameText, lineText, opponentText, wagerText, homePlayerIn, homePlayerOut, homePosIn, homePosOut,homeTeamIn, homeTeamOut, awayPlayerIn, awayPlayerOut, awayPosIn, awayPosOut,awayTeamIn, awayTeamOut;
	Button makeBet, declineBet;
	Game addGame;
	int homeRosterID, playH1, playH2, playA1, playA2;
	int awayRosterID;
	int homeRoster2ID;
	int awayRoster2ID;
	int homePlayerAdd;
	int homePlayer2Add;
	int awayPlayerAdd;
	int awayPlayer2Add;
	int previousChalID;
	int chalStat;
	int accepter;
	int isPublic;
	int winner;
	double line, wager;
	String comboString;
	private ProgressDialog pDialog, pDialog2, pDialog3;
	int returnStatus;
	Challenge useChal = getSelectedChal();
	int[] players = new int[4];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.makechalconfirmation);
		Intent in = getIntent();
		chalStat = in.getIntExtra(TAG_STATUS, 0);
		accepter = in.getIntExtra(TAG_ACCEPTER, 0);
		homeRosterID = in.getIntExtra(TAG_HOME1, 0);
		homeRoster2ID = in.getIntExtra(TAG_HOME2, 0);
		awayRosterID = in.getIntExtra(TAG_AWAY1, 0);
		awayRoster2ID = in.getIntExtra(TAG_AWAY2, 0);
		playH1 = in.getIntExtra(TAG_HOMEP1, 0);
		playH2 = in.getIntExtra(TAG_HOMEP2, 0);
		playA1 = in.getIntExtra(TAG_AWAYP1, 0);
		playA2 = in.getIntExtra(TAG_AWAYP2, 0);
		winner = in.getIntExtra(TAG_WINNER, 0);
		line = in.getDoubleExtra(TAG_LINE, 0.5);
		wager = in.getIntExtra(TAG_VALUE, 1);
		useChal = new Challenge(0, thisUser, accepter, homeRosterID, playH1,
				homeRoster2ID, playH2, awayRosterID, playA1, awayRoster2ID,
				playA2, winner, line, wager, chalStat);
		setSelectedChal(useChal);
		detector = new SimpleGestureFilter(this, this);
		ActionBar actionBar = getActionBar();
		actionBar.show();

		lineText = (TextView) findViewById(R.id.confirmTextLine);
		opponentText = (TextView) findViewById(R.id.confirmTextOpponent);
		wagerText = (TextView) findViewById(R.id.confirmTextWager);
		makeBet = (Button) findViewById(R.id.buttonMakeConfirmedChal);
		
		makeBet.setOnClickListener(this);

		// Get JSON values from previous intent
		previousChalID = in.getIntExtra(TAG_ID, 0);
		isPublic = in.getIntExtra("previousPublicBetID", 0);

		convertToChalDisplay(useChal); // NULL
		comboString = "You win if: \n" + getDetailChalDisplay();
		lineText.setText(getChalDisplayString());
		opponentText.setText(getOpponentString());
		wagerText.setText(getWagerDisplayString());

		Team h;
		Team a;

		h = currentTeamList.getTeamByID(currentRosterList.getRosterByID(
				homeRosterID).getTeamID());
		a = currentTeamList.getTeamByID(currentRosterList.getRosterByID(
				awayRosterID).getTeamID());

		ImageView awayLogo = (ImageView) findViewById(R.id.counterAwayLogo);
		ImageView homeLogo = (ImageView) findViewById(R.id.counterHomeLogo);

		String homeIcon = h.getLogo();
		String awayIcon = a.getLogo();

		new DownloadImageTask(homeLogo).execute(homeIcon);
		new DownloadImageTask(awayLogo).execute(awayIcon);
		
		homePlayerIn = (TextView) findViewById(R.id.homeInPlayer);
		homePlayerOut = (TextView) findViewById(R.id.homeOutPlayer);
		homePosIn = (TextView) findViewById(R.id.homeInPos);
		homePosOut = (TextView) findViewById(R.id.homeOutPos);
		homeTeamIn = (TextView) findViewById(R.id.homeInTeam);
		homeTeamOut = (TextView) findViewById(R.id.homeOutTeam);
		awayPlayerIn = (TextView) findViewById(R.id.awayInPlayer);
		awayPlayerOut = (TextView) findViewById(R.id.awayOutPlayer);
		awayPosIn = (TextView) findViewById(R.id.awayInPos);
		awayPosOut = (TextView) findViewById(R.id.awayOutPos);
		awayTeamIn = (TextView) findViewById(R.id.awayInTeam);
		awayTeamOut = (TextView) findViewById(R.id.awayOutTeam);
		
		homePlayerIn.setText(currentPlayerList.getPlayerByID(playH2).getName());
		homePlayerOut.setText(currentPlayerList.getPlayerByID(playH1).getName());
		awayPlayerIn.setText(currentPlayerList.getPlayerByID(playA2).getName());
		awayPlayerOut.setText(currentPlayerList.getPlayerByID(playA1).getName());
		homePosIn.setText(currentPlayerList.getPlayerByID(playH2).getPosition());
		homePosOut.setText(currentPlayerList.getPlayerByID(playH1).getPosition());
		awayPosIn.setText(currentPlayerList.getPlayerByID(playA2).getPosition());
		awayPosOut.setText(currentPlayerList.getPlayerByID(playA1).getPosition());
		homeTeamIn.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(playH2).getTeam()).getLetters());
		homeTeamOut.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(playH1).getTeam()).getLetters());
		awayTeamIn.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(playA2).getTeam()).getLetters());
		awayTeamOut.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(playA1).getTeam()).getLetters());

	}

	public void onResume() {
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
		final Context context = this;
		switch (v.getId()) {
		case R.id.buttonMakeConfirmedChal:
			new makeChalActivity().execute();
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

	public class makeChalActivity extends AsyncTask<Void, Void, Void> {

		Handler handle;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MakeChalConfirmation.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (Looper.myLooper() == null)
				Looper.prepare();
			handle = new Handler();
			int r = CHAL_STATUS_SUCCESSFUL;
			if (previousChalID > 0) {
				WebContentGet webOb = new WebContentGet();
				r = webOb.declineChal(previousChalID);
			}
			if(r == CHAL_STATUS_SUCCESSFUL){
			JSONObject jOb = new JSONObject();
			useChal.makeJSON(jOb);
			WebContentGet webObj = new WebContentGet();
			returnStatus = webObj.makeChal(jOb);
			}
			else returnStatus = r;

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog

			switch (returnStatus) {

			case CHAL_STATUS_FAILED:
				Toast.makeText(MakeChalConfirmation.this,
						"Could not Communicate", Toast.LENGTH_LONG).show();
				break;
			case GAME_STATUS_CLOSED:
				Toast.makeText(MakeChalConfirmation.this,
						"Game has started, not accepting challenges",
						Toast.LENGTH_LONG).show();
				break;
			case TRANSACTION_STATUS_NSF:
				showPayDialog(
						MakeChalConfirmation.this,
						"Account Low",
						"Your card will be charged to load your account with enough funds to complete challenge.");
				break;
			case CHAL_STATUS_SUCCESSFUL:
				Toast.makeText(MakeChalConfirmation.this, "Challenge Made",
						Toast.LENGTH_LONG).show();
				Intent intent1 = new Intent(MakeChalConfirmation.this,
						HomeTownSportsHome.class);
				startActivity(intent1);
				break;
			default:
				Toast.makeText(MakeChalConfirmation.this,
						"Default " + String.valueOf(returnStatus),
						Toast.LENGTH_LONG).show();
				break;
			}

			if (pDialog.isShowing())
				pDialog.dismiss();

		}
	}

	public void showPayDialog(Activity activity, String title,
			CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// close box
				new makePayActivity().execute();
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

	public class makePayActivity extends AsyncTask<Void, Void, Void> {

		Handler handle;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog3 = new ProgressDialog(MakeChalConfirmation.this);
			pDialog3.setMessage("Please wait...");
			pDialog3.setCancelable(false);
			pDialog3.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (Looper.myLooper() == null)
				Looper.prepare();
			handle = new Handler();
			WebContentGet webObj = new WebContentGet();
			int amount = (((int) useChal.getWager() / 5) + 1) * 5;
			webObj.dummyCharge(amount);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog3.isShowing())
				pDialog3.dismiss();

			new makeChalActivity().execute();
		}
	}

}
