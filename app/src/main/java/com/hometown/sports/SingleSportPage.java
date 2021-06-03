package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class SingleSportPage extends MenuBarActivity implements
		SimpleGestureListener, View.OnClickListener {
	private ProgressDialog pDialog2, pDialog3;
	int sport = 0;
	int importStatus = -1;
	String title = "";
	TextView noGameLabel;
	ImageView imagevw;
	private SimpleGestureFilter detector;
	// JSON Node names
	JSONArray hockeygamesin, teamlistin;
	gameList upcomingGames = new gameList(1);
	// Hashmap for ListView
	ArrayList<HashMap<String, String>> hockeygamelist;
	ListView lv;
	Spinner filter;
	Button createChal;
	boolean useFilter = false;
	int[] subIDs = new int[20];
	ArrayList<String> filterList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlesportlayout);
		ActionBar actionBar = getActionBar();
		actionBar.show();

		Intent in = getIntent();
		sport = in.getIntExtra("sportType", 0);
		noGameLabel = (TextView) findViewById(R.id.noGameTextView);
		noGameLabel.setText("");
		imagevw = (ImageView) findViewById(R.id.sportImage);
		
		switch (sport) {
		case NFL_ID:
			title = "Upcoming NFL Games:";
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_footballnfl", "drawable",
							getPackageName())));
			useFilter = false;
			break;
		case NHL_ID:
			title = "Upcoming NHL Games:";
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier("drawable/" + "ic_hockey",
							"drawable", getPackageName())));
			useFilter = false;
			break;
		case NBA_ID:
			title = "Upcoming NBA Games:";
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_basketballnba", "drawable",
							getPackageName())));
			useFilter = false;
			break;
		case MLB_ID:
			title = "Upcoming MLB Games:";
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier("drawable/" + "ic_baseball",
							"drawable", getPackageName())));
			useFilter = false;
			break;
		case NCAAF_ID:
			title = "Upcoming NCAA Games:";
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_footballncaa", "drawable",
							getPackageName())));
			useFilter = true;
			break;
		case NCAAB_ID:
			title = "Upcoming NCAA Games:";
			imagevw.setImageDrawable(getResources().getDrawable(
					getResources().getIdentifier(
							"drawable/" + "ic_basketballncaa", "drawable",
							getPackageName())));
			useFilter = true;
			break;
		default:
			title = "error";
			useFilter = false;
			break;

		}

		createChal = (Button) findViewById(R.id.buttonCustomChallenge);
		//createChal.setOnClickListener(this);
		
		filter = (Spinner) findViewById(R.id.sportfilter);

		noGameLabel.setText(title);

		hockeygamelist = new ArrayList<HashMap<String, String>>();

		lv = (ListView) findViewById(R.id.sportList);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				Game selectedG = currentGameList.getGame(position);
				setSelectedGame(selectedG);
				
				int homeID = selectedG.getHomeID();
				int awayID = selectedG.getAwayID();
				int allowindex = 0;
				int ros2ID = 0;
				int home2ID = 0;
				int away2ID = 0;
				
				Roster homeR = currentRosterList.getRosterByID(homeID);
				long hometime = homeR.getTime();
				long hometmin = hometime - (600L);
				long hometmax = hometime + (600L);
				int checksport = currentTeamList.getTeamByID(homeR.getTeamID()).getSport();
				for(int i = 0; i<currentRosterList.getRosters();i++){
					Roster checkR = currentRosterList.getRoster(i);
					if(checksport==currentTeamList.getTeamByID(checkR.getTeamID()).getSport()){
					if(checkR.getID()!=homeID && checkR.getID()!=awayID){
						allowindex = i;
						long checkt = checkR.getTime();
						if(checkt>hometmin&&checkt<hometmax){
							ros2ID = checkR.getID();
							break;
						}
					}
					}
				}
				
				if(ros2ID == 0) ros2ID = currentRosterList.getRoster(allowindex).getID();
				
				home2ID = ros2ID;
				
				for(int i = 0; i<currentGameList.getGames();i++){
					if(currentGameList.getGame(i).getAwayID()==home2ID) {
						away2ID = currentGameList.getGame(i).getHomeID();
						break;
					}
					if(currentGameList.getGame(i).getHomeID()==home2ID) {
						away2ID = currentGameList.getGame(i).getAwayID();
						break;
					}
				}		

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SwapPage.class);
				in.putExtra(TAG_HOME1, homeID);
				in.putExtra(TAG_AWAY1, awayID);
				in.putExtra(TAG_HOME2, home2ID);
				in.putExtra(TAG_AWAY2, away2ID);
				in.putExtra(TAG_DISPLAY, selectedG.getDisplay());
				in.putExtra(TAG_SPORTID, sport);
				in.putExtra(TAG_GAMEID, selectedG.getID());

				startActivity(in);

			}
		});

		Log.d("single sport on create", "declaring and launching Getgames()");
		Getgames gameGet = new Getgames(this);
		gameGet.execute();
		
		
		if(useFilter){
			filter.setVisibility(View.VISIBLE);
			subIDs[0] = 1;
			subIDs[1] = 2;
			subIDs[2] = 3;
			subIDs[3] = 4;
			subIDs[4] = 5;
			subIDs[5] = 6;
			subIDs[6] = 7;
			subIDs[7] = 8;
			subIDs[8] = 9;
			subIDs[9] = 10;

			filterList = new ArrayList<String>();
			filterList.add("Top 25");
			filterList.add("ACC");
			filterList.add("B1G");
			filterList.add("Big XII");
			filterList.add("Pac 12");
			filterList.add("SEC");
			filterList.add("American");
			filterList.add("Big East");
			filterList.add("MAC");
			filterList.add("WAC");
			filterList.add("Sun Belt");
			final ArrayAdapter<String> adapterFilter = new ArrayAdapter<String>(
					this, R.layout.spinnerlayout, filterList);
			
			filter.setAdapter(adapterFilter);
			
			
		}
		else{
			filter.setVisibility(View.GONE);
			
		}
		
		filter.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				setGameDisplay(subIDs[pos]);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		detector = new SimpleGestureFilter(this, this);
		
		
	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Pick a Matchup";
		String txt = "Select one of the proposed matchups, or create your own matchup";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.buttonCustomChallenge:
			Intent intent30 = new Intent(context, CreateChallenge.class);
			intent30.putExtra("sportType", sport);
			startActivity(intent30);
		}
	}

	private class Getgames extends AsyncTask<Void, Void, Void> {
		private Activity activity;
		Handler handle;

		public Getgames(Activity a) {
			activity = a;
			Log.d("single sport get games", "constructor");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog2 = new ProgressDialog(SingleSportPage.this);
			pDialog2.setMessage("Please wait...");
			pDialog2.setCancelable(false);
			pDialog2.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();

			WebContentGet webOb1 = new WebContentGet();
			WebContentGet webOb2 = new WebContentGet();

				//need beta function
				try {
					hockeygamesin = webOb2.BRAVO(sport);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				parseBravo(hockeygamesin,sport);

			if (currentGameList.getGames()==0) {
				importStatus = 0;
			} else {
				importStatus = 1;
				hockeygamelist = games2Adapter(currentGameList);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog2.isShowing())
				pDialog2.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			Log.d("single sport get games", "onPostExecute - creating adapter");
			ListAdapter adapter = new GameListAdapter(SingleSportPage.this,
					R.layout.gametable, hockeygamelist);

			lv.setAdapter(adapter);

			Log.d("single sport get games",
					"ImportStatus = " + String.valueOf(importStatus));

			if (importStatus == 0) {
				noGameLabel.setText("There are no Upcoming Games");
			} else if (importStatus == -1) {
				noGameLabel.setText("Communication Error");
			} else {
				noGameLabel.setText("");
				if(useFilter) setGameDisplay(subIDs[0]);
			}
		}
	}
	
	private void setGameDisplay(int id){
		hockeygamelist.clear();
		gameList useGameList = new gameList(1);
		int checkH;
		int checkA;
		
		for(int i = 0; i<currentGameList.getGames();i++){
			checkH = currentTeamList.getTeamByID(currentRosterList.getRosterByID(currentGameList.getGame(i).getHomeID()).getTeamID()).getSportSubID();
			checkA = currentTeamList.getTeamByID(currentRosterList.getRosterByID(currentGameList.getGame(i).getAwayID()).getTeamID()).getSportSubID();
			if(id==20){
				if(checkA>20 | checkH>20){
					useGameList.push(currentGameList.getGame(i));
				}
			}
			else{
				if(checkA == id | checkA == id+20 | checkH == id | checkH == id+20){
					useGameList.push(currentGameList.getGame(i));
				}
			}
		}
		hockeygamelist = games2Adapter(currentGameList);
		ListAdapter adapter = new GameListAdapter(SingleSportPage.this,
				R.layout.gametable, hockeygamelist);
		
		lv.setAdapter(adapter);
		
		if(useGameList.getGames()==0){
			noGameLabel.setText("There are no Upcoming Games");	
		} else {
			noGameLabel.setText("");
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
	
	private class DemoGetgames2 extends AsyncTask<Void, Void, Void> {
		private Activity activity;
		Handler handle;

		public DemoGetgames2(Activity a) {
			activity = a;
			Log.d("single sport get games", "constructor");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog3 = new ProgressDialog(SingleSportPage.this);
			pDialog3.setMessage("Please wait...");
			pDialog3.setCancelable(false);
			pDialog3.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();
			
			WebContentGet webOb = new WebContentGet();
			
			try {
				JSONArray jAr = webOb.getGameList(sport, GAME_STATUS_OPEN, 1);
				
				parseBravoDemo(jAr);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (currentGameList.getGames()==0) {
				importStatus = 0;
			} else {
				importStatus = 1;
				hockeygamelist = games2Adapter(currentGameList);
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog3.isShowing())
				pDialog3.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			Log.d("single sport get games", "onPostExecute - creating adapter");
			ListAdapter adapter = new GameListAdapter(SingleSportPage.this,
					R.layout.gametable, hockeygamelist);

			lv.setAdapter(adapter);

			Log.d("single sport get games",
					"ImportStatus = " + String.valueOf(importStatus));

			if (importStatus == 0) {
				noGameLabel.setText("There are no Upcoming Games");
			} else if (importStatus == -1) {
				noGameLabel.setText("Communication Error");
			} else {
				noGameLabel.setText("");
				if(useFilter) setGameDisplay(subIDs[0]);
			}
		}
	}
	
	
	private class DemoGetgames extends AsyncTask<Void, Void, Void> {
		private Activity activity;
		Handler handle;

		public DemoGetgames(Activity a) {
			activity = a;
			Log.d("single sport get games", "constructor");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog3 = new ProgressDialog(SingleSportPage.this);
			pDialog3.setMessage("Please wait...");
			pDialog3.setCancelable(false);
			pDialog3.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			handle = new Handler();

			long timer = 1431558000L;
			long timer2 = 1431558700L;
			Game addGame1 = new Game(72,70,8,1,timer,30);
			Game addGame2 = new Game(98,79,9,1,timer,30);
			Game addGame3 = new Game(71,96,12,1,timer2,30);
			Game addGame4 = new Game(93,75,13,1,timer2,30);
			upcomingGames.push(addGame1);
			upcomingGames.push(addGame2);
			upcomingGames.push(addGame3);
			upcomingGames.push(addGame4);

			if (upcomingGames.getGames()==0) {
				importStatus = 0;
			} else {
				importStatus = 1;
				hockeygamelist = games2Adapter(upcomingGames);
			}

			
			createplayerdata();
			
			return null;
		}

		private void createplayerdata() {
			RosterAssign a1 = new RosterAssign(1,1381,70,310,0);
			RosterAssign a2 = new RosterAssign(2,1382,70,310,0);
			RosterAssign a3 = new RosterAssign(3,1383,70,310,0);
			RosterAssign a4 = new RosterAssign(4,1384,70,310,0);
			RosterAssign a5 = new RosterAssign(5,1385,70,310,0);
			RosterAssign a6 = new RosterAssign(6,1386,70,310,0);
			RosterAssign a7 = new RosterAssign(7,1387,70,310,0);
			RosterAssign a8 = new RosterAssign(8,1388,70,310,0);
			RosterAssign a9 = new RosterAssign(9,1389,70,310,0);
			RosterAssign a10 = new RosterAssign(10,1390,70,310,0);
			RosterAssign a11 = new RosterAssign(11,1391,70,310,0);
			RosterAssign a12 = new RosterAssign(12,1392,70,310,0);
			RosterAssign a13 = new RosterAssign(13,1393,70,310,0);
			RosterAssign a14 = new RosterAssign(14,1394,70,310,0);
			RosterAssign a15 = new RosterAssign(15,1395,70,310,0);
			RosterAssign a16 = new RosterAssign(16,1396,70,311,0);
			RosterAssign a17 = new RosterAssign(17,1397,70,311,0);
			RosterAssign a18 = new RosterAssign(18,1398,70,311,0);
			RosterAssign a19 = new RosterAssign(19,1399,70,311,0);
			RosterAssign a20 = new RosterAssign(20,1400,70,311,0);
			RosterAssign a21 = new RosterAssign(21,1401,71,310,0);
			RosterAssign a22 = new RosterAssign(22,1402,71,310,0);
			RosterAssign a23 = new RosterAssign(23,1403,71,310,0);
			RosterAssign a24 = new RosterAssign(24,1404,71,310,0);
			RosterAssign a25 = new RosterAssign(25,1405,71,310,0);
			RosterAssign a26 = new RosterAssign(26,1406,71,310,0);
			RosterAssign a27 = new RosterAssign(27,1407,71,310,0);
			RosterAssign a28 = new RosterAssign(28,1408,71,310,0);
			RosterAssign a29 = new RosterAssign(29,1409,71,310,0);
			RosterAssign a30 = new RosterAssign(30,1410,71,310,0);
			RosterAssign a31 = new RosterAssign(31,1411,71,310,0);
			RosterAssign a32 = new RosterAssign(32,1412,71,310,0);
			RosterAssign a33 = new RosterAssign(33,1413,71,310,0);
			RosterAssign a34 = new RosterAssign(34,1414,71,310,0);
			RosterAssign a35 = new RosterAssign(35,1415,71,310,0);
			RosterAssign a36 = new RosterAssign(36,1416,71,311,0);
			RosterAssign a37 = new RosterAssign(37,1417,71,311,0);
			RosterAssign a38 = new RosterAssign(38,1418,71,311,0);
			RosterAssign a39 = new RosterAssign(39,1419,71,311,0);
			RosterAssign a40 = new RosterAssign(40,1420,71,311,0);
			RosterAssign a41 = new RosterAssign(41,1421,72,310,0);
			RosterAssign a42 = new RosterAssign(42,1422,72,310,0);
			RosterAssign a43 = new RosterAssign(43,1423,72,310,0);
			RosterAssign a44 = new RosterAssign(44,1424,72,310,0);
			RosterAssign a45 = new RosterAssign(45,1425,72,310,0);
			RosterAssign a46 = new RosterAssign(46,1426,72,310,0);
			RosterAssign a47 = new RosterAssign(47,1427,72,310,0);
			RosterAssign a48 = new RosterAssign(48,1428,72,310,0);
			RosterAssign a49 = new RosterAssign(49,1429,72,310,0);
			RosterAssign a50 = new RosterAssign(50,1430,72,310,0);
			RosterAssign a51 = new RosterAssign(51,1431,72,310,0);
			RosterAssign a52 = new RosterAssign(52,1432,72,310,0);
			RosterAssign a53 = new RosterAssign(53,1433,72,310,0);
			RosterAssign a54 = new RosterAssign(54,1434,72,310,0);
			RosterAssign a55 = new RosterAssign(55,1435,72,310,0);
			RosterAssign a56 = new RosterAssign(56,1436,72,311,0);
			RosterAssign a57 = new RosterAssign(57,1437,72,311,0);
			RosterAssign a58 = new RosterAssign(58,1438,72,311,0);
			RosterAssign a59 = new RosterAssign(59,1439,72,311,0);
			RosterAssign a60 = new RosterAssign(60,1440,72,311,0);
			RosterAssign a61 = new RosterAssign(61,1481,75,310,0);
			RosterAssign a62 = new RosterAssign(62,1482,75,310,0);
			RosterAssign a63 = new RosterAssign(63,1483,75,310,0);
			RosterAssign a64 = new RosterAssign(64,1484,75,310,0);
			RosterAssign a65 = new RosterAssign(65,1485,75,310,0);
			RosterAssign a66 = new RosterAssign(66,1486,75,310,0);
			RosterAssign a67 = new RosterAssign(67,1487,75,310,0);
			RosterAssign a68 = new RosterAssign(68,1488,75,310,0);
			RosterAssign a69 = new RosterAssign(69,1489,75,310,0);
			RosterAssign a70 = new RosterAssign(70,1490,75,310,0);
			RosterAssign a71 = new RosterAssign(71,1491,75,310,0);
			RosterAssign a72 = new RosterAssign(72,1492,75,310,0);
			RosterAssign a73 = new RosterAssign(73,1493,75,310,0);
			RosterAssign a74 = new RosterAssign(74,1494,75,310,0);
			RosterAssign a75 = new RosterAssign(75,1495,75,310,0);
			RosterAssign a76 = new RosterAssign(76,1496,75,311,0);
			RosterAssign a77 = new RosterAssign(77,1497,75,311,0);
			RosterAssign a78 = new RosterAssign(78,1498,75,311,0);
			RosterAssign a79 = new RosterAssign(79,1499,75,311,0);
			RosterAssign a80 = new RosterAssign(80,1500,75,311,0);
			RosterAssign a81 = new RosterAssign(81,1561,79,310,0);
			RosterAssign a82 = new RosterAssign(82,1562,79,310,0);
			RosterAssign a83 = new RosterAssign(83,1563,79,310,0);
			RosterAssign a84 = new RosterAssign(84,1564,79,310,0);
			RosterAssign a85 = new RosterAssign(85,1565,79,310,0);
			RosterAssign a86 = new RosterAssign(86,1566,79,310,0);
			RosterAssign a87 = new RosterAssign(87,1567,79,310,0);
			RosterAssign a88 = new RosterAssign(88,1568,79,310,0);
			RosterAssign a89 = new RosterAssign(89,1569,79,310,0);
			RosterAssign a90 = new RosterAssign(90,1570,79,310,0);
			RosterAssign a91 = new RosterAssign(91,1571,79,310,0);
			RosterAssign a92 = new RosterAssign(92,1572,79,310,0);
			RosterAssign a93 = new RosterAssign(93,1573,79,310,0);
			RosterAssign a94 = new RosterAssign(94,1574,79,310,0);
			RosterAssign a95 = new RosterAssign(95,1575,79,310,0);
			RosterAssign a96 = new RosterAssign(96,1576,79,311,0);
			RosterAssign a97 = new RosterAssign(97,1577,79,311,0);
			RosterAssign a98 = new RosterAssign(98,1578,79,311,0);
			RosterAssign a99 = new RosterAssign(99,1579,79,311,0);
			RosterAssign a100 = new RosterAssign(100,1580,79,311,0);
			RosterAssign a101 = new RosterAssign(101,1841,93,310,0);
			RosterAssign a102 = new RosterAssign(102,1842,93,310,0);
			RosterAssign a103 = new RosterAssign(103,1843,93,310,0);
			RosterAssign a104 = new RosterAssign(104,1844,93,310,0);
			RosterAssign a105 = new RosterAssign(105,1845,93,310,0);
			RosterAssign a106 = new RosterAssign(106,1846,93,310,0);
			RosterAssign a107 = new RosterAssign(107,1847,93,310,0);
			RosterAssign a108 = new RosterAssign(108,1848,93,310,0);
			RosterAssign a109 = new RosterAssign(109,1849,93,310,0);
			RosterAssign a110 = new RosterAssign(110,1850,93,310,0);
			RosterAssign a111 = new RosterAssign(111,1851,93,310,0);
			RosterAssign a112 = new RosterAssign(112,1852,93,310,0);
			RosterAssign a113 = new RosterAssign(113,1853,93,310,0);
			RosterAssign a114 = new RosterAssign(114,1854,93,310,0);
			RosterAssign a115 = new RosterAssign(115,1855,93,310,0);
			RosterAssign a116 = new RosterAssign(116,1856,93,311,0);
			RosterAssign a117 = new RosterAssign(117,1857,93,311,0);
			RosterAssign a118 = new RosterAssign(118,1858,93,311,0);
			RosterAssign a119 = new RosterAssign(119,1859,93,311,0);
			RosterAssign a120 = new RosterAssign(120,1860,93,311,0);
			RosterAssign a121 = new RosterAssign(121,1901,96,310,0);
			RosterAssign a122 = new RosterAssign(122,1902,96,310,0);
			RosterAssign a123 = new RosterAssign(123,1903,96,310,0);
			RosterAssign a124 = new RosterAssign(124,1904,96,310,0);
			RosterAssign a125 = new RosterAssign(125,1905,96,310,0);
			RosterAssign a126 = new RosterAssign(126,1906,96,310,0);
			RosterAssign a127 = new RosterAssign(127,1907,96,310,0);
			RosterAssign a128 = new RosterAssign(128,1908,96,310,0);
			RosterAssign a129 = new RosterAssign(129,1909,96,310,0);
			RosterAssign a130 = new RosterAssign(130,1910,96,310,0);
			RosterAssign a131 = new RosterAssign(131,1911,96,310,0);
			RosterAssign a132 = new RosterAssign(132,1912,96,310,0);
			RosterAssign a133 = new RosterAssign(133,1913,96,310,0);
			RosterAssign a134 = new RosterAssign(134,1914,96,310,0);
			RosterAssign a135 = new RosterAssign(135,1915,96,310,0);
			RosterAssign a136 = new RosterAssign(136,1916,96,311,0);
			RosterAssign a137 = new RosterAssign(137,1917,96,311,0);
			RosterAssign a138 = new RosterAssign(138,1918,96,311,0);
			RosterAssign a139 = new RosterAssign(139,1919,96,311,0);
			RosterAssign a140 = new RosterAssign(140,1920,96,311,0);
			RosterAssign a141 = new RosterAssign(141,1941,98,310,0);
			RosterAssign a142 = new RosterAssign(142,1942,98,310,0);
			RosterAssign a143 = new RosterAssign(143,1943,98,310,0);
			RosterAssign a144 = new RosterAssign(144,1944,98,310,0);
			RosterAssign a145 = new RosterAssign(145,1945,98,310,0);
			RosterAssign a146 = new RosterAssign(146,1946,98,310,0);
			RosterAssign a147 = new RosterAssign(147,1947,98,310,0);
			RosterAssign a148 = new RosterAssign(148,1948,98,310,0);
			RosterAssign a149 = new RosterAssign(149,1949,98,310,0);
			RosterAssign a150 = new RosterAssign(150,1950,98,310,0);
			RosterAssign a151 = new RosterAssign(151,1951,98,310,0);
			RosterAssign a152 = new RosterAssign(152,1952,98,310,0);
			RosterAssign a153 = new RosterAssign(153,1953,98,310,0);
			RosterAssign a154 = new RosterAssign(154,1954,98,310,0);
			RosterAssign a155 = new RosterAssign(155,1955,98,310,0);
			RosterAssign a156 = new RosterAssign(156,1956,98,311,0);
			RosterAssign a157 = new RosterAssign(157,1957,98,311,0);
			RosterAssign a158 = new RosterAssign(158,1958,98,311,0);
			RosterAssign a159 = new RosterAssign(159,1959,98,311,0);
			RosterAssign a160 = new RosterAssign(160,1960,98,311,0);
			
			currentAssignList.push(a1);
			currentAssignList.push(a2);
			currentAssignList.push(a3);
			currentAssignList.push(a4);
			currentAssignList.push(a5);
			currentAssignList.push(a6);
			currentAssignList.push(a7);
			currentAssignList.push(a8);
			currentAssignList.push(a9);
			currentAssignList.push(a10);
			currentAssignList.push(a11);
			currentAssignList.push(a12);
			currentAssignList.push(a13);
			currentAssignList.push(a14);
			currentAssignList.push(a15);
			currentAssignList.push(a16);
			currentAssignList.push(a17);
			currentAssignList.push(a18);
			currentAssignList.push(a19);
			currentAssignList.push(a20);
			currentAssignList.push(a21);
			currentAssignList.push(a22);
			currentAssignList.push(a23);
			currentAssignList.push(a24);
			currentAssignList.push(a25);
			currentAssignList.push(a26);
			currentAssignList.push(a27);
			currentAssignList.push(a28);
			currentAssignList.push(a29);
			currentAssignList.push(a30);
			currentAssignList.push(a31);
			currentAssignList.push(a32);
			currentAssignList.push(a33);
			currentAssignList.push(a34);
			currentAssignList.push(a35);
			currentAssignList.push(a36);
			currentAssignList.push(a37);
			currentAssignList.push(a38);
			currentAssignList.push(a39);
			currentAssignList.push(a40);
			currentAssignList.push(a41);
			currentAssignList.push(a42);
			currentAssignList.push(a43);
			currentAssignList.push(a44);
			currentAssignList.push(a45);
			currentAssignList.push(a46);
			currentAssignList.push(a47);
			currentAssignList.push(a48);
			currentAssignList.push(a49);
			currentAssignList.push(a50);
			currentAssignList.push(a51);
			currentAssignList.push(a52);
			currentAssignList.push(a53);
			currentAssignList.push(a54);
			currentAssignList.push(a55);
			currentAssignList.push(a56);
			currentAssignList.push(a57);
			currentAssignList.push(a58);
			currentAssignList.push(a59);
			currentAssignList.push(a60);
			currentAssignList.push(a61);
			currentAssignList.push(a62);
			currentAssignList.push(a63);
			currentAssignList.push(a64);
			currentAssignList.push(a65);
			currentAssignList.push(a66);
			currentAssignList.push(a67);
			currentAssignList.push(a68);
			currentAssignList.push(a69);
			currentAssignList.push(a70);
			currentAssignList.push(a71);
			currentAssignList.push(a72);
			currentAssignList.push(a73);
			currentAssignList.push(a74);
			currentAssignList.push(a75);
			currentAssignList.push(a76);
			currentAssignList.push(a77);
			currentAssignList.push(a78);
			currentAssignList.push(a79);
			currentAssignList.push(a80);
			currentAssignList.push(a81);
			currentAssignList.push(a82);
			currentAssignList.push(a83);
			currentAssignList.push(a84);
			currentAssignList.push(a85);
			currentAssignList.push(a86);
			currentAssignList.push(a87);
			currentAssignList.push(a88);
			currentAssignList.push(a89);
			currentAssignList.push(a90);
			currentAssignList.push(a91);
			currentAssignList.push(a92);
			currentAssignList.push(a93);
			currentAssignList.push(a94);
			currentAssignList.push(a95);
			currentAssignList.push(a96);
			currentAssignList.push(a97);
			currentAssignList.push(a98);
			currentAssignList.push(a99);
			currentAssignList.push(a100);
			currentAssignList.push(a101);
			currentAssignList.push(a102);
			currentAssignList.push(a103);
			currentAssignList.push(a104);
			currentAssignList.push(a105);
			currentAssignList.push(a106);
			currentAssignList.push(a107);
			currentAssignList.push(a108);
			currentAssignList.push(a109);
			currentAssignList.push(a110);
			currentAssignList.push(a111);
			currentAssignList.push(a112);
			currentAssignList.push(a113);
			currentAssignList.push(a114);
			currentAssignList.push(a115);
			currentAssignList.push(a116);
			currentAssignList.push(a117);
			currentAssignList.push(a118);
			currentAssignList.push(a119);
			currentAssignList.push(a120);
			currentAssignList.push(a121);
			currentAssignList.push(a122);
			currentAssignList.push(a123);
			currentAssignList.push(a124);
			currentAssignList.push(a125);
			currentAssignList.push(a126);
			currentAssignList.push(a127);
			currentAssignList.push(a128);
			currentAssignList.push(a129);
			currentAssignList.push(a130);
			currentAssignList.push(a131);
			currentAssignList.push(a132);
			currentAssignList.push(a133);
			currentAssignList.push(a134);
			currentAssignList.push(a135);
			currentAssignList.push(a136);
			currentAssignList.push(a137);
			currentAssignList.push(a138);
			currentAssignList.push(a139);
			currentAssignList.push(a140);
			currentAssignList.push(a141);
			currentAssignList.push(a142);
			currentAssignList.push(a143);
			currentAssignList.push(a144);
			currentAssignList.push(a145);
			currentAssignList.push(a146);
			currentAssignList.push(a147);
			currentAssignList.push(a148);
			currentAssignList.push(a149);
			currentAssignList.push(a150);
			currentAssignList.push(a151);
			currentAssignList.push(a152);
			currentAssignList.push(a153);
			currentAssignList.push(a154);
			currentAssignList.push(a155);
			currentAssignList.push(a156);
			currentAssignList.push(a157);
			currentAssignList.push(a158);
			currentAssignList.push(a159);
			currentAssignList.push(a160);
			
			Player p1381 = new Player(1381,"Starting K","K",70,1,300);
			Player p1382 = new Player(1382,"Starting G","G",70,70,300);
			Player p1383 = new Player(1383,"Starting C","C",70,71,300);
			Player p1384 = new Player(1384,"Starting WR","WR",70,80,300);
			Player p1385 = new Player(1385,"Starting WR2","WR",70,81,300);
			Player p1386 = new Player(1386,"Starting DE","DE",70,90,300);
			Player p1387 = new Player(1387,"Starting DT","DT",70,91,300);
			Player p1388 = new Player(1388,"Starting LB","LB",70,54,300);
			Player p1389 = new Player(1389,"Starting LB2","LB",70,50,300);
			Player p1390 = new Player(1390,"Starting LB3","LB",70,51,300);
			Player p1391 = new Player(1391,"Starting CB","CB",70,31,300);
			Player p1392 = new Player(1392,"Starting CB2","CB",70,30,300);
			Player p1393 = new Player(1393,"Backup RB","RB",70,24,300);
			Player p1394 = new Player(1394,"Backup T","T",70,77,300);
			Player p1395 = new Player(1395,"Backup QB","QB",70,4,300);
			Player p1396 = new Player(1396,"Backup DE","DE",70,94,300);
			Player p1397 = new Player(1397,"Starting QB","QB",70,6,300);
			Player p1398 = new Player(1398,"Starting RB","RB",70,22,300);
			Player p1399 = new Player(1399,"Backup C","C",70,57,300);
			Player p1400 = new Player(1400,"Backup WR","WR",70,88,300);
			Player p1401 = new Player(1401,"Starting K","K",71,1,300);
			Player p1402 = new Player(1402,"Starting G","G",71,70,300);
			Player p1403 = new Player(1403,"Starting C","C",71,71,300);
			Player p1404 = new Player(1404,"Starting WR","WR",71,80,300);
			Player p1405 = new Player(1405,"Starting WR2","WR",71,81,300);
			Player p1406 = new Player(1406,"Starting DE","DE",71,90,300);
			Player p1407 = new Player(1407,"Starting DT","DT",71,91,300);
			Player p1408 = new Player(1408,"Starting LB","LB",71,54,300);
			Player p1409 = new Player(1409,"Starting LB2","LB",71,50,300);
			Player p1410 = new Player(1410,"Starting LB3","LB",71,51,300);
			Player p1411 = new Player(1411,"Starting CB","CB",71,31,300);
			Player p1412 = new Player(1412,"Starting CB2","CB",71,30,300);
			Player p1413 = new Player(1413,"Backup RB","RB",71,24,300);
			Player p1414 = new Player(1414,"Backup T","T",71,77,300);
			Player p1415 = new Player(1415,"Backup QB","QB",71,4,300);
			Player p1416 = new Player(1416,"Backup DE","DE",71,94,300);
			Player p1417 = new Player(1417,"Starting QB","QB",71,6,300);
			Player p1418 = new Player(1418,"Starting RB","RB",71,22,300);
			Player p1419 = new Player(1419,"Backup C","C",71,57,300);
			Player p1420 = new Player(1420,"Backup WR","WR",71,88,300);
			Player p1421 = new Player(1421,"Starting K","K",72,1,300);
			Player p1422 = new Player(1422,"Starting G","G",72,70,300);
			Player p1423 = new Player(1423,"Starting C","C",72,71,300);
			Player p1424 = new Player(1424,"Starting WR","WR",72,80,300);
			Player p1425 = new Player(1425,"Starting WR2","WR",72,81,300);
			Player p1426 = new Player(1426,"Starting DE","DE",72,90,300);
			Player p1427 = new Player(1427,"Starting DT","DT",72,91,300);
			Player p1428 = new Player(1428,"Starting LB","LB",72,54,300);
			Player p1429 = new Player(1429,"Starting LB2","LB",72,50,300);
			Player p1430 = new Player(1430,"Starting LB3","LB",72,51,300);
			Player p1431 = new Player(1431,"Starting CB","CB",72,31,300);
			Player p1432 = new Player(1432,"Starting CB2","CB",72,30,300);
			Player p1433 = new Player(1433,"Backup RB","RB",72,24,300);
			Player p1434 = new Player(1434,"Backup T","T",72,77,300);
			Player p1435 = new Player(1435,"Backup QB","QB",72,4,300);
			Player p1436 = new Player(1436,"Backup DE","DE",72,94,300);
			Player p1437 = new Player(1437,"Starting QB","QB",72,6,300);
			Player p1438 = new Player(1438,"Starting RB","RB",72,22,300);
			Player p1439 = new Player(1439,"Backup C","C",72,57,300);
			Player p1440 = new Player(1440,"Backup WR","WR",72,88,300);
			Player p1481 = new Player(1481,"Starting K","K",75,1,300);
			Player p1482 = new Player(1482,"Starting G","G",75,70,300);
			Player p1483 = new Player(1483,"Starting C","C",75,71,300);
			Player p1484 = new Player(1484,"Starting WR","WR",75,80,300);
			Player p1485 = new Player(1485,"Starting WR2","WR",75,81,300);
			Player p1486 = new Player(1486,"Starting DE","DE",75,90,300);
			Player p1487 = new Player(1487,"Starting DT","DT",75,91,300);
			Player p1488 = new Player(1488,"Starting LB","LB",75,54,300);
			Player p1489 = new Player(1489,"Starting LB2","LB",75,50,300);
			Player p1490 = new Player(1490,"Starting LB3","LB",75,51,300);
			Player p1491 = new Player(1491,"Starting CB","CB",75,31,300);
			Player p1492 = new Player(1492,"Starting CB2","CB",75,30,300);
			Player p1493 = new Player(1493,"Backup RB","RB",75,24,300);
			Player p1494 = new Player(1494,"Backup T","T",75,77,300);
			Player p1495 = new Player(1495,"Backup QB","QB",75,4,300);
			Player p1496 = new Player(1496,"Backup DE","DE",75,94,300);
			Player p1497 = new Player(1497,"Starting QB","QB",75,6,300);
			Player p1498 = new Player(1498,"Starting RB","RB",75,22,300);
			Player p1499 = new Player(1499,"Backup C","C",75,57,300);
			Player p1500 = new Player(1500,"Backup WR","WR",75,88,300);
			Player p1561 = new Player(1561,"Starting K","K",79,1,300);
			Player p1562 = new Player(1562,"Starting G","G",79,70,300);
			Player p1563 = new Player(1563,"Starting C","C",79,71,300);
			Player p1564 = new Player(1564,"Starting WR","WR",79,80,300);
			Player p1565 = new Player(1565,"Starting WR2","WR",79,81,300);
			Player p1566 = new Player(1566,"Starting DE","DE",79,90,300);
			Player p1567 = new Player(1567,"Starting DT","DT",79,91,300);
			Player p1568 = new Player(1568,"Starting LB","LB",79,54,300);
			Player p1569 = new Player(1569,"Starting LB2","LB",79,50,300);
			Player p1570 = new Player(1570,"Starting LB3","LB",79,51,300);
			Player p1571 = new Player(1571,"Starting CB","CB",79,31,300);
			Player p1572 = new Player(1572,"Starting CB2","CB",79,30,300);
			Player p1573 = new Player(1573,"Backup RB","RB",79,24,300);
			Player p1574 = new Player(1574,"Backup T","T",79,77,300);
			Player p1575 = new Player(1575,"Backup QB","QB",79,4,300);
			Player p1576 = new Player(1576,"Backup DE","DE",79,94,300);
			Player p1577 = new Player(1577,"Starting QB","QB",79,6,300);
			Player p1578 = new Player(1578,"Starting RB","RB",79,22,300);
			Player p1579 = new Player(1579,"Backup C","C",79,57,300);
			Player p1580 = new Player(1580,"Backup WR","WR",79,88,300);
			Player p1841 = new Player(1841,"Starting K","K",93,1,300);
			Player p1842 = new Player(1842,"Starting G","G",93,70,300);
			Player p1843 = new Player(1843,"Starting C","C",93,71,300);
			Player p1844 = new Player(1844,"Starting WR","WR",93,80,300);
			Player p1845 = new Player(1845,"Starting WR2","WR",93,81,300);
			Player p1846 = new Player(1846,"Starting DE","DE",93,90,300);
			Player p1847 = new Player(1847,"Starting DT","DT",93,91,300);
			Player p1848 = new Player(1848,"Starting LB","LB",93,54,300);
			Player p1849 = new Player(1849,"Starting LB2","LB",93,50,300);
			Player p1850 = new Player(1850,"Starting LB3","LB",93,51,300);
			Player p1851 = new Player(1851,"Starting CB","CB",93,31,300);
			Player p1852 = new Player(1852,"Starting CB2","CB",93,30,300);
			Player p1853 = new Player(1853,"Backup RB","RB",93,24,300);
			Player p1854 = new Player(1854,"Backup T","T",93,77,300);
			Player p1855 = new Player(1855,"Backup QB","QB",93,4,300);
			Player p1856 = new Player(1856,"Backup DE","DE",93,94,300);
			Player p1857 = new Player(1857,"Starting QB","QB",93,6,300);
			Player p1858 = new Player(1858,"Starting RB","RB",93,22,300);
			Player p1859 = new Player(1859,"Backup C","C",93,57,300);
			Player p1860 = new Player(1860,"Backup WR","WR",93,88,300);
			Player p1901 = new Player(1901,"Starting K","K",96,1,300);
			Player p1902 = new Player(1902,"Starting G","G",96,70,300);
			Player p1903 = new Player(1903,"Starting C","C",96,71,300);
			Player p1904 = new Player(1904,"Starting WR","WR",96,80,300);
			Player p1905 = new Player(1905,"Starting WR2","WR",96,81,300);
			Player p1906 = new Player(1906,"Starting DE","DE",96,90,300);
			Player p1907 = new Player(1907,"Starting DT","DT",96,91,300);
			Player p1908 = new Player(1908,"Starting LB","LB",96,54,300);
			Player p1909 = new Player(1909,"Starting LB2","LB",96,50,300);
			Player p1910 = new Player(1910,"Starting LB3","LB",96,51,300);
			Player p1911 = new Player(1911,"Starting CB","CB",96,31,300);
			Player p1912 = new Player(1912,"Starting CB2","CB",96,30,300);
			Player p1913 = new Player(1913,"Backup RB","RB",96,24,300);
			Player p1914 = new Player(1914,"Backup T","T",96,77,300);
			Player p1915 = new Player(1915,"Backup QB","QB",96,4,300);
			Player p1916 = new Player(1916,"Backup DE","DE",96,94,300);
			Player p1917 = new Player(1917,"Starting QB","QB",96,6,300);
			Player p1918 = new Player(1918,"Starting RB","RB",96,22,300);
			Player p1919 = new Player(1919,"Backup C","C",96,57,300);
			Player p1920 = new Player(1920,"Backup WR","WR",96,88,300);
			Player p1941 = new Player(1941,"Starting K","K",98,1,300);
			Player p1942 = new Player(1942,"Starting G","G",98,70,300);
			Player p1943 = new Player(1943,"Starting C","C",98,71,300);
			Player p1944 = new Player(1944,"Starting WR","WR",98,80,300);
			Player p1945 = new Player(1945,"Starting WR2","WR",98,81,300);
			Player p1946 = new Player(1946,"Starting DE","DE",98,90,300);
			Player p1947 = new Player(1947,"Starting DT","DT",98,91,300);
			Player p1948 = new Player(1948,"Starting LB","LB",98,54,300);
			Player p1949 = new Player(1949,"Starting LB2","LB",98,50,300);
			Player p1950 = new Player(1950,"Starting LB3","LB",98,51,300);
			Player p1951 = new Player(1951,"Starting CB","CB",98,31,300);
			Player p1952 = new Player(1952,"Starting CB2","CB",98,30,300);
			Player p1953 = new Player(1953,"Backup RB","RB",98,24,300);
			Player p1954 = new Player(1954,"Backup T","T",98,77,300);
			Player p1955 = new Player(1955,"Backup QB","QB",98,4,300);
			Player p1956 = new Player(1956,"Backup DE","DE",98,94,300);
			Player p1957 = new Player(1957,"Starting QB","QB",98,6,300);
			Player p1958 = new Player(1958,"Starting RB","RB",98,22,300);
			Player p1959 = new Player(1959,"Backup C","C",98,57,300);
			Player p1960 = new Player(1960,"Backup WR","WR",98,88,300);
			
			currentPlayerList.push(p1381);
			currentPlayerList.push(p1382);
			currentPlayerList.push(p1383);
			currentPlayerList.push(p1384);
			currentPlayerList.push(p1385);
			currentPlayerList.push(p1386);
			currentPlayerList.push(p1387);
			currentPlayerList.push(p1388);
			currentPlayerList.push(p1389);
			currentPlayerList.push(p1390);
			currentPlayerList.push(p1391);
			currentPlayerList.push(p1392);
			currentPlayerList.push(p1393);
			currentPlayerList.push(p1394);
			currentPlayerList.push(p1395);
			currentPlayerList.push(p1396);
			currentPlayerList.push(p1397);
			currentPlayerList.push(p1398);
			currentPlayerList.push(p1399);
			currentPlayerList.push(p1400);
			currentPlayerList.push(p1401);
			currentPlayerList.push(p1402);
			currentPlayerList.push(p1403);
			currentPlayerList.push(p1404);
			currentPlayerList.push(p1405);
			currentPlayerList.push(p1406);
			currentPlayerList.push(p1407);
			currentPlayerList.push(p1408);
			currentPlayerList.push(p1409);
			currentPlayerList.push(p1410);
			currentPlayerList.push(p1411);
			currentPlayerList.push(p1412);
			currentPlayerList.push(p1413);
			currentPlayerList.push(p1414);
			currentPlayerList.push(p1415);
			currentPlayerList.push(p1416);
			currentPlayerList.push(p1417);
			currentPlayerList.push(p1418);
			currentPlayerList.push(p1419);
			currentPlayerList.push(p1420);
			currentPlayerList.push(p1421);
			currentPlayerList.push(p1422);
			currentPlayerList.push(p1423);
			currentPlayerList.push(p1424);
			currentPlayerList.push(p1425);
			currentPlayerList.push(p1426);
			currentPlayerList.push(p1427);
			currentPlayerList.push(p1428);
			currentPlayerList.push(p1429);
			currentPlayerList.push(p1430);
			currentPlayerList.push(p1431);
			currentPlayerList.push(p1432);
			currentPlayerList.push(p1433);
			currentPlayerList.push(p1434);
			currentPlayerList.push(p1435);
			currentPlayerList.push(p1436);
			currentPlayerList.push(p1437);
			currentPlayerList.push(p1438);
			currentPlayerList.push(p1439);
			currentPlayerList.push(p1440);
			currentPlayerList.push(p1481);
			currentPlayerList.push(p1482);
			currentPlayerList.push(p1483);
			currentPlayerList.push(p1484);
			currentPlayerList.push(p1485);
			currentPlayerList.push(p1486);
			currentPlayerList.push(p1487);
			currentPlayerList.push(p1488);
			currentPlayerList.push(p1489);
			currentPlayerList.push(p1490);
			currentPlayerList.push(p1491);
			currentPlayerList.push(p1492);
			currentPlayerList.push(p1493);
			currentPlayerList.push(p1494);
			currentPlayerList.push(p1495);
			currentPlayerList.push(p1496);
			currentPlayerList.push(p1497);
			currentPlayerList.push(p1498);
			currentPlayerList.push(p1499);
			currentPlayerList.push(p1500);
			currentPlayerList.push(p1561);
			currentPlayerList.push(p1562);
			currentPlayerList.push(p1563);
			currentPlayerList.push(p1564);
			currentPlayerList.push(p1565);
			currentPlayerList.push(p1566);
			currentPlayerList.push(p1567);
			currentPlayerList.push(p1568);
			currentPlayerList.push(p1569);
			currentPlayerList.push(p1570);
			currentPlayerList.push(p1571);
			currentPlayerList.push(p1572);
			currentPlayerList.push(p1573);
			currentPlayerList.push(p1574);
			currentPlayerList.push(p1575);
			currentPlayerList.push(p1576);
			currentPlayerList.push(p1577);
			currentPlayerList.push(p1578);
			currentPlayerList.push(p1579);
			currentPlayerList.push(p1580);
			currentPlayerList.push(p1841);
			currentPlayerList.push(p1842);
			currentPlayerList.push(p1843);
			currentPlayerList.push(p1844);
			currentPlayerList.push(p1845);
			currentPlayerList.push(p1846);
			currentPlayerList.push(p1847);
			currentPlayerList.push(p1848);
			currentPlayerList.push(p1849);
			currentPlayerList.push(p1850);
			currentPlayerList.push(p1851);
			currentPlayerList.push(p1852);
			currentPlayerList.push(p1853);
			currentPlayerList.push(p1854);
			currentPlayerList.push(p1855);
			currentPlayerList.push(p1856);
			currentPlayerList.push(p1857);
			currentPlayerList.push(p1858);
			currentPlayerList.push(p1859);
			currentPlayerList.push(p1860);
			currentPlayerList.push(p1901);
			currentPlayerList.push(p1902);
			currentPlayerList.push(p1903);
			currentPlayerList.push(p1904);
			currentPlayerList.push(p1905);
			currentPlayerList.push(p1906);
			currentPlayerList.push(p1907);
			currentPlayerList.push(p1908);
			currentPlayerList.push(p1909);
			currentPlayerList.push(p1910);
			currentPlayerList.push(p1911);
			currentPlayerList.push(p1912);
			currentPlayerList.push(p1913);
			currentPlayerList.push(p1914);
			currentPlayerList.push(p1915);
			currentPlayerList.push(p1916);
			currentPlayerList.push(p1917);
			currentPlayerList.push(p1918);
			currentPlayerList.push(p1919);
			currentPlayerList.push(p1920);
			currentPlayerList.push(p1941);
			currentPlayerList.push(p1942);
			currentPlayerList.push(p1943);
			currentPlayerList.push(p1944);
			currentPlayerList.push(p1945);
			currentPlayerList.push(p1946);
			currentPlayerList.push(p1947);
			currentPlayerList.push(p1948);
			currentPlayerList.push(p1949);
			currentPlayerList.push(p1950);
			currentPlayerList.push(p1951);
			currentPlayerList.push(p1952);
			currentPlayerList.push(p1953);
			currentPlayerList.push(p1954);
			currentPlayerList.push(p1955);
			currentPlayerList.push(p1956);
			currentPlayerList.push(p1957);
			currentPlayerList.push(p1958);
			currentPlayerList.push(p1959);
			currentPlayerList.push(p1960);
			
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog2.isShowing())
				pDialog2.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			Log.d("single sport get games", "onPostExecute - creating adapter");
			ListAdapter adapter = new GameListAdapter(SingleSportPage.this,
					R.layout.gametable, hockeygamelist);

			lv.setAdapter(adapter);

			Log.d("single sport get games",
					"ImportStatus = " + String.valueOf(importStatus));

			if (importStatus == 0) {
				noGameLabel.setText("There are no Upcoming Games");
			} else if (importStatus == -1) {
				noGameLabel.setText("Communication Error");
			} else {
				noGameLabel.setText("");
			}
		}
	}
}