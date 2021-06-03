package com.hometown.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ConfirmationPage extends MenuBarActivity implements
		SimpleGestureListener, View.OnClickListener {
	private SimpleGestureFilter detector;
	Challenge useChal;
	TextView lineText, opponentText, wagerText;
	Button makeBet, counterBet, declineBet;
	int homeRoster, homeRoster2ID, homePlayerAdd, homePlayer2Add;
	int awayRoster, awayRoster2ID, awayPlayerAdd, awayPlayer2Add;
	int opponentID;
	int previousChalID;
	int isPublic;
	String comboString;
	ImageView awayLogo, homeLogo;
	TextView homePlayerIn, homePlayerOut, homePosIn, homePosOut,homeTeamIn, homeTeamOut, awayPlayerIn, awayPlayerOut, awayPosIn, awayPosOut,awayTeamIn, awayTeamOut;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmationpagelayout);
		detector = new SimpleGestureFilter(this, this);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		lineText = (TextView) findViewById(R.id.confirmTextLine);
		opponentText = (TextView) findViewById(R.id.confirmTextOpponent);
		wagerText = (TextView) findViewById(R.id.confirmTextWager);
		makeBet = (Button) findViewById(R.id.buttonAcceptChal);
		counterBet = (Button) findViewById(R.id.buttonCounter);
		declineBet = (Button) findViewById(R.id.buttonDeclineChal);
		awayLogo = (ImageView) findViewById(R.id.counterAwayLogo);
		homeLogo = (ImageView) findViewById(R.id.counterHomeLogo);
		
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
		
		Intent in = getIntent();
		homeRoster = in.getIntExtra(TAG_HOME1, 0);
		homeRoster2ID = in.getIntExtra(TAG_HOME2, 0);
		awayRoster = in.getIntExtra(TAG_AWAY1, 0);
		awayRoster2ID = in.getIntExtra(TAG_AWAY2, 0);
		homePlayerAdd = in.getIntExtra(TAG_HOMEP1, 0);
		homePlayer2Add = in.getIntExtra(TAG_HOMEP2, 0);
		awayPlayerAdd = in.getIntExtra(TAG_AWAYP1, 0);
		awayPlayer2Add = in.getIntExtra(TAG_AWAYP2, 0);
		int oppID = in.getIntExtra(TAG_ACCEPTER, 0);
		int winner = in.getIntExtra(TAG_WINNER, 0);
		double line = in.getDoubleExtra(TAG_LINE, 0.5);
		double wager = in.getDoubleExtra(TAG_VALUE,1.0);
		int challengerID = in.getIntExtra(TAG_CHALLENGER, 0);
		int bStatus = in.getIntExtra(TAG_STATUS, 0);
		int chalid = in.getIntExtra(TAG_ID,0);
		
		opponentID = challengerID;

		useChal = new Challenge(chalid,challengerID, oppID,
				homeRoster, homePlayerAdd, homeRoster2ID, homePlayer2Add, awayRoster, awayPlayerAdd, awayRoster2ID, awayPlayer2Add, winner, line,
				wager, bStatus);

		makeBet.setOnClickListener(this);
		counterBet.setOnClickListener(this);
		declineBet.setOnClickListener(this);

		// SERVER
		Team hT,aT;
		hT = currentTeamList.getTeamByID(currentRosterList.getRosterByID(useChal.getHomeRoster()).getTeamID());
		aT = currentTeamList.getTeamByID(currentRosterList.getRosterByID(useChal.getAwayRoster()).getTeamID());
		
		convertToChalDisplay(useChal);
		comboString = "You win if: \n" + getDetailChalDisplay();
		lineText.setText(getChalDisplayString());
		opponentText.setText(getOpponentString());
		wagerText.setText(getWagerDisplayString());

		new DownloadImageTask(homeLogo).execute(hT.getLogo());
		new DownloadImageTask(awayLogo).execute(aT.getLogo());

		getRostersDEMO getRos = new getRostersDEMO();
		getRos.execute();
		

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
		final Context context = this;
		switch (v.getId()) {
		case R.id.buttonAcceptChal:
			// server
			new postAcceptance().execute();
			break;
		case R.id.buttonCounter:
			Intent intent2 = new Intent(context, SwapPage.class);
			intent2.putExtra(TAG_DISPLAY, getGameDisplayString());
			intent2.putExtra(TAG_HOME1, useChal.getHomeRoster());
			intent2.putExtra(TAG_AWAY1, useChal.getAwayRoster());
			intent2.putExtra(TAG_HOME2, useChal.getHomeRoster2());
			intent2.putExtra(TAG_AWAY2, useChal.getAwayRoster2());
			intent2.putExtra(TAG_ACCEPTER, opponentID);
			intent2.putExtra(TAG_SPORTID, currentTeamList.getTeamByID(currentRosterList.getRosterByID(useChal.getHomeRoster()).getTeamID()).getSport());
			intent2.putExtra(TAG_ISCOUNTER, true);
			
			// Buddybets.ourServer.declineBet(selectedBet.getBetID());

			startActivity(intent2);
			// do stuff;
			break;
		case R.id.buttonDeclineChal:
			// server
			new postDecline().execute();

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

	private ProgressDialog pDialog, pDialog2, pDialog3;

public class getRostersDEMO extends AsyncTask<Void, Void, Void> {
		
		Handler handle;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog2 = new ProgressDialog(ConfirmationPage.this);
			pDialog2.setMessage("Please wait...");
			pDialog2.setCancelable(false);
			pDialog2.show();
			Log.d("MakeChal","Starting get RosterDemo");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			WebContentGet webObj1 = new WebContentGet();
			JSONObject jR1 = new JSONObject();
			jR1 = webObj1.getPlayerInfo(homePlayerAdd);
			Player p1 = new Player(jR1);
			currentPlayerList.push(p1);
			
			WebContentGet webObj2 = new WebContentGet();
			JSONObject jR2 = new JSONObject();
			jR2 = webObj2.getPlayerInfo(homePlayer2Add);
			Player p2 = new Player(jR2);
			currentPlayerList.push(p2);
			
			WebContentGet webObj3 = new WebContentGet();
			JSONObject jR3 = new JSONObject();
			jR3 = webObj3.getPlayerInfo(awayPlayerAdd);
			Player p3 = new Player(jR3);
			currentPlayerList.push(p3);
			
			WebContentGet webObj4 = new WebContentGet();
			JSONObject jR4 = new JSONObject();
			jR4 = webObj4.getPlayerInfo(awayPlayer2Add);
			Player p4 = new Player(jR4);
			currentPlayerList.push(p4);
			
			Player useP;
			
			for(int i = 0;i<4;i++){
				switch(i){
				case 0:
					useP = p1;
					break;
				case 1:
					useP=p2;
					break;
				case 2: 
					useP=p3;
					break;
					default:
						useP=p4;
						break;
				}
				
				if(!currentTeamList.containsTeam(useP.getTeam())){
					WebContentGet webOb = new WebContentGet();
					JSONObject jO = new JSONObject();
					jO = webOb.getTeamInfo(useP.getTeam());
					Team t = new Team(jO);
					currentTeamList.push(t);
				}
			}
			
			

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			
			
			homePlayerIn.setText(currentPlayerList.getPlayerByID(homePlayer2Add).getName());
			homePlayerOut.setText(currentPlayerList.getPlayerByID(homePlayerAdd).getName());
			awayPlayerIn.setText(currentPlayerList.getPlayerByID(awayPlayer2Add).getName());
			awayPlayerOut.setText(currentPlayerList.getPlayerByID(awayPlayerAdd).getName());
			homePosIn.setText(currentPlayerList.getPlayerByID(homePlayer2Add).getPosition());
			homePosOut.setText(currentPlayerList.getPlayerByID(homePlayerAdd).getPosition());
			awayPosIn.setText(currentPlayerList.getPlayerByID(awayPlayer2Add).getPosition());
			awayPosOut.setText(currentPlayerList.getPlayerByID(awayPlayerAdd).getPosition());
			homeTeamIn.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(homePlayer2Add).getTeam()).getLetters());
			homeTeamOut.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(homePlayerAdd).getTeam()).getLetters());
			awayTeamIn.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(awayPlayer2Add).getTeam()).getLetters());
			awayTeamOut.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(awayPlayerAdd).getTeam()).getLetters());

			if (pDialog2.isShowing())
				pDialog2.dismiss();
			
		}
	}

	private class postAcceptance extends AsyncTask<Void, Void, Void> {

		int r;
		Handler handle;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ConfirmationPage.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			WebContentGet webOb = new WebContentGet();
			r = webOb.acceptChal(useChal.getChalID());

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
				Toast.makeText(ConfirmationPage.this, "Could not Communicate",
						Toast.LENGTH_LONG).show();
				break;
			case (GAME_STATUS_CLOSED):
				Toast.makeText(ConfirmationPage.this,
						"Game has started, not accepting challenges",
						Toast.LENGTH_LONG).show();
				break;
			case TRANSACTION_STATUS_NSF:
				showPayDialog(ConfirmationPage.this,"Account Low", "Your card will be charged to load your account with enough funds to complete challenge.");
				break;
			case (CHAL_STATUS_SUCCESSFUL):
				Toast.makeText(ConfirmationPage.this, "Challenge Made",
						Toast.LENGTH_LONG).show();
				Intent intent1 = new Intent(ConfirmationPage.this,
						HomeTownSportsHome.class);
				startActivity(intent1);
				break;
			}

		}
	}

	private class postDecline extends AsyncTask<Void, Void, Void> {

		int r;
		Handler handle;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ConfirmationPage.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			WebContentGet webOb = new WebContentGet();
			r = webOb.declineChal(useChal.getChalID());

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
				Toast.makeText(ConfirmationPage.this, "Could not Communicate",
						Toast.LENGTH_LONG).show();
				break;
			case (GAME_STATUS_CLOSED):
				Toast.makeText(ConfirmationPage.this,
						"Game has started, all challenges closed",
						Toast.LENGTH_LONG).show();
				break;
			case (CHAL_STATUS_SUCCESSFUL):
				Toast.makeText(ConfirmationPage.this, "Challenge Declined",
						Toast.LENGTH_LONG).show();
				Intent intent3 = new Intent(ConfirmationPage.this,
						Pendingchallenges.class);
				intent3.putExtra("chalType", PENDING_CHAL_ID);
				startActivity(intent3);
				break;
			}

		}
	}
	
	public void showPayDialog(Activity activity, String title, CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// close box
				new makePayActivity().execute();
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
			pDialog3 = new ProgressDialog(ConfirmationPage.this);
			pDialog3.setMessage("Please wait...");
			pDialog3.setCancelable(false);
			pDialog3.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			WebContentGet webObj = new WebContentGet();
			int amount = (((int) useChal.getWager()/5)+1)*5;
			webObj.dummyCharge(amount);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog3.isShowing())
				pDialog3.dismiss();

			new postAcceptance().execute();
		}
	}
}