package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.MenuBarActivity.DownloadImageTask;
import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SingleChalActivity extends MenuBarActivity implements
		SimpleGestureListener {

	private ProgressDialog pDialog5;

	// URL to get contacts JSON

	private SimpleGestureFilter detector;
	// JSON Node names

	JSONArray chalsin = new JSONArray();
	chalList chalListIn = new chalList(1);
	gameList gameListIn = new gameList(1);
	boolean isChallenger = true;
	// Game currentGame;
	Roster homeRoster, homeRoster2, awayRoster, awayRoster2;
	int homeRosterID, homeRoster2ID, awayRosterID, awayRoster2ID, homeTeamID,
			awayTeamID;
	int homePlayerAdd, homePlayer2Add, awayPlayerAdd, awayPlayer2Add;
	int opponentID;
	Team homeTeam, awayTeam;
	Challenge currentChal;
	TextView TextTitle;
	TextView noBetTitle;
	TextView homePlayerIn, homePlayerOut, homePosIn, homePosOut,homeTeamIn, homeTeamOut, awayPlayerIn, awayPlayerOut, awayPosIn, awayPosOut,awayTeamIn, awayTeamOut;
	int chalID;
	User opponent;
	Button trashTalk;
	TableLayout tab;
	// int gameID;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> chalarraylist;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlechallayout);
		ActionBar actionBar = getActionBar();
		actionBar.show();

		trashTalk = (Button) findViewById(R.id.trashTalk);
		trashTalk.setVisibility(View.GONE);

		tab = (TableLayout) findViewById(R.id.RostersTable);
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
		homeRosterID = in.getIntExtra(TAG_HOME1, 0);
		homeRoster2ID = in.getIntExtra(TAG_HOME2, 0);
		awayRosterID = in.getIntExtra(TAG_AWAY1, 0);
		awayRoster2ID = in.getIntExtra(TAG_AWAY2, 0);
		homePlayerAdd = in.getIntExtra(TAG_HOMEP1, 0);
		homePlayer2Add = in.getIntExtra(TAG_HOMEP2, 0);
		awayPlayerAdd = in.getIntExtra(TAG_AWAYP1, 0);
		awayPlayer2Add = in.getIntExtra(TAG_AWAYP2, 0);
		opponentID = in.getIntExtra(TAG_ACCEPTER, 0);
		int winner = in.getIntExtra(TAG_WINNER, 0);
		double line = in.getDoubleExtra(TAG_LINE, 0.5);
		double wager = in.getDoubleExtra(TAG_VALUE,1.0);
		int challengerID = in.getIntExtra(TAG_CHALLENGER, 0);
		int bStatus = in.getIntExtra(TAG_STATUS, 0);
		int chalid = in.getIntExtra(TAG_ID,0);

		currentChal = new Challenge(chalid,challengerID, opponentID,
				homeRosterID, homePlayerAdd, homeRoster2ID, homePlayer2Add, awayRosterID, awayPlayerAdd, awayRoster2ID, awayPlayer2Add, winner, line,
				wager, bStatus);
		if (opponentID == thisUser) {
			isChallenger = false;
			opponentID = currentChal.getChallenger();
		} else
			isChallenger = true;

		int[] input = new int[5];
		input[0] = homeRosterID;
		input[1] = homeRoster2ID;
		input[2] = awayRosterID;
		input[3] = awayRoster2ID;
		input[4] = opponentID;

		GetPageAsync gp = new GetPageAsync(input);
		gp.execute();

		homeRoster = currentRosterList.getRosterByID(homeRosterID);
		homeRoster2 = currentRosterList.getRosterByID(homeRoster2ID);
		awayRoster = currentRosterList.getRosterByID(awayRosterID);
		awayRoster2 = currentRosterList.getRosterByID(awayRoster2ID);

		homeTeam = currentTeamList.getTeamByID(homeRoster.getTeamID());
		awayTeam = currentTeamList.getTeamByID(awayRoster.getTeamID());

		opponent = currentUserList.getUserByID(opponentID);

		// JSONObject c = Buddybets.ourServer.getGame(gameID);

		String chalName = opponent.getName();
		String acpName = opponent.getName();

		String chalDisplayString = "";
		String dispLine = "";
		String team = "";
		String wagerDisplayString = "";

		if (isChallenger) {
			String opp = acpName;
			String wgrStr = "$" + String.valueOf(wager);
			wagerDisplayString = "vs. " + opp + " for " + wgrStr;
			switch (winner) {
			case (0):
				team = awayTeam.getDisplayName();
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				break;
			case (1):
				team = homeTeam.getDisplayName();
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				break;
			case (2):
				team = "Over";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				break;
			case (3):
				team = "Under";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				break;

			}
		} else {
			String opp = chalName;
			String wgrStr = "$" + String.valueOf(wager);
			wagerDisplayString = "vs. " + opp + " for " + wgrStr;
			switch (winner) {
			case (0):
				team = homeTeam.getDisplayName();
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				break;
			case (1):
				team = awayTeam.getDisplayName();
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				break;
			case (2):
				team = "Under";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				break;
			case (3):
				team = "Over";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				break;

			}

		}

		String homeIcon = homeTeam.getLogo();
		String awayIcon = awayTeam.getLogo();

		TextView awayTeamV = (TextView) findViewById(R.id.awayName);
		TextView homeTeamV = (TextView) findViewById(R.id.homeName);
		TextView awayScore = (TextView) findViewById(R.id.awayScore);
		TextView homeScore = (TextView) findViewById(R.id.homeScore);
		TextView dateView = (TextView) findViewById(R.id.awayTime);
		TextView timeView = (TextView) findViewById(R.id.homeTime);
		ImageView homeLogo = (ImageView) findViewById(R.id.homeLogo);
		ImageView awayLogo = (ImageView) findViewById(R.id.awayLogo);
		// TextView lineDisplay = (TextView) findViewById(R.id.betTitle);
		TextView wagerDisplay = (TextView) findViewById(R.id.wagerTitle);
		TextView statusDisplay = (TextView) findViewById(R.id.chalStatus);

		homeTeamV.setText(homeTeam.getDisplayName());
		awayTeamV.setText(awayTeam.getDisplayName() + " at");
		
		/*

		int homescore = currentAssignList.getRosterScore(homeRoster.getID(),
				homeRoster.getSelectedPlayer())
				- currentAssignList.getPlayerScore(homeRoster2.getID(),
						homeRoster2.getSelectedPlayer());
		int awayscore = currentAssignList.getRosterScore(awayRoster.getID(),
				awayRoster.getSelectedPlayer())
				- currentAssignList.getPlayerScore(awayRoster2.getID(),
						awayRoster2.getSelectedPlayer());

		if (homescore == 2000) {
			awayScore.setText("");
			homeScore.setText("");
		} else {
			awayScore.setText(String.valueOf(awayscore));
			homeScore.setText(String.valueOf(homescore));
		}

		long homeRTime = homeRoster.getTime();
		long awayRTime = awayRoster.getTime();
		long away2RTime = awayRoster2.getTime();
		long home2RTime = homeRoster2.getTime();

		long c1Time = Math.min(homeRTime, awayRTime);
		long c2Time = Math.min(home2RTime, away2RTime);
		long cTime = Math.min(c1Time, c2Time);

		calcGameTime(cTime, homeTeam.getSport());

		String dateString = setDateString();
		String timeString = setTimeString();

		dateView.setText(dateString);
		timeView.setText(timeString);
		*/
		
		new DownloadImageTask(awayLogo).execute(awayIcon);
		new DownloadImageTask(homeLogo).execute(homeIcon);

		// lineDisplay.setText(chalDisplayString);
		wagerDisplay.setText(wagerDisplayString);

		switch (bStatus) {
		case CHAL_STATUS_OPENPUBLIC:
			statusDisplay.setText("Public");
			statusDisplay.setTextColor(Color.GRAY);
			break;
		case CHAL_STATUS_ACCEPTEDPUBLIC:
			if (gameperiod == 0) {
				statusDisplay.setText("Accepted");
				statusDisplay.setTextColor(Color.BLUE);
			} else {
				statusDisplay.setText("Game Started");
				statusDisplay.setTextColor(Color.BLUE);
			}
			break;
		case CHAL_STATUS_OPENFRIEND:
			statusDisplay.setText("Pending");
			statusDisplay.setTextColor(Color.MAGENTA);
			break;
		case CHAL_STATUS_ACCEPTEDFRIEND:
			if (gameperiod == 0) {
				statusDisplay.setText("Accepted");
				statusDisplay.setTextColor(Color.BLUE);
			} else {
				statusDisplay.setText("Game Started");
				statusDisplay.setTextColor(Color.BLUE);
			}
			break;
		case CHAL_STATUS_CLOSEDFRIEND:
			boolean win = currentChal.challengerWin();
			if (isChallenger) {
				if (win) {
					statusDisplay.setText("WON");
					statusDisplay.setTextColor(Color.GREEN);
				} else {
					statusDisplay.setText("LOST");
					statusDisplay.setTextColor(Color.RED);
				}
			} else {
				if (!win) {
					statusDisplay.setText("WON");
					statusDisplay.setTextColor(Color.GREEN);
				} else {
					statusDisplay.setText("LOST");
					statusDisplay.setTextColor(Color.RED);
				}
			}
			break;
		case CHAL_STATUS_CLOSEDPUBLIC:
			boolean win2 = currentChal.challengerWin();
			if (isChallenger) {
				if (win2) {
					statusDisplay.setText("WON");
					statusDisplay.setTextColor(Color.GREEN);
				} else {
					statusDisplay.setText("LOST");
					statusDisplay.setTextColor(Color.RED);
				}
			} else {
				if (!win2) {
					statusDisplay.setText("WON");
					statusDisplay.setTextColor(Color.GREEN);
				} else {
					statusDisplay.setText("LOST");
					statusDisplay.setTextColor(Color.RED);
				}
			}
			break;
		}

		TextView homeName, awayName;
		homeName = (TextView) findViewById(R.id.homeTeamName);
		awayName = (TextView) findViewById(R.id.awayTeamName);
		homeName.setText(homeTeam.getDisplayName());
		awayName.setText(awayTeam.getDisplayName());

		detector = new SimpleGestureFilter(this, this);
		
		
	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Review this Challenge";
		String txt = "Look over the current score and rosters associated with this challenge";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
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

	private class GetPageAsync extends AsyncTask<int[], Void, Void> {

		int[] inputs;
		int[] teams = new int[4];
		Handler handle;

		GetPageAsync(int[] in) {
			inputs = in;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog5 = new ProgressDialog(SingleChalActivity.this);
			pDialog5.setMessage("Please wait...");
			pDialog5.setCancelable(false);
			pDialog5.show();
		}

		@Override
		protected Void doInBackground(int[]... args) {
			
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();

			for (int i = 0; i < 2; i++) {
				int currentRosterID = inputs[i];
				Roster currentRoster;
				// get roster if necessary
				if (!currentRosterList.containsRoster(currentRosterID)) {
					JSONObject rosJ = new JSONObject();
					WebContentGet webOb = new WebContentGet();
					rosJ = webOb.getRosterInfo(currentRosterID);
					currentRoster = new Roster(rosJ);
					currentRosterList.push(currentRoster);
					teams[i] = currentRoster.getTeamID();
				} else {
					teams[i] = currentRosterList.getRosterByID(currentRosterID)
							.getTeamID();
				}
				// get teams if needed
				if (!currentTeamList.containsTeam(teams[i])) {
					JSONObject rosT = new JSONObject();
					WebContentGet webOb = new WebContentGet();
					rosT = webOb.getTeamInfo(teams[i]);
					Team homeR = new Team(rosT);
					currentTeamList.push(homeR);
				}
			}
			
			WebContentGet webObj3 = new WebContentGet();
			JSONObject jR3 = new JSONObject();
			jR3 = webObj3.getPlayerInfo(homePlayer2Add);
			Player p3 = new Player(jR3);
			currentPlayerList.push(p3);
			
			WebContentGet webObj4 = new WebContentGet();
			JSONObject jR4 = new JSONObject();
			jR4 = webObj4.getPlayerInfo(awayPlayer2Add);
			Player p4 = new Player(jR4);
			currentPlayerList.push(p4);

			// get User if needed
			if (!currentUserList.contains(inputs[4])) {
				JSONObject rosU = new JSONObject();
				WebContentGet webOb = new WebContentGet();
				rosU = webOb.getUserName(inputs[4]);
				User homeR = new User(rosU);
				currentUserList.push(homeR);
			}

			// get RosterAssignments
			currentAssignList.clear();
			currentPlayerList.clear();
			WebContentGet webObj = new WebContentGet();
			JSONArray jR = new JSONArray();
			try {
				jR = webObj.CHARLIE(inputs[0]);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			WebContentGet webObj2 = new WebContentGet();
			JSONArray jR2 = new JSONArray();
			try {
				jR2 = webObj2.CHARLIE(inputs[2]);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			parseCharlie(jR);
			parseCharlie(jR2);
			

			if (!currentPlayerList.containsPlayer(currentChal.getPlayerID(2))) {
				Log.d("in !currentAssignList contains player 2", " getting player info");
				WebContentGet wO2 = new WebContentGet();
				JSONObject jO2 = new JSONObject();
				jO2 = wO2.getPlayerAssignment(currentChal.getPlayerID(2));
				RosterAssign addAss2 = new RosterAssign(jO2);
				currentAssignList.push(addAss2);
				Player addPlay2 = new Player(jO2);
				currentPlayerList.push(addPlay2);
			}
			
			if (!currentPlayerList.containsPlayer(currentChal.getPlayerID(4))) {
				Log.d("in !currentAssignList contains player 4", " getting player info");
				WebContentGet wO3 = new WebContentGet();
				JSONObject jO3 = new JSONObject();
				jO3 = wO3.getPlayerAssignment(currentChal.getPlayerID(4));
				RosterAssign addAss3 = new RosterAssign(jO3);
				currentAssignList.push(addAss3);
				Player addPlay3 = new Player(jO3);
				currentPlayerList.push(addPlay3);
			}
			

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			boolean usePlayer1, usePlayer2, mainRoster1, mainRoster2;
			float textsize = 0.6f;
			int padding = 5;
			TextView statusText1, scoreText1, positionText1, nameText1, statusText2, scoreText2, positionText2, nameText2;
			TableRow row;
			Activity useActivity = SingleChalActivity.this;
			rosterAssignList useR2 = homeRoster.getSortedRoster();
			rosterAssignList useR1 = awayRoster.getSortedRoster();
			rosterAssignList useH4 = homeRoster2.getSortedRoster();
			rosterAssignList useA3 = awayRoster2.getSortedRoster();
			for (int a = 0; a < useA3.getAssignments(); a++) {
				Log.d("pushing to base rosterlists","useA3");
				useR1.push(useA3.getAssign(a));
			}
			for (int b = 0; b < useH4.getAssignments(); b++) {
				Log.d("pushing to base rosterlists","useH4");
				useR2.push(useH4.getAssign(b));
			}
			int ros1Len = useR1.getAssignments();
			int ros2Len = useR2.getAssignments();
			int len = 0;
			if (ros2Len >= ros1Len)
				len = ros2Len;
			else
				len = ros1Len;

			for (int i = 0; i < len; i++) {
				row = new TableRow(useActivity);
				row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				statusText1 = new TextView(useActivity);
				scoreText1 = new TextView(useActivity);
				positionText1 = new TextView(useActivity);
				nameText1 = new TextView(useActivity);
				statusText2 = new TextView(useActivity);
				scoreText2 = new TextView(useActivity);
				positionText2 = new TextView(useActivity);
				nameText2 = new TextView(useActivity);
				if (i <= ros1Len) {
					Player addplayer = currentPlayerList.getPlayerByID(useR1
							.getAssign(i).getPlayer());
					if (addplayer.getTeam() == awayTeam.getTeamID()) {
						usePlayer1 = true;
						mainRoster1 = true;
					} else if (useR1.getAssign(i).getStatus() == ASSIGN_STATUS_UNLOCKED) {
						mainRoster1 = false;
						usePlayer1 = true;
					} else {
						mainRoster1 = false;
						usePlayer1 = false;
					}

					if (usePlayer1) {

						String playerName = addplayer.getName();
						int lenName = playerName.length() + 1;
						String playerTeam = currentTeamList.getTeamByID(
								addplayer.getTeam()).getLetters();
						int lenTeam = playerTeam.length();
						String displayName = playerName + " " + playerTeam;
						int lenDisp = displayName.length();
						SpannableString ss1 = new SpannableString(displayName);
						ss1.setSpan(new RelativeSizeSpan(0.6f), lenName,
								lenDisp, 0); // set
						// size
						nameText1.setText(ss1);
						positionText1.setText(addplayer.getPosition());
						String score = String.valueOf(useR1.getAssign(i)
								.getPoints());
						scoreText1.setText(score);
						if (mainRoster1) {
							if (addplayer.getID() != awayPlayerAdd)
								statusText1.setText("Active");
							else
								statusText1.setText("Bench");
						}
					} else {
						if (addplayer.getID() == awayPlayer2Add)
							statusText1.setText("Active");
						else
							statusText1.setText("Bench");
					}

				} else {
					nameText1.setText("");
					positionText1.setText("");
					statusText1.setText("");
				}
				if (i <= ros2Len) {
					Player addplayer2 = currentPlayerList.getPlayerByID(useR2
							.getAssign(i).getPlayer());
					if (addplayer2.getTeam() == homeTeam.getTeamID()) {
						usePlayer2 = true;
						mainRoster2 = true;
					} else if (useR2.getAssign(i).getStatus() == ASSIGN_STATUS_UNLOCKED) {
						mainRoster2 = false;
						usePlayer2 = true;
					} else {
						mainRoster2 = false;
						usePlayer2 = false;
					}

					if (usePlayer2) {
						String playerName = addplayer2.getName();
						int lenName = playerName.length() + 1;
						String playerTeam = currentTeamList.getTeamByID(
								addplayer2.getTeam()).getLetters();
						int lenTeam = playerTeam.length();
						String displayName = playerName + " " + playerTeam;
						int lenDisp = displayName.length();
						SpannableString ss2 = new SpannableString(displayName);
						ss2.setSpan(new RelativeSizeSpan(0.6f), lenName,
								lenDisp, 0); // set
						// size
						nameText2.setText(ss2);
						positionText2.setText(addplayer2.getPosition());
						String score2 = String.valueOf(useR2.getAssign(i)
								.getPoints());
						scoreText2.setText(score2);
						if (mainRoster2) {
							if (addplayer2.getID() != homePlayerAdd)
								statusText2.setText("Active");
							else
								statusText2.setText("Bench");
						}
					} else {
						if (addplayer2.getID() == homePlayer2Add)
							statusText2.setText("Active");
						else
							statusText2.setText("Bench");
					}
				} else {
					nameText2.setText("");
					positionText2.setText("");
					statusText2.setText("");
				}
				statusText1.setTextScaleX(textsize);
				statusText1.setPadding(2 * padding, 0, padding, 0);
				statusText1.setTextColor(Color.WHITE);
				scoreText1.setTextScaleX(textsize);
				scoreText1.setPadding(padding / 2, 0, padding / 2, 0);
				scoreText1.setTextColor(Color.WHITE);
				positionText1.setTextScaleX(textsize);
				positionText1.setPadding(padding / 2, 0, padding / 2, 0);
				positionText1.setTextColor(Color.WHITE);
				nameText1.setTextScaleX(textsize);
				nameText1.setPadding(padding / 2, 0, padding / 2, 0);
				nameText1.setTextColor(Color.WHITE);
				nameText2.setTextScaleX(textsize);
				nameText2.setPadding(padding / 2, 0, padding / 2, 0);
				nameText2.setTextColor(Color.WHITE);
				nameText2.setGravity(Gravity.RIGHT);
				positionText2.setTextScaleX(textsize);
				positionText2.setPadding(padding / 2, 0, padding / 2, 0);
				positionText2.setTextColor(Color.WHITE);
				scoreText2.setTextScaleX(textsize);
				scoreText2.setPadding(padding / 2, 0, padding / 2, 0);
				scoreText2.setTextColor(Color.WHITE);
				statusText2.setTextScaleX(textsize);
				statusText2.setPadding(padding, 0, 2 * padding, 0);
				statusText2.setTextColor(Color.WHITE);

				row.addView(statusText1);
				row.addView(scoreText1);
				row.addView(positionText1);
				row.addView(nameText1);
				row.addView(nameText2);
				row.addView(positionText2);
				row.addView(scoreText2);
				row.addView(statusText2);
				tab.addView(row);
			}
			
			// Need a score/date display here

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

			
			// Dismiss the progress dialog
			if (pDialog5.isShowing())
				pDialog5.dismiss();
		}
	}
}