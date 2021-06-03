package com.hometown.sports;

import java.io.InputStream;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

public class SingleGameActivity extends MenuBarActivity implements
		View.OnClickListener, SimpleGestureListener {

	private SimpleGestureFilter detector;

	// JSON node keys
	Button button1, button2, button3;
	ImageButton getInfo;

	Spinner spinner;
	Spinner spinnerLine;
	Spinner spinnerWager;
	double line1dbl[] = new double[200];
	double line2dbl[] = new double[200];
	int wagerints[] = new int[10];
	int gID;
	String name;
	int home, home2, playH1, playH2;
	int away, away2, playA1, playA2;
	int chalType = 0;
	String winnerSelected = "";
	String loserSelected = "";
	String lineSelected = "$1";
	String wagerSelected = "";
	int chalTypeSelected = 0;
	ArrayList<String> listLine1;
	ArrayList<String> listLine2;
	ArrayList<String> listWager1;
	int centerteamindex = 0;
	int centeroverindex = 0;
	Roster homeR, homeR2;
	Roster awayR, awayR2;
	Team homeT;
	Team awayT;
	String comboString = "Use the drop down menus to select a winner, line and amount";
	TextView homePlayerIn, homePlayerOut, homePosIn, homePosOut,homeTeamIn, homeTeamOut, awayPlayerIn, awayPlayerOut, awayPosIn, awayPosOut,awayTeamIn, awayTeamOut;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		detector = new SimpleGestureFilter(this, this);
		ActionBar actionBar = getActionBar();
		actionBar.show();

		setContentView(R.layout.activity_single_game);
		spinner = (Spinner) findViewById(R.id.spinWinnerList);
		spinnerLine = (Spinner) findViewById(R.id.spinLine);
		spinnerWager = (Spinner) findViewById(R.id.spinWager);
		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		name = in.getStringExtra(TAG_DISPLAY);
		home = in.getIntExtra(TAG_HOME1, 0);
		away = in.getIntExtra(TAG_AWAY1, 0);
		home2 = in.getIntExtra(TAG_HOME2, 0);
		away2 = in.getIntExtra(TAG_AWAY2, 0);
		playH1 = in.getIntExtra(TAG_HOMEP1, 0);
		playA1 = in.getIntExtra(TAG_AWAYP1, 0);
		playH2 = in.getIntExtra(TAG_HOMEP2, 0);
		playA2 = in.getIntExtra(TAG_AWAYP2, 0);
		gID = in.getIntExtra(TAG_GAMEID, 1);
		int sportID = in.getIntExtra(TAG_SPORTID, 0);
		int startlineteam = 0;
		int startlineover = 0;
		int endlineteam = 1;
		int endlineover = 1;

		homeR = currentRosterList.getRosterByID(home);
		awayR = currentRosterList.getRosterByID(away);
		homeR2 = currentRosterList.getRosterByID(home2);
		awayR2 = currentRosterList.getRosterByID(away2);
		homeT = currentTeamList.getTeamByID(homeR.getTeamID());
		awayT = currentTeamList.getTeamByID(awayR.getTeamID());

		ImageView awayLogo = (ImageView) findViewById(R.id.counterAwayLogo);
		ImageView homeLogo = (ImageView) findViewById(R.id.counterHomeLogo);

		String homeIcon = homeT.getLogo();
		String awayIcon = awayT.getLogo();

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


		int rangeteam = 0;
		int rangeover = 0;

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
		case (5):
			startlineteam = -41; // NCAABasketball
			endlineteam = 41;
			startlineover = 120;
			endlineover = 275;
			centerteamindex = 41;
			centeroverindex = 70;
			break;
		case (6):
			startlineteam = -31; // NCAAfootball
			endlineteam = 31;
			startlineover = 2;
			endlineover = 90;
			centerteamindex = 31;
			centeroverindex = 40;

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
		// defining buttons
		button1 = (Button) findViewById(R.id.Checkpublic);
		button2 = (Button) findViewById(R.id.Challengefriend);
		button3 = (Button) findViewById(R.id.Postpublic);
		getInfo = (ImageButton) findViewById(R.id.getInfo);

		//button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		getInfo.setOnClickListener(this);

		ArrayList<String> listWinner = new ArrayList<String>();
		listWinner.add("Make a selection");
		listWinner.add(awayT.getDisplayName());
		listWinner.add(homeT.getDisplayName());
		listWinner.add("Over");
		listWinner.add("Under");
		// final Spinner spinner = (Spinner) findViewById(R.id.spinWinnerList);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<String> adapterWinner = new ArrayAdapter<String>(this,
				R.layout.spinnerlayout, listWinner);

		// ArrayAdapter.createFromResource(this, R.array.sound,
		// R.layout.spinnerLayout);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapterWinner);

		ArrayList<String> listLine = new ArrayList<String>();
		listLine.add("Pick Line Here");
		final ArrayAdapter<String> adapterLine = new ArrayAdapter<String>(this,
				R.layout.spinnerlayout, listLine);
		// final Spinner spinnerLine = (Spinner) findViewById(R.id.spinLine);
		spinnerLine.setAdapter(adapterLine);

		ArrayList<String> listWager = new ArrayList<String>();
		listWager.add("$$$");
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
					updateChalSummary(chalTypeSelected);

				} else if (pos < 2.5) {
					// winnerpicked = true;
					spinnerLine.setAdapter(adapterLine1);
					spinnerWager.setAdapter(adapterWager1);
					spinnerLine.setSelection(centerline1);
					if (pos == 2) {
						winnerSelected = homeT.getDisplayName();
						loserSelected = awayT.getDisplayName();
					} else {
						winnerSelected = awayT.getDisplayName();
						loserSelected = homeT.getDisplayName();
					}
					chalTypeSelected = 1;
					lineSelected = listLine1.get(centerteamindex);
					updateChalSummary(chalTypeSelected);

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
					updateChalSummary(chalTypeSelected);
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

				updateChalSummary(chalTypeSelected);

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

				updateChalSummary(chalTypeSelected);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void onResume() {
		super.onResume();
		String ttl = "This Challenge Explained:";
		String txt = "Create challenge to compete with friends or post to public, or review public challenges for this game./n/n This challenge Explained:/n"
				+ comboString;
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}

	public void updateChalSummary(int cType) {
		String output;
		output = "";

		switch (cType) {
		case (0):
			output = "Use the drop down menus to select a winner, line and $ amount";
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
		int wnr;
		final Context context = this;
		switch (v.getId()) {
		case R.id.Checkpublic:
			Intent intent3 = new Intent(context, PublicChalPage.class);
			intent3.putExtra("gameID", gID);
			startActivity(intent3);

			break;
		case R.id.Challengefriend:

			//
			//
			// HERE WE WILL GO TO FRIENDS PAGE... WHEN FRIEND IS SELECTED GO TO
			// CONFIRMATION PAGE

			wnr = spinner.getSelectedItemPosition();

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

				// TextView lblName = (TextView) findViewById(R.id.chosengame);

				Challenge useChal = new Challenge(0, thisUser, 0, home,
						homeR.getSelectedPlayer(), home2,
						homeR2.getSelectedPlayer(), away,
						awayR.getSelectedPlayer(), away2,
						awayR2.getSelectedPlayer(), wnr, line,
						wagerints[spinnerWager.getSelectedItemPosition()],
						CHAL_STATUS_OPENFRIEND);
				setSelectedChal(useChal);

				Intent intent2 = new Intent(context, PickOpponentPage.class);
				intent2.putExtra(TAG_HOME1, home);
				intent2.putExtra(TAG_HOME2, home2);
				intent2.putExtra(TAG_AWAY1, away);
				intent2.putExtra(TAG_AWAY2, away2);
				intent2.putExtra(TAG_HOMEP1, playH1);
				intent2.putExtra(TAG_HOMEP2, playH2);
				intent2.putExtra(TAG_AWAYP1, playA1);
				intent2.putExtra(TAG_AWAYP2, playA2);
				intent2.putExtra(TAG_WINNER, wnr);
				intent2.putExtra(TAG_LINE, line);
				intent2.putExtra(TAG_VALUE,
						wagerints[spinnerWager.getSelectedItemPosition()]);
				intent2.putExtra(TAG_STATUS, CHAL_STATUS_OPENFRIEND);
				startActivity(intent2);

			} else {
				// print out select bet warning;
			}

			break;
		case R.id.Postpublic:
			wnr = spinner.getSelectedItemPosition();

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

				// TextView lblName = (TextView) findViewById(R.id.chosengame);

				Challenge useChal = new Challenge(0, thisUser, 0, home,
						homeR.getSelectedPlayer(), home2,
						homeR2.getSelectedPlayer(), away,
						awayR.getSelectedPlayer(), away2,
						awayR2.getSelectedPlayer(), wnr, line,
						wagerints[spinnerWager.getSelectedItemPosition()],
						CHAL_STATUS_OPENFRIEND);
				setSelectedChal(useChal);

				Intent intent2 = new Intent(context, MakeChalConfirmation.class);
				intent2.putExtra(TAG_ACCEPTER, 0);
				intent2.putExtra(TAG_HOME1, home);
				intent2.putExtra(TAG_HOME2, home2);
				intent2.putExtra(TAG_AWAY1, away);
				intent2.putExtra(TAG_AWAY2, away2);
				intent2.putExtra(TAG_HOMEP1, playH1);
				intent2.putExtra(TAG_HOMEP2, playH2);
				intent2.putExtra(TAG_AWAYP1, playA1);
				intent2.putExtra(TAG_AWAYP2, playA2);
				intent2.putExtra(TAG_WINNER, wnr);
				intent2.putExtra(TAG_LINE, line);
				intent2.putExtra(TAG_VALUE,
						wagerints[spinnerWager.getSelectedItemPosition()]);
				intent2.putExtra(TAG_STATUS, CHAL_STATUS_OPENFRIEND);
				startActivity(intent2);

			}

			else {
				// print out select bet warning;
			}

			break;
		case R.id.getInfo:
			showDialog(this, "This Challenge Explained:", comboString);

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
