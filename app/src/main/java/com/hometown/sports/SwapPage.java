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
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

public class SwapPage extends MenuBarActivity implements SimpleGestureListener,
		View.OnClickListener {
	private SimpleGestureFilter detector;
	
	String name;
	int home, away, home2, away2, gID, sportID;
	int playH1, playH2, playA1, playA2 = 0;
	Roster homeR, awayR, homeR2, awayR2;
	Team homeT, awayT;
	Button next;
	ListView homeSwapList, awaySwapList;
	int[] awayList1, awayList2, homeList1, homeList2;
	TextView homePlayerAdd, homePlayerDrop, awayPlayerAdd, awayPlayerDrop;
	boolean isCounter;
	int accptr, prevID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swappage);
		detector = new SimpleGestureFilter(this, this);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		Intent in = getIntent();

		// Get JSON values from previous intent
		name = in.getStringExtra(TAG_DISPLAY);
		home = in.getIntExtra(TAG_HOME1, 0);
		away = in.getIntExtra(TAG_AWAY1, 0);
		home2 = in.getIntExtra(TAG_HOME2, 0);
		away2 = in.getIntExtra(TAG_AWAY2, home2);
		gID = in.getIntExtra(TAG_GAMEID, 1);
		sportID = in.getIntExtra(TAG_SPORTID, 0);
		isCounter = in.getBooleanExtra(TAG_ISCOUNTER,false);
		accptr = in.getIntExtra(TAG_ACCEPTER,0);
		accptr = in.getIntExtra(TAG_ID,0);
		

		homeR = currentRosterList.getRosterByID(home);
		awayR = currentRosterList.getRosterByID(away);
		homeR2 = currentRosterList.getRosterByID(home2);
		awayR2 = currentRosterList.getRosterByID(away2);
		homeT = currentTeamList.getTeamByID(homeR.getTeamID());
		awayT = currentTeamList.getTeamByID(awayR.getTeamID());
		TextView awayName = (TextView) findViewById(R.id.swapAwayName);
		TextView homeName = (TextView) findViewById(R.id.swapHomeName);
		homePlayerAdd = (TextView) findViewById(R.id.swapHomePlayerIn);
		homePlayerDrop = (TextView) findViewById(R.id.swapHomePlayerOut);
		awayPlayerAdd = (TextView) findViewById(R.id.swapAwayPlayerIn);
		awayPlayerDrop = (TextView) findViewById(R.id.swapAwayPlayerOut);
		awayList1 = new int[100];
		awayList2 = new int[100];
		homeList1 = new int[100];
		homeList2 = new int[100];

		awayName.setText(awayT.getDisplayName());
		homeName.setText(homeT.getDisplayName());

		ImageView awayLogo = (ImageView) findViewById(R.id.swapAwayLogo);
		ImageView homeLogo = (ImageView) findViewById(R.id.swapHomeLogo);

		String homeIcon = homeT.getLogo();
		String awayIcon = awayT.getLogo();

		new DownloadImageTask(homeLogo).execute(homeIcon);
		new DownloadImageTask(awayLogo).execute(awayIcon);

		next = (Button) findViewById(R.id.swapNextButton);
		next.setText("Pick Swap Ins");
		next.setOnClickListener(this);

		awaySwapList = (ListView) findViewById(R.id.swapAwayList);
		awaySwapList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				playA1 = awayList1[position];
				//playA2 = awayList2[position];

				updateSwapDisplay(1);
				updateLists(2);

			}

		});
		homeSwapList = (ListView) findViewById(R.id.swapHomeList);
		homeSwapList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				playH1 = homeList1[position];
				//playH2 = homeList2[position];

				updateSwapDisplay(2);
				updateLists(1);

			}
		});
		
		new getRosters().execute();

	}

	public void onResume() {
		super.onResume();
		String ttl = "Set the Swap";
		String txt = "Select one player to drop from each roster.  Each roster's score will be calculated as the sum of all points scored by non-dropped players and the added player during today's games. \n\nThis is an important step in ensuring that GameDaySwap maintains legal compliance with UIGEA.";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}

	private void updateSwapDisplay(int i) {
		Player dropPlayer, addedPlayer;
		if (i == 1) {
			dropPlayer = currentPlayerList.getPlayerByID(playA1);
			addedPlayer = currentPlayerList.getPlayerByID(playA2);
		} else {
			dropPlayer = currentPlayerList.getPlayerByID(playH1);
			addedPlayer = currentPlayerList.getPlayerByID(playH2);
		}

		String dropPos = dropPlayer.getPosition();
		String dropName = dropPos + " " + dropPlayer.getName();
		int lenName = dropName.length() + 1;
		String dropTeam = currentTeamList.getTeamByID(dropPlayer.getTeam())
				.getLetters();
		String displayName = dropName + " " + dropTeam;
		int lenDisp = displayName.length();
		SpannableString ss1 = new SpannableString(displayName);
		ss1.setSpan(new RelativeSizeSpan(0.6f), lenName, lenDisp, 0);
/*
		String addedPos = addedPlayer.getPosition();
		String addedName = addedPos + " " + addedPlayer.getName();
		int lenName2 = addedName.length() + 1;
		String addedTeam = currentTeamList.getTeamByID(addedPlayer.getTeam())
				.getLetters();
		String displayName2 = addedName + " " + addedTeam;
		int lenDisp2 = displayName2.length();
		SpannableString ss2 = new SpannableString(displayName2);
		ss2.setSpan(new RelativeSizeSpan(0.6f), lenName2, lenDisp2, 0);
		*/

		if (i == 1) {
			awayPlayerDrop.setText(ss1);
			awayPlayerAdd.setText("Pick on next page");
		} else {
			homePlayerDrop.setText(ss1);
			homePlayerAdd.setText("Pick on next page");
		}
	}

	private void updateLists(int i) {
		ArrayList<HashMap<String, String>> useList = new ArrayList<HashMap<String, String>>();
		int rosterID, roster2, checkPlayer;
		int pos = 0;
		if (i == 1) {
			rosterID = away;
			roster2 = home;
			checkPlayer = playH2;
		} else {
			rosterID = home;
			roster2 = away;
			checkPlayer = playA2;
		}
		
		for (int j = 0; j < currentAssignList.getAssignments(); j++) {
			RosterAssign useAss = currentAssignList.getAssign(j);
			if (useAss.getRoster() == rosterID
					&& useAss.getStatus() > ASSIGN_STATUS_LOCKED) {
				Player subPlayer = currentPlayerList
						.getPlayerByID(useAss.getPlayer());
				
				HashMap<String, String> swapmap = new HashMap<String, String>();
				swapmap.put("Type", String.valueOf(2));
				swapmap.put("subName", subPlayer.getName());
				swapmap.put("subPos", subPlayer.getPosition());
				swapmap.put("subTeam",
						currentTeamList
								.getTeamByID(subPlayer.getTeam())
								.getLetters());
				useList.add(swapmap);
				if (i == 1) {
					//awayList1[pos] = mainPlayer.getID();
					awayList1[pos] = subPlayer.getID();
				} else {
					//homeList1[pos] = mainPlayer.getID();
					homeList1[pos] = subPlayer.getID();
				}
				pos++;
			}
		}

		/*
		for (int j = 0; j < currentAssignList.getAssignments(); j++) {
			RosterAssign useAss = currentAssignList.getAssign(j);
			if (useAss.getRoster() == rosterID
					&& useAss.getStatus() > ASSIGN_STATUS_LOCKED) {
				for (int k = 0; k < currentAssignList.getAssignments(); k++) {
					RosterAssign useAss2 = currentAssignList.getAssign(k);
					if (useAss2.getRoster() != rosterID
							&& useAss2.getRoster() != roster2
							&& useAss2.getStatus() == useAss.getStatus()
							&& useAss2.getPlayer() != checkPlayer) {
						HashMap<String, String> swapmap = new HashMap<String, String>();
						Player mainPlayer = currentPlayerList
								.getPlayerByID(useAss.getPlayer());
						Player subPlayer = currentPlayerList
								.getPlayerByID(useAss2.getPlayer());
						swapmap.put("mainName", mainPlayer.getName());
						swapmap.put("mainPos", mainPlayer.getPosition());
						swapmap.put(
								"mainTeam",
								currentTeamList.getTeamByID(
										mainPlayer.getTeam()).getLetters());
						swapmap.put("subName", subPlayer.getName());
						swapmap.put("subPos", subPlayer.getPosition());
						swapmap.put("subTeam",
								currentTeamList
										.getTeamByID(subPlayer.getTeam())
										.getLetters());
						useList.add(swapmap);
						if (i == 1) {
							awayList1[pos] = mainPlayer.getID();
							awayList2[pos] = subPlayer.getID();
						} else {
							homeList1[pos] = mainPlayer.getID();
							homeList2[pos] = subPlayer.getID();
						}
						pos++;
					}
				}
			}
		}
		*/

		ListAdapter adapter = new SwapListAdapter(SwapPage.this,
				R.layout.swap_list_item, useList);

		if (i == 1)
			awaySwapList.setAdapter(adapter);
		else
			homeSwapList.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.swapNextButton:
			if(!isCounter){
			Intent intent1 = new Intent(context, SwapPage2.class);
			intent1.putExtra(TAG_HOME1, home);
			intent1.putExtra(TAG_HOME2, home2);
			intent1.putExtra(TAG_AWAY1, away);
			intent1.putExtra(TAG_AWAY2, away2);
			intent1.putExtra(TAG_HOMEP1, playH1);
			intent1.putExtra(TAG_HOMEP2, playH2);
			intent1.putExtra(TAG_AWAYP1, playA1);
			intent1.putExtra(TAG_AWAYP2, playA2);
			intent1.putExtra(TAG_SPORTID, sportID);
			intent1.putExtra(TAG_DISPLAY, name);
			intent1.putExtra(TAG_GAMEID, gID);
			startActivity(intent1);
			}
			else{
				Intent intent1 = new Intent(context, SwapPage2.class);
				intent1.putExtra(TAG_HOME1, home);
				intent1.putExtra(TAG_HOME2, home2);
				intent1.putExtra(TAG_AWAY1, away);
				intent1.putExtra(TAG_AWAY2, away2);
				intent1.putExtra(TAG_HOMEP1, playH1);
				intent1.putExtra(TAG_HOMEP2, playH2);
				intent1.putExtra(TAG_AWAYP1, playA1);
				intent1.putExtra(TAG_AWAYP2, playA2);
				intent1.putExtra(TAG_SPORTID, sportID);
				intent1.putExtra(TAG_DISPLAY, name);
				intent1.putExtra(TAG_ACCEPTER, accptr);
				intent1.putExtra(TAG_ID, prevID);
				startActivity(intent1);
			}
			break;
		}
	}

	@Override
	public void onSwipe(int direction) {
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
		// TODO Auto-generated method stub

	}

	ProgressDialog pDialog2;

	public class getRostersDEMO extends AsyncTask<Void, Void, Void> {

		Handler handle;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog2 = new ProgressDialog(SwapPage.this);
			pDialog2.setMessage("Please wait...");
			pDialog2.setCancelable(false);
			pDialog2.show();
			Log.d("SwapPage", "Starting get RosterDemo");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (Looper.myLooper() == null)
				Looper.prepare();
			handle = new Handler();
			
			currentAssignList.clear();
			currentPlayerList.clear();
			WebContentGet webObj = new WebContentGet();
			JSONArray jR = new JSONArray();
			jR = webObj.getFullRoster(home);
/*
			WebContentGet webObj2 = new WebContentGet();
			JSONArray jR2 = new JSONArray();
			jR2 = webObj2.getFullRoster(home2);
*/
			WebContentGet webObj3 = new WebContentGet();
			JSONArray jR3 = new JSONArray();
			jR3 = webObj3.getFullRoster(away);
/*			
			int stoploop = 0;
			JSONArray jR4 = new JSONArray();
			if (away2!= home2){
				stoploop = 4;
				WebContentGet webObj4 = new WebContentGet();
				jR4 = webObj4.getFullRoster(away2);
			}
			else stoploop = 3;
			*/
			JSONArray useArray;

			for (int j = 0; j < 2; j++) {
				switch (j) {
				case 0:
					useArray = jR;
					break;
				case 1:
					useArray = jR3;
					break;
				case 2:
					useArray = jR3;
					break;
				case 3:
					useArray = jR3;
					break;
				default:
					useArray = jR;
				}

				for (int i = 0; i < useArray.length(); i++) {
					JSONObject o = new JSONObject();
					int rosAssID = 1;
					try {
						o = useArray.getJSONObject(i);
						rosAssID = Integer.parseInt(o.getString("paID"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (!currentAssignList.containsRosterAssign(rosAssID)) {
						WebContentGet wO = new WebContentGet();
						JSONObject jO = new JSONObject();
						jO = wO.getPlayerAssignment(rosAssID);
						RosterAssign addAss = new RosterAssign(jO);
						currentAssignList.push(addAss);
						Player addPlay = new Player(jO);
						currentPlayerList.push(addPlay);
						
						if(playH1 == 0 && addAss.getRoster() == home && addAss.getStatus()>ASSIGN_STATUS_LOCKED) playH1 = addPlay.getID();
						//if(playH2 == 0 && addAss.getRoster() == home2 && addAss.getStatus()>ASSIGN_STATUS_LOCKED) playH2 = addPlay.getID();
						if(playA1 == 0 && addAss.getRoster() == away && addAss.getStatus()>ASSIGN_STATUS_LOCKED) playA1 = addPlay.getID();
						//if(playA2 == 0 && addAss.getRoster() == away2 && addAss.getStatus()>ASSIGN_STATUS_LOCKED && addPlay.getID()!=playH2) playA2 = addPlay.getID();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.d("SwapPage", "Leaving get RosterDemo");
			updateLists(1);
			updateLists(2);
			updateSwapDisplay(1);
			updateSwapDisplay(2);
			if (pDialog2.isShowing())
				pDialog2.dismiss();
		}
	}
	
	public class getRosters extends AsyncTask<Void, Void, Void> {

		Handler handle;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog2 = new ProgressDialog(SwapPage.this);
			pDialog2.setMessage("Please wait...");
			pDialog2.setCancelable(false);
			pDialog2.show();
			Log.d("SwapPage", "Starting get Roster");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (Looper.myLooper() == null)
				Looper.prepare();
			handle = new Handler();
			
			currentAssignList.clear();
			currentPlayerList.clear();
			WebContentGet webObj = new WebContentGet();
			JSONArray jR = new JSONArray();
			try {
				jR = webObj.CHARLIE(home);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			WebContentGet webObj3 = new WebContentGet();
			JSONArray jR3 = new JSONArray();
			try {
				jR3 = webObj3.CHARLIE(away);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			parseCharlie(jR);
			parseCharlie(jR3);

			
				for (int i = 0; i < currentAssignList.getAssignments(); i++) {
					
					RosterAssign addAss = currentAssignList.getAssign(i);
					Player addPlay = currentPlayerList.getPlayerByID(addAss.getPlayer());
					
						
						if(playH1 == 0 && addAss.getRoster() == home && addAss.getStatus()>ASSIGN_STATUS_LOCKED) playH1 = addPlay.getID();
						//if(playH2 == 0 && addAss.getRoster() == home2 && addAss.getStatus()>ASSIGN_STATUS_LOCKED) playH2 = addPlay.getID();
						if(playA1 == 0 && addAss.getRoster() == away && addAss.getStatus()>ASSIGN_STATUS_LOCKED) playA1 = addPlay.getID();
						//if(playA2 == 0 && addAss.getRoster() == away2 && addAss.getStatus()>ASSIGN_STATUS_LOCKED && addPlay.getID()!=playH2) playA2 = addPlay.getID();
						
						if(playH1 !=0 && playA1!=0) break;
					
				}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.d("SwapPage", "Leaving get Roster");
			updateLists(1);
			updateLists(2);
			updateSwapDisplay(1);
			updateSwapDisplay(2);
			if (pDialog2.isShowing())
				pDialog2.dismiss();
		}
	}

}
