package com.hometown.sports;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.MakeChalConfirmation.makeChalActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MenuBarActivity extends FragmentActivity {
	public static final int NFL_ID = 1;
	public static final int NHL_ID = 3;
	public static final int MLB_ID = 2;
	public static final int NBA_ID = 4;
	public static final int NCAAF_ID = 5;
	public static final int NCAAB_ID = 6;
	public static final int ACTIVE_ID = 2;
	public static final int UPCOMING_ID = 1;
	public static final int COMPLETED_ID = 3;
	public static final int PENDING_CHAL_ID = 1;
	public static final int ACTIVE_CHAL_ID = 2;
	public static final int COMPLETE_CHAL_ID = 3;
	public static final int CLOSED_CHAL_ID = 4;
	public static final int LOGIN_STATUS_NULL = 0;
	public static final int LOGIN_STATUS_TRUE = 1;
	public static final int LOGIN_STATUS_FALSE = 2;
	public static final int USER_STATUS_FAILEDLOGIN = 10;
	public static final int USER_STATUS_SUCCESSFULLOGIN = 11;
	public static final int KEY_STATUS_FAILED = 12;
	public static final int KEY_STATUS_PASS = 13;
	public static final int CHAL_STATUS_OPENPUBLIC = 20;
	public static final int CHAL_STATUS_ACCEPTEDPUBLIC = 21;
	public static final int CHAL_STATUS_CLOSEDPUBLIC = 22;
	public static final int CHAL_STATUS_OPENFRIEND = 25;
	public static final int CHAL_STATUS_ACCEPTEDFRIEND = 26;
	public static final int CHAL_STATUS_CLOSEDFRIEND = 27;
	public static final int CHAL_STATUS_FAILED = 28;
	public static final int CHAL_STATUS_SUCCESSFUL = 29;
	public static final int GAME_STATUS_OPEN = 30;
	public static final int GAME_STATUS_CLOSED = 31;
	public static final int GAME_STATUS_INPROGRESS = 32;
	public static final int GAME_STATUS_FINISHED = 33;
	public static final int GAME_STATUS_CANCELLED = 34;
	public static final int TRANSACTION_STATUS_DEPOSITEDMONEY = 40;
	public static final int TRANSACTION_STATUS_WONMONEY = 41;
	public static final int TRANSACTION_STATUS_FROZENMONEY = 42;
	public static final int TRANSACTION_STATUS_LOSTMONEY = 43;
	public static final int TRANSACTION_STATUS_CASHOUT = 44;
	public static final int TRANSACTION_STATUS_REFUND = 45;
	public static final int TRANSACTION_STATUS_NSF = 46; // NOT SUFFICIENT FUNDS
	public static final int DATA_STATUS_WRONGTYPE = 60;
	public static final int DATA_STATUS_TYPENOTSET = 61;
	public static final int LIST_STATUS_NORESULTS = 70;
	public static final int LIST_STATUS_RESULTS = 71;
	public static final int LIST_STATUS_PERSONAL = 75;
	public static final int LIST_STATUS_PUBLIC = 76;
	public static final int WINNER_STATUS_HOME = 90;
	public static final int WINNER_STATUS_AWAY = 91;
	public static final int WINNER_STATUS_UNDER = 93;
	public static final int WINNER_STATUS_OVER = 92;
	public static final int USER_STATUS_CREATED = 120;
	public static final int USER_STATUS_PREEXISTINGEMAIL = 121;
	public static final int USER_STATUS_INVALIDEMAIL = 122;
	public static final int USER_STATUS_INVALIDPW = 123;
	public static final int FRIEND_STATUS_SHOW = 141;
	public static final int FRIEND_STATUS_HIDE = 140;
	public static final int FRIEND_STATUS_MEMBER = 142;
	public static final int FRIEND_STATUS_NONMEMBER = 143;
	public static final int CREDITCARD_STATUS_PASS = 200;
	public static final int CREDITCARD_STATUS_FAIL = 201;
	public static final int PLAYER_STATUS_ACTIVE = 300;
	public static final int PLAYER_STATUS_INJURED = 301;
	public static final int ASSIGN_STATUS_LOCKED = 310;
	public static final int ASSIGN_STATUS_UNLOCKED = 311;
	public static final int CHAL_STATUS_ALLOPEN = 400;
	public static final int CHAL_STATUS_ALLACCEPTED = 401;
	public static final int CHAL_STATUS_ALLCLOSED = 402;
	public static final int CHAL_STATUS_ALLOPENACCEPTED = 403;
	public static final String TAG_DISPLAY = "gamedisplay";
	public static final String TAG_HOME1 = "home1";
	public static final String TAG_AWAY1 = "away1";
	public static final String TAG_HOME2 = "home2";
	public static final String TAG_AWAY2 = "away2";
	public static final String TAG_HOMEP1 = "homeP1";
	public static final String TAG_AWAYP1 = "awayP1";
	public static final String TAG_HOMEP2 = "homeP2";
	public static final String TAG_AWAYP2 = "awayP2";
	public static final String TAG_GAMEID = "gameID";
	public static final String TAG_SPORTID = "sportID";
	public static final String TAG_WINNER = "winner";
	public static final String TAG_LINE = "line";
	public static final String TAG_VALUE = "value";
	public static final String TAG_STATUS = "status";
	public static final String TAG_ACCEPTER = "accepter";
	public static final String TAG_ID = "chalID";
	public static final String TAG_CHALLENGER = "challenger";
	public static final String TAG_ISCOUNTER = "isCounter";
	public static final String URL = "http://54.149.109.217/api/";
	public static final String ImageURL = "http://54.149.109.217/api/uploads/logos/";
	public static int playerH1;
	public static int playerH2;
	public static int playerA1;
	public static int playerA2;
	public int TRYINT = 7;
	public Game selectedGame;
	public static String userFName;
	public static String userLName;
	public static String userName;
	public static double userTotalAccount;
	public static double userFrozenAccount;
	public static double userFreeAccount;
	public static String userEmail;
	public static String userCCNDisp;
	public static String userPW;
	private String gameDisplayString;
	private String chalDisplayString;
	private String opponentString;
	private String dispLine;
	private String wagerDisplayString;
	private String detailChalDisplay;
	public static Challenge selectedChal;
	public static int thisUser;
	private String keyA;
	private String keyB;
	private int token;
	private String Salt;
	private long expires;
	static public gameList currentGameList = new gameList(1);
	static public friendList currentFriendList = new friendList(1);
	static public playerList currentPlayerList = new playerList(1);
	static public rosterList currentRosterList = new rosterList(1);
	static public rosterAssignList currentAssignList = new rosterAssignList(1);
	static public teamList currentTeamList = new teamList(1);
	static public userList currentUserList = new userList(1);
	static public chalList currentChalList = new chalList(1);
	public int gameperiod;
	public int gamemin;
	public int gamesec;
	public String gamedate;
	public String gametime;
	public static String bottomTitle = "";
	public static String bottomText = "";
	public static int bottomInt = 0;
	public static SessionManager session;
	public static Activity currentActivity;
	public static Context currentContext;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		// session = new SessionManager(getApplicationContext());
		inflater.inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Context context = this;
		WebContentGet wbcOb = new WebContentGet();
		switch (item.getItemId()) {
		case R.id.menu_goHome:

			Intent intent17 = new Intent(context, HomeTownSportsHome.class);
			startActivity(intent17);
			return true;
		case R.id.menu_inviteFriends:
			// intent start invite page
			Intent intent18 = new Intent(context, InviteFriends.class);
			startActivity(intent18);

			return true;
		case R.id.menu_makeChal:

			Intent intent11 = new Intent(context, ChallengePage.class);
			startActivity(intent11);
			return true;
		case R.id.menu_checkActive:

			Intent intent12 = new Intent(context, UserChalListPage.class);
			intent12.putExtra("chalType", CHAL_STATUS_ALLACCEPTED);
			startActivity(intent12);
			return true;
		case R.id.menu_checkPending:
			Intent intent13 = new Intent(context, Pendingchallenges.class);
			intent13.putExtra("chalType", CHAL_STATUS_ALLOPEN);
			startActivity(intent13);
			return true;
		case R.id.menu_checkHistory:
			
			/*
			Intent intent15 = new Intent(context, UserChalListPage.class);
			intent15.putExtra("chalType", CHAL_STATUS_ALLCLOSED);
			startActivity(intent15);
			*/
			
			WebContentGet webOb5 = new WebContentGet();
			try {
				JSONArray jAR5 = webOb5.ECHO(3);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			return true;
		case R.id.menu_accountInfo:

			Intent intent14 = new Intent(context, AccountInfo.class);
			startActivity(intent14);
			return true;
		case R.id.menu_upgradeAcct:

			WebContentGet webOb = new WebContentGet();
			WebContentGet webOb2 = new WebContentGet();
			JSONArray jAR = webOb.getFriends();
			// friendID //sender //receiver //status

			webOb.getFriends();
			
			WebContentGet webOb3 = new WebContentGet();
			JSONArray conAr = new JSONArray();
			JSONObject Cont = new JSONObject();
			JSONObject Cont2 = new JSONObject();
			long phone1 = 9375326699L;
			long phone2 = 9375326702L;
			try {
				Cont.put("number1",phone1);
				Cont.put("number2",phone2);
				
				Cont2.put("Number", phone2);
				Cont2.put("Name", "Mark Cubes");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			conAr.put(Cont);
			conAr.put(Cont2);
			
			webOb2.makeFriends(10);
			webOb3.updateFriends(4,140);
			
			WebContentGet webOb6 = new WebContentGet();
			webOb6.getAddFriends(Cont);
			
			WebContentGet webOb7 = new WebContentGet();
			JSONArray jarray1 = new JSONArray();
			jarray1 = webOb7.getPublicChalList(72,79);
			
			// Intent intent30 = new Intent(context, CreateChallenge.class);
			// startActivity(intent30);
			

			return true;
		case R.id.menu_logOff:

			session.logoutUser();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static void setUserName(String fn, String ln) {
		userFName = fn;
		userLName = ln;
		userName = fn + " " + ln;

	}

	public static void setUserAcctInfo(double free, double frozen, String CCND,
			String email) {
		userFreeAccount = free;
		userFrozenAccount = frozen;
		userTotalAccount = userFreeAccount + userFrozenAccount;
		userCCNDisp = CCND;
		userEmail = email;
	}

	public void setUserAcctMoney(double free, double frozen) {
		userFreeAccount = free;
		userFrozenAccount = frozen;
		userTotalAccount = userFreeAccount + userFrozenAccount;
	}

	public void convertToChalDisplay(Challenge c) {
		
		int chalID = c.getChallenger();
		int opponentID;

		boolean isChallenger = false;
		if (chalID == thisUser)
			isChallenger = true;
		else
			isChallenger = false;
		String acpName = "";
		if(c.getAccepter()!=0) acpName= currentUserList
				.getUserByID(c.getAccepter()).getName();
		else acpName = "Public";
		String chalName = "";
		if(chalID!=0) chalName = currentUserList.getUserByID(
				c.getChallenger()).getName();
		else chalName = "Public";
		int winner = c.getWinner();
		double line = c.getLine();
		double wager = c.getWager();
		int gid = 0;
		int h = 0;
		int a = 0;
		int spid = 0;
		int time = 0;
		int date = 0;
		int homeRoster = c.getHomeRoster();
		int awayRoster = c.getAwayRoster();

		if (currentRosterList.containsRoster(homeRoster)) {
			Roster homeR = currentRosterList.getRosterByID(homeRoster);
			h = homeR.getTeamID();
		} else {
			WebContentGet webOb = new WebContentGet();
			JSONObject j = webOb.getRosterInfo(homeRoster);
			Roster homeR = new Roster(j);
			currentRosterList.push(homeR);
			h = homeR.getTeamID();
		}

		if (currentRosterList.containsRoster(awayRoster)) {
			Roster awayR = currentRosterList.getRosterByID(awayRoster);
			a = awayR.getTeamID();
		} else {
			WebContentGet webOb = new WebContentGet();
			JSONObject j = webOb.getRosterInfo(awayRoster);
			Roster awayR = new Roster(j);
			currentRosterList.push(awayR);
			a = awayR.getTeamID();
		}

		Team homeT;
		Team awayT;

		if (currentTeamList.containsTeam(h)) {
			homeT = currentTeamList.getTeamByID(h);
		} else {
			WebContentGet webOb = new WebContentGet();
			JSONObject j = webOb.getTeamInfo(h);
			homeT = new Team(j);
			currentTeamList.push(homeT);
		}
		if (currentTeamList.containsTeam(a)) {
			awayT = currentTeamList.getTeamByID(a);
		} else {
			WebContentGet webOb = new WebContentGet();
			JSONObject j = webOb.getTeamInfo(a);
			awayT = new Team(j);
			currentTeamList.push(awayT);
		}

		String dispH = homeT.getDisplayName();
		String dispA = awayT.getDisplayName();

		gameDisplayString = dispA + " at " + dispH;

		chalDisplayString = "";
		dispLine = "";
		String team = "";
		opponentString = "";
		wagerDisplayString = "";
		detailChalDisplay = "";
		String oTeam = "";

		if (isChallenger) {
			String opp = acpName;
			opponentID = selectedChal.getAccepter();
			String wgrStr = "$" + String.valueOf(wager);
			opponentString = opp;
			wagerDisplayString = wgrStr;
			switch (winner) {
			case (WINNER_STATUS_AWAY):
				team = dispA;
				oTeam = dispH;
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "the " + team + " final score " + dispLine
						+ " points is more than the " + oTeam + " final score";
				break;
			case (WINNER_STATUS_HOME):
				team = dispH;
				oTeam = dispA;
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "the " + team + " final score " + dispLine
						+ " points is more than the " + oTeam + " final score";
				break;
			case (WINNER_STATUS_OVER):
				team = "Over";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "Both teams combined score is " + team
						+ " " + dispLine + " points";
				break;
			case (WINNER_STATUS_UNDER):
				team = "Under";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "Both teams combined score is " + team
						+ " " + dispLine + " points";
				break;

			}

		} else {
			String opp = chalName;
			opponentID = selectedChal.getChallenger();
			String wgrStr = "$" + String.valueOf(wager);
			opponentString = opp;
			wagerDisplayString = wgrStr;
			switch (winner) {
			case (WINNER_STATUS_AWAY):
				team = dispH;
				oTeam = dispA;
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "the " + team + " final score " + dispLine
						+ " points is more than the " + oTeam + " final score";
				break;
			case (WINNER_STATUS_HOME):
				team = dispA;
				oTeam = dispH;
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "the " + team + " final score " + dispLine
						+ " points is more than the " + oTeam + " final score";
				break;
			case (WINNER_STATUS_OVER):
				team = "Under";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "Both teams combined score is " + team
						+ " " + dispLine + " points";
				break;
			case (WINNER_STATUS_UNDER):
				team = "Over";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "Both teams combined score is " + team
						+ " " + dispLine + " points";
				break;

			}

		}

	}
	
public void convertToChalDisplay() {
		
		int chalID = selectedChal.getChallenger();
		int opponentID;

		boolean isChallenger = false;
		if (chalID == thisUser)
			isChallenger = true;
		else
			isChallenger = false;
		String acpName = currentUserList
				.getUserByID(selectedChal.getAccepter()).getName();
		String chalName = currentUserList.getUserByID(
				selectedChal.getChallenger()).getName();
		int winner = selectedChal.getWinner();
		double line = selectedChal.getLine();
		double wager = selectedChal.getWager();
		int gid = 0;
		int h = 0;
		int a = 0;
		int spid = 0;
		int time = 0;
		int date = 0;
		int homeRoster = selectedChal.getHomeRoster();
		int awayRoster = selectedChal.getAwayRoster();

		if (currentRosterList.containsRoster(homeRoster)) {
			Roster homeR = currentRosterList.getRosterByID(homeRoster);
			h = homeR.getTeamID();
		} else {
			WebContentGet webOb = new WebContentGet();
			JSONObject j = webOb.getRosterInfo(homeRoster);
			Roster homeR = new Roster(j);
			currentRosterList.push(homeR);
			h = homeR.getTeamID();
		}

		if (currentRosterList.containsRoster(awayRoster)) {
			Roster awayR = currentRosterList.getRosterByID(awayRoster);
			a = awayR.getTeamID();
		} else {
			WebContentGet webOb = new WebContentGet();
			JSONObject j = webOb.getRosterInfo(awayRoster);
			Roster awayR = new Roster(j);
			currentRosterList.push(awayR);
			a = awayR.getTeamID();
		}

		Team homeT;
		Team awayT;

		if (currentTeamList.containsTeam(h)) {
			homeT = currentTeamList.getTeamByID(h);
		} else {
			WebContentGet webOb = new WebContentGet();
			JSONObject j = webOb.getTeamInfo(h);
			homeT = new Team(j);
			currentTeamList.push(homeT);
		}
		if (currentTeamList.containsTeam(a)) {
			awayT = currentTeamList.getTeamByID(a);
		} else {
			WebContentGet webOb = new WebContentGet();
			JSONObject j = webOb.getTeamInfo(a);
			awayT = new Team(j);
			currentTeamList.push(awayT);
		}

		String dispH = homeT.getDisplayName();
		String dispA = awayT.getDisplayName();

		gameDisplayString = dispA + " at " + dispH;

		chalDisplayString = "";
		dispLine = "";
		String team = "";
		opponentString = "";
		wagerDisplayString = "";
		detailChalDisplay = "";
		String oTeam = "";

		if (isChallenger) {
			String opp = acpName;
			opponentID = selectedChal.getAccepter();
			String wgrStr = "$" + String.valueOf(wager);
			opponentString = opp;
			wagerDisplayString = wgrStr;
			switch (winner) {
			case (WINNER_STATUS_AWAY):
				team = dispA;
				oTeam = dispH;
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "the " + team + " final score " + dispLine
						+ " points is more than the " + oTeam + " final score";
				break;
			case (WINNER_STATUS_HOME):
				team = dispH;
				oTeam = dispA;
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "the " + team + " final score " + dispLine
						+ " points is more than the " + oTeam + " final score";
				break;
			case (WINNER_STATUS_OVER):
				team = "Over";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "Both teams combined score is " + team
						+ " " + dispLine + " points";
				break;
			case (WINNER_STATUS_UNDER):
				team = "Under";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "Both teams combined score is " + team
						+ " " + dispLine + " points";
				break;

			}

		} else {
			String opp = chalName;
			opponentID = selectedChal.getChallenger();
			String wgrStr = "$" + String.valueOf(wager);
			opponentString = opp;
			wagerDisplayString = wgrStr;
			switch (winner) {
			case (WINNER_STATUS_AWAY):
				team = dispH;
				oTeam = dispA;
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "the " + team + " final score " + dispLine
						+ " points is more than the " + oTeam + " final score";
				break;
			case (WINNER_STATUS_HOME):
				team = dispA;
				oTeam = dispH;
				if (line > 0)
					dispLine = "+" + String.valueOf(line);
				else
					dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "the " + team + " final score " + dispLine
						+ " points is more than the " + oTeam + " final score";
				break;
			case (WINNER_STATUS_OVER):
				team = "Under";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "Both teams combined score is " + team
						+ " " + dispLine + " points";
				break;
			case (WINNER_STATUS_UNDER):
				team = "Over";
				dispLine = String.valueOf(line);
				chalDisplayString = team + " " + dispLine;
				detailChalDisplay = "Both teams combined score is " + team
						+ " " + dispLine + " points";
				break;

			}

		}

	}

	public void setUser(int id) {
		thisUser = id;
	}

	public void setSelectedGame(Game g) {
		selectedGame = g;
	}

	public static void setSelectedChal(Challenge b) {
		selectedChal = b;
	}

	public static Challenge getSelectedChal() {
		return selectedChal;
	}

	public String getGameDisplayString() {

		return gameDisplayString;
	}

	public String getChalDisplayString() {
		return chalDisplayString;
	}

	public String getOpponentString() {
		return opponentString;
	}

	public String getWagerDisplayString() {
		return wagerDisplayString;
	}

	public String getDetailChalDisplay() {
		return detailChalDisplay;
	}

	public String getKeyB() {
		String ret;
		if (keyB == null)
			ret = session.getKyB();
		else
			ret = keyB;
		return ret;
	}

	public String getKeyA() {
		String ret;
		if (keyA == null)
			ret = session.getKyA();
		else
			ret = keyA;
		return ret;
	}

	public int getTokenID() {
		int ret;
		if (token == 0)
			ret = session.getTkn();
		else
			ret = token;
		return ret;
	}

	public void setToken(int tkn) {
		token = tkn;
	}

	public void setKeyB(String kyB) {
		keyB = kyB;
	}

	public void setKeyA(String kyA) {
		keyA = kyA;
	}

	public void setSalt(String salt) {
		Salt = salt;
	}

	public void setExpires(long time) {
		expires = time;
	}

	public String getSalt() {
		return Salt;
	}

	public long getExpires() {
		return expires;
	}

	public static String getBottomTitle() {
		return bottomTitle;
	}

	public static String getBottomText() {
		return bottomText;
	}

	public static int getBottomInt() {
		return bottomInt;
	}

	public void setBottomTitle(String s) {
		bottomTitle = s;
	}

	public void setBottomText(String s) {
		bottomText = s;
	}

	public void setBottomInt(int i) {
		bottomInt = i;
	}

	public void assignSession(SessionManager sm) {
		session = sm;
	}
	
	public double getChargeFromBaseValue(int a){
		double chargeRate = 1.15;
		double minCharge = 1.5;
		double charge1 = a*chargeRate;
		double charge2 = a+minCharge;
		if(charge1>charge2) return charge1;
		else return charge2;
	}

	/*
	 * public void trialMakeBet() { Challenge addChal = new Challenge(12, 61, 2,
	 * "", 10, "", 1, 0.5, 4); int metsub = addChal.getWinner(); metsub = metsub
	 * + 89; addChal.setWinner(metsub); JSONObject betOb = new JSONObject();
	 * addChal.makeJSON(betOb); WebContentGet webOb2 = new WebContentGet();
	 * Log.d("trial MakeBet", "preparing web call"); int checkBet =
	 * webOb2.makeBet(betOb); if (checkBet == 28) Log.d("failed to make Bet",
	 * "Bet" + String.valueOf(1)); else if (checkBet == 46) {
	 * Log.d("trial MakeBet", "returned to process CC"); WebContentGet webOb3 =
	 * new WebContentGet(); int checkCharge = webOb3.dummyCharge(15);
	 * Log.d("trial MakeBet", "dummy Charge complete");
	 * Log.d("dummyCHarge status", String.valueOf(checkCharge)); if (checkCharge
	 * == 71) { Log.d("trial MakeBet", "dummyCharge passed"); WebContentGet
	 * webOb4 = new WebContentGet(); int checkBet2 = webOb4.makeBet(betOb); if
	 * (checkBet2 == 28) Log.d("failed to make Bet twice", "Bet" +
	 * String.valueOf(1)); else Log.d("trial MakeBet make bet time 2",
	 * "status= " + String.valueOf(checkBet2));
	 * 
	 * } else Log.d("failed to make charge", "user David"); } }
	 * 
	 * public void trialAcceptBet() { Log.d("trialAcceptBet", "entered");
	 * WebContentGet webOb9 = new WebContentGet(); int checkAcpt =
	 * webOb9.acceptBet(1); Log.d("returned from acceptBet", "status = " +
	 * String.valueOf(checkAcpt)); if (checkAcpt == 46) { Log.d("in status 46",
	 * "prepping charge card"); WebContentGet webOb5 = new WebContentGet(); int
	 * checkCharge = webOb5.dummyCharge(15); Log.d("in status 46",
	 * "charge status = " + String.valueOf(checkCharge)); if (checkCharge == 71)
	 * { WebContentGet webOb6 = new WebContentGet(); int checkBet2 =
	 * webOb6.acceptBet(1); Log.d("checkCharge 71", "checkBet2 = " +
	 * String.valueOf(checkBet2)); if (checkBet2 == 28)
	 * Log.d("failed to accept Bet twice", "Bet" + String.valueOf(1)); } else
	 * Log.d("failed to make charge", "user" + String.valueOf(10)); } else if
	 * (checkAcpt == 28) Log.d("failed to accept bet", "bet" +
	 * String.valueOf(1)); }
	 */

	/*
	 * public void makeServerBets(){
	 * 
	 * boolean madeBet = false;
	 * 
	 * 
	 * for(int i = 30; i>1;i--){ String email = emails[i]; String pw = "pw2";
	 * Log.d("prepping log in:", "user" + String.valueOf(i)); WebContentGet
	 * webOb1 = new WebContentGet(); long checkLog = webOb1.tryLogOn(email,pw);
	 * 
	 * if(checkLog == 11){ Log.d("in successful log in loop",""); for(int j = 1;
	 * j<allBets.getBets()-1;j++){ madeBet = false; Bet addBet; addBet =
	 * allBets.getBet(j); long chalNum = addBet.getChallenger();
	 * 
	 * int chalInt = (int) chalNum;
	 * 
	 * if (chalInt == i){ Log.d("in challer = i","sample text"); int metsub =
	 * addBet.getWinner(); metsub = metsub+89; addBet.setWinner(metsub);
	 * JSONObject betOb = new JSONObject(); addBet.makeJSON(betOb);
	 * WebContentGet webOb2 = new WebContentGet(); int checkBet =
	 * webOb2.makeBet(betOb); if(checkBet == 28) Log.d("failed to make Bet",
	 * "Bet"+String.valueOf(j)); else if (checkBet == 46){ WebContentGet webOb3
	 * = new WebContentGet(); int checkCharge = webOb3.dummyCharge(15);
	 * if(checkCharge == 71){ WebContentGet webOb4 = new WebContentGet(); int
	 * checkBet2 = webOb4.makeBet(betOb); if(checkBet2==28)
	 * Log.d("failed to make Bet twice", "Bet"+String.valueOf(j)); else madeBet
	 * = true; } else Log.d("failed to make charge", "user"+String.valueOf(i));
	 * } else madeBet = true; }
	 * 
	 * if(madeBet){ Thread sleeper = new Thread(){ public void run(){ try {
	 * sleep(250); } catch (InterruptedException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } } }; sleeper.start(); try {
	 * sleeper.join(); } catch (InterruptedException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } } }
	 * 
	 * 
	 * /* if(i==3){ for (int k = 31;k<34;k++){ WebContentGet webOb4 = new
	 * WebContentGet(); int kk = k+1; int checkAcp = webOb4.acceptBet(kk);
	 * if(checkAcp == 46){ WebContentGet webOb5 = new WebContentGet(); int
	 * checkCharge = webOb5.dummyCharge(15); if(checkCharge == 71){
	 * WebContentGet webOb6 = new WebContentGet(); int checkBet2 =
	 * webOb6.acceptBet(kk); if(checkBet2==28)
	 * Log.d("failed to accept Bet twice", "Bet"+String.valueOf(k)); } else
	 * Log.d("failed to make charge", "user"+String.valueOf(i)); } else
	 * if(checkAcp == 28) Log.d("failed to accept bet",
	 * "bet"+String.valueOf(k)); else{ Thread sleeper2 = new Thread(){ public
	 * void run(){ try { sleep(250); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * } }; sleeper2.start(); try { sleeper2.join(); } catch
	 * (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } } }
	 * 
	 * 
	 * 
	 * if(i==2){ for (int p = 41;p<44;p++){ int kk = p+1; WebContentGet webOb4 =
	 * new WebContentGet(); int checkAcp = webOb4.acceptBet(kk); if(checkAcp ==
	 * 46){ WebContentGet webOb5 = new WebContentGet(); int checkCharge =
	 * webOb5.dummyCharge(15); if(checkCharge == 71){ WebContentGet webOb6 = new
	 * WebContentGet(); int checkBet2 = webOb6.acceptBet(kk); if(checkBet2==28)
	 * Log.d("failed to accept Bet twice", "Bet"+String.valueOf(p)); } else
	 * Log.d("failed to make charge", "user"+String.valueOf(i)); } else
	 * if(checkAcp == 28) Log.d("failed to accept bet",
	 * "bet"+String.valueOf(p)); else{ Thread sleeper = new Thread(){ public
	 * void run(){ try { sleep(250); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } }; sleeper.start();
	 * try { sleeper.join(); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } } }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * } else Log.d("failed user log in", "user"+String.valueOf(i));
	 * 
	 * 
	 * 
	 * Thread sleepy = new Thread(){ public void run(){ try { sleep(125); }
	 * catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } };
	 * 
	 * sleepy.start(); try { sleepy.join(); } catch (InterruptedException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * /* int[] betstoacpt = new int[10]; betstoacpt[0]=2; betstoacpt[1]=3;
	 * betstoacpt[2]=4; betstoacpt[3]=5; betstoacpt[4]=6; betstoacpt[5]=16;
	 * betstoacpt[6]=17; betstoacpt[7]=18; betstoacpt[8]=19; betstoacpt[9]=20;
	 * 
	 * 
	 * for (int p = 0; p<betstoacpt.length;p++){ int h = betstoacpt[p]; int hh =
	 * h+1; long user1 = allBets.getBet(h).getAccepter(); WebContentGet webOb8 =
	 * new WebContentGet(); String email1 = emails[(int)user1]; String pw1 =
	 * "pw2"; long checkLog = webOb8.tryLogOn(email1, pw1); if(checkLog == 11){
	 * WebContentGet webOb9 = new WebContentGet(); int checkAcpt =
	 * webOb9.acceptBet(hh); if(checkAcpt == 46){ WebContentGet webOb5 = new
	 * WebContentGet(); int checkCharge = webOb5.dummyCharge(15); if(checkCharge
	 * == 71){ WebContentGet webOb6 = new WebContentGet(); int checkBet2 =
	 * webOb6.acceptBet(hh); if(checkBet2==28)
	 * Log.d("failed to accept Bet twice", "Bet"+String.valueOf(h)); } else
	 * Log.d("failed to make charge", "user"+String.valueOf(user1)); } else
	 * if(checkAcpt == 28) Log.d("failed to accept bet",
	 * "bet"+String.valueOf(h)); else{ Thread sleeper = new Thread(){ public
	 * void run(){ try { sleep(250); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } }; sleeper.start();
	 * try { sleeper.join(); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 * 
	 * }else Log.d("failed to logOn","user"+String.valueOf(user1));
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 */

	public class RosterDisplay extends AsyncTask<Void, Void, Void> {
		Activity useActivity;
		String title;
		Roster homeR1, homeR2, awayR1, awayR2;
		Class toClass;
		boolean editing;
		AlertDialog.Builder builder;
		ProgressDialog pDialog;
		Handler handle;
		Challenge useChal = getSelectedChal();

		RosterDisplay(Activity a, Class c, String t, Roster home1, Roster home2,
				Roster away1, Roster away2, boolean e) {

			title = t;
			useActivity = a;
			toClass = c;
			homeR1 = home1;
			homeR2 = home2;
			awayR1 = away1;
			awayR2 = away2;
			playerA1 = 0;
			playerA2 = 0;
			playerH2 = 0;
			playerH1 = 0;
			editing = e;
		}

		RosterDisplay(Activity a, String t, Roster home1, int play1,
				Roster home2, int play2, Roster away1, int play3, Roster away2,
				int play4, boolean e) {

			title = t;
			useActivity = a;
			homeR1 = home1;
			homeR2 = home2;
			awayR1 = away1;
			awayR2 = away2;
			playerA1 = play3;
			playerA2 = play4;
			playerH2 = play2;
			playerH1 = play1;
			if(playerA2 == playerH2) playerA2 = 0;
			editing = e;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(useActivity);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
			
			Log.d("MenuBar","Starting RosterDisplay");

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			if (Looper.myLooper() == null)
				Looper.prepare();
			handle = new Handler();
			float textsize = 0.75f;
			int padding = 4;
			builder = new AlertDialog.Builder(useActivity);
			builder.setTitle(title);
			LayoutInflater inflater = useActivity.getLayoutInflater();
			View displayView = inflater.inflate(R.layout.roster_popup, null);
			builder.setView(displayView);

			TableLayout tab;
			tab = (TableLayout) displayView.findViewById(R.id.rosterTable);
			TextView statusText1, positionText1, nameText1, statusText2, positionText2, nameText2;
			TableRow row;
			Button editRosters;
			editRosters = (Button) displayView
					.findViewById(R.id.rosterEditButton);
			TextView homeName, awayName;
			homeName = (TextView) displayView.findViewById(R.id.homeRosterName);
			awayName = (TextView) displayView.findViewById(R.id.awayRosterName);
			Team homeT = currentTeamList.getTeamByID(homeR1.getTeamID());
			Team awayT = currentTeamList.getTeamByID(awayR1.getTeamID());
			homeName.setText(homeT.getDisplayName());
			awayName.setText(awayT.getDisplayName());
			rosterAssignList home1 = homeR1.getSortedRoster();
			rosterAssignList home2 = homeR2.getSortedRoster();
			rosterAssignList away1 = awayR1.getSortedRoster();
			rosterAssignList away2 = awayR2.getSortedRoster();
			int hom2Len = home2.getAssignments();
			int aw2Len = away2.getAssignments();

			for (int i = 0; i < hom2Len; i++) {
				if (home2.getAssign(i).getStatus() == ASSIGN_STATUS_UNLOCKED) {
					home1.push(home2.getAssign(i));
				}
			}

			for (int i = 0; i < aw2Len; i++) {
				if (away2.getAssign(i).getStatus() == ASSIGN_STATUS_UNLOCKED) {
					away1.push(away2.getAssign(i));
				}
			}

			int aw1Len = away1.getAssignments();
			int hom1Len = home1.getAssignments();
			int len = 0;
			if (aw1Len >= hom1Len)
				len = aw1Len;
			else
				len = hom1Len;

			for (int i = 0; i < len; i++) {
				row = new TableRow(useActivity);
				row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				statusText1 = new TextView(useActivity);
				positionText1 = new TextView(useActivity);
				nameText1 = new TextView(useActivity);
				statusText2 = new TextView(useActivity);
				positionText2 = new TextView(useActivity);
				nameText2 = new TextView(useActivity);
				if (i < hom1Len) {
					Player addplayer = currentPlayerList.getPlayerByID(home1
							.getAssign(i).getPlayer());
					String playerName = addplayer.getName();
					int lenName = playerName.length() + 1;
					String playerTeam = currentTeamList.getTeamByID(
							addplayer.getTeam()).getLetters();
					int lenTeam = playerTeam.length();
					String displayName = playerName + " " + playerTeam;
					int lenDisp = displayName.length();
					SpannableString ss1 = new SpannableString(displayName);
					ss1.setSpan(new RelativeSizeSpan(0.6f), lenName, lenDisp, 0); // set
					// size
					nameText1.setText(ss1);
					positionText1.setText(addplayer.getPosition());
					if (home1.getAssign(i).getStatus() == ASSIGN_STATUS_LOCKED)
						statusText1.setText("Active");
					else {
						if (addplayer.getTeam() == homeR1.getTeamID()) {
							if (playerH1 == 0){
								playerH1 = addplayer.getID();
								Log.d("RosterDisplay setting playerH1",String.valueOf(playerH1));
							}
							if (playerH1 == addplayer.getID())
								statusText1.setText("Bench");
							else
								statusText1.setText("Active");
						} else {
							if (playerH2 == 0){
								playerH2 = addplayer.getID();
								Log.d("RosterDisplay setting playerH2",String.valueOf(playerH2));
							}
							if (playerH2 == addplayer.getID())
								statusText1.setText("Active");
							else
								statusText1.setText("Bench");
						}
					}

				} else {
					nameText1.setText("");
					positionText1.setText("");
					statusText1.setText("");
				}
				if (i < aw1Len) {
					Player addplayer2 = currentPlayerList.getPlayerByID(away1
							.getAssign(i).getPlayer());
					String playerName = addplayer2.getName();
					int lenName = playerName.length() + 1;
					String playerTeam = currentTeamList.getTeamByID(
							addplayer2.getTeam()).getLetters();
					int lenTeam = playerTeam.length();
					String displayName = playerName + " " + playerTeam;
					int lenDisp = displayName.length();
					SpannableString ss2 = new SpannableString(displayName);
					ss2.setSpan(new RelativeSizeSpan(0.6f), lenName, lenDisp, 0); // set
					// size
					nameText2.setText(ss2);
					positionText2.setText(addplayer2.getPosition());
					if (away1.getAssign(i).getStatus() == ASSIGN_STATUS_LOCKED)
						statusText2.setText("Active");
					else {
						if (addplayer2.getTeam() == awayR1.getTeamID()) {
							if (playerA1 == 0){
								playerA1 = addplayer2.getID();
								Log.d("RosterDisplay setting playerA1",String.valueOf(playerA1));
							}
							if (playerA1 == addplayer2.getID())
								statusText2.setText("Bench");
							else
								statusText2.setText("Active");
						} else {
							if (playerA2 == 0 && addplayer2.getID()!=playerH2){
								playerA2 = addplayer2.getID();
								Log.d("RosterDisplay setting playerA2",String.valueOf(playerA2));
							}
							if (playerA2 == addplayer2.getID())
								statusText2.setText("Active");
							else
								statusText2.setText("Bench");
						}
					}
				} else {
					nameText2.setText("");
					positionText2.setText("");
					statusText2.setText("");
				}
				statusText1.setTextScaleX(textsize);
				statusText1.setPadding(2 * padding, 0, padding, 0);
				positionText1.setTextScaleX(textsize);
				positionText1.setPadding(padding / 2, 0, padding / 2, 0);
				nameText1.setTextScaleX(textsize);
				nameText1.setPadding(padding / 2, 0, padding / 2, 0);
				nameText2.setTextScaleX(textsize);
				nameText2.setPadding(padding / 2, 0, padding / 2, 0);
				nameText2.setGravity(Gravity.RIGHT);
				positionText2.setTextScaleX(textsize);
				positionText2.setPadding(padding / 2, 0, padding / 2, 0);
				statusText2.setTextScaleX(textsize);
				statusText2.setPadding(padding, 0, 2 * padding, 0);

				row.addView(statusText1);
				row.addView(positionText1);
				row.addView(nameText1);
				row.addView(nameText2);
				row.addView(positionText2);
				row.addView(statusText2);
				tab.addView(row);
			}
			if (editing)
				editRosters.setVisibility(View.VISIBLE);
			else
				editRosters.setVisibility(View.GONE);
			editRosters.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showEditRosterDialog(useActivity,toClass, "Select Roster Edits",
							homeR1, homeR2, awayR1, awayR2);
					((DialogInterface) builder).dismiss();
				}
			});
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Log.d("RosterDisplay playerH1",String.valueOf(playerH1));
							Log.d("RosterDisplay playerH2",String.valueOf(playerH2));
							Log.d("RosterDisplay playerA1",String.valueOf(playerA1));
							Log.d("RosterDisplay playerA2",String.valueOf(playerA2));
						}
					});

			builder.create();
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog

			// show pop up
			builder.show();

			if (pDialog.isShowing())
				pDialog.dismiss();
			
			Log.d("MenuBar","Ending RosterDisplay logic, displaying");

		}

	}

	public static void showEditRosterDialog(Activity activity, Class c, String title,
			final Roster ros, final Roster ros2, final Roster ros3, final Roster ros4) {
		final int[] playerret = new int[4];
		int homedropcount = 0;
		int homeplaycount = 0;
		int awaydropcount = 0;
		int awayplaycount = 0;
		int homedropindex = 0;
		int homeplayindex = 0;
		int awaydropindex = 0;
		int awayplayindex = 0;
		final int[] homedropbuttons = new int[20];
		final int[] awaydropbuttons = new int[20];
		final int[] homeaddbuttons = new int[20];
		final int[] awayaddbuttons = new int[20];
		final int[] homedropplayers = new int[20];
		final int[] awaydropplayers = new int[20];
		final int[] homeaddplayers = new int[20];
		final int[] awayaddplayers = new int[20];
		final RadioGroup homedropgroup,awaydropgroup,homeplaygroup,awayplaygroup;
		final Activity useAct = activity;
		final Class toClass = c;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		LayoutInflater inflater = activity.getLayoutInflater();
		View displayView2 = inflater.inflate(R.layout.edit_roster_popup, null, false);
		builder.setView(displayView2);
		TextView homedrop, awaydrop, homeplay, awayplay;
		homedropgroup = (RadioGroup) displayView2
				.findViewById(R.id.homeDropRadioG);
		awaydropgroup = (RadioGroup) displayView2
				.findViewById(R.id.awayDropRadioG);
		homeplaygroup = (RadioGroup) displayView2
				.findViewById(R.id.homeAddRadioG);
		awayplaygroup = (RadioGroup) displayView2
				.findViewById(R.id.awayAddRadioG);
		homedrop = (TextView) displayView2.findViewById(R.id.homeDropText);
		awaydrop = (TextView) displayView2.findViewById(R.id.awayDropText);
		homeplay = (TextView) displayView2.findViewById(R.id.homeAddText);
		awayplay = (TextView) displayView2.findViewById(R.id.awayAddText);
		String homeName = currentTeamList.getTeamByID(ros.getTeamID())
				.getDisplayName();
		String awayName = currentTeamList.getTeamByID(ros3.getTeamID())
				.getDisplayName();
		homedrop.setText("Drop a " + homeName + " player");
		awaydrop.setText("Drop a " + awayName + " player");
		homeplay.setText("Replace with");
		awayplay.setText("Replace with");

		final int HOMERID = ros.getID();
		final int AWAYRID = ros3.getID();
		final int HOMER2ID = ros2.getID();
		final int AWAYR2ID = ros4.getID();

		float textsize = 0.85f;
		int padding = 5;
		homedrop.setTextScaleX(textsize);
		homedrop.setPadding(padding / 2, 0, padding, 0);
		awaydrop.setTextScaleX(textsize);
		awaydrop.setPadding(padding, 0, padding / 2, 0);
		homeplay.setTextScaleX(textsize);
		homeplay.setPadding(padding / 2, 0, padding, 0);
		awayplay.setTextScaleX(textsize);
		awayplay.setPadding(padding, 0, padding / 2, 0);

		rosterAssignList ros1List = ros.getSortedRoster();
		rosterAssignList ros2List = ros2.getSortedRoster();
		rosterAssignList ros3List = ros3.getSortedRoster();
		rosterAssignList ros4List = ros4.getSortedRoster();

		for (int i = 0; i < ros2List.getAssignments(); i++) {
			ros1List.push(ros2List.getAssign(i));
		}
		for (int i = 0; i < ros3List.getAssignments(); i++) {
			ros1List.push(ros3List.getAssign(i));
		}
		
		if(HOMER2ID!=AWAYR2ID){
			for (int i = 0; i < ros4List.getAssignments(); i++) {
				ros1List.push(ros4List.getAssign(i));
			}
		}
		

		homedropcount = 0;
		homeplaycount = 0;
		awaydropcount = 0;
		awayplaycount = 0;
		homedropindex = 0;
		homeplayindex = 0;
		awaydropindex = 0;
		awayplayindex = 0;
		for (int i = 0; i < ros1List.getAssignments(); i++) {
			RosterAssign ass = ros1List.getAssign(i);
			int playerstatus = ass.getStatus();
			if (playerstatus == ASSIGN_STATUS_UNLOCKED) {
				RadioButton addBtn = new RadioButton(activity);
				Player addplayer = currentPlayerList.getPlayerByID(ass
						.getPlayer());
				Log.d("edit roster adding player","player id " + String.valueOf(addplayer.getID()));
				int playID = addplayer.getNumber();
				String playerName = addplayer.getName();
				int lenName = playerName.length() + 1;
				String playerTeam = currentTeamList.getTeamByID(
						addplayer.getTeam()).getLetters();
				int lenTeam = playerTeam.length();
				String displayName = playerName + " " + playerTeam;
				int lenDisp = displayName.length();
				SpannableString ss2 = new SpannableString(displayName);
				ss2.setSpan(new RelativeSizeSpan(0.6f), lenName, lenDisp, 0); // set
																				// size
				addBtn.setText(ss2);
				// addBtn.setTextSize(0.65f);

				if (ass.getRoster() == HOMERID) {
					Log.d("Edit roster, adding","homedropgroup adding");
					Log.d("homedropgroup adding","player id " + String.valueOf(addplayer.getID()));
					homedropgroup.addView(addBtn);
					homedropbuttons[homedropcount] = addBtn.getId();
					homedropplayers[homedropcount] = playID;
					homedropindex = homedropcount;
					homedropcount++;
					Log.d("Edit roster, adding","homedropgroup added");
				}
				if (ass.getRoster() == AWAYRID) {
					Log.d("Edit roster, adding","awaydropgroup adding");
					Log.d("awaydropgroup adding","player id " + String.valueOf(addplayer.getID()));
					awaydropgroup.addView(addBtn);
					awaydropbuttons[awaydropcount] = addBtn.getId();
					awaydropplayers[awaydropcount] = playID;
					awaydropindex = awaydropcount;
					awaydropcount++;
					Log.d("Edit roster, adding","awaydropgroup added");
				}

				if (ass.getRoster() == HOMER2ID) {
					Log.d("Edit roster, adding","homeplaygroup adding");
					Log.d("homeplaygroup adding","player id " + String.valueOf(addplayer.getID()));
					homeplaygroup.addView(addBtn);
					homeaddbuttons[homeplaycount] = addBtn.getId();
					homeaddplayers[homeplaycount] = playID;
					homeplayindex = homeplaycount;
					homeplaycount++;
					Log.d("Edit roster, adding","homeplaygroup added");
				}
				if (ass.getRoster() == AWAYR2ID) {
					Log.d("Edit roster, adding","awayplaygroup adding");
					Log.d("awayplaygroup adding","player id " + String.valueOf(addplayer.getID()));
					RadioButton addBtn2 = new RadioButton(activity);
					addBtn2.setText(ss2);
					awayplaygroup.addView(addBtn2);
					awayaddbuttons[awayplaycount] = addBtn2.getId();
					awayaddplayers[awayplaycount] = playID;
					awayplayindex = awayplaycount;
					awayplaycount++;
					Log.d("Edit roster, adding","awayplaygroup added");
				}
			}
		}
		homedropgroup.check(homedropbuttons[0]);
		homeplaygroup.check(homeaddbuttons[0]);
		awaydropgroup.check(awaydropbuttons[0]);
		awayplaygroup.check(awayaddbuttons[0]);
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// close box
				// proceed with make bet
				// get selected players
				int homeplayerin = homeaddplayers[0];
				int homeplayerout = homedropplayers[0];
				int awayplayerin = awayaddplayers[0];
				int awayplayerout = awaydropplayers[0];
				int selectID = 0;
				selectID = homedropgroup.getCheckedRadioButtonId();
				for (int i = 0; i <= homedropgroup.getChildCount(); i++) {
					if (selectID == homedropbuttons[i]) {
						homeplayerout = homedropplayers[i];
						break;
					}
				}
				selectID = awaydropgroup.getCheckedRadioButtonId();
				for (int i = 0; i <= awaydropgroup.getChildCount(); i++) {
					if (selectID == awaydropbuttons[i]) {
						awayplayerout = awaydropplayers[i];
						break;
					}
				}
				selectID = homeplaygroup.getCheckedRadioButtonId();
				for (int i = 0; i <= homeplaygroup.getChildCount(); i++) {
					if (selectID == homeaddbuttons[i]) {
						homeplayerin = homeaddplayers[i];
						break;
					}
				}
				selectID = awayplaygroup.getCheckedRadioButtonId();
				for (int i = 0; i <= awayplaygroup.getChildCount(); i++) {
					if (selectID == awayaddbuttons[i]) {
						awayplayerin = awayaddplayers[i];
						break;
					}
				}
				// make challenge with previous info and selected players
				Log.d("home player in", String.valueOf(homeplayerin));
				Log.d("home player out", String.valueOf(homeplayerout));
				Log.d("away player in", String.valueOf(awayplayerin));
				Log.d("away player out", String.valueOf(awayplayerout));
				
				playerH1 = homeplayerout;
				playerH2 = homeplayerin;
				playerA1 = awayplayerout;
				playerA2 = awayplayerin;
				
			}
		});
		builder.create();
		builder.show();
	}
	
	

	public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;
	    /*
	    void downloadFile(String fileUrl) {
	    	try{
	    	      InputStream is = (InputStream) new URL(fileUrl).getContent();
	    	      Drawable d = Drawable.createFromStream(is, "src name");
	    	      imgView.setImageDrawable(d);            
	    	        } catch (IOException e) {
	    	            e.printStackTrace();                
	    	        }

	    	}
	    	*/

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	    	if(Looper.myLooper()==null) Looper.prepare();
	        String urldisplay = urls[0];
	        if(urldisplay.length() < 2){
	        	return null;
	        }
	        else{
	        String useurl = ImageURL + urldisplay +".png";
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(useurl).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	        }
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}

	public void calcGameTime(long timeStampIn, int sport) {
		long timeNow = System.currentTimeMillis() / 1000L;
		if (timeNow > timeStampIn) {
			int mins;
			int secs;
			mins = (int) ((int) timeStampIn) / 60;
			secs = (int) ((int) timeStampIn) - (60 * mins);
			switch (sport) {
			case NFL_ID:

				if (mins > 45) {
					gameperiod = 1;
					gamemin = (mins - 45);
					gamesec = secs;
				} else if (mins > 30) {
					gameperiod = 2;
					gamemin = (mins - 30);
					gamesec = secs;
				} else if (mins > 15) {
					gameperiod = 3;
					gamemin = (mins - 15);
					gamesec = secs;
				} else {
					gameperiod = 4;
					gamemin = mins;
					gamesec = secs;
				}
				break;
			case NHL_ID:
				if (mins > 40) {
					gameperiod = 1;
					gamemin = (mins - 40);
					gamesec = secs;
				} else if (mins > 20) {
					gameperiod = 2;
					gamemin = (mins - 20);
					gamesec = secs;
				} else {
					gameperiod = 3;
					gamemin = mins;
					gamesec = secs;
				}
				break;
			case MLB_ID:
				gameperiod = mins;
				gamesec = secs + 100;
			case NBA_ID:
				if (mins > 36) {
					gameperiod = 1;
					gamemin = (mins - 36);
					gamesec = secs;
				} else if (mins > 24) {
					gameperiod = 2;
					gamemin = (mins - 24);
					gamesec = secs;
				} else if (mins > 12) {
					gameperiod = 3;
					gamemin = (mins - 12);
					gamesec = secs;
				} else {
					gameperiod = 4;
					gamemin = (mins);
					gamesec = secs;
				}
				break;
			case NCAAF_ID:
				if (mins > 45) {
					gameperiod = 1;
					gamemin = (mins - 45);
					gamesec = secs;
				} else if (mins > 30) {
					gameperiod = 2;
					gamemin = (mins - 30);
					gamesec = secs;
				} else if (mins > 15) {
					gameperiod = 3;
					gamemin = (mins - 15);
					gamesec = secs;
				} else {
					gameperiod = 4;
					gamemin = mins;
					gamesec = secs;
				}
				break;
			case NCAAB_ID:
				if (mins > 20) {
					gameperiod = 1;
					gamemin = (mins - 20);
					gamesec = secs;
				} else {
					gameperiod = 2;
					gamemin = (mins);
					gamesec = secs;
				}
				break;
			}
		} else {

			timeStampIn = timeStampIn * (long) 1000;
			TimeZone.setDefault(TimeZone.getTimeZone("EST"));
			Date df = new Date(timeStampIn);
			String s = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
			String monStr = s.substring(0, 2);
			String dayStr = s.substring(3, 5);
			String hrStr = s.substring(12, 14);
			String minStr = s.substring(15);
			if (monStr.substring(0, 0).equals("0"))
				monStr = monStr.substring(1);
			if (dayStr.substring(0, 0).equals("0"))
				dayStr = dayStr.substring(1);
			if (hrStr.substring(0, 0).equals("0"))
				hrStr = hrStr.substring(1);
			int hr = Integer.parseInt(hrStr);
			if (hr > 12) {
				hr = hr - 12;
				hrStr = String.valueOf(hr);
			}
			gamedate = (monStr + "/" + dayStr);
			gametime = (hrStr + ":" + minStr);
			gameperiod = 0;

		}
	}

	String setDateString() {
		String re = "";
		switch (gameperiod) {
		case (0):
			re = gamedate;
			break;
		case 1:
			re = "1st";
			break;
		case 2:
			re = "2nd";
			break;
		case 3:
			re = "3rd";
			break;
		case 4:
			re = "4th";
			break;
		case 5:
			re = "5th";
			break;
		case 6:
			re = "6th";
			break;
		case 7:
			re = "7th";
			break;
		case 8:
			re = "8th";
			break;
		case 9:
			re = "9th";
			break;
		case 10:
			re = "OT";
			break;
		default:
			re = "";
			break;
		}

		return re;
	}

	String setTimeString() {
		String re = "";
		if (gameperiod == 0)
			re = gametime;
		else {
			switch (gamesec) {
			case 101:
				re = "Top";
				break;
			case 102:
				re = "Bottom";
				break;
			default:
				re = (String.valueOf(gamemin) + ":" + String.valueOf(gamesec));
				break;
			}
		}

		return re;
	}

	public void parseAlpha(JSONArray j) {
		currentChalList.clear();
		for (int i = 0; i < j.length(); i++) {
			JSONObject jO = new JSONObject();
			try {
				jO = j.getJSONObject(i);
				int chID = Integer.parseInt(jO.getString("ChallengeID"));
				int challenger = Integer.parseInt(jO.getString("ChallengerID"));
				int accepter = Integer.parseInt(jO.getString("accepterID")); // homeprimary
				int hr1 = Integer.parseInt(jO.getString("homePrimaryRosterID"));
				int hr2 = Integer.parseInt(jO.getString("homeAddedRosterID"));
				int ar1 = Integer.parseInt(jO.getString("awayPrimaryRosterID"));
				int ar2 = Integer.parseInt(jO.getString("awayAddedRosterID"));
				int hp1 = Integer.parseInt(jO.getString("homeRemovedPlayer"));
				int hp2 = Integer.parseInt(jO.getString("homeAddedPlayer"));
				int ap1 = Integer.parseInt(jO.getString("awayRemovedPlayer"));
				int ap2 = Integer.parseInt(jO.getString("awayAddedPlayer"));
				int win = Integer.parseInt(jO.getString("winner"));
				int status = Integer.parseInt(jO.getString("status"));
				double line = Double.parseDouble(jO.getString("line"));
				double wager = Double.parseDouble(jO.getString("wager"));

				Challenge addChal = new Challenge(chID, challenger, accepter,
						hr1, hp1, hr2, hp2, ar1, ap1, ar2, ap2, win, line,
						wager, status);

				currentChalList.push(addChal);

				for (int r = 0; r < 4; r++) {

					Roster checkRoster;
					int rosID = 0;
					String baseR = "";
					String baseT = "";
					String getTeam = "teamID";
					String getStat = "status";
					String getTime = "timestamp";
					String getCity = "teamcity";
					String getMas = "teamname";
					String getRank = "teamrank";
					String getLet = "teamletters";
					String getURL = "teamURL";
					String getSpt = "teamsport";

					switch (r) {
					case 0:
						baseR = "homePrimaryRosterID_";
						baseT = "homeprimary";
						rosID = hr1;
						break;
					case 1:
						baseR = "homeAddedRosterID_";
						baseT = "homeadded";
						rosID = hr2;
						break;
					case 2:
						baseR = "awayPrimaryRosterID_";
						baseT = "awayprimary";
						rosID = ar1;
						break;
					case 3:
						baseR = "awayAddedRosterID_";
						baseT = "awayadded";
						rosID = ar2;
						break;
					}

					if (!currentRosterList.containsRoster(rosID)) {
						String teams = baseR + getTeam;
						String stats = baseR + getStat;
						String times = baseR + getTime;
						int teamID = Integer.parseInt(jO.getString(teams));
						int rosStat = Integer.parseInt(jO.getString(stats));
						long rosTime = Long.parseLong(jO.getString(times));

						checkRoster = new Roster(rosID, teamID, rosTime,
								rosStat);
						currentRosterList.push(checkRoster);
					} else {
						checkRoster = currentRosterList.getRosterByID(rosID);
					}

					if (!currentTeamList.containsTeam(checkRoster.getTeamID())) {
						String citys = baseT + getCity;
						String mascs = baseT + getMas;
						String ranks = baseT + getRank;
						String letts = baseT + getLet;
						String urls = baseT + getURL;
						String sports = baseT + getSpt;
						String teamCity = jO.getString(citys);
						String teamName = jO.getString(mascs);
						String letters = jO.getString(letts);
						String rnk = jO.getString(ranks);
						int teamRank = 0;
						/*
						 * if (rnk!=null | !rnk.equals("null")){ teamRank =
						 * Integer.parseInt(rnk); } else { Log.d("rank null",
						 * "TeamID: "+ String.valueOf(checkRoster.getTeamID()));
						 * }
						 */
						String teamURL = jO.getString(urls);
						int teamSport = 1; // Integer.parseInt(jO.getString(sports));

						Team addTeam = new Team(checkRoster.getTeamID(),
								teamCity, teamName, letters, teamSport, 0,
								teamRank, teamURL);
						currentTeamList.push(addTeam);
					}
				}

				for (int u = 0; u < 2; u++) {
					String baseU = "";
					int uID;

					if (u == 0) {
						baseU = "Challenger";
						uID = challenger;
					} else {
						baseU = "accepter";
						uID = accepter;
					}

					String fname = "firstname";
					String lname = "lastname";

					String last = baseU + lname;
					String first = baseU + fname;

					if (!currentUserList.contains(uID)) {
						String userF = jO.getString(first);
						String userL = jO.getString(last);
						User addUser = new User(userF, userL, uID);

						currentUserList.push(addUser);
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void parseBravo(JSONArray j, int sport) {
		Log.d("parseBravo", "entering");
		currentGameList.clear();

		for (int i = 0; i < j.length(); i++) {
			try {
				JSONObject o = j.getJSONObject(i);
				int gID = Integer.parseInt(o.getString("gameId"));
				int homeR = Integer.parseInt(o.getString("homeRoster"));
				int awayR = Integer.parseInt(o.getString("awayRoster"));
				long time = Long.parseLong(o.getString("timeStamp"));
				//int sport = Integer.parseInt(o.getString("Sport"));
				int status = Integer.parseInt(o.getString("status"));

				Game addGame = new Game(homeR, awayR, gID, sport, time, status);
				currentGameList.push(addGame);

				Roster useR;
				String baseR = "";
				String getTeam = "teamID";
				String getStat = "status";
				String getTime = "timestamp";
				String getCity = "city";
				String getMas = "name";
				String getRank = "rank";
				String getLet = "letters";
				String getURL = "url";
				//String getSpt = "teamsport";

				if (currentRosterList.containsRoster(homeR))
					useR = currentRosterList.getRosterByID(homeR);
				else {
					baseR = "home";
					String teams = baseR + getTeam;
					String stats = baseR + getStat;
					String times = baseR + getTime;
					int teamID = Integer.parseInt(o.getString(teams));
					int rosStat = Integer.parseInt(o.getString(stats));
					long rosTime = Long.parseLong(o.getString(times));

					useR = new Roster(homeR, teamID, rosTime, rosStat);
					currentRosterList.push(useR);
				}

				if (!currentTeamList.containsTeam(useR.getTeamID())) {
					baseR = "home";
					String citys = baseR + getCity;
					String mascs = baseR + getMas;
					String ranks = baseR + getRank;
					String letts = baseR + getLet;
					String urls = baseR + getURL;
					//String sports = baseR + getSpt;
					String teamCity = o.getString(citys);
					String teamName = o.getString(mascs);
					String letters = o.getString(letts);
					int teamRank = Integer.parseInt(o.getString(ranks));
					String teamURL = o.getString(urls);
					int teamSport = sport;

					Team addTeam = new Team(useR.getTeamID(), teamCity,
							teamName, letters, teamSport, 0, teamRank, teamURL);
					currentTeamList.push(addTeam);
				}

				Roster useR2;
				if (currentRosterList.containsRoster(awayR))
					useR2 = currentRosterList.getRosterByID(awayR);
				else {
					baseR = "away";
					String teams = baseR + getTeam;
					String stats = baseR + getStat;
					String times = baseR + getTime;
					int teamID = Integer.parseInt(o.getString(teams));
					int rosStat = Integer.parseInt(o.getString(stats));
					long rosTime = Long.parseLong(o.getString(times));

					useR2 = new Roster(awayR, teamID, rosTime, rosStat);
					currentRosterList.push(useR2);
				}

				if (!currentTeamList.containsTeam(useR2.getTeamID())) {
					baseR = "away";
					String citys = baseR + getCity;
					String mascs = baseR + getMas;
					String ranks = baseR + getRank;
					String letts = baseR + getLet;
					String urls = baseR + getURL;
					//String sports = baseR + getSpt;
					String teamCity = o.getString(citys);
					String teamName = o.getString(mascs);
					String letters = o.getString(letts);
					int teamRank = Integer.parseInt(o.getString(ranks));
					String teamURL = o.getString(urls);
					int teamSport = sport;

					Team addTeam = new Team(useR2.getTeamID(), teamCity,
							teamName, letters, teamSport, 0, teamRank, teamURL);
					currentTeamList.push(addTeam);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void parseBravoDemo(JSONArray j) {
		currentGameList.clear();

		for (int i = 0; i < j.length(); i++) {
			try {
				JSONObject o = j.getJSONObject(i);
				int gID = Integer.parseInt(o.getString("gameID"));
				int homeR = Integer.parseInt(o.getString("homeRoster"));
				int awayR = Integer.parseInt(o.getString("awayRoster"));
				long time = Long.parseLong(o.getString("timeStamp"));
				int sport = Integer.parseInt(o.getString("sport"));
				int status = Integer.parseInt(o.getString("status"));

				Game addGame = new Game(homeR, awayR, gID, sport, time, status);
				currentGameList.push(addGame);

				Roster useR;
				String baseR = "";
				String getTeam = "teamid";
				String getStat = "status";
				String getTime = "timestamp";
				String getCity = "teamcity";
				String getMas = "teamname";
				String getRank = "teamrank";
				String getLet = "teamletters";
				String getURL = "teamURL";
				String getSpt = "teamsport";

				if (currentRosterList.containsRoster(homeR)){
					useR = currentRosterList.getRosterByID(homeR);
				}
				else {
					WebContentGet webOb2 = new WebContentGet();
					JSONObject jOb = webOb2.getRosterInfo(homeR);

					useR = new Roster(jOb);
					currentRosterList.push(useR);
				}

				if (!currentTeamList.containsTeam(useR.getTeamID())) {
					WebContentGet webOb4 = new WebContentGet();
					JSONObject Job3 = webOb4.getTeamInfo(useR.getTeamID());

					Team addTeam = new Team(Job3);
					currentTeamList.push(addTeam);
				}
				else {
				}

				Roster useR2;
				if (currentRosterList.containsRoster(awayR))
					useR2 = currentRosterList.getRosterByID(awayR);
				else {
					WebContentGet webOb3 = new WebContentGet();
					JSONObject jOb = webOb3.getRosterInfo(awayR);

					useR2 = new Roster(jOb);
					currentRosterList.push(useR2);
				}

				if (!currentTeamList.containsTeam(useR2.getTeamID())) {
					WebContentGet webOb5 = new WebContentGet();
					JSONObject Job4 = webOb5.getTeamInfo(useR2.getTeamID());

					Team addTeam = new Team(Job4);
					currentTeamList.push(addTeam);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	public void parseCharlie(JSONArray j) {
		for (int i = 0; i<j.length();i++){
			try {
				JSONObject jO = j.getJSONObject(i);
				int paid = Integer.parseInt(jO.getString("paId"));
				int rosterid = Integer.parseInt(jO.getString("rosterID"));
				int playerid = Integer.parseInt(jO.getString("playerId"));
				int pastatus = Integer.parseInt(jO.getString("PAstatus"));
				int points = Integer.parseInt(jO.getString("points"));
				int teamid = Integer.parseInt(jO.getString("teamID"));
				int jersey = Integer.parseInt(jO.getString("jersey"));
				int playerstatus = Integer.parseInt(jO.getString("Pstatus"));
				String name = jO.getString("name");
				String position = jO.getString("position");
				
				Player addP = new Player(playerid,name,position,teamid,jersey,playerstatus);
				RosterAssign addAss = new RosterAssign(paid,playerid,rosterid,pastatus,points);
				
				if(currentPlayerList.containsPlayer(playerid)){
					currentPlayerList.pop(playerid);
				}
				currentPlayerList.push(addP);
				
				if(currentAssignList.containsRosterAssign(paid)){
					currentAssignList.pop(paid);
				}
				currentAssignList.push(addAss);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void parseDelta(JSONArray j) {
		for (int i = 0; i<j.length();i++){
			try {
				JSONObject jO = j.getJSONObject(i);
				int paid = Integer.parseInt(jO.getString("paId"));
				int rosterid = Integer.parseInt(jO.getString("rosterID"));
				int playerid = Integer.parseInt(jO.getString("playerId"));
				int pastatus = Integer.parseInt(jO.getString("PAstatus"));
				int points = Integer.parseInt(jO.getString("points"));
				int teamid = Integer.parseInt(jO.getString("teamID"));
				int jersey = Integer.parseInt(jO.getString("jersey"));
				int playerstatus = Integer.parseInt(jO.getString("Pstatus"));
				String name = jO.getString("name");
				String position = jO.getString("position");
				
				Player addP = new Player(playerid,name,position,teamid,jersey,playerstatus);
				RosterAssign addAss = new RosterAssign(paid,playerid,rosterid,pastatus,points);
				
				if(currentPlayerList.containsPlayer(playerid)){
					currentPlayerList.pop(playerid);
				}
				currentPlayerList.push(addP);
				
				if(currentAssignList.containsRosterAssign(paid)){
					currentAssignList.pop(paid);
				}
				currentAssignList.push(addAss);
				
				if(!currentTeamList.containsTeam(teamid)){
					int sportid = Integer.parseInt(jO.getString("SportID"));
					int subsportid = Integer.parseInt(jO.getString("sportSubID"));
					int rank = Integer.parseInt(jO.getString("rank"));
					String teamname = jO.getString("Name");
					String city = jO.getString("City");
					String letters = jO.getString("letters");
					String url = jO.getString("URL");

					Team addTeam = new Team(teamid, city, teamname, letters, sportid, subsportid, rank, url);
					currentTeamList.push(addTeam);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void parseEcho(JSONArray j) {
		for (int i = 0; i<j.length();i++){
			try {
				JSONObject jO = j.getJSONObject(i);
				int paid = Integer.parseInt(jO.getString("paId"));
				int rosterid = Integer.parseInt(jO.getString("rosterID"));
				int playerid = Integer.parseInt(jO.getString("playerId"));
				int pastatus = Integer.parseInt(jO.getString("PAstatus"));
				int points = Integer.parseInt(jO.getString("points"));
				int teamid = Integer.parseInt(jO.getString("teamID"));
				int jersey = Integer.parseInt(jO.getString("jersey"));
				int playerstatus = Integer.parseInt(jO.getString("Pstatus"));
				String name = jO.getString("name");
				String position = jO.getString("position");
				
				Player addP = new Player(playerid,name,position,teamid,jersey,playerstatus);
				RosterAssign addAss = new RosterAssign(paid,playerid,rosterid,pastatus,points);
				
				if(currentPlayerList.containsPlayer(playerid)){
					currentPlayerList.pop(playerid);
				}
				currentPlayerList.push(addP);
				
				if(currentAssignList.containsRosterAssign(paid)){
					currentAssignList.pop(paid);
				}
				currentAssignList.push(addAss);
				
				if(!currentTeamList.containsTeam(teamid)){
					int sportid = Integer.parseInt(jO.getString("SportID"));
					int subsportid = Integer.parseInt(jO.getString("sportSubID"));
					int rank = Integer.parseInt(jO.getString("rank"));
					String teamname = jO.getString("Name");
					String city = jO.getString("City");
					String letters = jO.getString("letters");
					String url = jO.getString("URL");

					Team addTeam = new Team(teamid, city, teamname, letters, sportid, subsportid, rank, url);
					currentTeamList.push(addTeam);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("PARSE ECHO", "NEEDS WORK");
			}
			
		}
	}
	
	
	public ArrayList<HashMap<String, String>> chalList2Adapter(chalList cL) {
		ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < cL.getChals(); i++) {
			String chalDisplayString = "";
			String dispLine = "";
			String team = "";
			String wagerDisplayString = "";
			String gameDisplayString = "";
			boolean isChallenger = true;

			Challenge currentChal = cL.getChal(i);
			Roster homeR = currentRosterList.getRosterByID(currentChal
					.getHomeRoster());
			Roster awayR = currentRosterList.getRosterByID(currentChal
					.getAwayRoster());

			Team homeT;
			Team awayT;

			try {
				homeT = currentTeamList.getTeamByID(homeR.getTeamID());
			} catch (Exception e) {
				Log.d("Team assign exception", "");
				Log.d("Roster associated with error",
						String.valueOf(homeR.getID()));
				homeT = currentTeamList.getTeam(1);
			}

			try {
				awayT = currentTeamList.getTeamByID(awayR.getTeamID());
			} catch (Exception e) {
				Log.d("Team assign exception", "");
				Log.d("Roster associated with error",
						String.valueOf(awayR.getID()));
				awayT = currentTeamList.getTeam(1);
			}

			gameDisplayString = awayT.getDisplayName() + " at "
					+ homeT.getDisplayName();

			int challengerID = currentChal.getChallenger();
			if (challengerID != thisUser)
				isChallenger = false;
			else
				isChallenger = true;
			double wager = currentChal.getWager();
			int winner = currentChal.getWinner();
			double line = currentChal.getLine();
			int cStatus = currentChal.getStatus();

			if (isChallenger) {
				String opp = currentUserList.getUserByID(
						currentChal.getAccepter()).getName();
				String wgrStr = "$" + String.valueOf(wager);
				wagerDisplayString = "vs. " + opp + " for " + wgrStr;
				switch (winner) {
				case (WINNER_STATUS_AWAY):
					team = awayT.getDisplayName();
					if (line > 0)
						dispLine = "+" + String.valueOf(line);
					else
						dispLine = String.valueOf(line);
					chalDisplayString = team + " " + dispLine;
					break;
				case (WINNER_STATUS_HOME):
					team = homeT.getDisplayName();
					if (line > 0)
						dispLine = "+" + String.valueOf(line);
					else
						dispLine = String.valueOf(line);
					chalDisplayString = team + " " + dispLine;
					break;
				case (WINNER_STATUS_OVER):
					team = "Over";
					dispLine = String.valueOf(line);
					chalDisplayString = team + " " + dispLine;
					break;
				case (WINNER_STATUS_UNDER):
					team = "Under";
					dispLine = String.valueOf(line);
					chalDisplayString = team + " " + dispLine;
					break;
				}
			} else {
				String opp = currentUserList.getUserByID(
						currentChal.getChallenger()).getName();
				String wgrStr = "$" + String.valueOf(wager);
				wagerDisplayString = "vs. " + opp + " for " + wgrStr;
				switch (winner) {
				case (WINNER_STATUS_AWAY):
					team = homeT.getDisplayName();
					if (line < 0)
						dispLine = "+" + String.valueOf(line*-1);
					else
						dispLine = String.valueOf(line*-1);
					chalDisplayString = team + " " + dispLine;
					break;
				case (WINNER_STATUS_HOME):
					team = awayT.getDisplayName();
					if (line < 0)
						dispLine = "+" + String.valueOf(line*-1);
					else
						dispLine = String.valueOf(line*-1);
					chalDisplayString = team + " " + dispLine;
					break;
				case (WINNER_STATUS_OVER):
					team = "Under";
					dispLine = String.valueOf(line);
					chalDisplayString = team + " " + dispLine;
					break;
				case (WINNER_STATUS_UNDER):
					team = "Over";
					dispLine = String.valueOf(line);
					chalDisplayString = team + " " + dispLine;
					break;
				}
			}
			HashMap<String, String> chalstring1 = new HashMap<String, String>();
			// adding each child node to HashMap key =>
			// value

			String homeIcon = homeT.getLogo();
			String awayIcon = awayT.getLogo();

			chalstring1.put("gameTitle", gameDisplayString);
			chalstring1.put("betTitle", chalDisplayString);
			chalstring1.put("wagerTitle", wagerDisplayString);
			chalstring1.put("isChallenger", String.valueOf(isChallenger));
			chalstring1.put("homeTeam", homeIcon);
			chalstring1.put("awayTeam", awayIcon);
			chalstring1.put("betStatus", String.valueOf(cStatus));

			ret.add(chalstring1);
		}

		return ret;

	}

	public ArrayList<HashMap<String, String>> games2Adapter(gameList g) {
		ArrayList<HashMap<String, String>> re = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < g.getGames(); i++) {

			Game thisGame = g.getGame(i);
		
			Roster awayR = currentRosterList
					.getRosterByID(thisGame.getAwayID());
			Roster homeR = currentRosterList
					.getRosterByID(thisGame.getHomeID());

			Team awayTeam = currentTeamList.getTeamByID(awayR.getTeamID());
			Team homeTeam = currentTeamList.getTeamByID(homeR.getTeamID());

			String dispH = homeTeam.getDisplayName();
			String dispA = awayTeam.getDisplayName();
			String homeUrl = homeTeam.getLogo();
			String awayUrl = awayTeam.getLogo();

			String homeIcon = homeTeam.getLogo();
			String awayIcon = awayTeam.getLogo();

			long timeStamp = thisGame.getTime();

			int status = thisGame.getStatus();

			HashMap<String, String> gamemap = new HashMap<String, String>();
			gamemap.put("homeName", dispH);
			gamemap.put("awayName", dispA);
			gamemap.put("timeStamp", String.valueOf(timeStamp));
			gamemap.put("status", String.valueOf(status));
			gamemap.put("homeScore", String.valueOf(2000));
			gamemap.put("awayScore", String.valueOf(2000));
			gamemap.put("homeLogo", homeUrl);
			gamemap.put("awayLogo", awayUrl);
			// gamemap.put("homeLogo", String.valueOf(homeLogoId));
			// gamemap.put("awayLogo", String.valueOf(awayLogoId));
			gamemap.put("sport", String.valueOf(thisGame.getSport()));
			// Log.d("single sport get games","hashmap created");

			re.add(gamemap);
		}
		return re;
	}

}