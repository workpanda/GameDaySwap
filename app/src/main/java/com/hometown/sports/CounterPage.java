package com.hometown.sports;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CounterPage extends MenuBarActivity implements
		View.OnClickListener, SimpleGestureListener {

	private SimpleGestureFilter detector;

	// JSON node keys
	Button buttonCounter;
	ImageButton getInfo;
	TextView homePlayerIn, homePlayerOut, homePosIn, homePosOut,homeTeamIn, homeTeamOut, awayPlayerIn, awayPlayerOut, awayPosIn, awayPosOut,awayTeamIn, awayTeamOut;

	Spinner spinner;
	Spinner spinnerLine;
	Spinner spinnerWager;
	double line1dbl[] = new double[200];
	double line2dbl[] = new double[200];
	int wagerints[] = new int[10];
	int gID;
	String name;
	String home;
	String away;
	int chalType = 0;
	String winnerSelected = "";
	String loserSelected = "";
	String lineSelected = "$1";
	String wagerSelected = "";
	String comboString = "Use the drop down menus to select a winner, line and wager";
	int chalTypeSelected = 0;
	ArrayList<String> listLine1;
	ArrayList<String> listLine2;
	ArrayList<String> listWager1;
	int centerteamindex = 0;
	int centeroverindex = 0;
	int previousChalID;
	Challenge previousChal;
	int opponentID;
	String opponentStr;
	int home1, home2, away1, away2, homep1, homep2, awayp1, awayp2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		detector = new SimpleGestureFilter(this, this);
		ActionBar actionBar = getActionBar();
		actionBar.show();

		setContentView(R.layout.counterchallayout);
		spinner = (Spinner) findViewById(R.id.spinWinnerListCounter);
		spinnerLine = (Spinner) findViewById(R.id.spinLineCounter);
		spinnerWager = (Spinner) findViewById(R.id.spinWagerCounter);
		previousChal = selectedChal;
		previousChalID = previousChal.getChalID();
		
		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		name = in.getStringExtra(TAG_DISPLAY);
		gID = in.getIntExtra(TAG_GAMEID, 1);
		int sportID = in.getIntExtra(TAG_SPORTID, 0);
		opponentID = in.getIntExtra(TAG_ACCEPTER, 0);
		//opponentStr = currentUserList.getUserByID(opponentID).getName();
		home1 = in.getIntExtra(TAG_HOME1, 0);
		home2 = in.getIntExtra(TAG_HOME2, 0);
		away1 = in.getIntExtra(TAG_AWAY1, 0);
		away2 = in.getIntExtra(TAG_AWAY2, 0);
		homep1 = in.getIntExtra(TAG_HOMEP1, 0);
		homep2 = in.getIntExtra(TAG_HOMEP2, 0);
		awayp1 = in.getIntExtra(TAG_AWAYP1, 0);
		awayp2 = in.getIntExtra(TAG_AWAYP2, 0);
		previousChalID = in.getIntExtra(TAG_ID, 0);
		
		
		int startlineteam = 0;
		int startlineover = 0;
		int endlineteam = 1;
		int endlineover = 1;

		int rangeteam = 0;
		int rangeover = 0;

		int h = currentRosterList.getRosterByID(selectedChal.getHomeRoster())
				.getTeamID();
		int a = currentRosterList.getRosterByID(selectedChal.getAwayRoster())
				.getTeamID();

		switch (sportID) {
		case (1):
			startlineteam = -31; // football = 1 NFL
			endlineteam = 31;
			startlineover = 2;
			endlineover = 90;
			centerteamindex = 31;
			centeroverindex = 40;

			break;
		case (2):
			startlineteam = -11; // nhl id = 2
			endlineteam = 11;
			startlineover = 1;
			endlineover = 20;
			centerteamindex = 11;
			centeroverindex = 5;
			break;
		case (3):
			startlineteam = -11; // MLB
			endlineteam = 11;
			startlineover = 1;
			endlineover = 20;
			centerteamindex = 11;
			centeroverindex = 5;
			break;
		case (4):
			startlineteam = -41; // NBA
			endlineteam = 41;
			startlineover = 120;
			endlineover = 275;
			centerteamindex = 41;
			centeroverindex = 70;
			break;

		}
		rangeteam = endlineteam - startlineteam;
		rangeover = endlineover - startlineover;

		// double line1dbl[] = new double[rangeteam];
		// double line2dbl[] = new double[rangeover];
		int ctr1 = 0;
		int ctr2 = 0;

		listLine1 = new ArrayList<String>();
		for (int i = startlineteam; i < endlineteam; i++) {
			double sum = i + 0.5;
			String sign;
			if (sum > 0) {
				sign = "+";
			} else {
				sign = "";
			}

			String num = Double.toString(sum);
			String toAdd = sign + num;
			listLine1.add(toAdd);
			line1dbl[ctr1] = sum;
			ctr1++;
		}

		final ArrayAdapter<String> adapterLine1 = new ArrayAdapter<String>(
				this, R.layout.spinnerlayout, listLine1);
		final int centerline1 = centerteamindex;

		listLine2 = new ArrayList<String>();
		for (int i = startlineover; i < endlineover; i++) {
			double sum = i + 0.5;

			String num = Double.toString(sum);
			String toAdd = num;
			listLine2.add(toAdd);
			line2dbl[ctr2] = sum;
			ctr2++;
		}

		final ArrayAdapter<String> adapterLine2 = new ArrayAdapter<String>(
				this, R.layout.spinnerlayout, listLine2);
		final int centerline2 = centeroverindex;

		wagerints[0] = 1;
		wagerints[1] = 2;
		wagerints[2] = 3;
		wagerints[3] = 4;
		wagerints[4] = 5;
		wagerints[5] = 6;
		wagerints[6] = 7;
		wagerints[7] = 8;
		wagerints[8] = 9;
		wagerints[9] = 10;

		listWager1 = new ArrayList<String>();
		listWager1.add("$1");
		listWager1.add("$2");
		listWager1.add("$3");
		listWager1.add("$4");
		listWager1.add("$5");
		listWager1.add("$6");
		listWager1.add("$7");
		listWager1.add("$8");
		listWager1.add("$9");
		listWager1.add("$10");

		final ArrayAdapter<String> adapterWager1 = new ArrayAdapter<String>(
				this, R.layout.spinnerlayout, listWager1);

		boolean linepicked = false;
		boolean wagerpicked = false;

		// Displaying all values on the screen

		String winnerStr;
		String lineStr;
		String wagerStr;

		

		ImageView awayLogo = (ImageView) findViewById(R.id.counterAwayLogo);
		ImageView homeLogo = (ImageView) findViewById(R.id.counterHomeLogo);

		Team hm = currentTeamList.getTeamByID(h);
		Team aw = currentTeamList.getTeamByID(a);

		String homeIcon = hm.getLogo();
		String awayIcon = aw.getLogo();

		new DownloadImageTask(awayLogo).execute(awayIcon);
		new DownloadImageTask(homeLogo).execute(homeIcon);
		
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
		
		homePlayerIn.setText(currentPlayerList.getPlayerByID(homep2).getName());
		homePlayerOut.setText(currentPlayerList.getPlayerByID(homep1).getName());
		awayPlayerIn.setText(currentPlayerList.getPlayerByID(awayp2).getName());
		awayPlayerOut.setText(currentPlayerList.getPlayerByID(awayp1).getName());
		homePosIn.setText(currentPlayerList.getPlayerByID(homep2).getPosition());
		homePosOut.setText(currentPlayerList.getPlayerByID(homep1).getPosition());
		awayPosIn.setText(currentPlayerList.getPlayerByID(awayp2).getPosition());
		awayPosOut.setText(currentPlayerList.getPlayerByID(awayp1).getPosition());
		homeTeamIn.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(homep2).getTeam()).getLetters());
		homeTeamOut.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(homep1).getTeam()).getLetters());
		awayTeamIn.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(awayp2).getTeam()).getLetters());
		awayTeamOut.setText(currentTeamList.getTeamByID(currentPlayerList.getPlayerByID(awayp1).getTeam()).getLetters());



		// defining buttons
		buttonCounter = (Button) findViewById(R.id.buttonSendCounter);
		getInfo = (ImageButton) findViewById(R.id.getInfo);

		buttonCounter.setOnClickListener(this);
		getInfo.setOnClickListener(this);

		ArrayList<String> listWinner = new ArrayList<String>();
		listWinner.add("Make a selection");
		listWinner.add(away);
		listWinner.add(home);
		listWinner.add("Over");
		listWinner.add("Under");
		// final Spinner spinner = (Spinner) findViewById(R.id.spinWinnerList);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<String> adapterWinner = new ArrayAdapter<String>(this,
				R.layout.spinnerlayout, listWinner);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapterWinner);

		ArrayList<String> listLine = new ArrayList<String>();
		listLine.add("Pick Line Here");
		final ArrayAdapter<String> adapterLine = new ArrayAdapter<String>(this,
				R.layout.spinnerlayout, listLine);
		// final Spinner spinnerLine = (Spinner) findViewById(R.id.spinLine);
		spinnerLine.setAdapter(adapterLine);

		ArrayList<String> listWager = new ArrayList<String>();
		listWager.add("Make Wager Here");
		final ArrayAdapter<String> adapterWager = new ArrayAdapter<String>(
				this, R.layout.spinnerlayout, listWager);
		// final Spinner spinnerWager = (Spinner) findViewById(R.id.spinWager);
		spinnerWager.setAdapter(adapterWager);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (pos == 0) {
					// winnerpicked = false;
					spinnerWager.setAdapter(adapterWager);
					spinnerLine.setAdapter(adapterLine);
					chalType = 0;
					winnerSelected = "";
					loserSelected = "";
					lineSelected = "";
					wagerSelected = "";
					chalTypeSelected = 0;
					updateBetSummary(chalTypeSelected);

				} else if (pos < 2.5) {
					// winnerpicked = true;
					spinnerLine.setAdapter(adapterLine1);
					spinnerWager.setAdapter(adapterWager1);
					spinnerLine.setSelection(centerline1);
					if (pos == 2) {
						winnerSelected = home;
						loserSelected = away;
					} else {
						winnerSelected = away;
						loserSelected = home;
					}
					chalTypeSelected = 1;
					lineSelected = listLine1.get(centerteamindex);
					updateBetSummary(chalTypeSelected);

				} else {
					spinnerLine.setAdapter(adapterLine2);
					spinnerWager.setAdapter(adapterWager1);
					spinnerLine.setSelection(centerline2);
					if (pos == 3) {
						winnerSelected = "over";
						loserSelected = "";
					} else {
						winnerSelected = "under";
						loserSelected = "";
					}
					chalTypeSelected = 2;
					lineSelected = listLine2.get(centeroverindex);
					updateBetSummary(chalTypeSelected);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spinnerLine.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				switch (chalTypeSelected) {
				case (1):
					lineSelected = listLine1.get(pos);
					break;
				case (2):
					lineSelected = listLine2.get(pos);
					break;
				}

				updateBetSummary(chalTypeSelected);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spinnerWager.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				wagerSelected = listWager1.get(pos);

				updateBetSummary(chalTypeSelected);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		
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

	public void updateBetSummary(int btType) {
		String output;
		output = "";

		switch (btType) {
		case (0):
			output = "Use the drop down menus to select a winner, line and wager";
			break;
		case (1):
			output = "You win if: \nthe " + winnerSelected + " final score "
					+ lineSelected + " points is more than the "
					+ loserSelected + " final score";

			break;
		case (2):
			output = "You win if: \nBoth teams combined score is "
					+ winnerSelected + " " + lineSelected + " points";
			break;

		}
		comboString = output;

	}

	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {

		case R.id.buttonSendCounter:

			int wnr = spinner.getSelectedItemPosition();

			if (wnr > 0.5) {

				int linepos = spinnerLine.getSelectedItemPosition();
				double line;
				if (wnr > 2.5) {
					line = line2dbl[linepos];
				} else {
					line = line1dbl[linepos];
				}

				switch (wnr) {
				case 1:
					wnr = WINNER_STATUS_AWAY;
					break;
				case 2:
					wnr = WINNER_STATUS_HOME;
					break;
				case 3:
					wnr = WINNER_STATUS_OVER;
					break;
				case 4:
					wnr = WINNER_STATUS_UNDER;
					break;
				}


				Intent intent2 = new Intent(context, MakeChalConfirmation.class);
				// TextView lblName = (TextView) findViewById(R.id.chosengame);

				intent2.putExtra(TAG_ACCEPTER, opponentID);
				intent2.putExtra(TAG_HOME1, home1);
				intent2.putExtra(TAG_HOME2, home2);
				intent2.putExtra(TAG_AWAY1, away1);
				intent2.putExtra(TAG_AWAY2, away2);
				intent2.putExtra(TAG_HOMEP1, homep1);
				intent2.putExtra(TAG_HOMEP2, homep2);
				intent2.putExtra(TAG_AWAYP1, awayp1);
				intent2.putExtra(TAG_AWAYP2, awayp2);
				intent2.putExtra(TAG_WINNER, wnr);
				intent2.putExtra(TAG_LINE, line);
				intent2.putExtra(TAG_VALUE, wagerints[spinnerWager.getSelectedItemPosition()]);
				intent2.putExtra(TAG_STATUS, CHAL_STATUS_OPENFRIEND);
				intent2.putExtra(TAG_ID, previousChalID);

				startActivity(intent2);

			} else {
				// print out select bet warning;
			}

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
}
